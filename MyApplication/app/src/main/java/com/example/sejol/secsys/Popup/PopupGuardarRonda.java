package com.example.sejol.secsys.Popup;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sejol.secsys.Clases.Ronda;
import com.example.sejol.secsys.Clases.Tag;
import com.example.sejol.secsys.Clases.Usuario;
import com.example.sejol.secsys.R;
import com.example.sejol.secsys.Utilidades.SQLite_Controller;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PopupGuardarRonda extends AppCompatActivity {

    ArrayList<Tag> puntosPorRecorrer; // Tag de la ruta seleccionada
    Tag[] estadoDeRonda; // Estado del recorrido de la ruta --> Tag de la ronda

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_guardar_ronda);

        final TextView txtNombre = (TextView)findViewById(R.id.txtAddRndNombre);

        Intent intent = getIntent();
        puntosPorRecorrer = (ArrayList<Tag>) intent.getSerializableExtra("pntPorRecorrer");
        estadoDeRonda = (Tag[]) intent.getSerializableExtra("estadoRonda");
        ArrayList<String> printListTags = new ArrayList<>();
        for(int i = 0 ; i < estadoDeRonda.length ; i++){
            List<String> codeData = Arrays.asList(puntosPorRecorrer.get(i).getCodigo().split("_"));
            if(estadoDeRonda[i] != null){
                printListTags.add(
                        "Punto: " + codeData.get(0) +
                                " (Completado)");
            }else{
                printListTags.add(
                        "Punto: " + codeData.get(0) +
                                " (Incompleto)");
            }
        }

        final ListView lv = (ListView)findViewById(R.id.popLvEstadoRonda);
        lv.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,printListTags));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabGuardar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent localIntent = new Intent();
                localIntent.putExtra("nombre",txtNombre.getText().toString());
                setResult(2, localIntent);
                finish();
            }
        });

        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int width = localDisplayMetrics.widthPixels;
        int height = localDisplayMetrics.heightPixels;
        getWindow().setLayout((int)(0.70 * width), (int)(0.50 * height));
    }


}
