package com.mus.tec;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MainActivityProfesor extends AppCompatActivity {

    TextView emailProfesor;
    Button cerrarSesion;

    // Beta
    Button enviarNoti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_profesor);

        emailProfesor = (TextView) findViewById(R.id.email_profesor);
        cerrarSesion = (Button) findViewById(R.id.boton_cerrar_sesion);

        // BETA XXXX
        enviarNoti = (Button) findViewById(R.id.boton_enviar_notificacion);

        // creando variable user para tener información del Inicio de Sesión
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Condición para comprobar que hay usuario con cuenta activa
        if(user != null){
        emailProfesor.setText(user.getEmail());
        }


        // Boton de cerrar Sesión
        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();   // Cerrando Sesión
                Toast.makeText(MainActivityProfesor.this, "Sesión Cerrada", Toast.LENGTH_SHORT).show();
                onBackPressed();    // Pulsando boton atras
            }
        });

        // BETA XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
        enviarNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivityProfesor.this, "Enviando notificación", Toast.LENGTH_SHORT).show();
                String topic = "todos";
                String mensaje = "Mensaje chido";
                String TAG = "TAG chido";
                enviarNotificacion(topic, mensaje, TAG);

            }
        });
        // BETA XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

    }
    // BETA XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    public static void enviarNotificacion(String topic_name, final String message, String TAG) {
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://musapp-7d55e.firebaseio.com/");
        final DatabaseReference notifications = ref.child("solicitudDeNotificaciones");
        Map notification = new HashMap<>();
        notification.put("topic_name", topic_name);
        notification.put("message", message);
        notification.put("click_action", TAG);
        notifications.push().setValue(notification);
    }
    // BETA XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
}
