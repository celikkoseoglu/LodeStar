package com.lodestarapp.cs491.lodestar.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lodestarapp.cs491.lodestar.R;
import com.lodestarapp.cs491.lodestar.UserPage;

import java.util.List;

import static com.facebook.share.model.ShareMessengerMediaTemplateContent.MediaType.IMAGE;

public class UserPageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int PROFILE = 0;
    private static final int POSTS = 1;

    List<String> userInfoWithPosts;

    public UserPageAdapter(List<String> userInfoWithPosts){
        this.userInfoWithPosts = userInfoWithPosts;
    }

    static class UserPagePosts extends RecyclerView.ViewHolder {


        ImageView profilePictureView;
        TextView realNameView;
        TextView userNameView;
        TextView dateView;
        TextView userPostView;

        UserPagePosts(final View itemView){
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

        RecyclerView.ViewHolder viewHolder;

        View postsView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_me_posts, parent, false);
        viewHolder = new UserPagePosts(postsView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

}
