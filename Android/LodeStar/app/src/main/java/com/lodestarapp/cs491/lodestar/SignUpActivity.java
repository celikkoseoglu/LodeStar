package com.lodestarapp.cs491.lodestar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    //Declerations
    private ProgressDialog dialog; //To be used after registering
    private EditText emailField,passwordField,reTypeField;
    private Button registerButton;
    private TextView txtViewSignIn;
    private FirebaseAuth authManager;
    //   private boolean isDBAuthanticated; //Check if DB is authenticated



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Toast.makeText(this,"Nerdesin gozum",Toast.LENGTH_LONG).show();
        super.onCreate(savedInstanceState);
        // isDBAuthanticated = false;
        initializeDB();
        authManager = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_sign_up);

        //Bind Buttons & EditText & TextViews & bars
        registerButton = (Button) findViewById(R.id.RegButton);
        txtViewSignIn = (TextView) findViewById(R.id.textSignin);
        reTypeField = (EditText) findViewById(R.id.reTypePassword);
        emailField = (EditText) findViewById(R.id.emailEdit);
        passwordField = (EditText) findViewById(R.id.textPassword);
        dialog = new ProgressDialog(this);

        //Set Listeners
        registerButton.setOnClickListener(this);
        txtViewSignIn.setOnClickListener(this);


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

        dialog.setMessage("Please wait :) ");
        dialog.show();
        authManager.createUserWithEmailAndPassword(emailStr,passwordStr).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> t) {
                if(t.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this,"Congrats! You can now explore anywhere with LodeStar!",Toast.LENGTH_SHORT).show();
                    //  startActivity(new Intent(this,UserLoginActivity.class));
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

    public int tryToRegister2(String password1,String email1) {
        //Get authentication
        String passwordStr = password1;
        String emailStr = email1;

        if (TextUtils.isEmpty(emailStr) || TextUtils.isEmpty(passwordStr)) {

            Toast.makeText(this,"Please make sure you enter the credentials correctly",Toast.LENGTH_LONG).show();
            return -27; //Finish the function, cannot register

        }

        dialog.setMessage("Please wait :) ");
        dialog.show();
        authManager.createUserWithEmailAndPassword(emailStr,passwordStr).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> t) {
                if(t.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this,"Congrats! You can now explore anywhere with LodeStar!",Toast.LENGTH_SHORT).show();
                    //  startActivity(new Intent(this,UserLoginActivity.class));
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


}
