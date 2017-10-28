package com.example.mycseapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class Event_desc extends AppCompatActivity {
     ArrayList<Item> comments = new ArrayList<>();
    Context con  = this ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_desc);
        Intent i = getIntent() ;
        final String id = i.getStringExtra(EXTRA_MESSAGE) ;
        TextView t = findViewById(R.id.des) ;
        TextView t2 = findViewById(R.id.imported) ;

        t2.setText(Events.title);
        t.setText(Events.des);
        ListView com = findViewById(R.id.comments) ;
        final DatabaseReference mEvent = FirebaseDatabase.getInstance().getReference().child("Events").child(id) ;
        final DatabaseReference mComment = FirebaseDatabase.getInstance().getReference().child("Eventcom").child(id) ;
        //////////////set Background
        FirebaseDatabase.getInstance().getReference().child("Background")
                .child(MainActivity.Username).child("Background").child("e").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ConstraintLayout td = findViewById(R.id.groupb) ;
                td.setBackgroundResource(0);
                td.setBackgroundResource(BackgroundSelect.pics[Integer.parseInt(dataSnapshot.getValue().toString())]);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ///////////////////////////Remind Button
        Button r = findViewById(R.id.remind)  ;
        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater li = LayoutInflater.from(con);
                final View promptsView = li.inflate(R.layout.remainder, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        con);
                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);
                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.N)

                                    public void onClick(DialogInterface dialog, int id) {
                                        /** get time from edit texts */
                                        EditText h = promptsView.findViewById(R.id.HH) ;
                                        EditText m = promptsView.findViewById(R.id.MM) ;
                                        EditText s = promptsView.findViewById(R.id.SS) ;
                                        int H = Integer.parseInt(h.getText().toString()) ;
                                        int M = Integer.parseInt(m.getText().toString()) ;
                                        int S = Integer.parseInt(s.getText().toString()) ;

                                        Calendar calendar = Calendar.getInstance();
                                        calendar.set(Calendar.SECOND, 10) ;
                                        calendar.set(Calendar.MINUTE, 43);
                                        calendar.set(Calendar.HOUR, 8);
                                        calendar.set(Calendar.AM_PM, Calendar.AM);
                                        calendar.set(Calendar.DAY_OF_MONTH, 26);
                                        //Toast.makeText(con , Integer.toString(H) , Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(con, Notif.class);

                                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

     alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 20000, pendingIntent);
                                    //    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
            }
        }
    );

        //////////////////Like button

        final LikeButton likeButton = findViewById(R.id.likeButton) ;
        final DatabaseReference Ie = FirebaseDatabase.getInstance().getReference().child("Interested")
                .child(MainActivity.Username).child(id) ;
        likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {

                    Ie.setValue(Events.title) ;
            }

            @Override
            public void unLiked(LikeButton likeButton) {
              Ie.removeValue() ;
            }
        });
        //////////
        ///////// SET LIKE button
        /////////get likability of the event
        DatabaseReference req = FirebaseDatabase.getInstance().getReference()
                .child("Interested").child(MainActivity.Username) ;
        req.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.getKey().equals(id))
                {
                    likeButton.setLiked(true);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }) ;

        /////////////////////////////////
        final EditText s = findViewById(R.id.comment) ;
        /////////set the post button
        FloatingActionButton post = findViewById(R.id.postcomment) ;
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mes = s.getText().toString() ;
                s.setText("");
               mComment.child("com").push().child(MainActivity.Ausername).setValue(mes) ;
            }
        });
        ////////////////////////
        /////////////////////Comments //////////////////////////
        comments.clear();
        final chatadapter itemsAdapter =
                new chatadapter(this, comments);
        mComment.child("com").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(comments.size() == 1 && comments.get(0).getName().equals(""))
                {
                    comments.remove(0) ;
                }
                String name = dataSnapshot.getKey();
                if (!(name.equals("Number"))) {
                    ArrayList<String> Comm = ChatActivity.getinfo(dataSnapshot.getValue().toString()) ;
                    Item n = new Item(Comm.get(0), Comm.get(1));
                    comments.add(n);
                    itemsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if(comments.size() == 0)
        {
            comments.add(new Item("" , "Be the first to comment")) ;
        }
    com.setAdapter(itemsAdapter);
        ////////////Download the image
        try{
        StorageReference dwn = FirebaseStorage.getInstance().getReferenceFromUrl("gs://mycseapp.appspot.com/Events" ).child(id);
        final File localFile = File.createTempFile("images", "jpg");
        dwn.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        //Toast.makeText(getApplicationContext(), "Downloaded", Toast.LENGTH_LONG).show();

                        final Bitmap myBitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        mEvent.child("text").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                               String des = dataSnapshot.getValue().toString() ;
                                ImageView show = findViewById(R.id.Show) ;
                                int height = (myBitmap.getHeight() * 310)/myBitmap.getWidth() ;
                                Bitmap adjusted = Bitmap.createScaledBitmap(myBitmap,310, height , false) ;
                                show.setImageBitmap(adjusted);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        }) ;



                        // Successfully downloaded data to local file
                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getApplicationContext(), "Download Failed", Toast.LENGTH_LONG).show();


                // Handle failed download
                // ...
            }
        });
    } catch (IOException e) {
        e.printStackTrace();
    }

    }
}
