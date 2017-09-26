package com.mus.tec;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;
import com.hitomi.cmlibrary.OnMenuStatusChangeListener;

public class AlumnoQR extends AppCompatActivity {

    // - - - - - - - - - - - - < Animación entre actividades STARTS > - - - - - - - - - - - - -

    // Valores pedidos en la acterior actividad
    public static final String EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X";
    public static final String EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y";
    View vistaPrincipal;    // Vista principal para la animación
    private int revealX;
    private int revealY;

    // - - - - - - - - - - - - < Animación entre actividades ENDS > - - - - - - - - - - - - -

    CircleMenu menuCircularQR;

    final Activity actividad = this;    //Actividad a lanzar para el QR
    IntentIntegrator integrador = new IntentIntegrator(actividad);

    TextView fondoNoQR;     // Fondo de color para mostrar información sobre el qr

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumno_qr);

        // - - - - - - - - - - - - < Animación entre actividades STARTS > - - - - - - - - - - - - -

        final Intent intent = getIntent();  // Intent para la revelación circular entre actividades
        vistaPrincipal = findViewById(R.id.actividad_alumno_qr);    // Casteo layout de esta actividad para animación

        if (savedInstanceState == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                intent.hasExtra(EXTRA_CIRCULAR_REVEAL_X) &&
                intent.hasExtra(EXTRA_CIRCULAR_REVEAL_Y)) {
            vistaPrincipal.setVisibility(View.INVISIBLE);

            revealX = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_X, 0);
            revealY = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_Y, 0);


            ViewTreeObserver viewTreeObserver = vistaPrincipal.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        revealActivity(revealX, revealY);
                        vistaPrincipal.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
            }
        } else {
            vistaPrincipal.setVisibility(View.VISIBLE);
        }

        // - - - - - - - - - - - - < Animación entre actividades ENDS > - - - - - - - - - - - - -

        fondoNoQR = (TextView) findViewById(R.id.fondo_no_QR);

                // - - - - - - - - - - - - < MENÚ CIRCULAR STARTS > - - - - - - - - - - - - -

                menuCircularQR = (CircleMenu) findViewById(R.id.menuCir_QR);

                menuCircularQR.openMenu();

                menuCircularQR.setMainMenu(Color.parseColor("#000000"), R.drawable.ic_qr, R.drawable.ic_pregunta)
                        .addSubMenu(Color.parseColor("#4CAF50"), R.drawable.ic_correcto)
                        .addSubMenu(Color.parseColor("#d50000"), R.drawable.ic_incorrecto)
                        .setOnMenuSelectedListener(new OnMenuSelectedListener() {

                            @Override
                            public void onMenuSelected(int index) {

                                switch (index){

                                    case 0:

                                        Handler handlerQR = new Handler();    // Metodo para hacer delay al abrir el scanner
                                        handlerQR.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {

                                                //  Iniciar Scanner QR
                                                //El objeto "integrador" es el que da atributos al lector (cámara)
                                                integrador.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                                                integrador.setCaptureActivity(ScannerVertical.class);
                                                integrador.setOrientationLocked(true);
                                                integrador.setPrompt(getResources().getString(R.string.promptSccanner));
                                                integrador.initiateScan();
                                            }
                                        }, 500);    // Tiempo que tardará en abrir en milisegundos

                                        break;      // break caso 0

                                    case 1:

                                        Handler handlerNoQR = new Handler();    // Metodo para hacer delay al abrir el scanner
                                        handlerNoQR.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {


                                                // - - - - - - - - - - - - < Efecto Revelar Starts > - - - - - - - - - - - - -

                                                if (Build.VERSION.SDK_INT >= 21) {  //Condición para el material design

                                                    int cx = (fondoNoQR.getLeft() + fondoNoQR.getRight()) / 2;
                                                    int cy = (fondoNoQR.getTop() + fondoNoQR.getBottom()) / 2;

                                                    // Consiguiendo el Final del radio
                                                    int finalRadius = Math.max(fondoNoQR.getWidth(), fondoNoQR.getHeight());

                                                    // Creando la animacion para esta vista, el radio de inico es cero
                                                    Animator anim =
                                                            ViewAnimationUtils.createCircularReveal(fondoNoQR, cx, cy, 0, finalRadius);

                                                    // Haciendo la vista visible e iniciando la animación
                                                    fondoNoQR.setVisibility(View.VISIBLE);
                                                    anim.setDuration(600);  // Velocidad de la animación al revelar
                                                    anim.start();
                                                }else{
                                                    fondoNoQR.setVisibility(View.VISIBLE);  // Acción sin animación
                                                }

                                                // - - - - - - - - - - - - < Efecto Revelar Ends > - - - - - - - - - - - - -
                                            }
                                        }, 700);    // Tiempo que tardará en abrir en milisegundos

                                        break;      // break caso 1
                                }

                            }

                        }).setOnMenuStatusChangeListener(new OnMenuStatusChangeListener() {

                    @Override
                    public void onMenuOpened() {


                        // - - - - - - - - - - - - < Efecto Revelar Starts > - - - - - - - - - - - - -

                        if (Build.VERSION.SDK_INT >= 21) {

                            // obteniendo el centro de el circulo
                            int cx = (fondoNoQR.getLeft() + fondoNoQR.getRight()) / 2;
                            int cy = (fondoNoQR.getTop() + fondoNoQR.getBottom()) / 2;

                            // obteniendo el radio inicial de el circulo
                            int initialRadius = fondoNoQR.getWidth();

                            // creando la animación (el final del radio es cero)
                            Animator anim =
                                    ViewAnimationUtils.createCircularReveal(fondoNoQR, cx, cy, initialRadius, 0);

                            // haciendo la vista invisible cuando la animación esta acabada
                            anim.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    fondoNoQR.setVisibility(View.INVISIBLE);
                                }
                            });

                            // inicia la animación
                            anim.start();
                        } else {
                            fondoNoQR.setVisibility(View.INVISIBLE);  // Acción sin animación
                        }


                        // - - - - - - - - - - - - < Efecto Revelar Ends > - - - - - - - - - - - - -
                    }

                    @Override
                    public void onMenuClosed() {}

                });

                // - - - - - - - - - - - - < MENÚ CIRCULAR ENDS > - - - - - - - - - - - - -

    }

    // Resultado del Scanner
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult resultado = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (resultado != null){
            if (resultado.getContents() == null){
                // Mensaje en caso de ser cancelada la lectura del QR
                View viewSnackbar = findViewById(R.id.actividad_alumno_qr);
                Snackbar.make(viewSnackbar, R.string.lecturaQRCancel, Snackbar.LENGTH_LONG).show();
            }else{

                String idValido = resultado.getContents();
                if(idValido.length()==128){

                    // - - - -  -- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

                    String encriptado = idValido;

                    // Extrayendo con la key
                    String numeroControl = encriptado.substring(8, 9) +
                            encriptado.substring(16, 17) +
                            encriptado.substring(32, 33) +
                            encriptado.substring(56, 57) +
                            encriptado.substring(64, 65) +
                            encriptado.substring(84, 85) +
                            encriptado.substring(98, 99) +
                            encriptado.substring(114, 115);

                    // Recuperando números
                    numeroControl = numeroControl.replace("=", "1");
                    numeroControl = numeroControl.replace("#", "2");
                    numeroControl = numeroControl.replace("+", "3");
                    numeroControl = numeroControl.replace("a", "4");
                    numeroControl = numeroControl.replace("e", "5");
                    numeroControl = numeroControl.replace("F", "6");
                    numeroControl = numeroControl.replace("d", "7");
                    numeroControl = numeroControl.replace("y", "8");
                    numeroControl = numeroControl.replace("z", "9");
                    numeroControl = numeroControl.replace("B", "0");

                    // - - - -  -- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -


                    Toast.makeText(this, "El ID es " + numeroControl, Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(this, idValido + "\n \n Es un codigo no valido para esta app " , Toast.LENGTH_LONG).show();
                }
            }



        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    // - - - - - - - - - - - - < Animación entre actividades STARTS METODOS> - - - - - - - - - - - - -

    protected void revealActivity(int x, int y) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            float finalRadius = (float) (Math.max(vistaPrincipal.getWidth(), vistaPrincipal.getHeight()) * 1.1);

            // Crear la animación desde esta vista
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(vistaPrincipal, x, y, 0, finalRadius);
            circularReveal.setDuration(400);
            circularReveal.setInterpolator(new AccelerateInterpolator());

            // Hacer visible la animacion
            vistaPrincipal.setVisibility(View.VISIBLE);
            circularReveal.start();
        } else {
            finish();
        }
    }

    protected void unRevealActivity() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            finish();
        } else {
            float finalRadius = (float) (Math.max(vistaPrincipal.getWidth(), vistaPrincipal.getHeight()) * 1.1);
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(
                    vistaPrincipal, revealX, revealY, finalRadius, 0);

            circularReveal.setDuration(400);
            circularReveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    vistaPrincipal.setVisibility(View.INVISIBLE);
                    finish();
                }
            });


            circularReveal.start();
        }
    }

    // - - - - - - - - - - - - < Animación entre actividades ENDS METODOS> - - - - - - - - - - - - -

}
