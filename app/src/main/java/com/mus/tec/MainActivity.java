package com.mus.tec;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;
import com.hitomi.cmlibrary.OnMenuStatusChangeListener;
import com.mus.tec.Clases.AnimacionCircular;

public class MainActivity extends AppCompatActivity {

    // Fondo de color para menú circular
    TextView fondoMenuCircular;

    // Listener para Firebase
    FirebaseAuth.AuthStateListener escuchaInicioSesionProfesor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Casteo
        fondoMenuCircular = (TextView) findViewById(R.id.fondo_menuCir);
        // View Para animación entre actividades
        final View viewActividadPrincipal = findViewById(R.id.layout_activity_main);

        // Menú Circular
        final CircleMenu menuCircular = (CircleMenu) findViewById(R.id.menuCir_inicio);

        // - - - - - - - - - - - - < Snackbar STARTS > - - - - - - - - - - - - -
        final Snackbar snackbar = Snackbar
                .make(viewActividadPrincipal, R.string.snackbarInicio, Snackbar.LENGTH_INDEFINITE)
                .setActionTextColor(getResources().getColor(R.color.amarillo))
                .setAction(R.string.snackbarInicioAccion, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {    // acción a realizar después de seleccionar
                        menuCircular.closeMenu();   // Oculta el menú circular

                        // Dialogo
                        final AlertDialog.Builder creador = new AlertDialog.Builder(MainActivity.this);
                        creador.setTitle(R.string.tituloAyuda)
                                .setMessage(R.string.mensajeAyuda)
                                .setCancelable(false)
                                .setNeutralButton(R.string.ayudaSig,
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {

                                // Dialogo Visitante
                                creador.setTitle(R.string.tituloAyudaV)
                                        .setMessage(R.string.mensajeAyudaV)
                                        .setIcon(R.drawable.ic_visitante)
                                        .setCancelable(false)
                                        .setNeutralButton(R.string.ayudaSig,
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                // Dialogo Alumno
                                creador.setTitle(R.string.tituloAyudaA)
                                        .setMessage(R.string.mensajeAyudaA)
                                        .setIcon(R.drawable.ic_alumno)
                                        .setCancelable(false)
                                        .setNeutralButton(R.string.ayudaSig,
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                //Dialogo Profesor
                                creador.setTitle(R.string.tituloAyudaP)
                                        .setMessage(R.string.mensajeAyudaP)
                                        .setIcon(R.drawable.ic_profesor)
                                        .setCancelable(false)
                                        .setNeutralButton(R.string.ayudaFin,
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        menuCircular.openMenu();
                                                    }
                                                        });
                                //Mostrar Dialogo Profesor
                                AlertDialog mensaje = creador.create();
                                mensaje.show();
                            }
                        });
                                //Mostrar Dialogo Alumno
                                AlertDialog mensaje = creador.create();
                                mensaje.show();
                            }
                        });
                                //Mostrar Dialogo Visitante
                                AlertDialog mensaje = creador.create();
                                mensaje.show();
                            }
                        });

                        // Mostrar primer Dialogo
                        AlertDialog mensaje = creador.create();
                        mensaje.show();

                    }
                });
        // - - - - - - - - - - - - < Snackbar ENDS > - - - - - - - - - - - - -



        // - - - - - - - - - - - - < Inicio Sesión Firebase STARTS > - - - - - - - - - - - - -
        escuchaInicioSesionProfesor = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth cuentaFirebase) {
                FirebaseUser user = cuentaFirebase.getCurrentUser();
                if (user != null){
                    // Inicio de actividad después de comprobar sesión iniciada < - - # # # # # #
                    startActivity(new Intent(getApplicationContext(), MainActivityProfesor.class));
                    finish();
                    Log.i("Estado Inicio Sesión","Sesión Iniciada con el correo "+user.getEmail());
                } else{
                    Log.i("Estado Inicio Sesión","Sesión Cerrada");
                }
            }
        };
        // - - - - - - - - - - - - < Inicio Sesión Firebase ENDS> - - - - - - - - - - - - -



        // Menú Circular
        menuCircular.setMainMenu(Color.parseColor("#f44336"),
                R.drawable.ic_nuevo_mas, R.drawable.ic_nuevo_cancelar)
                .addSubMenu(Color.parseColor("#4CAF50"), R.drawable.ic_visitante)
                .addSubMenu(Color.parseColor("#FF5722"), R.drawable.ic_profesor)
                .addSubMenu(Color.parseColor("#2196F3"), R.drawable.ic_alumno)
                .setOnMenuSelectedListener(new OnMenuSelectedListener() {
                    @Override
                    public void onMenuSelected(int index) {

                        switch (index){


                            case 0:
                                // Profesor Seccionado
                                Handler retardoVisitante = new Handler();    // Retardo para abrir el Intent
                                retardoVisitante.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        // Inicia sin animación hasta la espera de un Main Visitante
                                        startActivity(new Intent(getApplicationContext(), MapaTec.class));

                                    }
                                }, 400);    // Tiempo en milisegundos para iniciar la actividad

                                break;

                            case 1:
                                // Profesor Seccionado
                                Handler retardoProfesor = new Handler();    // Retardo para abrir el Intent
                                retardoProfesor.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        Intent intent = new Intent(MainActivity.this, ProfesorLogIn.class);
                                        SigActividadConAnimCir
                                                (viewActividadPrincipal, intent);
                                    }
                                }, 400);    // Tiempo en milisegundos para iniciar la actividad

                                break;

                            case 2:
                                // Alumno Seleccionado
                                Handler retardoAlumno = new Handler();    // Retardo para abrir el Intent
                                retardoAlumno.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        Intent intent = new Intent(MainActivity.this, AlumnoLogInQR.class);
                                        SigActividadConAnimCir
                                                (viewActividadPrincipal, intent);

                                    }
                                }, 400);    // Tiempo en milisegundos para iniciar la actividad

                                break;

                        } // Fin switch
                    } // Fin OnMenuSelect

                    // Estado del menú (Abierto & cerrado)
                }).setOnMenuStatusChangeListener(new OnMenuStatusChangeListener() {



            @Override
            public void onMenuOpened() {

                // Muestra fondo animado en API <21
                AnimacionCircularFondoOpen();

                // Snackbar
                snackbar.show();

            }

            @Override
            public void onMenuClosed() {

                // Retardo para volver a cerrar el fondo
                Handler retardoProfesor = new Handler();
                retardoProfesor.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Muestra fondo animado en API <21
                        AnimacionCircularFondoClose();
                    }
                }, 500);

                // Snackbar
                snackbar.dismiss();

            }
        });
    }


    // - - - - - - - - - - - - < Animación del fondo Starts > - - - - - - - - - - - - - - - -
    private void AnimacionCircularFondoOpen() {
        if (Build.VERSION.SDK_INT >= 21) {  //Condición para el material design
            int cx = (fondoMenuCircular.getLeft() + fondoMenuCircular.getRight()) / 2;
            int cy = (fondoMenuCircular.getTop() + fondoMenuCircular.getBottom()) / 2;
            // Consiguiendo el Final del radio
            int finalRadius = Math.max(fondoMenuCircular.getWidth(), fondoMenuCircular.getHeight());
            // Creando la animacion para esta vista, el radio de inico es cero
            Animator anim =
                    ViewAnimationUtils.createCircularReveal
                            (fondoMenuCircular, cx, cy, 0, finalRadius);
            // Haciendo la vista visible e iniciando la animación
            fondoMenuCircular.setVisibility(View.VISIBLE);
            anim.setDuration(800);  // Velocidad de la animación al revelar
            anim.start();
        }else{
            fondoMenuCircular.setVisibility(View.VISIBLE);  // Acción sin animación
        }
    }
    private void AnimacionCircularFondoClose() {
        if (Build.VERSION.SDK_INT >= 21) {
            // obteniendo el centro de el circulo
            int cx = (fondoMenuCircular.getLeft() + fondoMenuCircular.getRight()) / 2;
            int cy = (fondoMenuCircular.getTop() + fondoMenuCircular.getBottom()) / 2;
            // obteniendo el radio inicial de el circulo
            int initialRadius = fondoMenuCircular.getWidth();
            Animator anim =
                    ViewAnimationUtils.createCircularReveal
                            (fondoMenuCircular, cx, cy, initialRadius, 0);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    fondoMenuCircular.setVisibility(View.INVISIBLE);
                }
            });
            // inicia la animación
            anim.setDuration(500);
            anim.start();
        } else {
            fondoMenuCircular.setVisibility(View.INVISIBLE);  // Acción sin animación
        }
    }// - - - - - - - - - - - - < Animación del fondo Ends > - - - - - - - - - - - - - - - - - -



    // - - - - - - - - - - - - < Siguiente Actividad con Animación Starts > - - - - - - - - - - - -
    public void SigActividadConAnimCir(View v, Intent intent) {
        // calculando el centro del view
        int revealX = (int) (v.getX() + v.getWidth() / 2);
        int revealY = (int) (v.getY() + v.getHeight() / 2);
        // Obteniendo cordenadas del intent
        intent.putExtra(AnimacionCircular.EXTRA_CIRCULAR_REVEAL_X, revealX);
        intent.putExtra(AnimacionCircular.EXTRA_CIRCULAR_REVEAL_Y, revealY);
        // Iniciando la actividad
        ActivityCompat.startActivity(this, intent, null);
        // Anulando otras transacciones
        overridePendingTransition(0, 0);
    }// - - - - - - - - - - - - < Siguiente Actividad con Animación Starts > - - - - - - - - - - - -




    // - - - - - - - - - - - - < Escucha inicio de sesión FIrebase Starts > - - - - - - - - - - - - -
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(escuchaInicioSesionProfesor);
    }
    @Override
    protected void onStop() {
        super.onStop();

        if (escuchaInicioSesionProfesor != null){
            FirebaseAuth.getInstance().removeAuthStateListener(escuchaInicioSesionProfesor);
        }
    }// - - - - - - - - - - - - < Escucha inicio de sesión FIrebase ENDS > - - - - - - - - - - - - -

}
