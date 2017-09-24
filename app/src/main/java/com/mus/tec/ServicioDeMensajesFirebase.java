package com.mus.tec;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.drm.DrmStore;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.RingtonePreference;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by andres on 22/09/17.
 */

public class ServicioDeMensajesFirebase extends FirebaseMessagingService {

    public ServicioDeMensajesFirebase() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        if(remoteMessage == null)
        {
            return;
        }
        if(remoteMessage.getNotification() != null)
        {
            handleNotification(remoteMessage.getNotification().getTitle());
        }
    }
    public void handleNotification(String message)
    {
        sendNotification(message);
    }

    public void sendNotification(String message)
    {
        Intent intent = new Intent(this, VerNotificacion.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_qr)
                //.setContentTitle(“Title”)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }
}
