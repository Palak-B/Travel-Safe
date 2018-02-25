package com.microsoft.CognitiveServicesExample;

/**
 * Created by rishavg on 2/24/18.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by rishavg on 6/11/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG="MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //displayng data in log
        //optional
        Log.d(TAG,"From: "+remoteMessage.getFrom());
        Log.d(TAG,"Notification Message Body: "+remoteMessage);

        //calling method to generate notification
        sendNotification(remoteMessage.getNotification().getBody());
    }
    private void sendNotification(String messageBody)
    {
        Intent intent = new Intent(this,Result.class);
        Bundle bb=new Bundle();
        String l[]=new String[15];
        l=messageBody.split(" ");
        bb.putString("pl",l[8]);
        bb.putString("lat",l[11]);
        bb.putString("lon",l[14]);
        intent.putExtras(bb);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder= new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Firebase Push Notification")
                .setAutoCancel(true)
                .setContentText(messageBody)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager= (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notificationBuilder.build());
    }
}
