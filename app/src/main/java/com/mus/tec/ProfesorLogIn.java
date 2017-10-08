package com.mus.tec;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.mus.tec.Clases.AnimacionCircular;

import java.util.regex.Pattern;

public class ProfesorLogIn extends AppCompatActivity{

    // Variable animación Circular entre activities
    AnimacionCircular animacionCircular;

    private EditText textoCorreo, textoPass;
    private TextInputLayout correo, pass;
    ProgressDialog dialogoProgresoLogIn;

    Button ingresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profesor_log_in);

        // - - - - - - - - - - - - < Continuar Animación Circular Starts > - - - - - - - - - - - - -
        final Intent intent = this.getIntent();   // Recibe las cordenadas de la actividad anterior
        // Toma el ID de esta activdad
        RelativeLayout rootLayout = (RelativeLayout) findViewById(R.id.layout_activity_profesor_LogIn);
        animacionCircular = new AnimacionCircular(rootLayout, intent, this);
        // - - - - - - - - - - - - < Continuar Animación Circular Ends > - - - - - - - - - - - - - -



        // Casteo
        textoCorreo = (EditText) findViewById(R.id.profesor_correo);
        textoPass = (EditText) findViewById(R.id.profesor_pass);
        correo = (TextInputLayout) findViewById(R.id.profesor_correo_mat);
        pass = (TextInputLayout) findViewById(R.id.profesor_pass_mat);
        ingresar = (Button) findViewById(R.id.boton_ingresar_profesor);

        dialogoProgresoLogIn = new ProgressDialog(this);



        // Boton para ingresar
        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarTextoInsertado();
                String email = textoCorreo.getText().toString();
                String pass = textoPass.getText().toString();
                if(!email.isEmpty() && !pass.isEmpty()) {   // Campos no vacíos
                    IniciarSesion(email, pass);
                    Log.i("Estado Inicio Sesión", "Solicitado el inicio de Sesión");
                }
            }
        });
    }



    // - - - - - - - - - - - - < VALIDAR CAMPOS STARTS > - - - - - - - - - - - - -
    private void validarTextoInsertado(){
        boolean esValido = true;
        if(textoCorreo.getText().toString().isEmpty()){         // Validar Correo
            correo.setError(getString(R.string.campoVacio));
            esValido = false;
        } else if (!validarEmail(textoCorreo.getText().toString())){
            correo.setError(getString(R.string.correoInvalido));
            esValido = false;
        } else {
            correo.setErrorEnabled(false);
        }
        if(textoPass.getText().toString().isEmpty()){           // Validar pass
            pass.setError(getString(R.string.campoVacio));
            esValido = false;
        } else {
            pass.setErrorEnabled(false);
        }
    }
    private boolean validarEmail(String email) {                // Validar expresión regular email
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }// - - - - - - - - - - - - < VALIDAR CAMPOS ENDS > - - - - - - - - - - - - -




    // - - - - - - - - - - - - < Iniciar Sesion STARTS> - - - - - - - - - - - - -
    private void IniciarSesion(String email, String pass){
        // Dialogo de Inicio de sesión
        dialogoProgresoLogIn.setTitle(getString(R.string.progreso_dialogo_profesor_titulo));
        dialogoProgresoLogIn
                .setMessage(getString(R.string.progreso_dialogo_profesor_mensaje) + email);
        dialogoProgresoLogIn.setCancelable(false);
        dialogoProgresoLogIn.show();
        // Inicio de Sesión y comprobación
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // View para SnackBar
                View viewActividadPrincipal = findViewById(R.id.layout_activity_profesor_LogIn);
                // Comprobacion del Inicio de Sesión
                if(task.isSuccessful()){
                    Log.i("Estado Inicio Sesión","Inicio de sesión correcto");
                    // Ocultar el dialo de progreso
                    dialogoProgresoLogIn.dismiss();
                    // Go to Main Profesor
                    startActivity(new Intent(getApplicationContext(), MainActivityProfesor.class));
                    finish();
                } else{ // Manejo de excepciones - - - - - - - - - - - - - - - - - - -

                    // Ocultar el dialo de progreso
                    dialogoProgresoLogIn.dismiss();

                    if(task.getException().getLocalizedMessage().toString()
                            .equals("The password is invalid or the user does not have a password.")){
                        Snackbar.make(viewActividadPrincipal, R.string.passInvalido, Snackbar.LENGTH_LONG).show();
                    }else if(task.getException().getLocalizedMessage().toString()
                            .equals("There is no user record corresponding to this identifier. The user may have been deleted.")){
                        Snackbar.make(viewActividadPrincipal, R.string.emailInvalido, Snackbar.LENGTH_LONG).show();
                    }else if(task.getException().getLocalizedMessage().toString()
                            .equals("A network error (such as timeout, interrupted connection or unreachable host) has occurred.")) {
                        Snackbar.make(viewActividadPrincipal, R.string.sinRed, Snackbar.LENGTH_LONG).show();
                    }
                    Log.e("Estado Inicio Sesión","Error: "+task.getException().getLocalizedMessage());
                }
            }
        });
    }// - - - - - - - - - - - - < Iniciar Sesion ENDS> - - - - - - - - - - - - -



    // Animación circular de regreso
    @Override
    public void onBackPressed() {
        animacionCircular.ActividadReveladaInversamente();
    }



    /*/ - - - - - - - - - - - - < Siguiente Actividad con Animación Starts > - - - - - - - - - - - -
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
    }/*/// - - - - - - - - - - - - < Siguiente Actividad con Animación Starts > - - - - - - - - - - - -

}
