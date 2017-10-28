package com.example.mycseapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static DatabaseReference mData ;
    Context con = this ;
    public static String Ausername = "" ;/////////// The changable user name
    public static String Username ="" ; /////////// The User Id invariant
    public static String enumber = "" ; /////////// The events status of user.
    private static final String TAG = "EmailPassword";
    private ProgressBar spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance() ;

        //////// connect to 2 buttons and progressbar.
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setScaleY(3f);
        spinner.setVisibility(View.INVISIBLE);

        /////////////give functionality and set inital spinner state.
        Button r = (Button) findViewById(R.id.authentify);
        r.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /////////get form
                final EditText e = (EditText) findViewById(R.id.E);
                final EditText p = (EditText) findViewById(R.id.P);
                ////////////validate form
                if (!validateForm()) {
                    return;        }
                /////////// check net connectivity
                spinner.setVisibility(View.VISIBLE);
                if(isNetworkAvailable()) {
                 //////////// sign in.
                   signIn(e.getText().toString(), p.getText().toString());
                }
                else
                {
                    //////////show network error
                    Toast.makeText(getApplicationContext(), "Internet Not connected" , Toast.LENGTH_LONG).show();
                    spinner.setVisibility(View.INVISIBLE);
                }



            }
        });
        ////////////Register button
        TextView register = findViewById(R.id.register) ;
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registergo = new Intent(con , Register.class) ;
                startActivity(registergo);
            }
        });



    }
    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        final EditText e = (EditText) findViewById(R.id.E);
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            spinner.setVisibility(View.INVISIBLE);
                                    // yourMethod();
                                   // gete();
                                    Intent intent = new Intent(con, Splash.class);
                                    startActivity(intent);





                        } else {

                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Login Failed " , Toast.LENGTH_LONG).show();  ;
                            spinner.setVisibility(View.INVISIBLE);

                        }


                    }
                });

    }

//////////////// check for internet connection
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    /////////check for valid password submission
    private boolean validateForm() {
        boolean valid = true;
        final EditText e = (EditText) findViewById(R.id.E);
        final EditText p = (EditText) findViewById(R.id.P);
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


}

