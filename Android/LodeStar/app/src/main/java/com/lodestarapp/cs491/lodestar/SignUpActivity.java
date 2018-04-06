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

import org.w3c.dom.Text;

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

    public TextView passwordText;

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
        passwordText = (TextView) findViewById(R.id.textPassword1);
        passwordText.setVisibility((View.GONE));
        //Set Listeners


        //mDatabase = FirebaseDatabase.getInstance().getReference();

        registerButton.setOnClickListener(this);
        txtViewSignIn.setOnClickListener(this);
        retrieveDBValues();

        passwordField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    passwordText.setVisibility((View.VISIBLE));
                } else {
                    passwordText.setVisibility((View.GONE));
                }
            }
        });

        reTypeField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    passwordText.setVisibility((View.VISIBLE));
                } else {
                    passwordText.setVisibility((View.GONE));
                }
            }
        });

    }

    private void retrieveDBValues() {


        mDatabase.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                for (com.google.firebase.database.DataSnapshot child : dataSnapshot.getChildren()) {
                    for (com.google.firebase.database.DataSnapshot child2 : child.getChildren()) {
                        Log.i("aga",child2.child("e-mail").toString());
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }







    private void writeNewUser(String uid, String userName, String email) {

        mDatabase.child("users").child(uid).child("username").setValue(userName);
        mDatabase.child("users").child(uid).child("e-mail").setValue(email);
    }

    @Override
    public void onClick(View v) {

        String str1 = String.valueOf(reTypeField.getText());

        String str2 = String.valueOf(passwordField.getText());

        if(!str1.equals(str2)) {
            Toast.makeText(this,"Passwords Do Not Match",Toast.LENGTH_LONG).show();
        }
        int isRegisteredSuccessfully = 0;
        if(v == registerButton) {
            //if register button is click, try to register!
            isRegisteredSuccessfully = tryToRegister();
        }
        else if(v == txtViewSignIn) {
            //Toast.makeText(this,".(",Toast.LENGTH_LONG).show();
            //startActivity(new Intent(this,LoginActivity.class));
        }



    }

    private int tryToRegister() {
        //Get authentication
        String myUName = String.valueOf(uName.getText());
        final String passwordStr = passwordField.getText().toString().trim();
        //String emailStr = emailField.getText().toString().trim() + "----" + myUName;
        final String emailStr = emailField.getText().toString().trim();


        if (TextUtils.isEmpty(emailStr) || TextUtils.isEmpty(passwordStr)) {

            Toast.makeText(this,"Please make sure you enter the credentials correctly",Toast.LENGTH_LONG).show();
            return -27; //Finish the function, cannot register

        }


        String myemail = String.valueOf(emailField.getText());
        final User u;
        u = new User(myUName,myemail);

        dialog.setMessage("Please wait :) ");
        dialog.show();
        final String myTMp = emailStr;
        final String pswrdSTR = passwordStr;
        authManager.createUserWithEmailAndPassword(emailStr,passwordStr).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> t) {

                if(t.isSuccessful()) {
                    //Toast.makeText(SignUpActivity.this,u.getEmail(),Toast.LENGTH_SHORT).show();
                    u.uid = t.getResult().getUser().getUid();
                    writeNewUser(u.getUid(),u.getName(),u.getEmail()); // removed writing to the username too
                 //   createNewUser(u);
                    //  startActivity(new Intent(this,UserLoginActivity.class));
                   // finish();
                    startActivity(new Intent(getApplicationContext(),TabActivity.class));
                }
                else {
                    if(!myTMp.contains("@") || !myTMp.contains(".") )
                        Toast.makeText(SignUpActivity.this,"Please Correct the E-mail Structure",Toast.LENGTH_SHORT).show();
                    if(pswrdSTR.length() < 9)
                        Toast.makeText(SignUpActivity.this,"Please Correct the Length of The Password",Toast.LENGTH_SHORT).show();
                    if(pswrdSTR.equals(pswrdSTR.toLowerCase()))
                        Toast.makeText(SignUpActivity.this,"Please Make Sure You Use Uppercase Letters in your Password",Toast.LENGTH_SHORT).show();
                    if(pswrdSTR.equals(pswrdSTR.toUpperCase()))
                        Toast.makeText(SignUpActivity.this,"Please Make Sure You Use Lowercase Letters in your Password",Toast.LENGTH_SHORT).show();
                    if(!pswrdSTR.matches(".*\\d+.*"))
                        Toast.makeText(SignUpActivity.this,"Please Make Sure You Use Numbers in your Password",Toast.LENGTH_SHORT).show();
                    else{
                        Toast.makeText(SignUpActivity.this,"Please check your internet connection and be sure to not use the same E-mail adress for different accounts",Toast.LENGTH_SHORT).show();
                    }



                }

            }
        }); // Inster to database
        dialog.dismiss();



        return 1;
    } //returns a positive number if registered successfully

    public int tryToRegister2(String password1,String email1) {
        //Get authentication
        String passwordStr = password1;
        final String emailStr = email1;

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
                    startActivity(new Intent(getApplicationContext(),SignUpActivity.class));

                }
                else {
                    if(!emailStr.contains("@"))
                        Toast.makeText(SignUpActivity.this,"Please Correct the E-mail Structure",Toast.LENGTH_SHORT).show();
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

//Some ideas mentioned in https://stackoverflow.com/questions/35112204/get-current-user-firebase-android are used