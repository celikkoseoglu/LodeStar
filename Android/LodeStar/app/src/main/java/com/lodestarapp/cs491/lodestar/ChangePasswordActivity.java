package com.lodestarapp.cs491.lodestar;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth managerDB;
    private Button b4;
    private EditText etext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        b4 = (Button) findViewById(R.id.RegButton98);
        etext = (EditText) findViewById(R.id.emailEdit75);

        b4.setOnClickListener(this);

        managerDB = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        if(v == b4) {
            //    Toast.makeText(this,"Please make sure you enter the credentials correctly",Toast.LENGTH_LONG).show();
            forgotPassword();
        }
    }


    private void forgotPassword() {
        String emailAddress = String.valueOf(etext.getText());
        //Toast.makeText(this,"Please make sure you enter the credentials correctly",Toast.LENGTH_LONG).show();

        managerDB.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Toast.makeText(ForgotPasswordActivity.this,"LOL",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
