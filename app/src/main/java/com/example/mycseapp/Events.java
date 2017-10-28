package com.example.mycseapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import static android.provider.AlarmClock.EXTRA_MESSAGE;


public class Events extends Fragment {
    View rootView ;
    StorageReference mStorageRef;
    /////////To be used for
    static String des ;
    static String title ;
    ListView set ;
    private DatabaseReference mDatabase  ;
    String  enumber , Username;
    String number ;
    int i  = 0  , j  = 0 ; ///////The number which tells user's last event's id.
    private ArrayList<Eventclass> A = new ArrayList<>() ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.events, container, false);
        A.clear();
        //////////connect to database
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Events") ;
        ///////////get user name and his event number ;
        Username = MainActivity.Username ;
        enumber = MainActivity.enumber ;
        ////////set will be the event adapter
        set = rootView.findViewById(R.id.eventview) ;
        set.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Eventclass name  = (Eventclass) adapterView.getItemAtPosition(i) ;
                Intent ia = new Intent(getActivity(), Event_desc.class) ;
                //////// send the id of event to event desc post
                String n = name.id ;
                des = name.des ;
                title = name.s ;
                ia.putExtra(EXTRA_MESSAGE , n) ;
                startActivity(ia);
            }
        });
        ////////////////// Set Background.

        /////////get background info from database
        FirebaseDatabase.getInstance().getReference().child("Background")
                .child(Username).child("Background").child("e").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                RelativeLayout td = rootView.findViewById(R.id.groupb) ;
                String x = dataSnapshot.getValue().toString() ;
                td.setBackgroundResource(0);
                td.setBackgroundResource(BackgroundSelect.pics[Integer.parseInt(x)]);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ////////// posting button
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent( getActivity() , Event_posting.class) ;
                startActivity(i);


            }
        });
        ///////////load more button
        FloatingActionButton fab2 = (FloatingActionButton) rootView.findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            DownloadMore();


            }
        });
        ///////clear content before uploading
        A.clear();
        //////////////////load  no. of events there in the database
        mDatabase.child("Number").child("number").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                number = dataSnapshot.getValue().toString() ;

                j = Integer.parseInt(MainActivity.enumber) ;
//                if(i == 0 )
//                {
//                    Download("1" , 0 , 1);
//                }
//                else
                i = j - A.size() ;
                if(j < Integer.parseInt(number))
                DownloadNew();
                else
                {

                    DownloadMore();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }); ;
        //////////Loading option only to User name rahul
        if(!(MainActivity.Ausername.equals("rahul")))
        {
            View b = rootView.findViewById(R.id.fab) ;
            b.setVisibility(View.INVISIBLE);
//            View c = rootView.findViewById(R.id.fab2) ;
//            c.setVisibility(View.GONE);
        }

        return rootView;
    }


    ////////////when more is asked for.
    public void DownloadMore()
    {

        ////////Download previous 5 events.
        for(int k = 0 ; k < 5 ; k++)
        {
             {
                 if(i <= 0)
                 {
                     break ;
                 }
                Download(Integer.toString(i) , 0 , 0);
                i--;
            }
        }
    }
    /////////When a new event is posted.
    public void DownloadNew()
    {

        int ta = Integer.parseInt(number) ;
        //////////Always make sure there is something on the feed.
        while(  j < ta )
        {
            j++  ;
            Download(Integer.toString(j) , 1, 1);

        }

       MainActivity.enumber = Integer.toString(j) ;
        FirebaseDatabase.getInstance().getReference().child("Users").child(Username)
                .child("event_no").setValue(MainActivity.enumber) ;

    }


//////Download an event with a particular url.
       public void Download(final String S , final int x , final int k) {


           //Toast.makeText(getApplicationContext(), "OK ", Toast.LENGTH_LONG).show();

           final Eventclass r = new Eventclass();
           mDatabase.child(S).addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(DataSnapshot dataSnapshot) {
                   String til = dataSnapshot.child("title").getValue().toString();
                   String info = dataSnapshot.child("By").getValue().toString();
                   String desc = dataSnapshot.child("text").getValue().toString();
                   String type = dataSnapshot.child("type").getValue().toString();
                   r.type = type;
                   r.s = til;
                   r.info = info;
                   r.des = desc;
                   r.id = S;
                   r.pos = k ;
                   /////////add event to array at the beginning or at the
                   ////////end appropriately
                   if (x == 1) {
                       if (A.size() == 0) {
                           A.add(r);
                       } else
                           A.add(0, r);
                   }
                   if (x == 0) {
                       A.add(r);
                   }
                   ////////create adapter from data ;
                   final EventAdapter itemsAdapter =
                           new EventAdapter(getContext(), A);
                    ///////set adapter
                   set.setAdapter(itemsAdapter);
                   itemsAdapter.notifyDataSetChanged();

               }

               @Override
               public void onCancelled(DatabaseError databaseError) {

               }
           });
       }

}





