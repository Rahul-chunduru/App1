package com.example.mycseapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

/**
 This class is for sending notifications when remainder is called
 */

public class Notif extends BroadcastReceiver {

    
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent notificationIntent = new Intent(context,MainActivity.class);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(100, FLAG_UPDATE_CURRENT);
        
        Notification notification = new Notification.Builder(context)
                .setContentTitle("REMAINDER")
                .setContentText("FOR THE POST")
                .setTicker("New Message Alert!")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent).build();
        
        notificationManager.notify(0, notification);

    }
}
