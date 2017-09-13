package com.andreslim.reckless;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class AlumnoQR extends AppCompatActivity {

    private Button boton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumno_qr);

        boton = (Button) findViewById(R.id.boton_leerQR);
        final Activity actividad = this;    //Actividad a lanzar para el QR
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                IntentIntegrator integrador = new IntentIntegrator(actividad);

                //El objeto "integrador" es el que da atributos al lector (cámara)
                integrador.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrador.setCaptureActivity(ScannerVertical.class);
                integrador.setOrientationLocked(false);
                integrador.setPrompt("Orale prro escaneale!");
                integrador.initiateScan();

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult resultado = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (resultado != null){
            if (resultado.getContents() == null){
                Toast.makeText(this, "Se ha cancelado la lectura", Toast.LENGTH_SHORT).show();
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
