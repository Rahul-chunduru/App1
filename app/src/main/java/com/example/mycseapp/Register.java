package com.example.mycseapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
    FirebaseAuth mAuth ;
    String TAG = "Register" ;
    Context con = this ;
    String Username ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance() ;
        final EditText email = findViewById(R.id.mail) ;
        final EditText pass = findViewById(R.id.password) ;


        Button reg = findViewById(R.id.register) ;
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(validateForm())) {
                    return;
                }
                createAccount( email.getText().toString() , pass.getText().toString());
            }
        });



    }
    private boolean validateForm() {
        boolean valid = true;
        final EditText e = (EditText) findViewById(R.id.E);
        final EditText p = (EditText) findViewById(R.id.password);
        String email = e.getText().toString();
        if (TextUtils.isEmpty(email)) {
            e.setError("Required.");
            valid = false;
        } else {
            e.setError(null);
        }

        String password = p.getText().toString();
        if (TextUtils.isEmpty(password)) {
            p.setError("Required.");
            valid = false;
        } else {
            p.setError(null);
        }

        return valid;
    }
    private void createAccount(String email, String password) {
        // Create Account with the email name and password
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            Log.d(TAG, "createUserWithEmail:success");
                            /////////Set default settings for user.
                            FirebaseUser user = mAuth.getCurrentUser();
                            EditText use = findViewById(R.id.E) ;
                            String Uid = user.getUid() ;
                            /////////add user to all users (to know users and their names)
                            FirebaseDatabase.getInstance().getReference().
                                    child("AllUsers").child(Uid).setValue(use.getText().toString()) ;
                            ///// add user to Users to get groups and event no of user when required
                            FirebaseDatabase.getInstance().getReference().
                                    child("Users").child(Uid).child("name").setValue(use.getText().toString()) ;
                            //////////set default background info to user
                            FirebaseDatabase.getInstance().getReference()
                                    .child("Background").child(Uid).child("Background").
                                    child("e").setValue("0") ;
                            FirebaseDatabase.getInstance().getReference()
                                    .child("Background").child(Uid).child("Background").
                                    child("g").setValue("0") ;
                            ///////////set 0 as event no for user
                            FirebaseDatabase.getInstance().getReference()
                                    .child("Users").child(Uid).child("event_no").
                                    setValue("0") ;
                            ///////////initialise his notes account
                            FirebaseDatabase.getInstance().getReference()
                                    .child("Notes").child(Uid).setValue("") ;
                            Toast.makeText(con, "Successfully registered",
                                    Toast.LENGTH_SHORT).show();
                            Handler  h = new Handler() ;
                            h.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                 startActivity( new Intent(getApplicationContext() , MainActivity.class));
                                }
                            } , 1000) ;

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(con, "Registration failed. \n You may have been already registered",
                                    Toast.LENGTH_SHORT).show();

                        }


                    }
                });
        // [END create_user_with_email]
    }
}
