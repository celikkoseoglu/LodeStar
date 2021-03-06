package com.lodestarapp.cs491.lodestar;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
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
import java.util.Random;

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
    Uri myUri = null;
    ListView listView;
    ADDITIONAL_USER au;
    ADDITIONAL_USER au3;
    ADDITIONAL_USER au2,au4;
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

    public void profilePicAdd() {

        if(myUri != null) {
            //Save user photo to database
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            ref = database.getReference();

            ref.child("users").addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                        au3 = childSnapshot.getValue(ADDITIONAL_USER.class);
                        userMe = FirebaseAuth.getInstance().getCurrentUser();


                        if(userMe.getEmail().equals(au3.getemail())) {

                            String str = "";

                            if(au3.getphotouris() != null)
                                str = au3.getposts();

                            mDatabase.child("users").child(childSnapshot.getKey()).child("photouris").setValue(myUri);

                        }


                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {




        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        final View view = inflater.inflate(R.layout.activity_user_page, container, false);

        final ImageView view3 = view.findViewById(R.id.me_profile_picture);


        this.view = view;

       String url = "https://firebasestorage.googleapis.com/v0/b/firebase-lodestar.appspot.com/o/download.png?alt=media&token=504973c5-01b1-4649-9a34-f938980854de";

       Glide.with(getContext()).load(url).into(view3);


      //  final Button avatarButton = view.findViewById(R.id.button11);

        //Give a random avatar Reference: https://stackoverflow.com/questions/6029495/how-can-i-generate-random-number-in-specific-range-in-android
        int min = 1;
        int max = 4;

        Random r = new Random();
        final int i1 = r.nextInt(max - min + 1) + min;

//
//        ref = database.getReference();
//
//        ref.child("users").addValueEventListener(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
//                    au4 = childSnapshot.getValue(ADDITIONAL_USER.class);
//                    user = FirebaseAuth.getInstance().getCurrentUser();
//
//                    Log.i("agam",user.getEmail() + " vs atalim " + au4.getemail());
//
//                    if(user != null && au4 != null && au4.getemail().equals(user.getEmail())){
//
//                       // if(au4.getphotouris() == null) {
//                        //    if(i1 == 1) {
//                             //   String url = "https://firebasestorage.googleapis.com/v0/b/firebase-lodestar.appspot.com/o/download.png?alt=media&token=504973c5-01b1-4649-9a34-f938980854de";
//                           //     Glide.clear(view3);
//                             //   Glide.with(getContext()).load(url).into(view3);
//                          /*      mDatabase.child("users").child(childSnapshot.getKey()).child("photouris").setValue("" + i1);
//                            }else if(i1 == 2){
//                                String url = "https://firebasestorage.googleapis.com/v0/b/firebase-lodestar.appspot.com/o/maxresdefault.jpg?alt=media&token=6456ce90-64fc-43d7-bd94-aa99494501f4";
//                                Glide.clear(view3);
//                                Glide.with(getContext()).load(url).into(view3);
//                                mDatabase.child("users").child(childSnapshot.getKey()).child("photouris").setValue("" + i1);
//                            }else if(i1 == 3){
//                                String url = "https://firebasestorage.googleapis.com/v0/b/firebase-lodestar.appspot.com/o/maxresdefault.jpg?alt=media&token=6456ce90-64fc-43d7-bd94-aa99494501f4";
//                                Glide.clear(view3);
//                                Glide.with(getContext()).load(url).into(view3);
//                                mDatabase.child("users").child(childSnapshot.getKey()).child("photouris").setValue("" + i1);
//                            }else if(i1 == 4) {
//                                String url = "https://firebasestorage.googleapis.com/v0/b/firebase-lodestar.appspot.com/o/maxresdefault.jpg?alt=media&token=6456ce90-64fc-43d7-bd94-aa99494501f4";
//                                Glide.clear(view3);
//                                Glide.with(getContext()).load(url).into(view3);
//                                mDatabase.child("users").child(childSnapshot.getKey()).child("photouris").setValue("" + i1);
//                            }
//                      //  }
//                       /* else {
//                            String str = au4.getphotouris();
//
//                            if(str.equals("1")) {
//                                String url = "https://firebasestorage.googleapis.com/v0/b/firebase-lodestar.appspot.com/o/LodeStar-GroupPhoto.jpg?alt=media&token=0f9a3665-f90e-4433-b6a0-853db737c260";
//                                Glide.clear(view3);
//                                Glide.with(getContext()).load(url).into(view3);
//                                mDatabase.child("users").child(childSnapshot.getKey()).child("photouris").setValue("" + i1);
//                            }else if(str.equals("2")){
//                                String url = "https://firebasestorage.googleapis.com/v0/b/firebase-lodestar.appspot.com/o/maxresdefault.jpg?alt=media&token=6456ce90-64fc-43d7-bd94-aa99494501f4";
//                                Glide.clear(view3);
//                                Glide.with(getContext()).load(url).into(view3);
//                                mDatabase.child("users").child(childSnapshot.getKey()).child("photouris").setValue("" + i1);
//                            }else if(str.equals("3")){
//                                String url = "https://firebasestorage.googleapis.com/v0/b/firebase-lodestar.appspot.com/o/maxresdefault.jpg?alt=media&token=6456ce90-64fc-43d7-bd94-aa99494501f4";
//                                Glide.clear(view3);
//                                Glide.with(getContext()).load(url).into(view3);
//                                mDatabase.child("users").child(childSnapshot.getKey()).child("photouris").setValue("" + i1);
//                            }else if(str.equals("4")) {
//                                String url = "https://firebasestorage.googleapis.com/v0/b/firebase-lodestar.appspot.com/o/maxresdefault.jpg?alt=media&token=6456ce90-64fc-43d7-bd94-aa99494501f4";
//                                Glide.clear(view3);
//                                Glide.with(getContext()).load(url).into(view3);
//                                mDatabase.child("users").child(childSnapshot.getKey()).child("photouris").setValue("" + i1);
//                            }
//                        }
//*/
//
//
//                    }
//
//                }
//
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//
//
//        listView = new ListView(view.getContext());
//
//        String items[] = {"Avatar 1","Avatar 2","Avatar 3", "Avatar 4"};
//
//        ArrayAdapter<String> adapter=new ArrayAdapter<String>(view.getContext(),
//                R.layout.list_item, R.id.txtitem,items);
//        listView.setAdapter(adapter);
//
//        listView.setOnItemClickListener(new
//                                                AdapterView.OnItemClickListener() {
//
//                                                    @Override
//
//                                                    public void onItemClick(AdapterView<?> parent, View view, int
//                                                            position, long id) {
//
//                                                        ViewGroup vg=(ViewGroup)view;
//
//                                                        final TextView txt=(TextView)vg.findViewById(R.id.txtitem);
//
//                                                        Toast.makeText(view.getContext(),"You chose " + txt.getText().toString(),Toast.LENGTH_LONG).show();
//
//                                                        //DB islemleri
//
//                                                        ref = database.getReference();
//
//                                                        ref.child("users").addValueEventListener(new ValueEventListener() {
//
//                                                            @Override
//                                                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                                                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
//                                                                    au4 = childSnapshot.getValue(ADDITIONAL_USER.class);
//                                                                    user = FirebaseAuth.getInstance().getCurrentUser();
//
//                                                                    if(user != null && au4 != null && au4.getemail().equals(user.getEmail())){
//                                                                        Log.i("agam","Photo URIS" + txt.getText().toString());
//
//                                                                        mDatabase.child("users").child(childSnapshot.getKey()).child("photouris").setValue(txt.getText().toString());
//                                                                    }
//
//                                                                }
//
//
//                                                            }
//
//                                                            @Override
//                                                            public void onCancelled(DatabaseError databaseError) {
//
//                                                            }
//                                                        });
//
//
//
//                                                    }
//
//                                                });
//
//
//
//        avatarButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDialogListView(view);


/*
                ref = database.getReference();

                final boolean[] wasInside = {false};

                ref.child("users").addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                            au4 = childSnapshot.getValue(ADDITIONAL_USER.class);
                            user = FirebaseAuth.getInstance().getCurrentUser();

                            if(wasInside[0])
                                return;

                            Log.i("agam",user.getEmail() + " vs atalim " + au4.getemail());

                            if(user != null && au4 != null && au4.getemail().equals(user.getEmail())){

                                if(user != null && au4 != null && au4.getemail().equals(user.getEmail())){

                                    if(au4.getphotouris().equals("Avatar 1")) {
                                        String url = "https://firebasestorage.googleapis.com/v0/b/firebase-lodestar.appspot.com/o/LodeStar-GroupPhoto.jpg?alt=media&token=0f9a3665-f90e-4433-b6a0-853db737c260";
                                        Glide.with(view.getContext()).load(url).into(view3);
                                        mDatabase.child("users").child(childSnapshot.getKey()).child("photouris").setValue("Avatar 1");
                                        wasInside[0] = true;
                                    }else if(au4.getphotouris().equals("Avatar 2")){
                                        String url = "https://firebasestorage.googleapis.com/v0/b/firebase-lodestar.appspot.com/o/LodeStar-GroupPhoto.jpg?alt=media&token=0f9a3665-f90e-4433-b6a0-853db737c260";
                                        Glide.with(view.getContext()).load(url).into(view3);
                                        mDatabase.child("users").child(childSnapshot.getKey()).child("photouris").setValue("Avatar 2");
                                        wasInside[0] = true;
                                    } else if(au4.getphotouris().equals("Avatar 3")){
                                        String url = "https://firebasestorage.googleapis.com/v0/b/firebase-lodestar.appspot.com/o/LodeStar-GroupPhoto.jpg?alt=media&token=0f9a3665-f90e-4433-b6a0-853db737c260";
                                        Glide.with(view.getContext()).load(url).into(view3);
                                        mDatabase.child("users").child(childSnapshot.getKey()).child("photouris").setValue("Avatar 3");
                                        wasInside[0] = true;
                                    }else if(au4.getphotouris().equals("Avatar 4")) {
                                        String url = "https://firebasestorage.googleapis.com/v0/b/firebase-lodestar.appspot.com/o/maxresdefault.jpg?alt=media&token=6456ce90-64fc-43d7-bd94-aa99494501f4";
                                        Glide.with(view.getContext()).load(url).into(view3);
                                        mDatabase.child("users").child(childSnapshot.getKey()).child("photouris").setValue("Avatar 4");
                                        wasInside[0] = true;
                                    }

                                }

                              //  }

                                //        profilePhoto.setImageURI(Uri.parse(au4.getphotouris()));
                            }

                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                */

                //view3.setImageURI(uri);
                //view3.setImageURI(Uri.parse("https://firebasestorage.googleapis.com/v0/b/firebase-lodestar.appspot.com/o/LodeStar-GroupPhoto.jpg?alt=media&token=0f9a3665-f90e-4433-b6a0-853db737c260"));



//
//            }
//        });
//






        //Bastaki imageı doldur
       // final ImageView profilePhoto = this.view.findViewById(R.id.me_profile_picture);

        final String[] userEmail = {""};
        final String[] uNAME = {""};

        ref = database.getReference();

        ref.child("users").addValueEventListener(new ValueEventListener() {

                                                     @Override
                                                     public void onDataChange(DataSnapshot dataSnapshot) {
                                                         for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                                                             au4 = childSnapshot.getValue(ADDITIONAL_USER.class);
                                                             user = FirebaseAuth.getInstance().getCurrentUser();
//
                                                             if(user != null && au4 != null && au4.getemail().equals(user.getEmail())){
                                                                    userEmail[0] = "" + au4.getemail();
                                                                    uNAME[0] = "" + au4.getusername();

                                                                 Log.i("agam","usename" + uNAME[0]);

                                                                 mAdapter = new UserPageAdapter(userInfoWithPosts,uNAME[0],userEmail[0],getContext());
                                                                 mRecyclerView.setAdapter(mAdapter);

                                                             }
                                                         }

                                                     }

                                                     @Override
                                                     public void onCancelled(DatabaseError databaseError) {

                                                     }
                                                 });


        //view3.setImageURI(uri);

        //------------------

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


        //Databaseden cek username ve email





        AppCompatButton button = view.findViewById(R.id.write_post);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writePost();
            }
        });

//        AppCompatButton button2 = view.findViewById(R.id.profile_picture_add);
//        button2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                profilePicAdd();
//                pickImageAndChangeTheProfilePicture();
//            }
//        });


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

                        if(au.gettrips() != null) {
                            if(au.gettrips().split("!").length == 0)
                                tripCount.setText("0");
                            else
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

                    if(user != null && user.getEmail().equals(au2.getemail())) {
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


    public void showDialogListView(View view){

        AlertDialog.Builder builder=new
                AlertDialog.Builder(view.getContext());

        builder.setCancelable(true);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        builder.setView(listView);

        AlertDialog dialog=builder.create();


        dialog.show();



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
        Log.i("agam","bu babam icin");
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a photo for your profile"), IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == IMAGE){
            if (resultCode == RESULT_OK){
                final Uri uri = data.getData();


              //  Log.i("agam","uri: " + uri);
               // myUri = uri;
            }
        }
    }
}


