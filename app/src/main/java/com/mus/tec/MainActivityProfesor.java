package com.mus.tec;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MainActivityProfesor extends AppCompatActivity {

    CardView cardEnLinea;
    TextView textoProfesorDisponible, textoProfesorNoDisponible;
    Boolean profesorDisponible = false;


    // Beta
    Button enviarNoti;
    Button cerrarSesion;
    TextView emailProfesor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_profesor);

        // Casteo
        emailProfesor = (TextView) findViewById(R.id.email_profesor);
        cerrarSesion = (Button) findViewById(R.id.boton_cerrar_sesion);
        cardEnLinea = (CardView) findViewById(R.id.cardview_profesor_online);
        textoProfesorNoDisponible = (TextView) findViewById(R.id.texto_profesor_NoDisponible);
        textoProfesorDisponible = (TextView) findViewById(R.id.texto_profesor_disponible);


        // CardView para poner en linea
        cardEnLinea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

                if(!profesorDisponible) {
                    // - - - - - - - - - - - - < Efecto Revelar Disponible Starts > - - - - - - - - - - - - -

                    if (Build.VERSION.SDK_INT >= 21) {  //Condición para el material design

                        int cx = (textoProfesorDisponible.getLeft() + textoProfesorDisponible.getRight()) / 2;
                        int cy = (textoProfesorDisponible.getTop() + textoProfesorDisponible.getBottom()) / 2;

                        // Consiguiendo el Final del radio
                        int finalRadius = Math.max(textoProfesorDisponible.getWidth(), textoProfesorDisponible.getHeight());

                        // Creando la animacion para esta vista, el radio de inico es cero
                        Animator anim =
                                ViewAnimationUtils.createCircularReveal(textoProfesorDisponible, cx, cy, 0, finalRadius);

                        // Haciendo la vista visible, iniciando la animación y fijando texto
                        textoProfesorDisponible.setVisibility(View.VISIBLE);
                        anim.setDuration(1200);  // Velocidad de la animación al revelar

                        anim.start();
                        profesorDisponible = true;  // Profesor no disponible
                    } else {
                        textoProfesorDisponible.setVisibility(View.VISIBLE);  // Acción sin animación
                        profesorDisponible = true;
                    }
                }else{

                    // - - - - - - - - - - - - < Efecto Revelar Starts > - - - - - - - - - - - - -

                    if (Build.VERSION.SDK_INT >= 21) {

                        // obteniendo el centro de el circulo
                        int cx = (textoProfesorDisponible.getLeft() + textoProfesorDisponible.getRight()) / 2;
                        int cy = (textoProfesorDisponible.getTop() + textoProfesorDisponible.getBottom()) / 2;

                        // obteniendo el radio inicial de el circulo
                        int initialRadius = textoProfesorDisponible.getWidth();

                        // creando la animación (el final del radio es cero)
                        Animator anim =
                                ViewAnimationUtils.createCircularReveal(textoProfesorDisponible, cx, cy, initialRadius, 0);

                        // haciendo la vista invisible cuando la animación esta acabada
                        anim.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                textoProfesorDisponible.setVisibility(View.INVISIBLE);
                            }
                        });
                        anim.setDuration(1200);

                        // Haciendo la vista invisible, iniciando la animación y fijando texto
                        anim.start();
                        profesorDisponible = false; // Profesor disponible
                    } else {
                        textoProfesorDisponible.setVisibility(View.INVISIBLE);  // Acción sin animación
                        profesorDisponible = false;
                    }


                    // - - - - - - - - - - - - < Efecto Revelar Ends > - - - - - - - - - - - - -

                }

                // - - - - - - - - - - - - < Efecto Revelar Disponible Ends > - - - - - - - - - - - - -

                //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

            }
        });









        // XXXXXXXXXXXXXXXXXXXXXXXXXXXXX ( BETA ) XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
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
