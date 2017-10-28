    package com.example.mycseapp;

    import android.os.Bundle;
    import android.support.v7.app.AppCompatActivity;
    import android.view.View;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.Toast;

    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.ValueEventListener;

    /** This class represents the notes page
     *
     */
    public class Notes extends AppCompatActivity {
    private DatabaseReference data;

        /**
         * connect to the user's notes and load his notes.
         * and give functionality to save button
         * @param savedInstanceState
         */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);


        final EditText e = findViewById(R.id.note);
        data = FirebaseDatabase.getInstance().getReference();
        /** connect to user's notes in database and load it */
        data.child("Notes").child(MainActivity.Username).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                      e.setText(dataSnapshot.getValue().toString()) ;
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    /** on clicking save button , his notes get saved in database */
        Button save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                data.child("Notes").child(MainActivity.Username).setValue(e.getText().toString());
                Toast.makeText(getApplicationContext() , "Saved" , Toast.LENGTH_SHORT) ;
            }
        });



    }
}
