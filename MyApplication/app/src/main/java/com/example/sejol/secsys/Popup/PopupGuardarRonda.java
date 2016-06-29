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
    ArrayList<Tag> puntosRecorridos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_guardar_ronda);

        final TextView txtNombre = (TextView)findViewById(R.id.txtAddRndNombre);

        Intent intent = getIntent();
        Ronda ronda = (Ronda) intent.getSerializableExtra("ronda");
        puntosPorRecorrer = (ArrayList<Tag>) intent.getSerializableExtra("pntPorRecorrer");
        puntosRecorridos = (ArrayList<Tag>) intent.getSerializableExtra("pntRecorridos");

        ArrayList<String> printListTags = new ArrayList<>();
        for(int i = 0 ; i < puntosPorRecorrer.size() ; i++){
            List<String> codeData = Arrays.asList(puntosPorRecorrer.get(i).getCodigo().split("_"));
            if(contarRepeticiones(puntosPorRecorrer.get(i).getMac()) >= Integer.parseInt(ronda.getVueltas())){
                printListTags.add(
                        "Punto: " + codeData.get(0) + "  -> Completa ( " + contarRepeticiones(puntosPorRecorrer.get(i).getMac())+" )");
            }else{
                printListTags.add(
                        "Punto: " + codeData.get(0) + "  -> Incompleta ( " + contarRepeticiones(puntosPorRecorrer.get(i).getMac())+" )");
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

    private int contarRepeticiones(String mac) {
        int counter = 0;
        for(Tag tag:puntosRecorridos){
            if(tag.getMac().equals(mac)){
                counter++;
            }
        }
        return counter;
    }
}
