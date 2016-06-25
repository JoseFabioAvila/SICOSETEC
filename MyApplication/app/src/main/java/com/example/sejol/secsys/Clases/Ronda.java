package com.example.sejol.secsys.Clases;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sejol on 4/6/2016.
 */
public class Ronda implements Serializable{
    private String codigo,nombre,fecha,usuario;
    private ArrayList<Reporte> reportes;

    public Ronda() {
    }

    public Ronda(String codigo, String nombre, String fecha, String usuario) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.fecha = fecha;
        this.usuario = usuario;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

}
