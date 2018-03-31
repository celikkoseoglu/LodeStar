package com.lodestarapp.cs491.lodestar;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangeUserActivity extends AppCompatActivity {

    Button changeUsername;
    FirebaseDatabase db;
    FirebaseAuth mAuth;
    EditText newUNAME;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i("agam","1");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user);
        Log.i("agam","2");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        changeUsername = (Button) findViewById(R.id.usernameChangeButton);
        newUNAME = (EditText) findViewById(R.id.usernameChangeEditTxtw);

        changeUsername.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Search the database for a specific user value
                db.getReference("users").addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                    @Override
                    public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                        for (com.google.firebase.database.DataSnapshot data : dataSnapshot.getChildren()) {
                            Log.i("agam",data + "");
                            String str = data + "";
                            String tmpKey = "";

                            //Parsing the value in the database
                            String myArr[] = str.split("e-mail=");
                            String tmp = myArr[myArr.length - 1];
                            tmp = removeLastCh(tmp); //Removes the last characther
                            tmp = tmp.substring(0,tmp.length()-2);
                            int foccurence = returnCurrentEmailofTheUserUnparsed().indexOf("-");
                            int foccurence2 = tmp.substring(0,tmp.length()).indexOf("-");

                            String subString = null,substring2 = null;
                            //Used code in https://stackoverflow.com/questions/7683448/in-java-how-to-get-substring-from-a-string-till-a-character-c for parsing
                            if (foccurence != -1)
                            {
                                subString= returnCurrentEmailofTheUserUnparsed().substring(0 , foccurence);
                            }

                            if(foccurence2 != -1) {
                                substring2 = returnCurrentEmailofTheUserUnparsed().substring(0 , foccurence2);
                            }

                            String myStr = returnCurrentEmailofTheUserUnparsed();
                            String tmpSTRARRAY[] = myStr.split("----");
                            myStr = tmpSTRARRAY[tmpSTRARRAY.length-1];




                            Log.i("agam","-----------" + subString + " vs " + myStr);

                            //Return the string value until "-" is encountered

                            if(subString.equals(substring2)) {
                                //Get the key
                                tmpKey = str;
                                tmpKey = tmpKey.substring(tmpKey.indexOf("key = ") + 1);
                                tmpKey = tmpKey.substring(0, tmpKey.indexOf(", value ="));
                                tmpKey = tmpKey.substring(5,tmpKey.length());
                                Log.i("agam","oo yea" + tmpKey);

                                   databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(tmpKey).child("username");
                                Log.i("agam","key: " + tmpKey);
                                String newUname2 = String.valueOf(newUNAME.getText());


                                databaseReference.setValue(myStr + "/" + newUname2);
                                Toast.makeText(ChangeUserActivity.this,"Congragulations, you have sucessfully changed the username ",Toast.LENGTH_LONG).show();
                                redirectToLogin();
                            }







                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });
    }






    public String returnCurrentEmailofTheUserUnparsed() {

        FirebaseUser currentUser = mAuth.getCurrentUser();

        return currentUser.getEmail() + "";
    }//Returns the current email of the user

    public String removeLastCh(String mySTR) {
        String tmp = mySTR.substring(0, mySTR.length() - 1);
        return tmp;
    }//Removes the last characther

    public void redirectToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

}
