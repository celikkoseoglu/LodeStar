package com.lodestarapp.cs491.lodestar;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lodestarapp.cs491.lodestar.Adapters.UserPageAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;

public class UserPage extends android.support.v4.app.Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseAuth mAuth;
    String currentUserName;

    private List<String> userInfoWithPosts = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_user_page, container, false);

        ImageView profileImageView = (ImageView) view.findViewById(R.id.me_profile_picture);

        /*Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_003_user);
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        roundedBitmapDrawable.setCircular(true);
        profileImageView.setImageDrawable(roundedBitmapDrawable);*/

        mRecyclerView = view.findViewById(R.id.my_user_page_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new UserPageAdapter(userInfoWithPosts);
        mRecyclerView.setAdapter(mAdapter);

        ImageButton button = view.findViewById(R.id.write_post);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writePost();
            }
        });


        takeTheUser();

        return view;

    }


    @Override
    public void onStart() {
        super.onStart();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser != null) {
//            String str;
//            str = currentUser.getEmail();
//            String[] tmp = str.split("----");
//            currentUserName = tmp[tmp.length -1];
//            Log.d("user", currentUser.getDisplayName());   //?
//        }
    }

    //Reference to retrieving current user: https://stackoverflow.com/questions/35112204/get-current-user-firebase-android

    public void takeTheUser(){
        userInfoWithPosts.add("efe");
        mAdapter.notifyDataSetChanged();
    }

    public void writePost(){
        //REFERENCE:https://developer.android.com/guide/topics/ui/dialogs.html
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();

        builder.setView(layoutInflater.inflate(R.layout.dialog_post, null))
                .setPositiveButton(R.string.write_post_message, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //SEND TO DATABASE
                        //REFRESH
                    }
                })
                .setNegativeButton(R.string.write_post_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //CANCEL
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}


