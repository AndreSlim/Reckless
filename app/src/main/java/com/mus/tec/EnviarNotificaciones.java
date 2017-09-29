package com.mus.tec;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EnviarNotificaciones extends AppCompatActivity {

    Button enviarNoti;

    private EditText titulo, mensaje;
    private TextInputLayout tituloMaterial, mensajeMaterial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_notificaciones);

        // Casteo
        titulo = (EditText) findViewById(R.id.enviar_notificacion_titulo);
        mensaje = (EditText) findViewById(R.id.enviar_notificacion_mensaje);
        tituloMaterial = (TextInputLayout) findViewById(R.id.enviar_notificacion_titulo_material);
        mensajeMaterial = (TextInputLayout) findViewById(R.id.enviar_notificacion_mensaje_material);
        enviarNoti = (Button) findViewById(R.id.boton_enviar_notificacion);


        // Boton enviar
        enviarNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarTextoInsertado();    // Valida campos no vacíos
                String tituloNoti = titulo.getText().toString();
                String mensajeNoti = mensaje.getText().toString();
                if(!tituloNoti.isEmpty() && !mensajeNoti.isEmpty()) {   // Campos no vacíos
                    Toast.makeText(EnviarNotificaciones.this, "Pulsado!", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }



    // - - - - - - - - - - - - < VALIDAR CAMPOS STARTS > - - - - - - - - - - - - -
    private void validarTextoInsertado(){
        boolean esValido = true;
        if(titulo.getText().toString().isEmpty()) {         // Validar titulo
            tituloMaterial.setError(getString(R.string.campoVacio));
            esValido = false;
        }else {
            tituloMaterial.setErrorEnabled(false);
        }
        if(mensaje.getText().toString().isEmpty()){           // Validar mensaje
            mensajeMaterial.setError(getString(R.string.campoVacio));
            esValido = false;
        } else {
            mensajeMaterial.setErrorEnabled(false);
        }
    }// - - - - - - - - - - - - < VALIDAR CAMPOS ENDS > - - - - - - - - - - - - -

}
