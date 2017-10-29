package com.example.mycseapp.MyFirebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.example.mycseapp.MainActivity;
import com.example.mycseapp.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

/**
 Here the notifications recieved on android are managed
 */




public class MyFirebaseMessagingService extends FirebaseMessagingService {


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    /**
    
    */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        
        //different types of messages are recieved 

        if (remoteMessage.getData().size() > 0) {
            Map<String, String> payload = remoteMessage.getData();

            if (payload.get("Type").equals("website")){
                showNotification_website(payload);
                //website tracking notifictaions
            }
            else
            if(payload.get("Type").equals("event"))
            {
                showNotification(payload);
                //event notifications 
            }            
            else
                if(payload.get("Type").equals("non_grp")){
                    showNotification_noncourse(payload);
                    //non_course groups notifications;

            }


        }
    }

    /**
     * for showing the event notifications
     * @param payload
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void showNotification(Map<String, String> payload) {

        Intent notificationIntent = new Intent(this,MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("MyCseApp")
                .setContentText(payload.get("title"))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        // .setSound(defaultSoundUri);

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());

    }


    /**
     * for showing website tracking notifications
     * @param payload
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void showNotification_website(Map<String, String> payload) {
        Intent notificationIntent = new Intent("android.intent.action.MAIN");
        String url = "https://www.cse.iitb.ac.in/saiganesh/test.html";
        try {

            notificationIntent.setComponent(ComponentName.unflattenFromString("com.android.chrome/com.android.chrome.Main"));
            notificationIntent.addCategory("android.intent.category.LAUNCHER");
            notificationIntent.setData(Uri.parse(url));

        }
        catch(ActivityNotFoundException e) {
            // Chrome is not installed
             notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

        }



//        String[] A = {payload.get("grp_name"),"abc"};
//        notificationIntent.putExtra(EXTRA_MESSAGE,A);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(payload.get("title"))
                .setContentText(payload.get("text"))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        // .setSound(defaultSoundUri);

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());

    }
    
    //use of Notification manager for displaying the notification 

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void showNotification_course(Map<String, String> payload) {

        Intent notificationIntent = new Intent(this,MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(payload.get("grp_name"))
                .setContentText(payload.get("text"))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        // .setSound(defaultSoundUri);

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void showNotification_noncourse(Map<String, String> payload) {

        Intent notificationIntent = new Intent(this,MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(payload.get("body"))
                .setContentText("A new message arrived")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        // .setSound(defaultSoundUri);

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());

    }
}
