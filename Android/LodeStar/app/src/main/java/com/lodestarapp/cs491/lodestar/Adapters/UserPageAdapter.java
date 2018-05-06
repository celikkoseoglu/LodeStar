package com.lodestarapp.cs491.lodestar.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lodestarapp.cs491.lodestar.Controllers.TripController;
import com.lodestarapp.cs491.lodestar.Models.ImageStorage;
import com.lodestarapp.cs491.lodestar.R;
import com.lodestarapp.cs491.lodestar.UserPage;

import org.json.JSONObject;

import java.io.File;
import java.util.Date;
import java.util.List;

import static com.facebook.share.model.ShareMessengerMediaTemplateContent.MediaType.IMAGE;
import static java.security.AccessController.getContext;

import java.util.Calendar;

public class UserPageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> userInfoWithPosts;
    String mayusername = "";
    String mayemail = "";
    Context context = null;


    public UserPageAdapter(List<String> userInfoWithPosts,String username,String emeyil,Context c){
        this.userInfoWithPosts = userInfoWithPosts;
        mayusername = username;
        mayemail = emeyil;
        context = c;

    }

    static class UserPagePosts extends RecyclerView.ViewHolder {


        ImageView profilePictureView;

        TextView dateView;
        TextView userPostView;
        ImageView imagePosts;
        TextView realNameView;
        TextView userNameView;

        UserPagePosts(final View itemView){
            super(itemView);

            this.profilePictureView = itemView.findViewById(R.id.me_profile_picture);
            this.realNameView = itemView.findViewById(R.id.me_realName);
            this.userNameView = itemView.findViewById(R.id.me_username);
            this.dateView = itemView.findViewById(R.id.me_post_date);
            this.userPostView = itemView.findViewById(R.id.me_user_post);
            this.imagePosts = itemView.findViewById(R.id.user_post_city_more_info_button);

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
        ((UserPagePosts) holder).userPostView.setText(userInfoWithPosts.get(position));
        ((UserPagePosts) holder).userPostView.setText(userInfoWithPosts.get(position));
        ((UserPagePosts) holder).userNameView.setText("");
        ((UserPagePosts) holder).realNameView.setText(mayusername);
        String url = "https://firebasestorage.googleapis.com/v0/b/firebase-lodestar.appspot.com/o/download.png?alt=media&token=504973c5-01b1-4649-9a34-f938980854de";

   //     Glide.with(UserPage.class).load(url).into(view3);
        ((UserPagePosts) holder).profilePictureView.setImageURI(Uri.parse(url));
        Date currentTime = Calendar.getInstance().getTime();
        ((UserPagePosts) holder).dateView.setText(currentTime.toString().substring(0, currentTime.toString().length() -23));

        if (ImageStorage.checkIfImageExists("London")){
            File file = ImageStorage.getImage("/" + "London" + ".png");
            assert file != null;
            String p = file.getAbsolutePath();
            Bitmap b = BitmapFactory.decodeFile(p);
            ((UserPagePosts) holder).imagePosts.setImageBitmap(b);
            //mAdapter.notifyDataSetChanged();


        }
    }

    @Override
    public int getItemCount() {
        return userInfoWithPosts.size();
    }

}
