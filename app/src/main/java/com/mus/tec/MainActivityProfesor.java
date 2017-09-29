package com.mus.tec;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mus.tec.Clases.ObjetoProfesorFirebase;

import java.util.HashMap;
import java.util.Map;

public class MainActivityProfesor extends AppCompatActivity {

    CardView cardEnLinea;
    TextView textoProfesorDisponible, textoProfesorNoDisponible;

    // Base de datos
    DatabaseReference referenciaBD;
    // Información de la cuenta actual
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String usuarioProfesor = user.getEmail().replace(".","_");
    // Variable evitar clicks rapidos
    long ultimoClick = 0;

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

        // Inicio de la base de datos
        referenciaBD = FirebaseDatabase.getInstance().getReference()
                .child("Profesores").child(usuarioProfesor);


        // Leyendo la base de datos
        referenciaBD.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Variable profe tiene los valores de la BD
                final ObjetoProfesorFirebase profe = dataSnapshot.getValue(ObjetoProfesorFirebase.class);


                // Comprueba con la base de datos el estado del profesor
                if(profe.isDisponible()) {
                    // - - - - - - - - - - - - < Efecto Revelar Disponible Starts > - - - - - - - - - - - - -
                    if (Build.VERSION.SDK_INT >= 21) {  //Condición para el material design
                        int cx = (textoProfesorDisponible.getLeft() + textoProfesorDisponible.getRight()) / 2;
                        int cy = (textoProfesorDisponible.getTop() + textoProfesorDisponible.getBottom()) / 2;
                        // Consiguiendo el Final del radio
                        int finalRadius = Math.max(textoProfesorDisponible.getWidth(),
                                textoProfesorDisponible.getHeight());
                        // Creando la animacion para esta vista, el radio de inico es cero
                        Animator anim =
                                ViewAnimationUtils.createCircularReveal
                                        (textoProfesorDisponible, cx, cy, 0, finalRadius);
                        // Acciones con animación
                        textoProfesorDisponible.setVisibility(View.VISIBLE);
                        anim.setDuration(1200);  // Velocidad de la animación al revelar
                        anim.start();
                    } else {
                        // Acción sin animación
                        textoProfesorDisponible.setVisibility(View.VISIBLE);
                    }
                }else{// - - - - - - - - - - - - < Efecto Revelar Starts > - - - - - - - - - - - - -
                    if (Build.VERSION.SDK_INT >= 21) {
                        // obteniendo el centro de el circulo
                        int cx = (textoProfesorDisponible.getLeft() + textoProfesorDisponible.getRight()) / 2;
                        int cy = (textoProfesorDisponible.getTop() + textoProfesorDisponible.getBottom()) / 2;
                        // obteniendo el radio inicial de el circulo
                        int initialRadius = textoProfesorDisponible.getWidth();
                        // creando la animación (el final del radio es cero)
                        Animator anim =
                                ViewAnimationUtils.createCircularReveal
                                        (textoProfesorDisponible, cx, cy, initialRadius, 0);
                        // haciendo la vista invisible cuando la animación esta acabada
                        anim.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                // Acciones con animación
                                textoProfesorDisponible.setVisibility(View.INVISIBLE);
                            }
                        });
                        anim.setDuration(1200);
                        anim.start();
                    } else {
                        // Acciones sin animación
                        textoProfesorDisponible.setVisibility(View.INVISIBLE);
                    } // - - - - - - - - - - - - < Efecto Revelar Ends > - - - - - - - - - - - - -

                }

        // Pulsación del CardView para poner en linea
        cardEnLinea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!profe.isDisponible()) {
                    // - - - - - - - - - - - - < Efecto Revelar Disponible Starts > - - - - - - - - - - - - -
                    if (Build.VERSION.SDK_INT >= 21) {  //Condición para el material design
                        // - - - - - - - prevencion doble clic x x x
                        if (SystemClock.elapsedRealtime() - ultimoClick < 1000){
                            return;}
                        ultimoClick = SystemClock.elapsedRealtime();
                        // - - - - - - - prevencion doble clic x x x
                        int cx = (textoProfesorDisponible.getLeft() + textoProfesorDisponible.getRight()) / 2;
                        int cy = (textoProfesorDisponible.getTop() + textoProfesorDisponible.getBottom()) / 2;
                        // Consiguiendo el Final del radio
                        int finalRadius = Math.max(textoProfesorDisponible.getWidth(),
                                textoProfesorDisponible.getHeight());
                        // Creando la animacion para esta vista, el radio de inico es cero
                        Animator anim =
                                ViewAnimationUtils.createCircularReveal
                                        (textoProfesorDisponible, cx, cy, 0, finalRadius);
                        // Acciones con animación
                        textoProfesorDisponible.setVisibility(View.VISIBLE);
                        anim.setDuration(1200);  // Velocidad de la animación al revelar
                        anim.start();
                        referenciaBD.child(getString(R.string.atributo_BD_profesor_DISPONIBLE)).setValue(true);
                    } else {
                        // Acción sin animación
                        textoProfesorDisponible.setVisibility(View.VISIBLE);
                        referenciaBD.child(getString(R.string.atributo_BD_profesor_DISPONIBLE)).setValue(true);
                    }
                }else{
                    // - - - - - - - - - - - - < Efecto Revelar Starts > - - - - - - - - - - - - -
                    if (Build.VERSION.SDK_INT >= 21) {
                        // - - - - - - - prevencion doble clic x x x
                        if (SystemClock.elapsedRealtime() - ultimoClick < 1000){
                            return;}
                        ultimoClick = SystemClock.elapsedRealtime();
                        // - - - - - - - prevencion doble clic x x x
                        // obteniendo el centro de el circulo
                        int cx = (textoProfesorDisponible.getLeft() + textoProfesorDisponible.getRight()) / 2;
                        int cy = (textoProfesorDisponible.getTop() + textoProfesorDisponible.getBottom()) / 2;
                        // obteniendo el radio inicial de el circulo
                        int initialRadius = textoProfesorDisponible.getWidth();
                        // creando la animación (el final del radio es cero)
                        Animator anim =
                                ViewAnimationUtils.createCircularReveal
                                        (textoProfesorDisponible, cx, cy, initialRadius, 0);
                        // haciendo la vista invisible cuando la animación esta acabada
                        anim.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                // Acciones con animación
                                textoProfesorDisponible.setVisibility(View.INVISIBLE);
                            }
                        });
                        anim.setDuration(1200);
                        anim.start();
                        referenciaBD.child(getString(R.string.atributo_BD_profesor_DISPONIBLE)).setValue(false);
                    } else {
                        // Acciones sin animación
                        textoProfesorDisponible.setVisibility(View.INVISIBLE);
                        referenciaBD.child(getString(R.string.atributo_BD_profesor_DISPONIBLE)).setValue(false);
                    }
                    // - - - - - - - - - - - - < Efecto Revelar Ends > - - - - - - - - - - - - -

                }
            }
        });

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {} // Errores de BD
        });







        // XXXXXXXXXXXXXXXXXXXXXXXXXXXXX ( BETA ) XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
        enviarNoti = (Button) findViewById(R.id.boton_enviar_notificacion);

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
