package com.lodestarapp.cs491.lodestar;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lodestarapp.cs491.lodestar.Adapters.ADDITIONAL_USER;
import com.lodestarapp.cs491.lodestar.Adapters.UserPageAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.widget.Toast.LENGTH_LONG;

public class UserPage extends android.support.v4.app.Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseAuth mAuth;
    TextView tw,tripLogs,lastTrip;
    private TextView tripCount;
    private EditText posts;
    FirebaseUser user,userMe;
    public FirebaseUser mUser;
    private DatabaseReference mDatabase;

    ADDITIONAL_USER au;
    ADDITIONAL_USER au2;
    DatabaseReference ref;

    private List<String> userInfoWithPosts = new ArrayList<>();

    private final int IMAGE = 1;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_user_page, container, false);

        this.view = view;

        tw = view.findViewById(R.id.me_realName);
        tripLogs = view.findViewById(R.id.me_trip_logs_count);
        tripCount = view.findViewById(R.id.me_trip_count);
        lastTrip = view.findViewById(R.id.me_last_trip_city);
        ImageView profileImageView = (ImageView) view.findViewById(R.id.me_profile_picture);

        /*Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_003_user);
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        roundedBitmapDrawable.setCircular(true);
        profileImageView.setImageDrawable(roundedBitmapDrawable);*/

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mRecyclerView = view.findViewById(R.id.my_user_page_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new UserPageAdapter(userInfoWithPosts);
        mRecyclerView.setAdapter(mAdapter);

        AppCompatButton button = view.findViewById(R.id.write_post);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writePost();
            }
        });

        AppCompatButton button2 = view.findViewById(R.id.profile_picture_add);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImageAndChangeTheProfilePicture();
            }
        });

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        //   ADDITIONAL_USER au = dataSnapshot.getValue(ADDITIONAL_USER.class);
        // Log.i("agam",au.username);
        ref = database.getReference();

        ref.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    au = childSnapshot.getValue(ADDITIONAL_USER.class);
                    user = FirebaseAuth.getInstance().getCurrentUser();

                    if(user.getEmail().equals(au.getemail())) {
                        //  Toast.makeText(this,au.gettrips(),Toast.LENGTH_LONG).show();
                        tw.setText(au.getusername());

                        if(au.gettrips() != null && au.gettrips().contains("!")) {
                            tripCount.setText(au.gettrips().split("!").length + "");
                        } else {
                            tripCount.setText("0");
                        }


                        if(au.gettrips() != null) {
                            String str = au.gettrips();
                            String myTMPARR[];

                            if(str.contains("To: ")) {
                                myTMPARR = str.split("To: ");
                               // Log.i("agam","bune: " + myTMPARR[1]);
                                lastTrip.setText(myTMPARR[1].substring(0,3));
                            }
                            else {
                                lastTrip.setText("N/A");
                            }


                        }

                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        au2 = new ADDITIONAL_USER();
        //   ADDITIONAL_USER au = dataSnapshot.getValue(ADDITIONAL_USER.class);
        // Log.i("agam",au.username);
        ref = database.getReference();

        //userInfoWithPosts = new ArrayList<>();
        ref.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    au2 = childSnapshot.getValue(ADDITIONAL_USER.class);
                    user = FirebaseAuth.getInstance().getCurrentUser();

                    if(user.getEmail().equals(au2.getemail())) {
                        String toBeParsed = au2.getposts();

                        if(au2.getposts() != null) {
                            String tmpArray[] = toBeParsed.split("&&&");
                            tripLogs.setText(tmpArray.length + "");
                            for(int i = 0; i < tmpArray.length;i++) {
                                userInfoWithPosts.add(tmpArray[i]);
                                //Log.i("agam","bariscim: " + noteArrayList.get(i));
                            }
                        }

                    }
                }
                mAdapter.notifyDataSetChanged();
                userInfoWithPosts = new ArrayList<>();
                //UserPage.this.onCreate(null);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        return view;

    }


    @Override
    public void onStart() {
        super.onStart();
    }

    //Reference to retrieving current user: https://stackoverflow.com/questions/35112204/get-current-user-firebase-android

    public void writePost(){
        //REFERENCE:https://developer.android.com/guide/topics/ui/dialogs.html
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        int t = 0;
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        //Reference: https://stackoverflow.com/questions/44212583/why-am-i-getting-edittext-gettext-on-a-null-object-reference
        View dialogView = LayoutInflater.from(getActivity().getApplicationContext())
                .inflate(R.layout.dialog_post, null);
        posts = dialogView.findViewById(R.id.user_post_EditText);
        builder.setView(dialogView)
                .setPositiveButton(R.string.write_post_message, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        //   ADDITIONAL_USER au = dataSnapshot.getValue(ADDITIONAL_USER.class);
                         Log.i("agam",posts.getText().toString());
                        ref = database.getReference();
                        userMe = FirebaseAuth.getInstance().getCurrentUser();
                        ref.child("users").addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                                    au = childSnapshot.getValue(ADDITIONAL_USER.class);

                                    Log.i("agam",userMe.getEmail() + " vs " + au.getemail());
                                    if(userMe.getEmail().equals(au.getemail())) {
                                        String str = "";
                                        if(au.getposts() != null)
                                            str = au.getposts();

                                        mDatabase.child("users").child(childSnapshot.getKey()).child("posts").setValue(posts.getText().toString() + "&&&" + str);
                            
                                    }
                                }
                            posts.setText("");
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                })
                .setNegativeButton(R.string.write_post_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //REFERENCE: https://developer.android.com/reference/android/content/Intent.html
    public void pickImageAndChangeTheProfilePicture(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a photo for your profile"), IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == IMAGE){
            if (resultCode == RESULT_OK){
                Uri uri = data.getData();
                ImageView view3 = this.view.findViewById(R.id.me_profile_picture);
                view3.setImageURI(uri);
            }
        }
    }
}


