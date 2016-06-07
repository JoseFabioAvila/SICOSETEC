package com.example.sejol.secsys.Activitys;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sejol.secsys.R;
import com.example.sejol.secsys.Utilidades.GPS_Tracker;
import com.example.sejol.secsys.Utilidades.NFC_Controller;
import com.example.sejol.secsys.Utilidades.SQLite_Controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class AgregarRutaActivity extends AppCompatActivity {

    List PntsTagRuta = new ArrayList<>();
    SQLite_Controller db;

    ArrayAdapter<String> adapter;

    private NFC_Controller nfcController;
    private ListView listView;
    private TextView txtNombre;

    private boolean dialog = false;
    int codigoTag = 0;
    CustomDialogClass cdd;
    String codigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_ruta);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cdd = new CustomDialogClass(AgregarRutaActivity.this);
                cdd.show();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new SQLite_Controller(this);
        txtNombre= (TextView) findViewById(R.id.txtAddRutNombre);
        listView = (ListView) findViewById(R.id.listViewAR);
        adapterSet();

        nfcController = new NFC_Controller(this, 2);

        if (!nfcController.mNfcAdapter.isEnabled()) {
            Toast.makeText(this,"Lectura NFC desactivado",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this,"Lectura NFC activado",Toast.LENGTH_SHORT).show();
        }
    }

    private void adapterSet() {
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, PntsTagRuta);
        listView.setAdapter(adapter);
    }

    private void alerta()
    {
        new AlertDialog.Builder(this)
                .setTitle("Atención!")
                .setMessage("¿Desea crear esta ruta?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which) {
                        String nombre = txtNombre.getText().toString();
                        codigo = crearCodigoRuta(nombre);
                        db.insertRuta(codigo,nombre);
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                })
                .show();
    }

    private String crearCodigoRuta(String nombre){
        String fecha = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        Random randomGenerator = new Random();
        int RndNum = randomGenerator.nextInt(1000);

        return nombre+"-"+fecha+"-"+RndNum;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (dialog) {
            GPS_Tracker gps_tracker = new GPS_Tracker(this);
            String codigo = String.valueOf(codigoTag)+" "+gps_tracker.getLongitude()+" "+gps_tracker.getLatitude();

            PntsTagRuta.add(codigo);
            nfcController.write(intent, codigo);

            listView.setAdapter(adapter);
            cdd.dismiss();
            dialog = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        nfcController.enableForegroundDispatchSystem();
    }

    @Override
    protected void onPause() {
        super.onPause();
        nfcController.disableForegroundDispatchSystem();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_agregar_ruta, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_guardar_ruta) {
            alerta();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class CustomDialogClass extends Dialog implements android.view.View.OnClickListener {
        TextView textView1;

        public CustomDialogClass(Activity a) {
            super(a);
            dialog = true;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.popup_write_tag);

            codigoTag++;

            textView1 = (TextView) findViewById(R.id.dialogo);
            textView1.setText("Esperando TAG...");
        }

        @Override
        public void onClick(View v) {

        }
    }
}