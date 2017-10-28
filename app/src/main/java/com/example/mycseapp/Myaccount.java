package com.example.mycseapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * This class represents the my accounts page.
 */

public class Myaccount extends Fragment {
    /**
     * This creates a menu holder consisting of my accounts, notes, change password, username, background and
     * password.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.myaccount, container, false);
        ListView set = rootView.findViewById(R.id.my_account);
        /** create the menu array and load id */
        final ArrayList<String> myacc = new ArrayList<>();
        myacc.add("Interested Items");
        myacc.add("My Notes") ;
        myacc.add("Change Username");
        myacc.add("Change Password");
        myacc.add("Change Background");

        final ArrayAdapter<String> myaccad = new ArrayAdapter<String>
                (getActivity(), android.R.layout.simple_list_item_1, myacc);
        set.setAdapter(myaccad);
        set.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * On click, the menu will respond on basis of what is selected.
             * @param adapterView
             * @param view
             * @param i
             * @param l
             */
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = (String) adapterView.getItemAtPosition(i);
                if (name.equals("Interested Items")) {
                    Intent trans = new Intent(getActivity(), Liked_items.class);
                    startActivity(trans);
                }
                if (name.equals("Change Background")) {
                    Intent go = new Intent(getActivity(), BackgroundSelect.class);
                    startActivity(go);
                }
                if (name.equals("Change Password")) {
                    Intent go = new Intent(getActivity(), ChangePassword.class);
                    startActivity(go);
                }
                if (name.equals("My Notes")) {
                    startActivity(new Intent(getActivity(), Notes.class));
                }
                if (name.equals("Change Username")) {
                    LayoutInflater li = LayoutInflater.from(getActivity());
                    View promptsView = li.inflate(R.layout.newgroup, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            getActivity());
                    // set prompts.xml to alertdialog builder
                    alertDialogBuilder.setView(promptsView);
                    TextView t = promptsView.findViewById(R.id.textView1);
                    t.setText("New User name");
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
                                            MainActivity.Ausername = N ;
                                            FirebaseDatabase.getInstance().getReference().child("AllUsers")
                                                    .child(MainActivity.Username).setValue(N) ;
                                            FirebaseDatabase.getInstance().getReference().child("Users")
                                                    .child(MainActivity.Username).child("name")
                                                    .setValue(N) ;
                                            int k = Groups.group.size() ;
                                            for(int l = 0 ; l < k ; l++)
                                            {
                                                FirebaseDatabase.getInstance().getReference()
                                                        .child("Groups").child(Groups.group.get(l))
                                                        .child(MainActivity.Username).setValue( N) ;
                                            }

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
        });



        return rootView;
    }
}
