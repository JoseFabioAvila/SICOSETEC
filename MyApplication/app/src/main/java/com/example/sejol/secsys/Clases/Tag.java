package com.example.sejol.secsys.Clases;

/**
 * Created by sejol on 4/6/2016.
 */
public class Tag {
    private String codigo,ronda,pos;

    public Tag(String codigo, String ronda, String pos) {
        this.codigo = codigo;
        this.ronda = ronda;
        this.pos = pos;
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

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }
}
