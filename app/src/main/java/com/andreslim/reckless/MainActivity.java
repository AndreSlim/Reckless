package com.andreslim.reckless;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;
import com.hitomi.cmlibrary.OnMenuStatusChangeListener;

public class MainActivity extends AppCompatActivity {

    TextView fondo;     // Fondo de color para menú circular

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fondo = (TextView) findViewById(R.id.fondo_menuCir);

        // - - - - - - - - - - - - < MENÚ CIRCULAR STARTS > - - - - - - - - - - - - -

        CircleMenu menuCircular = (CircleMenu) findViewById(R.id.menuCir);

        menuCircular.setMainMenu(Color.parseColor("#f44336"), R.drawable.ic_nuevo_mas, R.drawable.ic_nuevo_cancelar)
                .addSubMenu(Color.parseColor("#4CAF50"), R.drawable.ic_visitante)
                .addSubMenu(Color.parseColor("#FF5722"), R.drawable.ic_profesor)
                .addSubMenu(Color.parseColor("#2196F3"), R.drawable.ic_estudiante)
                .setOnMenuSelectedListener(new OnMenuSelectedListener() {

                    @Override
                    public void onMenuSelected(int index) {

                        // - - - - - - - - - - - - < Alunmo seleccionado Starts > - - - - - - - - - - - - -

                        switch (index){

                            case 2:     // Alumno seleccionado

                                Toast.makeText(MainActivity.this, "Alumno seleccionado", Toast.LENGTH_SHORT).show();
                        }

                        // - - - - - - - - - - - - < Alunmo seleccionado Ends > - - - - - - - - - - - - -

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
                    anim.start();
                }else{
                    fondo.setVisibility(View.VISIBLE);  // Acción sin animación
                }

                // - - - - - - - - - - - - < Efecto Revelar Ends > - - - - - - - - - - - - -

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
            }
        });

        // - - - - - - - - - - - - < MENÚ CIRCULAR ENDS > - - - - - - - - - - - - -
    }
}
