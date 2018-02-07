package com.lodestarapp.cs491.lodestar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.lodestarapp.cs491.lodestar.R;
import com.lodestarapp.cs491.lodestar.SignUpActivity;

public class UserLoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText emailF, passwordF,reEnter;
    private Button signButton;
    private TextView txtViewtoSignUp;
    private ProgressDialog pDialog;
    private FirebaseAuth managerDB;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        //DB Connection
        managerDB = FirebaseAuth.getInstance();

        //To be added later
//        if(managerDB!= null) {
//            finish();
//            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
//        }

        //Intialize Everything

        reEnter = (EditText) findViewById(R.id.reTypeLoginS);
        emailF = (EditText) findViewById(R.id.emailEditLoginS);
        signButton = (Button) findViewById(R.id.SButton);
        passwordF = (EditText) findViewById(R.id.textPasswordLoginS);
        txtViewtoSignUp = (TextView) findViewById(R.id.txtLoginS);
        pDialog= new ProgressDialog(this);

        signButton.setOnClickListener(this);
        txtViewtoSignUp.setOnClickListener(this);

        emailF.setTextColor(Color.WHITE);
        reEnter.setTextColor(Color.WHITE);
        passwordF.setTextColor(Color.WHITE);
        txtViewtoSignUp.setTextColor(Color.WHITE);


    }

    @Override
    public void onClick(View v) {


        if(reEnter != passwordF) {
            Toast.makeText(this,"Please make sure you enter the password correctly",Toast.LENGTH_LONG).show();
            return;
        }

        if(v == txtViewtoSignUp) {
            finish();
            startActivity(new Intent(this,SignUpActivity.class));
        }
        else if(v == signButton) {
            tryToLogIn();
        }

    }

    private void tryToLogIn() {
        String passwordStr = passwordF.getText().toString().trim();
        String emailStr = emailF.getText().toString().trim();
        if (TextUtils.isEmpty(emailStr) || TextUtils.isEmpty(passwordStr)) {
            Toast.makeText(this,"Please make sure you enter the credentials correctly",Toast.LENGTH_LONG).show();
            return; //Finish the function, cannot register
        }
        pDialog.setMessage("Loading...");
        pDialog.show();
        managerDB.signInWithEmailAndPassword(emailStr,passwordStr).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> t) {
                pDialog.dismiss();
                if(t.isSuccessful()) {
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                }
            }
        });

    }
}
