package com.codepath.apps.restclienttemplate.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.activities.ComposeActivity;
import com.codepath.apps.restclienttemplate.activities.ProfileFragment;
import com.codepath.apps.restclienttemplate.activities.TimelineActivity;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.parceler.Parcel;
import org.parceler.Parcels;

import java.util.List;

import okhttp3.Headers;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder>{

    private static Context context;
    private static  List<Tweet> tweets;
    private static final String TAG = "TweetsAdapter";

    // Pass in the context and list of tweets
    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    // For each row, inflate a layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }

    // Bind values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data at position
        Tweet tweet = tweets.get(position);
        // Bind the tweet with view holder
        holder.bind(tweet);
   }

    @Override
    public int getItemCount() {
        return tweets.size();
    }


    // Define a viewHolder
    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvBody;
        private TextView tvScreenName;
        private ImageView ivTweetImage;
        private ImageView ivProfileImage;
        private TextView tvRelativeTime;
        private ImageButton ibFavorite;
        private TextView tvFavoriteCount;
        private ImageButton  ibReply;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            ivTweetImage =  itemView.findViewById(R.id.ivTweetImage);
//            itemView.setOnClickListener((View.OnClickListener) this);
            ibFavorite =  itemView.findViewById(R.id.ibFavorite);
            tvFavoriteCount = itemView.findViewById(R.id.tvFavoriteCount);
            tvRelativeTime =  itemView.findViewById(R.id.tvRelativeTime);
            ibReply =  itemView.findViewById(R.id.ibReply);
        }

        public void bind(Tweet tweet) {
            tvBody.setText(tweet.body);
            tvScreenName.setText(tweet.user.screenName);
            Glide.with(context).load(tweet.user.profileImageUrl).into(ivProfileImage);
            tvFavoriteCount.setText(String.valueOf(tweet.favoriteCount));
            tvRelativeTime.setText(tweet.getRelativeTimeAgo(tweet.createdAt));

            Drawable newImage;
            if (tweet.isFavorited) {
                newImage = context.getDrawable(R.drawable.ic_vector_heart);
            } else {
                newImage = context.getDrawable(R.drawable.ic_vector_heart_stroke);
            }
            ibFavorite.setImageDrawable(newImage);

            Log.d(TAG, "tweet image url is " +  tweet.tweetImageUrl);
            if (tweet.tweetImageUrl ==  "none") {
                ivTweetImage.setVisibility(View.GONE);
            } else {
                ivTweetImage.setVisibility(View.VISIBLE);
                Glide.with(context).load(tweet.tweetImageUrl).into(ivTweetImage);
            }

            favoriteButtonOnClick(tweet);
            profileImageOnClick();
            retweetOnClick(tweet);
        }

        private void profileImageOnClick() {
            ivProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

        private void favoriteButtonOnClick (Tweet tweet) {
            ibFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // if not already favorited
                    if (!tweet.isFavorited) {
                        // tell twitter I want to favorite this
                        TwitterApp.getRestClient(context).favorite(tweet.id, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {
                                Log.i(TAG, "should be favorited, go check");
                            }
                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            }
                        });

                        // change the drawable to btn_star_big_on
                        tweet.isFavorited = true;
                        Drawable newImage = context.getDrawable(R.drawable.ic_vector_heart);
                        ibFavorite.setImageDrawable(newImage);

                        // increment the text in tvFavoriteCount
                        tweet.favoriteCount++;
                        tvFavoriteCount.setText(String.valueOf(tweet.favoriteCount));
                    } else {
                        // else if already favorited
                        // tell twitter I want to unfavorite this
                        TwitterApp.getRestClient(context).unfavorite(tweet.id, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {
                                Log.i(TAG, "should be unfavorited, go check");
                            }
                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            }
                        });

                        // change the drawable to btn_star_big_off
                        tweet.isFavorited =  false;
                        Drawable newImage = context.getDrawable(R.drawable.ic_vector_heart_stroke);
                        ibFavorite.setImageDrawable(newImage);

                        // decrement the text in tvFavoriteCount
                        tweet.favoriteCount--;
                        tvFavoriteCount.setText(String.valueOf(tweet.favoriteCount));
                    }
                }
            });
        }

        private void retweetOnClick(Tweet tweet) {
            ibReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // popup a compose screen
                    // ot not a gonna be a brand new tweet, it gonna be a it'll have an extra attribute
                        // extra attribute: " in reply to status id"

                    Intent i = new Intent(context, ComposeActivity.class);
//                    i.putExtra("should reply to tweet", true);
//                    i.putExtra("id_of_tweet_to_reply_to", tweet.id);
//                    i.putExtra("screen_name_of_tweet_to_reply_to", tweet.user.screenName);

                    i.putExtra("tweet_to_reply_to", Parcels.wrap(tweet));
                    ((Activity) context).startActivityForResult(i, TimelineActivity.REQUEST_CODE);
                }
            });
        }

        public void onClick () {

        }

    }

}
