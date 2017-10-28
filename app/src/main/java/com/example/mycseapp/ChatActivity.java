package com.example.mycseapp;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

/**
 * This class represents the chat activity page
 */
public class ChatActivity extends AppCompatActivity {



    DatabaseReference forUsers, ftUsers ;
    private DatabaseReference mDatabase , mUrlbase;
    String Username;
    String[] S ;
    Context context = this ;
    String number ;
    HashMap<String,String> pair = new HashMap<>() ;
    final ArrayList<String> U = new ArrayList<>();
    ArrayList<String> tU = new ArrayList<>() ;
    ///////For Url name
    String fUrl = "" ;
    ///////Content of Urls
    String cUrl = "" ;
    String Carrier = "" ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        Intent i = getIntent();
        S = i.getStringArrayExtra(EXTRA_MESSAGE);
        String token = FirebaseInstanceId.getInstance().getToken();
    /** setBackground for events */

        FirebaseDatabase.getInstance().getReference().child("Background")
                .child(MainActivity.Username).child("Background").child("g").addValueEventListener(new ValueEventListener() {
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
        /** set group name and url reference */
        setTitle(S[0]);

        /** set user name */
        Username = MainActivity.Ausername;

        /** Adapter , source and list view for chat. */
        final ArrayList<Item> h = new ArrayList<>();
        final chatadapter itemsAdapter =
                new chatadapter(this, h);
        final ListView set = (ListView) findViewById(R.id.mobile_list);

        /** Sending text and button */
        final EditText get = findViewById(R.id.E2);
        get.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                InputMethodManager m = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (m != null) {
                    m.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
                    get.requestFocus();
                }
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundColor(Color.argb(255,0,0,200));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = get.getText().toString();
                if( message.length() != 0) {
                    //////////wrap message , send it.
                    Map<String, String> A = new HashMap<String, String>();
                    A.put(Username, message);
                    int n = Integer.parseInt(number);
                    n++;
                    /////////Update position of text.
                    number = Integer.toString(n);
                    mDatabase.child(number).setValue(A);
                    mDatabase.child("Number").child("number").setValue(number);
                    get.setText("");
                }
                // FAB Action goes here
            }
        });
        /** Connect to database. */
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Group_Chat").child(S[0]);
        mUrlbase = FirebaseDatabase.getInstance().getReference().child("Groups").child(S[0]).child("Url");
        mDatabase.child("Number").child("number").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                number = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        set.setAdapter(itemsAdapter);
        /// connect to chat ;
        mDatabase.addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String message = dataSnapshot.getValue().toString();
                        String name = dataSnapshot.getKey();
                        if (!(name.equals("Number"))) {

                            ArrayList<String> fm = getinfo(message);
                            Item n = new Item(fm.get(0), fm.get(1));
                            ////////Check whether you sent it.
                            if(fm.get(0).equals(MainActivity.Ausername))
                            {
                                n.pos = true ;
                            }
                            else n.pos = false ;
                            h.add(n);
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
                }
        );
        /// get this groups users ;
        ftUsers = FirebaseDatabase.getInstance().getReference().child("Groups").child(S[0]);
        ftUsers.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(!(dataSnapshot.getKey().equals("Url") || dataSnapshot.getKey().equals("token") ||
                        dataSnapshot.getKey().equals("update") )) {
                    String name = dataSnapshot.getValue().toString();
                    tU.add(name);
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
        ///////get all users.
        forUsers = FirebaseDatabase.getInstance().getReference().child("AllUsers");
        forUsers.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String name = dataSnapshot.getValue().toString();
                pair.put(name , dataSnapshot.getKey()) ;
                U.add(name);
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


        /// get urls names and content.
       mUrlbase.addValueEventListener(
               new ValueEventListener() {
                   @Override
                   public void onDataChange(DataSnapshot dataSnapshot) {
                     fUrl = dataSnapshot.child("name").getValue().toString() ;
                       cUrl = dataSnapshot.child("no").getValue().toString() ;
                   }

                   @Override
                   public void onCancelled(DatabaseError databaseError) {

                   }
               }
       );



    }




    static ArrayList<String> getinfo(String S) {
        String name = "";
        S = S.substring(1);
        while (!((S.substring(0, 1)).equals("="))) {
            name = name + S.substring(0, 1);
            S = S.substring(1);
        }
        int j = S.length();
        String M = S.substring(1, j - 1);
        ArrayList<String> m = new ArrayList<>();
        m.add(name);
        m.add(M);
        return m;
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity3_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        //final TextView t = (TextView) findViewById(R.id.textView);
        switch (item.getItemId()) {
            case R.id.new_member:
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.memberlist, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);
            {
                for (int j = 0; j < U.size(); j++) {
                    if (tU.contains(U.get(j))) {
                        U.remove(j);
                    }

                }
            }
                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);
                ListView listView2 = (ListView) promptsView.findViewById(R.id.list_view);
                final ArrayAdapter<String> adapter1 =
                        new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1, U);
                listView2.setAdapter(adapter1);
               final  ArrayList<String> SelectedUser = new ArrayList<>() ;
                listView2.setOnItemClickListener(new  AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                        String item = adapter.getItemAtPosition(position).toString();
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
            //     set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                    for(int i = 0 ; i < SelectedUser.size(); i++)
                                    {
                                        String Us = SelectedUser.get(i) ;
                                        U.remove(Us) ;

                                        String Uid = pair.get(Us) ;
                                        FirebaseDatabase.getInstance().getReference().
                                                child("Groups").child(S[0]).child(Uid).setValue(Us) ;
                                        FirebaseDatabase.getInstance().getReference()
                                                .child("Users").child(Uid).child(S[0]).setValue("true") ;
                                    }

                                    adapter1.notifyDataSetChanged();
                                    SelectedUser.clear(); ;
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
                //
                return true;
            case R.id.users:
                LayoutInflater li2 = LayoutInflater.from(context);
                View promptsView2 = li2.inflate(R.layout.memberlist, null);

                AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(
                        context);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder2.setView(promptsView2);
                ListView listView3 = (ListView) promptsView2.findViewById(R.id.list_view);
                ArrayAdapter<String> adapter2 =
                        new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1, tU);
                listView3.setAdapter(adapter2);
                final  ArrayList<String> SelectedUser2 = new ArrayList<>() ;
                //     set dialog message
                alertDialogBuilder2
                        .setCancelable(false)

                        .setNegativeButton("Back",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog2 = alertDialogBuilder2.create();

                // show it
                alertDialog2.show();
                return true ;
            case R.id.Add_Url:
                LayoutInflater li3 = LayoutInflater.from(context);
                View promptsView3 = li3.inflate(R.layout.newurl , null);

                AlertDialog.Builder alertDialogBuilder3 = new AlertDialog.Builder(
                        context);
                // set prompts.xml to alertdialog builder
                alertDialogBuilder3.setView(promptsView3);

                final EditText userInput = (EditText) promptsView3
                        .findViewById(R.id.new_url);
                ListView listView4 = (ListView) promptsView3.findViewById(R.id.so_far_u);
                ArrayList<String> a = new ArrayList<>() ;
                a.add(fUrl) ;
                final ArrayAdapter<String> adapter3 =
                        new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1, a);
                listView4.setAdapter(adapter3);
                // set dialog message
                alertDialogBuilder3
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        String N = userInput.getText().toString();
                                        // change url name
                                       mUrlbase.
                                               child("name").setValue(N) ;
                                        // change no name.
                                       mUrlbase.child("no").setValue("0") ;

                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                // create alert dialog
                AlertDialog alertDialog3 = alertDialogBuilder3.create();
                // show it
                alertDialog3.show();
                return  true ;
            case R.id.Check_Content:

                Carrier = cUrl ;
                ( new ParseURL() ).execute(new String[]{fUrl});
                return true ;
            case R.id.go_to:
                String url = fUrl;
                try {
                    Intent i = new Intent("android.intent.action.MAIN");
                    i.setComponent(ComponentName.unflattenFromString("com.android.chrome/com.android.chrome.Main"));
                    i.addCategory("android.intent.category.LAUNCHER");
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
                catch(ActivityNotFoundException e) {
                    // Chrome is not installed
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(i);
                }



                return true ;
            case R.id.back :
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }





    private class ParseURL extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            StringBuffer buffer = new StringBuffer();
            int n =  0;
            try {
                Log.d("JSwa", "Connecting to ["+strings[0]+"]");
                Document doc  = Jsoup.connect(strings[0]).get();
                Log.d("JSwa", "Connected to ["+strings[0]+"]");
                // Get document (HTML page) title
                String title = doc.title();
                Log.d("JSwA", "Title ["+title+"]");
                //buffer.append("Title: " + title + "\r\n");

                //Elements table = doc.select("table");
                for (Element inntable : doc.select("ul").select("li").select("ul").select("li")){
                    String link = inntable.select("a").attr("href");
                    //  String name = inntable.select("td").select("a").select("b").text();
                    if (!(link.isEmpty())) {
                        n++ ;
                    }
                }
                buffer.append(Integer.toString(n)) ;

            }
            catch(Throwable t) {
                t.printStackTrace();
            }
            return buffer.toString();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(!(s.equals(Carrier)))
            {
                Toast.makeText(context , s + " ; no change", Toast.LENGTH_LONG).show();
                mUrlbase.child("no").setValue(s);
                if((S[0].equals("CS207")) || S[0].equals("CS251")) {

                    FirebaseDatabase.getInstance().getReference()
                            .child("Groups").child(S[0]).child("update").
                            child("update").setValue(s);
                }
                else
                {
                    if(Integer.parseInt(s) >Integer.parseInt(Carrier)) {
                        Toast.makeText(context, s + " some files are added", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(context, s + " some files are deleted", Toast.LENGTH_LONG).show();
                    }
                        int i = Integer.parseInt(Splash.Schrodinger);
                    i = 1 - i ;
                    Splash.Schrodinger = Integer.toString(i) ;
                    FirebaseDatabase.getInstance().getReference()
                            .child("Non_course").removeValue() ;
                    FirebaseDatabase.getInstance().getReference().child("Schrodinger")
                            .setValue(Splash.Schrodinger);
                    FirebaseDatabase.getInstance().getReference().child("Non_course")
                            .child(Splash.Schrodinger).child("a").setValue(S[0]);
                }

            }

        }
    }
}

