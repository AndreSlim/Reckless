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


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // Acceder al contenido de la notificación
        if(remoteMessage.getNotification() != null){    // Comprobar contenido no nulo

            MostrarNotificacion(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());

            Log.i("Firebase", "El mensaje es: " + remoteMessage.getNotification().getBody());
        }

    }

    private void MostrarNotificacion(String titulo, String cuerpo) {

        // Haciendo Intent para abrir despues de mostrar la notificación
        Intent intent = new Intent(this, VerNotificacion.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        // Sonido para mostrar la notificación
        Uri sonidoNotif = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // Creando la notificación
        NotificationCompat.Builder generarNotificacion = new NotificationCompat.Builder(this).
                setSmallIcon(R.drawable.ic_qr).
                setContentTitle(titulo).
                setContentText(cuerpo).
                setAutoCancel(true).
                setSound(sonidoNotif).
                setContentIntent(pendingIntent);

        // Administrador de notificaciones
        NotificationManager adminNotificaciones = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        adminNotificaciones.notify(0, generarNotificacion.build());

    }
}