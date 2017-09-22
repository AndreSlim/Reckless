package com.mus.tec;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class ProfesorLogIn extends AppCompatActivity{


    // - - - - - - - - - - - - < Animación entre actividades STARTS > - - - - - - - - - - - - -

    // Valores pedidos en la anterior actividad
    public static final String EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X";
    public static final String EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y";
    View vistaPrincipal;    // Vista principal para la animación
    private int revealX;
    private int revealY;

    // - - - - - - - - - - - - < Animación entre actividades ENDS > - - - - - - - - - - - - -

    private EditText correo, contra;
    private TextInputLayout layoutCorreo, layoutPass;

    Button ingresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profesor_log_in);


        // - - - - - - - - - - - - < Animación entre actividades STARTS > - - - - - - - - - - - - -

        final Intent intent = getIntent();  // Intent para la revelación circular entre actividades
        vistaPrincipal = findViewById(R.id.actividad_profesor_LogIn);    // Casteo layout de esta actividad para animación

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

        // - - - - - - - - - - - - < CAST STARTS > - - - - - - - - - - - - -

        correo = (EditText) findViewById(R.id.profesor_correo);
        contra = (EditText) findViewById(R.id.profesor_pass);

        layoutCorreo = (TextInputLayout) findViewById(R.id.profesor_correo_mat);
        layoutPass = (TextInputLayout) findViewById(R.id.profesor_pass_mat);

        ingresar = (Button) findViewById(R.id.boton_ingresar_profesor);
        // - - - - - - - - - - - - < CAST ENDS > - - - - - - - - - - - - -


        // Boton para ingresar pulsado - - - - - - - - - - - - - - - - - - - - - - - -
        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validar();

                String email = correo.getText().toString();
                String pass = contra.getText().toString();

                // Comprobando que no esten vacios ambos campos
                // if acortado preguntando que no sea null
                if(!email.isEmpty() && !pass.isEmpty()) {

                    IniciarSesion(email, pass);

                    Log.i("Estado Inicio Sesión", "Solicitado el inicio de Sesión");
                    Toast.makeText(ProfesorLogIn.this, "Iniciando Sesión...", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    // - - - - - - - - - - - - < VALIDAR CAMPOS STARTS > - - - - - - - - - - - - -
    private void validar(){

        boolean esValido = true;

        // Validar Correo
        if(correo.getText().toString().isEmpty()){
            layoutCorreo.setError(getString(R.string.campoVacio));
            esValido = false;
        } else if (!validarEmail(correo.getText().toString())){
            layoutCorreo.setError(getString(R.string.correoInvalido));
            esValido = false;
        } else {
            layoutCorreo.setErrorEnabled(false);
        }

        // Validar pass
        if(contra.getText().toString().isEmpty()){
            layoutPass.setError(getString(R.string.campoVacio));
            esValido = false;
        } else {
            layoutPass.setErrorEnabled(false);
        }
    }
    // Validar correo electronico bien escrito
    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    // - - - - - - - - - - - - < VALIDAR CAMPOS ENDS > - - - - - - - - - - - - -

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

    // - - - - - - - - - - - - < Iniciar Sesion STARTS> - - - - - - - - - - - - -

    private void IniciarSesion(String email, String pass){

        // Inicio de Sesión y comprobación
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

            // Comprobacion del Inicio de Sesión

                if(task.isSuccessful()){

                    Log.i("Estado Inicio Sesión","Inicio de sesión correcto");

                    // Intent corto para iniciar la Activity del profesor
                    startActivity(new Intent(getApplicationContext(), MainActivityProfesor.class));
                    finish();

                } else{ // Manejo de excepciones - - - - - - - - - - - - - - - - - - -

                    if(task.getException().getLocalizedMessage().toString().equals("The password is invalid or the user does not have a password.")){

                        Toast.makeText(ProfesorLogIn.this, "La contraseña es invalida", Toast.LENGTH_LONG).show();

                    }else if(task.getException().getLocalizedMessage().toString().equals("There is no user record corresponding to this identifier. The user may have been deleted.")){

                        Toast.makeText(ProfesorLogIn.this, "El correo electronico no existe o fue eliminado", Toast.LENGTH_SHORT).show();

                    }

                    Log.e("Estado Inicio Sesión","Error: "+task.getException().getLocalizedMessage());

                }

            }
        });

    }
    // - - - - - - - - - - - - < Iniciar Sesion ENDS> - - - - - - - - - - - - -

}
