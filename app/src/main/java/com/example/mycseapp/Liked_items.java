package com.example.mycseapp;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Liked_items extends AppCompatActivity {
    private DatabaseReference mDatabase  ;
    private ArrayList<Eventclass> A = new ArrayList<>() ;

    Context con = this ;
    ListView set ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked_items);
        set = findViewById(R.id.interested) ;
        /////////////connect to events
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Events") ;
        //////////connected to the place where the interested events are stored
        DatabaseReference req = FirebaseDatabase.getInstance().getReference()
                .child("Interested") ;
        req.child(MainActivity.Username).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        ///// download the interested item as done in events
                       Download(dataSnapshot.getKey() , 0);
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
    }
    ////////Download event with id S and position(front or back ) depending on x
    public void Download(final String S , final int x ) {
        final Eventclass r = new Eventclass();
        mDatabase.child(S).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //////get the information of event
                final String til = dataSnapshot.child("title").getValue().toString();
                final String info = dataSnapshot.child("By").getValue().toString();
                final String desc = dataSnapshot.child("text").getValue().toString();
                final String type = dataSnapshot.child("type").getValue().toString();

                Handler h = new Handler() ;
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        r.type = type;
                        r.s = til;
                        r.info = info;
                        r.des = desc;
                        r.id = S;
                        if (x == 1) {
                            if (A.size() == 0) {
                                A.add(r);
                            } else
                                A.add(0, r);
                        }
                        if (x == 0) {
                            A.add(r);
                        }
                        final EventAdapter itemsAdapter =
                                new EventAdapter(con, A);

                        set.setAdapter(itemsAdapter);
                        itemsAdapter.notifyDataSetChanged();

                    }
                } , 100);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}


