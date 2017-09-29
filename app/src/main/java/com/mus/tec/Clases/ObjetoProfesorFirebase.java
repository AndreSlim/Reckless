package com.mus.tec.Clases;

/**
 * Created by andres on 27/09/17.
 */

public class ObjetoProfesorFirebase {

    String nombre, informacion;
    boolean disponible;

    public ObjetoProfesorFirebase() {
    }

    public ObjetoProfesorFirebase(String nombre, String informacion, boolean disponible) {
        this.nombre = nombre;
        this.informacion = informacion;
        this.disponible = disponible;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getInformacion() {
        return informacion;
    }

    public void setInformacion(String informacion) {
        this.informacion = informacion;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
}
