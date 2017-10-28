package com.example.mycseapp.MyFirebase;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mycseapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

public class Main4Activity extends AppCompatActivity {
    private static final int GALLERY_INTENT = 1;
    StorageReference mStorageRef;
    private Uri uri;
    private DatabaseReference mDatabase  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Photos") ;
        mStorageRef = FirebaseStorage.getInstance().getReference();
        Download("4");
            Button b1 = (Button) findViewById(R.id.upload);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");

                startActivityForResult(intent, GALLERY_INTENT);


            }
        });
//        mDatabase.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Download(dataSnapshot.getValue().toString());
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//        Button b2 = findViewById(R.id.download);
//        b2.setOnClickListener(new View.OnClickListener() {
//            @Override
//
//            public void onClick(View view) {
//                //StorageReference riversRef = mStorageRef.child("photos").child(uri.getLastPathSegment());
//
//            }
//
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            uri = data.getData();
            StorageReference filepath = mStorageRef.child("photos").child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                    @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    mDatabase.push().setValue(uri.getLastPathSegment());
                }
            });
        }
    }

    public void Download(String S) {
        StorageReference dwn = FirebaseStorage.getInstance().getReferenceFromUrl("gs://mycseapp.appspot.com/Events").child(S);

        //Toast.makeText(getApplicationContext(), "OK ", Toast.LENGTH_LONG).show();
        try {
            final File localFile = File.createTempFile("images", "jpg");
            dwn.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getApplicationContext(), "Downloaded", Toast.LENGTH_LONG).show();

                            Bitmap myBitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            ImageView img = (ImageView) findViewById(R.id.imageView2);
                            img.setImageBitmap(myBitmap) ;
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
