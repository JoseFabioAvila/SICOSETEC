package com.example.sejol.secsys.Popup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sejol.secsys.Clases.Ruta;
import com.example.sejol.secsys.R;
import com.example.sejol.secsys.Utilidades.SQLite_Controller;

import java.util.ArrayList;

public class PopupSeleccionarRuta extends AppCompatActivity {

    ArrayList<Ruta> rutas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_seleccionar_ruta);

        SQLite_Controller db = new SQLite_Controller(this);
        rutas = db.getRutas();
        ArrayList<String> nombresRutas = new ArrayList<>();
        for(Ruta ruta:rutas){
            nombresRutas.add(ruta.getNombre());
        }

        final ListView lv = (ListView)findViewById(R.id.popLvSelectRuta);
        lv.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,nombresRutas));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent localIntent = new Intent();
                localIntent.putExtra("ruta",rutas.get(position));
                setResult(1, localIntent);
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
