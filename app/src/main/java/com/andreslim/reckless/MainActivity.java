package com.andreslim.reckless;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;
import com.hitomi.cmlibrary.OnMenuStatusChangeListener;

public class MainActivity extends AppCompatActivity {

    String arreglo[] = {"arreglo de String_1", "arreglo de String_2"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CircleMenu menuCircular = (CircleMenu) findViewById(R.id.menuCir);

        menuCircular.setMainMenu(Color.parseColor("#f44336"), R.drawable.ic_nuevo_mas, R.drawable.ic_nuevo_cancelar)
                .addSubMenu(Color.parseColor("#4CAF50"), R.drawable.ic_visitante)
                .addSubMenu(Color.parseColor("#2196F3"), R.drawable.ic_profesor)
                .addSubMenu(Color.parseColor("#FF5722"), R.drawable.ic_estudiante)
                .setOnMenuSelectedListener(new OnMenuSelectedListener() {

                    @Override
                    public void onMenuSelected(int index) {

                    }

                }).setOnMenuStatusChangeListener(new OnMenuStatusChangeListener() {

            @Override
            public void onMenuOpened() {}

            @Override
            public void onMenuClosed() {}

        });

    }
}
