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

/** This class represents the groups tab
 *
 */
public class Groups extends Fragment {
    View rootView ;
    private DatabaseReference groupbase , Duser  ;

    private String Username ;
    static ArrayList<String > group  = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         rootView = inflater.inflate(R.layout.groups, container, false);

       Username = MainActivity.Username;
        group.clear();
         /**  get reference to user database */
        groupbase = FirebaseDatabase.getInstance().getReference().child("Users").child(MainActivity.Username) ;
        /** source list for list view , group
        Array adapter for list view */
        final ArrayAdapter<String> for_group = new ArrayAdapter<String>
                (getActivity(), android.R.layout.simple_list_item_1, group) ;

        final ListView Gs = rootView.findViewById(R.id.mobile_list2) ;
        /** give functionality to list view on click */
        Gs.setOnItemClickListener(new  AdapterView.OnItemClickListener() {
            /**
             * send the name of item clicked to chat activity
             *
             * @param adapter
             * @param v
             * @param position
             * @param id
             */
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position , long id){
                String item = adapter.getItemAtPosition(position).toString();
                Intent intent = new Intent(getActivity(),ChatActivity.class);
                String[] A = {item, Username} ;

                intent.putExtra(EXTRA_MESSAGE, A) ;
                startActivity(intent);
            }
        });
        FloatingActionButton flb = rootView.findViewById(R.id.flb) ;
        flb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater li = LayoutInflater.from(getActivity());
                /** Create a View for the dialog box*/
                View promptsView = li.inflate(R.layout.newgroup, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        getActivity());
                /** set the view to alertdialog builder */
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);
                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    /**
                                     * add group to user
                                     * add user to the group.
                                     * add number to group.
                                     * @param dialog
                                     * @param id
                                     */
                                    public void onClick(DialogInterface dialog, int id) {
                                        String N = userInput.getText().toString();
                                        /** add group to user
                                         * add user to the group.
                                         * add number to group.
                                         * */
                                        groupbase.child(N).setValue("true");
                                        /////
                                        FirebaseDatabase.getInstance().getReference().
                                                child("Groups").child(N).child(MainActivity.Username).setValue(MainActivity.Ausername) ;
                                        ////
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
                /** create alert dialog
                 * show it*/
                AlertDialog alertDialog = alertDialogBuilder.create();
                //
                alertDialog.show();

            }
        });
        Gs.setAdapter(for_group);
        /** get the user's groups */
        groupbase.addChildEventListener(
                new ChildEventListener() {
                    /**add the value to group array
                     * don't add event_no , name or Url
                     * @param dataSnapshot
                     * @param s
                     */
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String name = dataSnapshot.getKey() ;
                        /** don't add event_no , name or Url */
                        if(!((name.equals("event_no")) || name.equals("name") || name.equals("Url"))) {
                            String g = dataSnapshot.getKey();
                            FirebaseDatabase.getInstance().getReference()
                                    .child("Groups").child(g).child("token")
                                    .child(Splash.token).setValue("true") ;
                            /** add the value to group array */
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
