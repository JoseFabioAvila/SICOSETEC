package com.example.sejol.secsys.Activitys;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.example.sejol.secsys.R;
import com.example.sejol.secsys.Utilidades.SQLite_Controller;

public class RegistroActivity extends AppCompatActivity {

    SQLite_Controller db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new SQLite_Controller(this);
        final EditText nombre  = (EditText)findViewById(R.id.txtNombre);
        final EditText correo  = (EditText)findViewById(R.id.txtCorreo);
        final EditText contra  = (EditText)findViewById(R.id.txtContraseña);
        EditText contra2 = (EditText)findViewById(R.id.txtContraseña2);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabRegistrar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.insertUser(
                        nombre.getText().toString(),
                        correo.getText().toString(),
                        contra.getText().toString());
            }
        });
    }

}
