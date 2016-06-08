package com.example.sejol.secsys.Clases;

/**
 * Created by sejol on 4/6/2016.
 */
public class Tag {
    private String codigo,ronda,hora;

    public Tag(){

    }

    public Tag(String codigo, String ronda) {
        this.codigo = codigo;
        this.ronda = ronda;
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
}
