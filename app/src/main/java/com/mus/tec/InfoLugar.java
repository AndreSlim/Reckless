package com.mus.tec;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class InfoLugar extends DialogFragment {

    private static String contenido;
    private static String titulo;

    public InfoLugar() {
        // Required empty public constructor
    }

    public static InfoLugar newInstance(String contenido1, String titulo1) {
        contenido = contenido1;
        titulo = titulo1;
        InfoLugar fragment = new InfoLugar();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        //Obtiene instancia de la action bar
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            //hablita el up button
            actionBar.setDisplayHomeAsUpEnabled(true);
            //cambia el icono del up button
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_close);
            actionBar.setTitle(titulo);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info_lugar, container, false);
        TextView t = view.findViewById(R.id.textView2);
        t.setText(contenido);
        //ImageView i = (ImageView) view.findViewById(R.id.image);
       // i.setImageBitmap(bitma);
        //getDialog().setTitle(titulo);

        return view;
    }


}
