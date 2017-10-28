package com.example.mycseapp;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/** This class represents the select background page
 *
 */
public class BackgroundSelect extends AppCompatActivity {
    Context context = this ;
    //The pictures
    static Integer[] pics = {
            R.drawable.wap1 ,
            R.drawable.wap6 ,
            R.drawable.wap7 ,
            R.drawable.lk,
            R.drawable.group,
            R.drawable.k
    };
    /** \fn  protected void onCreate(Bundle savedInstanceState)
     *  \brief set background name
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background_select);

        setTitle("Choose Background");
        GridView gridView = (GridView) findViewById(R.id.grid_view);
        /** Reference to background information */
        final DatabaseReference Background = FirebaseDatabase.getInstance().getReference()
                .child("Background").child(MainActivity.Username).child("Background") ;
        /** generate adapter array for dialog box  */
        final ArrayList<String> U = new ArrayList<>() ;
        U.add("Set as groups Background") ;
        U.add("Set as events Background")  ;

        gridView.setAdapter(new ImageAdapter(context, pics));
        /** add onItemclicklistener to grid view */
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,final int i, long l) {
                LayoutInflater li = LayoutInflater.from(context);
                /** <create view prompts.xml */
                View promptsView = li.inflate(R.layout.memberlist, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);
                /** <set prompts.xml to alertdialog builder */
                alertDialogBuilder.setView(promptsView);
                ListView listView2 = (ListView) promptsView.findViewById(R.id.list_view);
                /** < set adapter array */
                final ArrayAdapter<String> adapter1 =
                        new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1, U);
                listView2.setAdapter(adapter1);
                /** create a selected user array */
                final ArrayList<String> SelectedUser = new ArrayList<>() ;
                /**< the item should get highlighted when touched and gets added to Selected User array.*/
                listView2.setOnItemClickListener(new  AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                        String item = Integer.toString(position);
                        if(SelectedUser.contains(item))
                        {
                            v.setBackgroundColor(0xFFFFFF);
                            SelectedUser.remove(item) ;
                        }
                        else
                        {
                            v.setBackgroundColor(0xFF00FF00);
                            SelectedUser.add(item);
                        }
                    }
                });
                /**<set dialog message */
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        String done = "" ;
                                        for(int k = 0 ; k < SelectedUser.size() ; k++)
                                        {
                                            done = done + SelectedUser.get(k) + " " ;
                            /**< add the  info to event's page or to group page of user looping over the selected user array
                              */
                                            if(SelectedUser.get(k).equals("0"))
                                            {
                                             Background.child("g").setValue(i) ;
                                            }
                                            else
                                            {
                                                Background.child("e").setValue(i) ;
                                            }
                                        }
                                        SelectedUser.clear();
                                        Toast.makeText(context, done , Toast.LENGTH_LONG).show(); ;

                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                /**< finally create alert dialog */
                AlertDialog alertDialog = alertDialogBuilder.create();

                /**< show it */
                alertDialog.show();

            }
        });
    }
}
