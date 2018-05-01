package com.lodestarapp.cs491.lodestar.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lodestarapp.cs491.lodestar.DirectedUserPage;
import com.lodestarapp.cs491.lodestar.R;

import org.w3c.dom.Text;

import java.util.List;

public class DirectedUserPageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> realName;
    private List<String> userInfoWithPosts;

    public DirectedUserPageAdapter(List<String> realName, List<String> userInfoWithPosts){
        this.realName = realName;
        this.userInfoWithPosts = userInfoWithPosts;
    }

    static class DirectedUserPagePosts extends RecyclerView.ViewHolder{

        ImageView directedProfilePicture;
        TextView directedRealName;
        //TextView directedUsername;
        TextView directedPostDate;
        TextView directedUserPost;

        public DirectedUserPagePosts(View itemView) {
            super(itemView);

            this.directedProfilePicture = itemView.findViewById(R.id.directed_profile_picture);
            this.directedRealName = itemView.findViewById(R.id.directed_realName);
            //this.directedUsername = itemView.findViewById(R.id.directed_username);
            this.directedPostDate = itemView.findViewById(R.id.directed_post_date);
            this.directedUserPost = itemView.findViewById(R.id.directed_user_post);

        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;

        View postsView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards_directed_user_page_posts, parent, false);
        viewHolder = new DirectedUserPagePosts(postsView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((DirectedUserPagePosts) holder).directedRealName.setText(realName.get(0));
        ((DirectedUserPagePosts) holder).directedUserPost.setText(userInfoWithPosts.get(position));
    }

    @Override
    public int getItemCount() {
        return this.userInfoWithPosts.size();
    }
}
