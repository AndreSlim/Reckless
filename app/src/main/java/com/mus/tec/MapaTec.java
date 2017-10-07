package com.mus.tec;

import android.animation.Animator;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapaTec extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnPolygonClickListener {
// - - - - - - - - - - - - < Animación entre actividades STARTS > - - - - - - - - - - - - -

    // Valores pedidos en la acterior actividad
    public static final String EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X";
    public static final String EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y";
    View vistaPrincipal;    // Vista principal para la animación
    private int revealX;
    private int revealY;

    // - - - - - - - - - - - - < Animación entre actividades ENDS > - - - - - - - - - - - - -


    private GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_tec);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);
        /*mMapView = (MapView) findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        mMapView.getMapAsync(this);*/

        // - - - - - - - - - - - - < Animación entre actividades STARTS > - - - - - - - - - - - - -

        final Intent intent = getIntent();  // Intent para la revelación circular entre actividades
        vistaPrincipal = findViewById(R.id.mapa);    // Casteo layout de esta actividad para animación

        if (savedInstanceState == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && intent.hasExtra(EXTRA_CIRCULAR_REVEAL_X) && intent.hasExtra(EXTRA_CIRCULAR_REVEAL_Y))
        {
            vistaPrincipal.setVisibility(View.INVISIBLE);
            revealX = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_X, 0);
            revealY = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_Y, 0);
            ViewTreeObserver viewTreeObserver = vistaPrincipal.getViewTreeObserver();
            if (viewTreeObserver.isAlive())
            {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
                {
                    @Override
                    public void onGlobalLayout()
                    {
                        revealActivity(revealX, revealY);
                        vistaPrincipal.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
            }
        } else {
            vistaPrincipal.setVisibility(View.VISIBLE);
        }

        // - - - - - - - - - - - - < Animación entre actividades ENDS > - - - - - - - - - - - - -


    }
    //Metodo para declarar el contenido del mapa
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(2);// 0 :ninguno, 1:normal, 2:satelite, 3:Hibrido, 4:Terreno
        mMap.setOnPolygonClickListener(this);
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomGesturesEnabled(true); //Modifica disponibilidad para los gestos de zoom
        uiSettings.setScrollGesturesEnabled(true); //Modifica gestos de desplazamiento
        uiSettings.setTiltGesturesEnabled(false); //Modifica gestos de la inclinación
        uiSettings.setRotateGesturesEnabled(false); //Modifica la disponibilidad de rotación
        //uiSettings.setAllGesturesEnabled(false); //Modifica la disponibilidad de todos los gestos al mismo tiempo


        LatLng tec = new LatLng(18.437212, -97.397943);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tec,17));

        Polygon canchafut = mMap.addPolygon(new PolygonOptions().add(new LatLng(18.437630, -97.396196), new LatLng(18.437176, -97.395790), new LatLng(18.437652, -97.395127), new LatLng(18.438141, -97.395556))
                .strokeColor(Color.WHITE)
                .fillColor(Color.parseColor("#A2405542"))
                .strokeWidth(1).clickable(true));
        canchafut.setTag("cancha fut");



        Polygon tre7 = mMap.addPolygon(new PolygonOptions().add(new LatLng(18.437208, -97.396494), new LatLng(18.437016, -97.396554), new LatLng(18.436915, -97.396159), new LatLng(18.437111, -97.396092))
                .strokeColor(Color.WHITE)
                .fillColor(Color.parseColor("#a7936163"))
                .strokeWidth(1).clickable(true));
        tre7.setTag("37");


        // color guinda #a7936163

        Polygon veintitres = mMap.addPolygon(new PolygonOptions().add(new LatLng(18.436356, -97.398283), new LatLng(18.436129, -97.398361), new LatLng(18.436071, -97.398116), new LatLng(18.436290, -97.398032))
                .strokeColor(Color.WHITE)
                .fillColor(Color.parseColor("#a7936163"))
                .strokeWidth(1).clickable(true));
        veintitres.setTag("23");
    }
    // Detección de evento al presonar un poligono
    @Override
    public void onPolygonClick(Polygon polygon) {
        String tipo="";
        if(polygon.getTag()!=null)
        {
            tipo =polygon.getTag().toString();
        }
        switch (tipo)
        {
            case "cancha fut":
                //Toast.makeText(this, "Se presiono "+polygon.getTag().toString(), Toast.LENGTH_SHORT).show();
                break;
            case "37":
                //Toast.makeText(this, "Se presiono el edificio "+polygon.getTag().toString(), Toast.LENGTH_SHORT).show();
                break;
            case "23":
                //Toast.makeText(this, "Se presiono el edificio "+polygon.getTag().toString(), Toast.LENGTH_SHORT).show();
                DialogLista().show();
                break;
        }

    }
    //Lista que muestra el nombreo o los departamentos de cada edificio
    public AlertDialog DialogLista()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MapaTec.this);

        final CharSequence[] items = new CharSequence[3];

        items[0] = "23-A";
        items[1] = "23-F";
        items[2] = "No se";

        builder.setTitle("Edificio de Cómputo").setItems(items, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        switch (which)
                        {
                            case 0:
                                LlamaFragLugar("Opcion uno seleccionada  :v ",items[0].toString());
                                break;
                            case 1:
                                LlamaFragLugar("Opcion dos seleccionada :3",items[1].toString());
                                break;
                            case 2:
                                LlamaFragLugar("Opcion tres seleccionada xD",items[2].toString());
                                break;
                        }

                    }
                });

        return builder.create();
    }
    public void LlamaFragLugar(String contenido,String titulo)
    {
       String t,c;
        t=ContenedorInfoLugar.TITULO;
        c=ContenedorInfoLugar.CONTENIDO;
       Intent intent=new Intent(this,ContenedorInfoLugar.class);
        intent.putExtra(t,titulo);
        intent.putExtra(c,contenido);
        startActivity(intent);
    }



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
    // - - - - - - - - - - - - < Animación entre actividades ENDS METODOS> - - - - - - - - - - - - -

}
