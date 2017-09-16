package com.andreslim.reckless;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;
import com.hitomi.cmlibrary.OnMenuStatusChangeListener;

public class AlumnoQR extends AppCompatActivity {

    CircleMenu menuCircularQR;

    final Activity actividad = this;    //Actividad a lanzar para el QR
    IntentIntegrator integrador = new IntentIntegrator(actividad);

    TextView fondoNoQR;     // Fondo de color para mostrar información sobre el qr

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumno_qr);

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
                                                integrador.setPrompt("Orale prro escaneale!");
                                                integrador.initiateScan();
                                            }
                                        }, 666);    // Tiempo que tardará en abrir en milisegundos

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
                    public void onMenuOpened() {}

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
                Toast.makeText(this, "Lectura cancelada", Toast.LENGTH_SHORT).show();
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
}
