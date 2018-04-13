package com.lodestarapp.cs491.lodestar;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.AbstractSequentialList;
import java.util.ArrayList;
import java.util.Map;

import static android.widget.Toast.LENGTH_LONG;

//Reference: Facebook login-signin documentation and how-do-i-pass-data-between-activities-in-android-application question in stackoverflow is used in some parts of the code
public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private static final String TAG = "consoleMessage";
    private static final int RC_SIGN_IN = 9001;

    private GoogleSignInClient googleSignInClient;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private int dbitemcouner = 0;
  //  private SignUpActivity signUpManager = new SignUpActivity();

    private SignInButton signInButton;

    private Button signInWithEmail;
    private Button userSearchButton;
    private CallbackManager callbackManager;
    private DatabaseReference mDatabase,mDatabase2;
    public ArrayList<String> userList;
    private Firebase fbase;
    String str = "";

    private static String[] strListUser;


    // private TextView alreadySignedInQuestion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase2 = mDatabase.child("users");

        //fbase = new Firebase("https://fir-lodestar.firebaseio.com/heynow");


        FacebookSdk.setApplicationId(getString(R.string.facebook_app_id));
        //initialize Facebook SDK
        FacebookSdk.sdkInitialize(getApplicationContext());
        if (BuildConfig.DEBUG) {
            FacebookSdk.setIsDebugEnabled(true);
            FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        signInWithEmail = (Button) findViewById(R.id.button_email);
        userSearchButton = (Button) findViewById(R.id.userSearch);
        callbackManager = CallbackManager.Factory.create();

        ProfileTracker profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    Profile oldProfile,
                    Profile currentProfile) {
                // App code



            }
        };

        //  alreadySignedInQuestion = (TextView) findViewById(R.id.login_text);
        // alreadySignedInQuestion.setOnClickListener(this);

//        signInWithEmail.setOnClickListener(
//                new Button.OnClickListener(){
//                    public void onClick(View v){
//
//                        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
//                        startActivity(intent);
//                    }
//                }
//        );

        signInButton = (SignInButton) findViewById(R.id.GoogleSignInButton);
        signInButton.setOnClickListener(this);



        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener(){
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(LoginActivity.this, "Error", LENGTH_LONG).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

       // retrieveDBValues();
      //  retrieveDBValues2();



       // strListUser = changeListToArray();

        //Log.i("aga",strListUser[0]);

        //Yeni eklendi, user signed in ise tab activiye yonlederiyor
       if( FirebaseAuth.getInstance().getCurrentUser() != null) {
           Intent i = new Intent(LoginActivity.this,TabActivity.class);
           startActivity(i);
       }

    }

    public  String[] getArray() {
        return strListUser;
    }


//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser != null)
//            updateUI(currentUser);
//    }

    private void updateUI(FirebaseUser currentUser) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                Log.w(TAG, "Kobe1 ");
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.w(TAG, "Kobe2");
                firebaseAuthWithGoogle(account);
                Log.w(TAG, "Kobe3");
            }catch (ApiException e){
                Log.w(TAG, "Google sign in failed: " + e.getStatusCode());
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    updateUI(firebaseUser);
                }
                else {
                    Log.w(TAG,"sign in failure" , task.getException());
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
//        Toast.makeText(this,".(",Toast.LENGTH_LONG).show();
//        if(view == alreadySignedInQuestion) {
//            Toast.makeText(this,".(",Toast.LENGTH_LONG).show();
//            startActivity(new Intent(this,WeatherInformation.class));
//        }


        switch (view.getId()){
            case R.id.GoogleSignInButton:
                signIn();
                break;
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /*private void signOut() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        updateUI(false);
                    }
                }
        );
    }*/

    /*private void revokeAccess() {

    }*/

    //@Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }
    public void userSearchStart(View v) {
        //Toast.makeText(LoginActivity.this, "LOLOL", LENGTH_LONG).show();
        Intent intent = new Intent(LoginActivity.this, SearchUserActivity.class);
        userList = new ArrayList<String>();
       // retrieveDBValues();
        //justAnotherMethod();
        Log.i("aga",dbitemcouner + "counter");
      //  intent.putExtra("key",userList);
        startActivity(intent);
    }

    /**
     * Unity Try
     */
    public void unityGameStart(View view){
        Intent intent = new Intent(this, BrickBreakerActivity.class);
        startActivity(intent);
    }


    /**
     * Weather
     */
    public void panoStart(View view){
        Intent intent = new Intent(this, PanoramaActivity.class);
        startActivity(intent);
    }

    public void tabStart(View view){
        Intent intent = new Intent(this, TabActivity.class);
        startActivity(intent);
    }

    public void placesToSeeStart(View view){
        Intent intent = new Intent(this, PlacesToSeeActivity.class);
        startActivity(intent);
    }

    public void signUpStart(View view) {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    public void signUpPlease(View v) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    public void meStart(View view){
        Intent intent = new Intent(this, UserPage.class);
        startActivity(intent);
    }

    public void VRstart(View view){
        Intent intent = new Intent(this, VRActivity.class);
        startActivity(intent);
    }


//    private void retrieveDBValues() {
//
//
//        mDatabase.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
//            @Override
//            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
//                for (com.google.firebase.database.DataSnapshot child : dataSnapshot.getChildren()) {
//                    for (com.google.firebase.database.DataSnapshot child2 : child.getChildren()) {
//                        userList.add(new User(child2.child("username").toString(),child2.child("e-mail").toString()));
//                       // Log.i("aga", child2.child("e-mail").getValue().toString());
//                    }
//                }
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//}

//    public void retrieveDBValues() {
//        Log.i("aga","gimme an angel");
//        //Toast.makeText(this, "U here",Toast.LENGTH_LONG).show();
//        mDatabase2.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//
//
////        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
////        ref.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
////            @Override
////            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
////              // list.add("")
////                userList.add("" + dataSnapshot.child("users").getChildrenCount() + 1);
////                Log.i("lol",userList.get(0));
////
////            }
////
////            @Override
////            public void onCancelled(DatabaseError databaseError) {
////
////            }
////        });
//
//
//    });
//    }

    public void retrieveDBValues2() {



        return;
    }



        
//        Log.i("aga","mywings");
//                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
//        ref.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
//            @Override
//            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
//              // list.add("")
//                userList.add("" + dataSnapshot.child("users").getChildrenCount() + 1);
//                Log.i("aga",userList.get(0));
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });




    private String[] changeListToArray() {

        String[] tmp = new String[userList.size()];
        Log.i("aga",":((()))" + userList.size());

        for (int i = 0; i < userList.size(); i++) {
            Log.i("aga","kkkkkkkkkk---kkkkkk");
            tmp[i] = userList.get(i);
            Toast.makeText(this, "Burda2",Toast.LENGTH_LONG).show();
            Log.i("a","sup" + tmp[i]);
        }

        return tmp;
    }



}
//Reference: https://stackoverflow.com/questions/44583834/firebase-how-to-check-if-user-is-logged-in?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa & https://firebase.google.com/docs/database/android/read-and-write