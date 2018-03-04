package com.lodestarapp.cs491.lodestar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.lodestarapp.cs491.lodestar.Adapters.User;

import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "asasa";
    //Declerations
    private ProgressDialog dialog; //To be used after registering
    private EditText emailField,passwordField,reTypeField;
    private Button registerButton;
    private TextView txtViewSignIn;
    private FirebaseAuth authManager;
   // private DatabaseReference mDatabase,mDatabase2;
    private EditText uName;
    private ArrayList<String> userList = new ArrayList<String>();

    //   private boolean isDBAuthanticated; //Check if DB is authenticated

    private DatabaseReference mDatabase;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Toast.makeText(this,"Nerdesin gozum",Toast.LENGTH_LONG).show();
        super.onCreate(savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance().getReference();


        // isDBAuthanticated = false;
        initializeDB();
        authManager = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_sign_up);

        //Bind Buttons & EditText & TextViews & bars
        uName = (EditText) findViewById(R.id.userName);
        registerButton = (Button) findViewById(R.id.RegButton);
        txtViewSignIn = (TextView) findViewById(R.id.textSignin);
        reTypeField = (EditText) findViewById(R.id.reTypePassword);
        emailField = (EditText) findViewById(R.id.emailEdit);
        passwordField = (EditText) findViewById(R.id.textPassword);
        dialog = new ProgressDialog(this);

        //Set Listeners


        //mDatabase = FirebaseDatabase.getInstance().getReference();

        registerButton.setOnClickListener(this);
        txtViewSignIn.setOnClickListener(this);
        retrieveDBValues();

    }

    private void retrieveDBValues() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
        ref.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                ArrayList<String> list = new ArrayList<String>();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


//
//        Firebase ref = new Firebase("https://fir-lodestar.firebaseio.com/");
//
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                Log.e("Count " ,""+snapshot.getChildrenCount());
//
//            }
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//                Log.e("The read failed: " ,firebaseError.getMessage());
//            }
//        });




    private void writeNewUser(String uid, String userName, String email) {

        mDatabase.child("users").child(uid).child("username").setValue(userName);
        mDatabase.child("users").child(uid).child("e-mail").setValue(email);

//        User toBeWrittenToDB = new User(userName,email);
//      //  mDatabase.child("users").child(uid).setValue(toBeWrittenToDB);
//        ArrayList<String> userNames = new ArrayList<>();
//        userNames.add(userName);
//       // mDatabase.child("usernamelist").setValue(userNames);
//        Toast.makeText(this,"AROG",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {

        String str1 = String.valueOf(reTypeField.getText());

        String str2 = String.valueOf(passwordField.getText());

        if(!str1.equals(str2)) {
            Toast.makeText(this,"Please check the password!",Toast.LENGTH_LONG).show();
        }
        int isRegisteredSuccessfully = 0;
        if(v == registerButton) {
            //if register button is click, try to register!
            isRegisteredSuccessfully = tryToRegister();
        }
        else if(v == txtViewSignIn) {
            Toast.makeText(this,".(",Toast.LENGTH_LONG).show();
            startActivity(new Intent(this,LoginActivity.class));
        }



    }

    private int tryToRegister() {
        //Get authentication
        String passwordStr = passwordField.getText().toString().trim();
        String emailStr = emailField.getText().toString().trim();


        if (TextUtils.isEmpty(emailStr) || TextUtils.isEmpty(passwordStr)) {

            Toast.makeText(this,"Please make sure you enter the credentials correctly",Toast.LENGTH_LONG).show();
            return -27; //Finish the function, cannot register

        }
        String myUName = String.valueOf(uName.getText());

        String myemail = String.valueOf(emailField.getText());
        final User u;
        u = new User(myUName,myemail);

        dialog.setMessage("Please wait :) ");
        dialog.show();
        authManager.createUserWithEmailAndPassword(emailStr,passwordStr).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> t) {
                if(t.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this,u.getEmail(),Toast.LENGTH_SHORT).show();
                    u.uid = t.getResult().getUser().getUid();
                    writeNewUser(u.getUid(),u.getName(),u.getEmail());
                 //   createNewUser(u);
                    //  startActivity(new Intent(this,UserLoginActivity.class));
                   // finish();
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                }
                else {
                    Toast.makeText(SignUpActivity.this,"Oooops Please Try Again :( ",Toast.LENGTH_SHORT).show();
                }

            }
        }); // Inster to database
        dialog.dismiss();



//        Toast.makeText(this,"AROG",Toast.LENGTH_LONG).show();
//        final String a;
//        String t;
//        int i = 0;
//        mDatabase.child("users");
//        mDatabase.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
//            @Override
//            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
//                final String a;
//                a = "" + dataSnapshot.getChildrenCount();
//                Log.d(TAG,"whatever it takes" + a);
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });


        return 1;
    } //returns a positive number if registered successfully

    public int tryToRegister2(String password1,String email1) {
        //Get authentication
        String passwordStr = password1;
        String emailStr = email1;

        if (TextUtils.isEmpty(emailStr) || TextUtils.isEmpty(passwordStr)) {

            Toast.makeText(this,"Please make sure you enter the credentials correctly",Toast.LENGTH_LONG).show();
            return -27; //Finish the function, cannot register

        }

        dialog.setMessage("Please wait :) ");

        String myUName = String.valueOf(uName.getText());

        String myemail = String.valueOf(emailField.getText());
        final User u;
        u = new User(myUName,myemail);

        dialog.show();
        authManager.createUserWithEmailAndPassword(emailStr,passwordStr).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> t) {
                if(t.isSuccessful()) {

                    //  startActivity(new Intent(this,UserLoginActivity.class));

                    u.uid = t.getResult().getUser().getUid();
                    createNewUser(u);
                    finish();
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));

                }
                else {
                    Toast.makeText(SignUpActivity.this,"Oooops Please Try Again :( ",Toast.LENGTH_SHORT).show();
                }

            }
        }); // Inster to database
        dialog.dismiss();
        return 1;
    } //returns a positive number if registered successfully

    public void initializeDB() {
        //  fa = FirebaseAuth.getInstance();
        //      isDBAuthanticated = true;
    } //Initializes the database

    private void createNewUser(User userFromRegistration) {
        String username = String.valueOf(uName.getText());
        String email = userFromRegistration.getEmail();
        String userId = userFromRegistration.getUid();

        User user = new User(username, email);

      //
        //  mDatabase.child("users").child(userId).setValue(user);

        Toast.makeText(SignUpActivity.this,"Databse ekledim ",Toast.LENGTH_SHORT).show();

    }


}
