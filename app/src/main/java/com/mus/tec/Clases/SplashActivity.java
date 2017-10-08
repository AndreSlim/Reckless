package com.mus.tec.Clases;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.mus.tec.MainActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // - - - - - - - - - - - - - - - - - - - - - - - - Suscripción a los mensajes del FCM
        FirebaseMessaging.getInstance().subscribeToTopic("todos");
        Toast.makeText(this, "Suscrito a los mensajes de Firebase", Toast.LENGTH_SHORT).show();
        // - - - - - - - - - - - - - - - - - - - - - - - -

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}