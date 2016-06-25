package com.example.sejol.secsys.Clases;

import java.io.Serializable;

/**
 * Created by jose on 6/21/16.
 */

public class Reporte implements Serializable{

    private String anomalia;
    private String descripcion;
    private String hora;

    public Reporte(String anomalia, String descripcion, String hora) {
        this.anomalia = anomalia;
        this.descripcion = descripcion;
        this.hora = hora;
    }

    public Reporte() {

    }

    public String getAnomalia() {
        return anomalia;
    }

    public void setAnomalia(String anomalia) {
        this.anomalia = anomalia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}