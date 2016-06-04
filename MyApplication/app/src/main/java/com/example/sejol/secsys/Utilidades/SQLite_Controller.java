package com.example.sejol.secsys.Utilidades;

/**
 * Created by sejol on 4/6/2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.sejol.secsys.Clases.Ronda;
import com.example.sejol.secsys.Clases.Usuario;

import java.util.ArrayList;

public class SQLite_Controller extends SQLiteOpenHelper {

    public static final String DATABASE_NOMBRE = "SecsysBD.db";

    public static final String TABLE_USUARIO = "usuario";
    public static final String COLUMN_NOMBRE        = "nombre";
    public static final String COLUMN_USUARIO       = "usuario";
    public static final String COLUMN_CONTRASEÑA    = "contraseña";

    public static final String TABLE_RONDA = "ronda";
    public static final String COLUMN_CODIGO_R = "codigo";
    public static final String REF_USUARIO     = "usuario";

    public static final String TABLE_TAG   = "tag";
    public static final String COLUMN_CODIGO_T = "codigo";
    public static final String REF_RONDA       = "ronda";
    public static final String COLUMN_POS      = "pos";

    public SQLite_Controller(Context context) {
        super(context, DATABASE_NOMBRE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " + TABLE_USUARIO +
                        "(" + COLUMN_USUARIO    + " text primary key," +
                              COLUMN_CONTRASEÑA + " text, " +
                              COLUMN_NOMBRE     + " text )");

        db.execSQL(
                "create table " + TABLE_RONDA +
                        "(" + COLUMN_CODIGO_R   + " text primary key," +
                              COLUMN_NOMBRE     + " text," +
                              REF_USUARIO       + " text," +
                             " FOREIGN KEY ("+REF_USUARIO+") REFERENCES "+TABLE_USUARIO+"("+COLUMN_USUARIO+"));");

        db.execSQL(
                "create table " + TABLE_TAG +
                        "(" + COLUMN_CODIGO_T   + " text primary key," +
                              REF_RONDA         + " text, " +
                              COLUMN_POS        + " text, " +
                             " FOREIGN KEY ("+REF_RONDA+") REFERENCES "+TABLE_RONDA+"("+COLUMN_CODIGO_R+"));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)  {
        db.execSQL("drop table if exists " + TABLE_USUARIO);
        onCreate(db);
    }

    public boolean insertUser(String nombre, String usuario, String contraseña){
        SQLiteDatabase bd = this.getWritableDatabase();
        ContentValues registro = new ContentValues();
        registro.put(COLUMN_NOMBRE , nombre);
        registro.put(COLUMN_USUARIO, usuario);
        registro.put(COLUMN_CONTRASEÑA, contraseña);
        bd.insert(TABLE_USUARIO, null, registro);
        bd.close();
        return true;
    }

    public boolean insertRonda(String codigo, String nombre, String usuario){
        SQLiteDatabase bd = this.getWritableDatabase();
        ContentValues registro = new ContentValues();
        registro.put(COLUMN_CODIGO_R , codigo);
        registro.put(COLUMN_NOMBRE, nombre);
        registro.put(REF_USUARIO, usuario);
        bd.insert(TABLE_RONDA, null, registro);
        bd.close();
        return true;
    }

    public boolean insertTag(String condigo, String ronda, String pos){
        SQLiteDatabase bd = this.getWritableDatabase();
        ContentValues registro = new ContentValues();
        registro.put(COLUMN_CODIGO_T , condigo);
        registro.put(REF_RONDA, ronda);
        registro.put(COLUMN_POS, pos);
        bd.insert(TABLE_TAG, null, registro);
        bd.close();
        return true;
    }

    public ArrayList<Ronda> selectRondas(Usuario usuario){
        String selectQuery = "SELECT  * FROM " + TABLE_RONDA + "WHERE" +  COLUMN_USUARIO + " = " + usuario.getUsuario();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<Ronda> arrayListData = new ArrayList<Ronda>();
        if (cursor.moveToFirst()) {
            do {
                Ronda ronda = new Ronda();
                ronda.setCodigo (cursor.getString(0));
                ronda.setNombre (cursor.getString(1));
                ronda.setUsuario(cursor.getString(2));
                arrayListData.add(ronda);
            } while (cursor.moveToNext());
        }
        return arrayListData;
    }


}

