package com.example.sejol.secsys.Clases;

import java.io.Serializable;

/**
 * Created by sejol on 4/6/2016.
 */
public class Tag implements Serializable{
    private String codigo,nombre,mac,ronda,hora;

    public Tag(){

    }

    public Tag(String codigo, String nombre, String mac, String ronda) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.mac = mac;
        this.ronda = ronda;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getRonda() {
        return ronda;
    }

    public void setRonda(String ronda) {
        this.ronda = ronda;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
}
