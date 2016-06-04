package com.example.sejol.secsys.Clases;

/**
 * Created by sejol on 4/6/2016.
 */
public class Ruta {
    private String codigo,nombre,usuario;

    public Ruta() {
    }

    public Ruta(String codigo, String nombre, String usuario) {
        this.codigo = codigo;
        this.nombre = nombre;
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

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}
