package com.example.sejol.secsys.Clases;

import java.io.Serializable;

/**
 * Created by sejol on 4/6/2016.
 */
public class Ruta implements Serializable{
    private String codigo,nombre;

    public Ruta() {
    }

    public Ruta(String codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
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
}
