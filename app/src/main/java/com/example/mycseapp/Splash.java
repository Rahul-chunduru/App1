package com.example.mycseapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

public class Splash extends AppCompatActivity {
Context con = this ;
    String Username;

    static String Schrodinger ;
    static  String token  ;
    private ProgressBar spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash2);

        /**
         * get user details
         */

        getUsername();

        /**add token for notifications */
         token = FirebaseInstanceId.getInstance().getToken() ;
        FirebaseDatabase.getInstance().getReference().
                child("User_keys").child(token).setValue("true")  ;

        /**get schrodinger(the one that will trigger cloud functions when
         a message is added in a group */
        FirebaseDatabase.getInstance().getReference()
                .child("Schrodinger").addValueEventListener(new ValueEventListener() {
            /**
             *
             * @param dataSnapshot
             */
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               Schrodinger =  dataSnapshot.getValue().toString() ;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /**  and call the gateway function */
        gateway();

    }

    /**
     *
     */
    public void gateway()
    {
        /**check if details are obtained.
         * if obtained , go to home activity, else
         * re run the function
         * */
        if(MainActivity.enumber.equals(""))
        {
            Handler h = new Handler( ) ;
            h.postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                gateway();
                        }
                    } , 1000)  ;
        }
        else
        {
            Intent intent = new Intent(con, Home.class);
            startActivity(intent);
        }
    }

    /** ask for user details and get user name and his event no.
     *  */
    public void getUsername()
    {
        final String U = FirebaseAuth.getInstance().getCurrentUser().getUid() ;
        MainActivity.Username = U ;
        DatabaseReference mData = FirebaseDatabase.getInstance().getReference().child("Users").child(U) ;
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Username = dataSnapshot.child("name").getValue().toString();
                MainActivity.enumber = dataSnapshot.child("event_no").getValue().toString() ;
                MainActivity.Ausername = Username ;


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }) ;
    }

}
