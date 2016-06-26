package com.example.sejol.secsys.Popup;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sejol.secsys.Clases.Ruta;
import com.example.sejol.secsys.R;
import com.example.sejol.secsys.Utilidades.SQLite_Controller;

import java.util.ArrayList;

public class PopupConfiguracionDeCorreos extends AppCompatActivity {

    ListView lstCorreos;
    SQLite_Controller db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_configuracion_de_correos);

        db = new SQLite_Controller(this);
        lstCorreos = (ListView)findViewById(R.id.popLvCorreos);
        final ArrayAdapter<String> aa;
        aa = new ArrayAdapter<String>(  this,
                android.R.layout.simple_list_item_1,
                db.getCorreos()
        );
        lstCorreos.setAdapter(aa);
        lstCorreos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                confirmarEliminarCorreo(lstCorreos.getItemAtPosition(i).toString());
            }
        });
        final EditText txtCorreo = (EditText)findViewById(R.id.popTxtCorreo);
        ImageButton btnAdd = (ImageButton)findViewById(R.id.popImgBtnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtCorreo.equals("")){
                    Toast.makeText(PopupConfiguracionDeCorreos.this, "Ingrese un correo",Toast.LENGTH_SHORT);
                }else{
                    db.insertCorreo(txtCorreo.getText().toString());
                    final ArrayAdapter<String> aa;
                    aa = new ArrayAdapter<String>(
                            PopupConfiguracionDeCorreos.this,
                            android.R.layout.simple_list_item_1,
                            db.getCorreos()
                    );
                    lstCorreos.setAdapter(aa);
                    txtCorreo.setText("");
                }
            }
        });

        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int width = localDisplayMetrics.widthPixels;
        int height = localDisplayMetrics.heightPixels;
        getWindow().setLayout((int)(0.70 * width), (int)(0.50 * height));
    }

    private void confirmarEliminarCorreo(final String s)
    {
        new AlertDialog.Builder(this)
                .setTitle("Atención!")
                .setMessage("¿Desea eliminar este correo?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        db.deleteCorreo(s);
                        final ArrayAdapter<String> aa;
                        aa = new ArrayAdapter<String>(
                                PopupConfiguracionDeCorreos.this,
                                android.R.layout.simple_list_item_1,
                                db.getCorreos()
                        );
                        lstCorreos.setAdapter(aa);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }
}
