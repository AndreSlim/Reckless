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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;
import com.hitomi.cmlibrary.OnMenuStatusChangeListener;
import com.mus.tec.Clases.AnimacionCircular;
import com.mus.tec.Clases.ScannerVertical;

public class AlumnoLogInQR extends AppCompatActivity {

    // Variable para realizar la animación
    AnimacionCircular animacionCircular;

    CircleMenu menuCircularQR;

    // Actividad a lanzar para el QR
    final Activity actividad = this;
    IntentIntegrator lector = new IntentIntegrator(actividad);

    // Fondo para no QR
    TextView fondoNoQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumno_log_in_qr);


        // - - - - - - - - - - - - < Continuar Animación Circular Starts > - - - - - - - - - - - - -
        Intent intent = this.getIntent();   // Recibe las cordenadas de la actividad anterior
        // Toma el ID de esta activdad
        RelativeLayout rootLayout = (RelativeLayout) findViewById(R.id.layout_activity_alumnoQR);
        animacionCircular = new AnimacionCircular(rootLayout, intent, this);
        // - - - - - - - - - - - - < Continuar Animación Circular Ends > - - - - - - - - - - - - - -


        // Casteo
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
                                        Handler handlerQR = new Handler();  // Delay
                                        handlerQR.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                //  Iniciar Scanner QR
                                                lector.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                                                lector.setCaptureActivity(ScannerVertical.class);
                                                lector.setOrientationLocked(true);
                                                lector.setPrompt(getResources().getString(R.string.promptSccanner));
                                                lector.initiateScan();
                                            }
                                        }, 500);    // delay
                                        break;

                                    case 1:
                                        Handler handlerNoQR = new Handler();    // Delay
                                        handlerNoQR.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {

                                                AnimacionCircularFondoOpen();

                                            }
                                        }, 700);    // Delay
                                        break;
                                }

                            }

                        }).setOnMenuStatusChangeListener(new OnMenuStatusChangeListener() {

                    @Override
                    public void onMenuOpened() {    // Menú abierto
                        AnimacionCircularFondoClose();
                    }
                    @Override
                    public void onMenuClosed() {}
                });
                // - - - - - - - - - - - - < MENÚ CIRCULAR ENDS > - - - - - - - - - - - - -

    }




// X X X X X X X X X X X X X X X X X X X X X BETA X X X X X X X X X X X X X X X X X X X X X
    // Resultado del Scanner
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult resultado = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (resultado != null){
            if (resultado.getContents() == null){
                // Mensaje en caso de ser cancelada la lectura del QR
                View viewSnackbar = findViewById(R.id.layout_activity_alumnoQR);
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
    // X X X X X X X X X X X X X X X X X X X X X BETA X X X X X X X X X X X X X X X X X X X X X




    // - - - - - - - - - - - - < Animación del fondo Starts > - - - - - - - - - - - - - - - -
    private void AnimacionCircularFondoOpen() {
        if (Build.VERSION.SDK_INT >= 21) {  //Condición para el material design
            int cx = (fondoNoQR.getLeft() + fondoNoQR.getRight()) / 2;
            int cy = (fondoNoQR.getTop() + fondoNoQR.getBottom()) / 2;
            // Consiguiendo el Final del radio
            int finalRadius = Math.max(fondoNoQR.getWidth(), fondoNoQR.getHeight());
            // Creando la animacion para esta vista, el radio de inico es cero
            Animator anim =
                    ViewAnimationUtils.createCircularReveal
                            (fondoNoQR, cx, cy, 0, finalRadius);
            // Haciendo la vista visible e iniciando la animación
            fondoNoQR.setVisibility(View.VISIBLE);
            anim.setDuration(800);  // Velocidad de la animación al revelar
            anim.start();
        }else{
            fondoNoQR.setVisibility(View.VISIBLE);  // Acción sin animación
        }
    }
    private void AnimacionCircularFondoClose() {
        if (Build.VERSION.SDK_INT >= 21) {
            // obteniendo el centro de el circulo
            int cx = (fondoNoQR.getLeft() + fondoNoQR.getRight()) / 2;
            int cy = (fondoNoQR.getTop() + fondoNoQR.getBottom()) / 2;
            // obteniendo el radio inicial de el circulo
            int initialRadius = fondoNoQR.getWidth();
            Animator anim =
                    ViewAnimationUtils.createCircularReveal
                            (fondoNoQR, cx, cy, initialRadius, 0);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    fondoNoQR.setVisibility(View.INVISIBLE);
                }
            });
            // inicia la animación
            anim.setDuration(500);
            anim.start();
        } else {
            fondoNoQR.setVisibility(View.INVISIBLE);  // Acción sin animación
        }
    }// - - - - - - - - - - - - < Animación del fondo Ends > - - - - - - - - - - - - - - - - - -




    // Animación circular de regreso
    @Override
    public void onBackPressed() {
        animacionCircular.ActividadReveladaInversamente();
    }


}
