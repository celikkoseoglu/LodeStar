package com.lodestarapp.cs491.lodestar.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lodestarapp.cs491.lodestar.R;

import java.util.List;

public class UserPageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int PROFILE = 0;
    private static final int POSTS = 1;

    List<String> userInfoWithPosts;

    public UserPageAdapter(List<String> userInfoWithPosts){
        this.userInfoWithPosts = userInfoWithPosts;
    }

    static class UserPageProfile extends RecyclerView.ViewHolder {

        ImageView profilePictureView;
        TextView realNameView;
        TextView tripLogsCountView;
        TextView tripsCountView;
        TextView lastTripToView;

        UserPageProfile(View itemView){
            super(itemView);

            this.profilePictureView = itemView.findViewById(R.id.me_profile_picture);
            this.realNameView = itemView.findViewById(R.id.me_realName);
            this.tripLogsCountView = itemView.findViewById(R.id.me_trip_logs_count);
            this.tripsCountView = itemView.findViewById(R.id.me_trip_count);
            this.lastTripToView = itemView.findViewById(R.id.me_last_trip_city);

        }

    }

    static class UserPagePosts extends RecyclerView.ViewHolder {

        ImageView profilePictureView;
        TextView realNameView;
        TextView userNameView;
        TextView dateView;
        TextView userPostView;

        UserPagePosts(View itemView){
            super(itemView);

            this.profilePictureView = itemView.findViewById(R.id.me_profile_picture);
            this.realNameView = itemView.findViewById(R.id.me_realName);
            this.userNameView = itemView.findViewById(R.id.me_username);
            this.dateView = itemView.findViewById(R.id.me_post_date);
            this.userPostView = itemView.findViewById(R.id.me_user_post);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;

        switch (viewType){
            case PROFILE:
                View profileView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_me, parent, false);
                viewHolder = new UserPageProfile(profileView);
                break;
            case POSTS:
                View postsView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_me_posts, parent, false);
                viewHolder = new UserPagePosts(postsView);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case PROFILE:

                ((UserPageProfile) holder).realNameView.setText(userInfoWithPosts.get(0));

                break;
            case POSTS:

                break;
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    @Override
    public int getItemViewType(int position){
        if (position == PROFILE)
            return PROFILE;
        return POSTS;
    }
}
