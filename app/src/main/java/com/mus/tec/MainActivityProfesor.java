package com.mus.tec;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
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

    CardView cardEnLinea, cardEnviarNoti;
    TextView textoProfesorDisponible, textoProfesorNoDisponible;

    // Base de datos
    DatabaseReference referenciaBD;
    // Información de la cuenta actual
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String usuarioProfesor = user.getEmail().replace(".","_");
    // Variable evitar clicks rapidos
    long ultimoClick = 0;

    // Beta
    Button cerrarSesion;
    TextView emailProfesor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_profesor);


        // Casteo
        emailProfesor = (TextView) findViewById(R.id.email_profesor);
        cerrarSesion = (Button) findViewById(R.id.boton_cerrar_sesion);
        textoProfesorDisponible = (TextView) findViewById(R.id.texto_profesor_disponible);
        textoProfesorNoDisponible = (TextView) findViewById(R.id.texto_profesor_NoDisponible);

        cardEnLinea = (CardView) findViewById(R.id.cardview_profesor_online);
        cardEnviarNoti = (CardView) findViewById(R.id.cardview_profesor_enviar_mensaje);


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
                    AnimacionCircularFondoOpen();
                }else{
                    AnimacionCircularFondoClose();
                }

                // Pulsación del CardView para poner en linea
                cardEnLinea.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // - - - - - - - prevencion doble clic x x x
                        if (SystemClock.elapsedRealtime() - ultimoClick < 1000){
                            return;}
                        ultimoClick = SystemClock.elapsedRealtime();
                        // - - - - - - - prevencion doble clic x x x
                        if(!profe.isDisponible()) {
                            AnimacionCircularFondoOpen();
                            referenciaBD.child(getString(R.string.atributo_BD_profesor_DISPONIBLE)).setValue(true);
                        }else{
                            AnimacionCircularFondoClose();
                            referenciaBD.child(getString(R.string.atributo_BD_profesor_DISPONIBLE)).setValue(false);
                        }
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {} // Errores de BD
        }); // Fin base de datos


        // Pulsación del CardView Enviar Notificación
        cardEnviarNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), EnviarNotificaciones.class));
            }
        });






        // XXXXXXXXXXXXXXXXXXXXXXXXXXXXX ( BETA ) XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
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

        // XXXXXXXXXXXXXXXXXXXXXXXXXXXXX ( BETA ) XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX





    } // metodo principal



    // - - - - - - - - - - - - < Animación del fondo Starts > - - - - - - - - - - - - - - - -
    private void AnimacionCircularFondoOpen() {
        if (Build.VERSION.SDK_INT >= 21) {  //Condición para el material design
            int cx = (textoProfesorDisponible.getLeft() + textoProfesorDisponible.getRight()) / 2;
            int cy = (textoProfesorDisponible.getTop() + textoProfesorDisponible.getBottom()) / 2;
            // Consiguiendo el Final del radio
            int finalRadius = Math.max(textoProfesorDisponible.getWidth(), textoProfesorDisponible.getHeight());
            // Creando la animacion para esta vista, el radio de inico es cero
            Animator anim =
                    ViewAnimationUtils.createCircularReveal
                            (textoProfesorDisponible, cx, cy, 0, finalRadius);
            // Haciendo la vista visible e iniciando la animación
            textoProfesorDisponible.setVisibility(View.VISIBLE);
            anim.setDuration(800);  // Velocidad de la animación al revelar
            anim.start();
        }else{
            textoProfesorDisponible.setVisibility(View.VISIBLE);  // Acción sin animación
        }
    }
    private void AnimacionCircularFondoClose() {
        if (Build.VERSION.SDK_INT >= 21) {
            // obteniendo el centro de el circulo
            int cx = (textoProfesorDisponible.getLeft() + textoProfesorDisponible.getRight()) / 2;
            int cy = (textoProfesorDisponible.getTop() + textoProfesorDisponible.getBottom()) / 2;
            // obteniendo el radio inicial de el circulo
            int initialRadius = textoProfesorDisponible.getWidth();
            Animator anim =
                    ViewAnimationUtils.createCircularReveal
                            (textoProfesorDisponible, cx, cy, initialRadius, 0);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    textoProfesorDisponible.setVisibility(View.INVISIBLE);
                }
            });
            // inicia la animación
            anim.setDuration(500);
            anim.start();
        } else {
            textoProfesorDisponible.setVisibility(View.INVISIBLE);  // Acción sin animación
        }
    }// - - - - - - - - - - - - < Animación del fondo Ends > - - - - - - - - - - - - - - - - - -



    // Termina el proceso para evitar errores de dibujado
    @Override
    public void onBackPressed() {
        android.os.Process.killProcess(android.os.Process.myPid());

    }
}
