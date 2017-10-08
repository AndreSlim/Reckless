package com.mus.tec;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class ContenedorInfoLugar extends AppCompatActivity {
    public static final String TITULO="titulo";
    public static final String CONTENIDO="contenido";
    private String titulo;
    private String contenido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contenedor_info_lugar);
        //Recibiendo parametros de MapaTec
        if(savedInstanceState==null)
        {

            Bundle extras=getIntent().getExtras();
            if(extras==null)
            {
                titulo=null;
                contenido=null;
            }
            else
            {
                titulo=extras.getString(TITULO);
                contenido=extras.getString(CONTENIDO);
            }
        }
        else
        {
            titulo=(String)savedInstanceState.getSerializable(TITULO);
            contenido=(String)savedInstanceState.getSerializable(CONTENIDO);
        }
        crearInfoFullDialogo();
    }
    //----------------Mandando parametros a InfoLugar-------------------------------
    private void crearInfoFullDialogo()
    {
        FragmentManager fragmentmanager=getSupportFragmentManager();
        InfoLugar info=new InfoLugar();
        info.newInstance(contenido,titulo);
        FragmentTransaction transaction =fragmentmanager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content,info,"InfoLugarFragment").commit();
    }
    //------------------------------------------------------------------------------

    //----------Habilita la acción de retroceder a la actividad anterior al presionar el botón de retroceso---------
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id=item.getItemId();
        switch (id)
        {
            case android.R.id.home:
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();
                }
                else
                {
                    super.onBackPressed();
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //--------------------------------------------------------------------------------------------------------------
}
