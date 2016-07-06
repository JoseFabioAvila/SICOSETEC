package com.example.sejol.secsys.Clases;

import android.content.Context;

import com.example.sejol.secsys.Utilidades.SQLite_Controller;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sejol on 4/6/2016.
 */
public class Ronda implements Serializable{
    private String codigo,nombre,fecha,vueltas,completa,usuario,ruta;
    public Ronda() {
        this.completa = "incompleta";
    }

    public Ronda(String codigo, String nombre, String fecha, String vueltas, String usuario, String ruta) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.fecha = fecha;
        this.vueltas = vueltas;
        this.usuario = usuario;
        this.ruta = ruta;
        this.completa = "incompleta";
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

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getVueltas() {
        return vueltas;
    }

    public void setVueltas(String vueltas) {
        this.vueltas = vueltas;
    }

    public String getCompleta() {
        return completa;
    }

    public void setCompleta(String completa) {
        this.completa = completa;
    }

    public String verificarCompletada(Context ctx){
        SQLite_Controller db = new SQLite_Controller(ctx);
        ArrayList<Tag> lstTags = db.getTagsDeRonda(this.codigo);
        if((lstTags.size() % Integer.getInteger(vueltas)) != 0){
            int[] tempCounter = new int[(lstTags.size() / Integer.getInteger(vueltas))];

        }else{
            return "incompleta";
        }
        return "completa";
    }
}
