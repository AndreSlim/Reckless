package com.mus.tec;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class VerNotificacion extends AppCompatActivity {

    TextView tituloNoti, cuerpoNoti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_notificacion);

        tituloNoti = (TextView) findViewById(R.id.texto_noti_titulo);
        cuerpoNoti = (TextView) findViewById(R.id.texto_noti_cuerpo);

                String mensaje = getIntent().getDataString();
            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
                cuerpoNoti.setText(mensaje);



    }
}
