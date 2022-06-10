package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
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
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

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
        TextView tvRelativeTime;
        private ImageButton ibFavorite;
        private TextView tvFavoriteCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            ivTweetImage =  itemView.findViewById(R.id.ivTweetImage);
//            itemView.setOnClickListener((View.OnClickListener) this);
            ibFavorite =  itemView.findViewById(R.id.ibFavorite);
            tvFavoriteCount = itemView.findViewById(R.id.tvFavoriteCount);
        }

        public void bind(Tweet tweet) {
            tvBody.setText(tweet.body);
            tvScreenName.setText(tweet.user.screenName);
            Glide.with(context).load(tweet.user.profileImageUrl).into(ivProfileImage);

//            String test = Tweet.getRelativeTimeAgo(tweet.createdAt);
//            tvRelativeTime.setText(test);

            Log.d(TAG, "tweet image url is " +  tweet.tweetImageUrl);
            if (tweet.tweetImageUrl ==  "none") {
                ivTweetImage.setVisibility(View.GONE);
            } else {
                ivTweetImage.setVisibility(View.VISIBLE);
                Glide.with(context).load(tweet.tweetImageUrl).into(ivTweetImage);
            }

            ibFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // if not already favorited

                        // tell twitter I want to favorite this

                        // change the drawable to btn_star_big_on

                        // increment the text in tvFavoriteCount

                    // else if already faovirted
                        // tell twitter I want to unfavorite this

                        // change the drawable to btn_star_big_off

                        // decrement the text in tvFavoriteCount
                }
            });
        }

        public void onClick () {

        }

    }

}
