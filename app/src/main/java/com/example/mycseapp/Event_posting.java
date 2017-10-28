package com.example.mycseapp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Event_posting extends AppCompatActivity {
    String number ;
    private Spinner s1 ;
    private static final int GALLERY_INTENT = 1;
    StorageReference mStorageRef;
    Uri uri ;
    Bitmap N  = null;
    private DatabaseReference mDatabase  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_posting);
        /////////reference to the events in database
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Events");
        mStorageRef = FirebaseStorage.getInstance().getReference();
        s1 = findViewById(R.id.type_p) ;
        //////////upload button
        FloatingActionButton b1 = findViewById(R.id.upload);
        //////on hitting the upload photo button
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");

                startActivityForResult(intent, GALLERY_INTENT);


            }
        });
        /////////get number from event's page
        mDatabase.child("Number").child("number").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                number = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
       final  EditText des = (EditText) findViewById(R.id.description) ;
        final EditText til = (EditText) findViewById(R.id.til) ;
        Button b2 = (Button) findViewById(R.id.post);
        //////////add functionality to post button
        b2.setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View view) {
                                      String ty = String.valueOf(s1.getSelectedItem());
                                      String d = des.getText().toString();
                                      if (!(ty.equals("Type-of-post"))) {

                                          if (!(d.equals("") && N == null)) {
                                              int n = Integer.parseInt(number);
                                              n++;
                                              number = Integer.toString(n);
                                              //////add event to database
                                              mDatabase.child(number).child("text").setValue(d);
                                              mDatabase.child(number).child("By").setValue(MainActivity.Ausername);
                                              mDatabase.child(number).child("type").setValue(ty);
                                              mDatabase.child(number).child("title").setValue(til.getText().toString());
                                              if (N == null) {
                                                  mDatabase.child(number).child("image").setValue("null");
                                                  mDatabase.child("Number").child("number").setValue(number);
                                                  Event_posting.super.onBackPressed();
                                              } else {
                                                  // add image to database
                                                  StorageReference filepath = mStorageRef.child("Events").child(number);
                                                  filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                      @Override
                                                      public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                          Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                                                          @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                                          mDatabase.child(number).child("image").setValue("okay");
                                                          mDatabase.child("Number").child("number").setValue(number);
                                                          Event_posting.super.onBackPressed();
                                                      }
                                                  });

                                              }

                                          }

                                      }
                                 }
                              }) ;

    }

    @Override
    ////////////on hitting the upload photo button
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            ////////get the bitmap from path
            uri = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(uri,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            N = BitmapFactory.decodeFile(picturePath);
            ////////set the photo to the image view in event post
            ImageView topost = (ImageView) findViewById(R.id.topost) ;
            topost.setImageBitmap(N);



        }
    }



}
