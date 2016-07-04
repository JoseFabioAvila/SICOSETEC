package com.example.sejol.secsys.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.example.sejol.secsys.Clases.Ruta;
import com.example.sejol.secsys.Clases.Tag;
import com.example.sejol.secsys.R;

import java.util.Arrays;
import java.util.List;

public class ModificarTagActivity extends AppCompatActivity {

    Tag tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_tag);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        tag = (Tag) intent.getSerializableExtra("tag");

        EditText txtNombre = (EditText)findViewById(R.id.txtMdfTagNombre);
        EditText txtLat    = (EditText)findViewById(R.id.txtMdfTagLat);
        EditText txtLng    = (EditText)findViewById(R.id.txtMdfTagLng);

        txtNombre.setText(tag.getNombre());
        List<String> tagCodigo = Arrays.asList(tag.getCodigo().split("_"));
        txtLat.setText(tagCodigo.get(3));
        txtLng.setText(tagCodigo.get(2));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
