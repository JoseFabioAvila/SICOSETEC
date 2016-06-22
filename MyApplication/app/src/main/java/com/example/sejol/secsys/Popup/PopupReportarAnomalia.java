package com.example.sejol.secsys.Popup;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;

import com.example.sejol.secsys.R;

public class PopupReportarAnomalia extends AppCompatActivity {

    EditText titulo;
    EditText descripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_reportar_anomalia);

        titulo = (EditText)findViewById(R.id.popAnomaliaTitulo);
        descripcion = (EditText)findViewById(R.id.popAnomaliaDescripcion);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAnomaliaGuardar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent localIntent = new Intent();
                localIntent.putExtra("titulo",titulo.getText().toString());
                localIntent.putExtra("descripcion",descripcion.getText().toString());
                setResult(3, localIntent);
                finish();
            }
        });

        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int width = localDisplayMetrics.widthPixels;
        int height = localDisplayMetrics.heightPixels;
        getWindow().setLayout((int)(0.70 * width), (int)(0.60 * height));
    }
}
