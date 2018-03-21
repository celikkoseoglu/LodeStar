package com.lodestarapp.cs491.lodestar;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class PreferencesActivity extends AppCompatActivity {


    Button changePassword, aboutLDSTR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        changePassword = (Button) findViewById(R.id.preferencesButton5);

        changePassword.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
             changePasswordPref();
            }
        });

        aboutLDSTR= (Button) findViewById(R.id.aboutLodeStar);



        aboutLDSTR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //retrieved these three lines from stackoverflow.com
                Intent websiteIntent = new Intent("android.intent.action.VIEW",
                                Uri.parse("http://lodestarapp.com/"));
                startActivity(websiteIntent);
            }
        });



    }

    public void meStart(View view){
        finish();
    }

    public void changePasswordPref() {
        Toast.makeText(this,"fıocdx",Toast.LENGTH_LONG);
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        startActivity(intent);
    }

}
