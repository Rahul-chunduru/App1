package com.example.mycseapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/** this class represent the change password page
 *
 */
public class ChangePassword extends AppCompatActivity {
    String TAG = "password" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser() ;
/** Get auth credentials from the user for re-authentication. The example below shows
 email and password credentials but there are multiple possible providers,
 such as GoogleAuthProvider or FacebookAuthProvider.
*/


/** Prompt the user to re-provide their sign-in credentials
 *
 */
        Button b = (Button) findViewById(R.id.confirm) ;
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText op = findViewById(R.id.op) ;
                EditText np = findViewById(R.id.np) ;
               final String newPass = np.getText().toString();
                String p = op.getText().toString() ;
                AuthCredential credential = EmailAuthProvider
                        .getCredential(user.getEmail(), p);

/** Update password with update password function */
                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "Password updated");
                                                Toast.makeText(getApplicationContext(), "Done" , Toast.LENGTH_LONG).show(); ;
                                            } else {
                                                Log.d(TAG, "Error password not updated , Make sure that password has 6 characters") ;
                                                Toast.makeText(getApplicationContext(), "Not Done" , Toast.LENGTH_LONG).show(); ;
                                            }
                                        }
                                    });
                                } else {
                                    Log.d(TAG, "Error auth failed") ;
                                    Toast.makeText(getApplicationContext(), "NOt there" , Toast.LENGTH_LONG).show();
                                }
                            }
                        });

            }
        });
    }


}
