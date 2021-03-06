package com.lodestarapp.cs491.lodestar;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.common.StringUtils;

public class PreferencesActivity extends AppCompatActivity {


    Button changePassword, aboutLDSTR, changeUsername , logout;
    DatabaseReference databaseReference;
    FirebaseDatabase db;
    String currentUserName;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        mAuth = FirebaseAuth.getInstance();

        //databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child("2T8K3vARnEY29luq6xS7YdRZ8Bg1").child("username");
        //databaseReference.setValue("bymyside");

        db = FirebaseDatabase.getInstance();
        changePassword = (Button) findViewById(R.id.preferencesButton5);


        changePassword.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                changePasswordPref();
            }
        });

        aboutLDSTR= (Button) findViewById(R.id.aboutLodeStar);

        logout = (Button) findViewById(R.id.logOutButton);

        logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(PreferencesActivity.this,SignInActivity.class);
                startActivity(i);
                Toast.makeText(PreferencesActivity.this,"Succesfully logged out",Toast.LENGTH_LONG).show();
            }
        });


        aboutLDSTR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //retrieved these three lines from stackoverflow.com
                Intent websiteIntent = new Intent("android.intent.action.VIEW",
                        Uri.parse("http://lodestarapp.com/"));
                startActivity(websiteIntent);
            }
        });

        changeUsername = (Button) findViewById(R.id.changeUserName);

        changeUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("agam","calling back to u");
                changeUNAME();
            }
        });

        changeUsername.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) { //Bu degişmeli çünkü db degişti
                //Search the database for a specific user value
                db.getReference("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Log.i("agam",data + "");
                            String str = data + "";
                            String tmpKey = "";

                            String myArr[] = str.split("email=");
                            Log.i("agam","OMYGOD: " + myArr[1]);
                            myArr[1] = myArr[1].substring(0, myArr[1].length() - 3);

                            //Parsing the value in the database
//                            String myArr[] = str.split("e-mail=");
//                            String tmp = myArr[myArr.length - 1];
//                            tmp = removeLastCh(tmp); //Removes the last characther
//                            tmp = tmp.substring(0,tmp.length()-2);
//                            int foccurence = returnCurrentEmailofTheUserUnparsed().indexOf("-");
//                            int foccurence2 = tmp.substring(0,tmp.length()).indexOf("-");
//
//                            String subString = null,substring2 = null;
//                            //Used code in https://stackoverflow.com/questions/7683448/in-java-how-to-get-substring-from-a-string-till-a-character-c for parsing
//                            if (foccurence != -1)
//                            {
//                                subString= returnCurrentEmailofTheUserUnparsed().substring(0 , foccurence);
//                            }
//
//                            if(foccurence2 != -1) {
//                                substring2 = returnCurrentEmailofTheUserUnparsed().substring(0 , foccurence2);
//                            }
//
//
//
//                            Log.i("agam","-----------" + subString + " vs " + substring2);
//
//                            //Return the string value until "-" is encountered
//
//                            if(subString.equals(substring2)) {
//                                //Get the key
//                                tmpKey = str;
//                                tmpKey = tmpKey.substring(tmpKey.indexOf("key = ") + 1);
//                                tmpKey = tmpKey.substring(0, tmpKey.indexOf(", value ="));
//                                tmpKey = tmpKey.substring(5,tmpKey.length());
//                                Log.i("agam","oo yea" + tmpKey);
//
//                                //   databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(tmpKey).child("username");
//                                Log.i("agam","key: " + tmpKey);
//                                //    databaseReference.setValue("bymyside");
//
//                            }

                            changeUNAME();





                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });



    }

    public void meStart(View view){
        finish();
    }

    public void changeUNAME() {
        Intent intent = new Intent(this, ChangeUserActivity.class);
        startActivity(intent);
    }

    public void changePasswordPref() {
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        startActivity(intent);
    }

    public String returnCurrentEmailofTheUserUnparsed() {

        FirebaseUser currentUser = mAuth.getCurrentUser();

        return currentUser.getEmail() + "";
    }//Returns the current email of the user

    public String removeLastCh(String mySTR) {
        String tmp = mySTR.substring(0, mySTR.length() - 1);
        return tmp;
    }//Removes the last characther

}
