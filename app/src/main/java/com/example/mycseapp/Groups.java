package com.example.mycseapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static android.provider.AlarmClock.EXTRA_MESSAGE;
public class Groups extends Fragment {
    View rootView ;
    private DatabaseReference groupbase , Duser  ;

    private String Username ;
    static ArrayList<String > group  = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         rootView = inflater.inflate(R.layout.groups, container, false);
        //getUsername();
       Username = MainActivity.Username;
        group.clear();
         ////// get reference to user database
        groupbase = FirebaseDatabase.getInstance().getReference().child("Users").child(MainActivity.Username) ;
        ///////source list for list view
        //// group = new ArrayList<>() ;
        //////////////Array adapter for list view
        final ArrayAdapter<String> for_group = new ArrayAdapter<String>
                (getActivity(), android.R.layout.simple_list_item_1, group) ;
        /////////list view
        final ListView Gs = rootView.findViewById(R.id.mobile_list2) ;
        /////////////give functionality to list view on click
        Gs.setOnItemClickListener(new  AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position , long id){
                String item = adapter.getItemAtPosition(position).toString();
                Intent intent = new Intent(getActivity(),ChatActivity.class);
                String[] A = {item, Username} ;
                //based on item add info to intent
                intent.putExtra(EXTRA_MESSAGE, A) ;
                startActivity(intent);
            }
        });
        //////////
        FloatingActionButton flb = rootView.findViewById(R.id.flb) ;
        flb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater li = LayoutInflater.from(getActivity());
                View promptsView = li.inflate(R.layout.newgroup, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        getActivity());
                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);
                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        String N = userInput.getText().toString();
                                        //// add group to user
                                        groupbase.child(N).setValue("true");
                                        ///// add user to the group.
                                        FirebaseDatabase.getInstance().getReference().
                                                child("Groups").child(N).child(MainActivity.Username).setValue(MainActivity.Ausername) ;
                                        //// add number to group.
                                        FirebaseDatabase.getInstance().getReference().
                                                child("Group_Chat").child(N).child("Number").child("number").setValue("0");
                                        FirebaseDatabase.getInstance().getReference().
                                                child("Groups").child(N).child("Url").child("name").setValue("none");
                                        FirebaseDatabase.getInstance().getReference().
                                                child("Groups").child(N).child("Url").child("no").setValue("none");
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
        });
        Gs.setAdapter(for_group);
        //////////get the user's groups
        groupbase.addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String name = dataSnapshot.getKey() ;
                        //////////don't add event_no , name or Url
                        if(!((name.equals("event_no")) || name.equals("name") || name.equals("Url"))) {
                            String g = dataSnapshot.getKey();
                            FirebaseDatabase.getInstance().getReference()
                                    .child("Groups").child(g).child("token")
                                    .child(Splash.token).setValue("true") ;
                            /////add the value to group array
                            group.add(g);
                            for_group.notifyDataSetChanged();
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
                }
        ) ;


        return rootView;
    }

}
