package com.mus.tec;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
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

public class MainActivity extends AppCompatActivity {

    TextView fondo;     // Fondo de color para menú circular

    // Listener para Firebase
    FirebaseAuth.AuthStateListener escuchaInicioSesionProfesor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fondo = (TextView) findViewById(R.id.fondo_menuCir);

        final CircleMenu menuCircular = (CircleMenu) findViewById(R.id.menuCir_inicio);

        // - - - - - - - - - - - - < Snackbar STARTS > - - - - - - - - - - - - -
        View viewSnackbar = findViewById(R.id.actividad_principal);
        final Snackbar snackbar = Snackbar
                .make(viewSnackbar, R.string.snackbarInicio, Snackbar.LENGTH_INDEFINITE)
                .setActionTextColor(getResources().getColor(R.color.amarillo))
                .setAction(R.string.snackbarInicioAccion, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {    // acción a realizar despues de seleccionar
                        menuCircular.closeMenu();   // Oculta el menú circular


                        // Cuadro de Dialogo principal
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

                    // Aqui va la actividad!! <---- # # # # # # # # # # # # # # # # #

                    Intent profesor = new Intent(MainActivity.this, MainActivityProfesor.class);
                    startActivity(profesor);
                    finish();

                    Log.i("Estado Inicio Sesión","Sesión Iniciada con el correo "+user.getEmail());

                    Toast.makeText(MainActivity.this, "Inicio de Sesión correcto con " + user.getEmail() + "\n\nApp Beta", Toast.LENGTH_LONG).show();

                } else{
                    Log.i("Estado Inicio Sesión","Sesión Cerrada");
                }

            }
        };
        // - - - - - - - - - - - - < Inicio Sesión Firebase ENDS> - - - - - - - - - - - - -

        final Activity actividad = this; // actividad para el QR

        // - - - - - - - - - - - - < MENÚ CIRCULAR STARTS > - - - - - - - - - - - - -

        menuCircular.setMainMenu(Color.parseColor("#f44336"), R.drawable.ic_nuevo_mas, R.drawable.ic_nuevo_cancelar)
                .addSubMenu(Color.parseColor("#4CAF50"), R.drawable.ic_visitante)
                .addSubMenu(Color.parseColor("#FF5722"), R.drawable.ic_profesor)
                .addSubMenu(Color.parseColor("#2196F3"), R.drawable.ic_alumno)
                .setOnMenuSelectedListener(new OnMenuSelectedListener() {

                    @Override
                    public void onMenuSelected(int index) {

                        switch (index){

                            case 1:     // Profesor seleccionado
                            // - - - - - - - - - - - - < Profesor seleccionado Starts > - - - - - - - - - - - - -

                                Handler handlerP = new Handler();    // Retardo para abrir el Intent
                                handlerP.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        View view = findViewById(R.id.actividad_principal);
                                        actividadPresenteToProfesor(view);

                                    }
                                }, 400);    // Tiempo en milisegundos para iniciar la actividad

                                break;
                            // - - - - - - - - - - - - < Profesor seleccionado Ends > - - - - - - - - - - - - -

                            case 2:     // Alumno seleccionado, Iniciar el QR
                            // - - - - - - - - - - - - < Alumno seleccionado Starts > - - - - - - - - - - - - -

                                Handler handlerA = new Handler();    // Retardo para abrir el Intent
                                handlerA.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {

                                        View view = findViewById(R.id.actividad_principal);
                                        actividadPresenteToAlumno(view);

                                                        }
                                }, 400);    // Tiempo en milisegundos para iniciar la actividad

                                break;
                        }

                            // - - - - - - - - - - - - < Alumno seleccionado Ends > - - - - - - - - - - - - -

                    }

                }).setOnMenuStatusChangeListener(new OnMenuStatusChangeListener() {

            @Override
            public void onMenuOpened() {

                // - - - - - - - - - - - - < Efecto Revelar Starts > - - - - - - - - - - - - -

                if (Build.VERSION.SDK_INT >= 21) {  //Condición para el material design

                    int cx = (fondo.getLeft() + fondo.getRight()) / 2;
                    int cy = (fondo.getTop() + fondo.getBottom()) / 2;

                    // Consiguiendo el Final del radio
                    int finalRadius = Math.max(fondo.getWidth(), fondo.getHeight());

                    // Creando la animacion para esta vista, el radio de inico es cero
                    Animator anim =
                            ViewAnimationUtils.createCircularReveal(fondo, cx, cy, 0, finalRadius);

                    // Haciendo la vista visible e iniciando la animación
                    fondo.setVisibility(View.VISIBLE);
                    anim.setDuration(500);  // Velocidad de la animación al revelar
                    anim.start();
                }else{
                    fondo.setVisibility(View.VISIBLE);  // Acción sin animación
                }

                // - - - - - - - - - - - - < Efecto Revelar Ends > - - - - - - - - - - - - -

                // Snackbar
                snackbar.show();

            }

            @Override
            public void onMenuClosed() {

                // - - - - - - - - - - - - < Efecto Revelar Starts > - - - - - - - - - - - - -

                if (Build.VERSION.SDK_INT >= 21) {

                    // obteniendo el centro de el circulo
                    int cx = (fondo.getLeft() + fondo.getRight()) / 2;
                    int cy = (fondo.getTop() + fondo.getBottom()) / 2;

                    // obteniendo el radio inicial de el circulo
                    int initialRadius = fondo.getWidth();

                    // creando la animación (el final del radio es cero)
                    Animator anim =
                            ViewAnimationUtils.createCircularReveal(fondo, cx, cy, initialRadius, 0);

                    // haciendo la vista invisible cuando la animación esta acabada
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            fondo.setVisibility(View.INVISIBLE);
                        }
                    });

                    // inicia la animación
                    anim.start();
                } else {
                    fondo.setVisibility(View.INVISIBLE);  // Acción sin animación
                }


                // - - - - - - - - - - - - < Efecto Revelar Ends > - - - - - - - - - - - - -

                // Snackbar
                snackbar.dismiss();

            }
        });

        // - - - - - - - - - - - - < MENÚ CIRCULAR ENDS > - - - - - - - - - - - - -
    }

    // - - - - - - - - - - - - < Animación entre actividades STARTS > - - - - - - - - - - - - -

    public void actividadPresenteToAlumno(View view) {
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, view, "transition");
        int revealX = (int) (view.getX() + view.getWidth() / 2);
        int revealY = (int) (view.getY() + view.getHeight() / 2);

        Intent intent = new Intent(this, AlumnoQR.class);

        intent.putExtra(AlumnoQR.EXTRA_CIRCULAR_REVEAL_X, revealX); // Valores tomados de la actividad a llegar
        intent.putExtra(AlumnoQR.EXTRA_CIRCULAR_REVEAL_Y, revealY);

        ActivityCompat.startActivity(this, intent, options.toBundle());

    }
    // - - - - - - - - - - - - < Animación entre actividades ENDS > - - - - - - - - - - - - -

    // - - - - - - - - - - - - < Animación entre actividades STARTS > - - - - - - - - - - - - -

    public void actividadPresenteToProfesor(View view) {
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, view, "transition");
        int revealX = (int) (view.getX() + view.getWidth() / 2);
        int revealY = (int) (view.getY() + view.getHeight() / 2);

        Intent intent = new Intent(this, ProfesorLogIn.class);

        intent.putExtra(AlumnoQR.EXTRA_CIRCULAR_REVEAL_X, revealX); // Valores tomados de la actividad a llegar
        intent.putExtra(AlumnoQR.EXTRA_CIRCULAR_REVEAL_Y, revealY);

        ActivityCompat.startActivity(this, intent, options.toBundle());

    }
    // - - - - - - - - - - - - < Animación entre actividades ENDS > - - - - - - - - - - - - -

    // - - - - - - - - - - - - < Escucha inicio de sesión Firebase STARTS > - - - - - - - - - - - - -

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

    }

    // - - - - - - - - - - - - < Escucha inicio de sesión FIrebase ENDS > - - - - - - - - - - - - -

}
