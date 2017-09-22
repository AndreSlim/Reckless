package com.mus.tec;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivityProfesor extends AppCompatActivity {

    TextView emailProfesor;
    Button cerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_profesor);

        emailProfesor = (TextView) findViewById(R.id.email_profesor);
        cerrarSesion = (Button) findViewById(R.id.boton_cerrar_sesion);

        // creando variable user para tener información del Inicio de Sesión
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

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

    }
}
