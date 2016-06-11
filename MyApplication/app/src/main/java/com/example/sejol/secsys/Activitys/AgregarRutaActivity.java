package com.example.sejol.secsys.Activitys;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sejol.secsys.Clases.Ruta;
import com.example.sejol.secsys.Clases.Tag;
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

    Ruta rutaA;

    ArrayList<String> PntsTagRuta = new ArrayList<>(); // Putos en el recorrido de la ruta
    SQLite_Controller db; // Clase para accesar a la base de datos

    ArrayAdapter<String> adapter; // Adapter para el listview

    private NFC_Controller nfcController; // Controlador de nfc
    private ListView listView; // Lista para mostrar los puntos en la ruta
    private TextView txtNombre; // Nombre para el recorrido

    private boolean dialog = false;

    int codigoTag = 0; // Contador de puntos

    CustomDialogClass cdd; //

    String codigo; // Codigo o id de la ruta a asignar a los puntos

    boolean agregar = false;

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
                cdd = new CustomDialogClass(AgregarRutaActivity.this); // En espera de NFC
                cdd.show();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new SQLite_Controller(this); // Inicializar la base de datos
        txtNombre = (TextView) findViewById(R.id.txtAddRutNombre); // Nombre de la ruta
        listView = (ListView) findViewById(R.id.listViewAR); // Lista de puntos

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        if(getIntent().getExtras().getString("ruta").equals("")) {
            agregar = true;
        }
        else {
            agregar = false;
            txtNombre.setText(getIntent().getExtras().getString("ruta"));
        }

        adapterSet(getIntent().getExtras().getString("ruta")); // Setear lista
        nfcController = new NFC_Controller(this, 2);

        if (!nfcController.mNfcAdapter.isEnabled()) { // Estado del NFC en el telefono
            Toast.makeText(this,"Lectura NFC desactivado",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this,"Lectura NFC activado",Toast.LENGTH_SHORT).show();
        }
    }

    /*
        Metodo para configurar el adapter para la lista de puntos en la ruta
     */
    private void adapterSet(String ruta) {
        if(!agregar){
            ArrayList<Ruta> ruta1 = db.getRutas();
            for(Ruta rut: ruta1){
                if(rut.getNombre().equals(getIntent().getExtras().getString("ruta"))){
                    rutaA = rut;
                }
            }
            ArrayList<Tag> tags = db.getTagsDeRutaPorRuta(rutaA.getCodigo());
            for(Tag tag: tags){
                if(tag.getRonda().equals(rutaA.getCodigo())){
                    PntsTagRuta.add(tag.getCodigo());
                }
            }
        }
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, PntsTagRuta);
        listView.setAdapter(adapter);
    }

    /*
        Mostrar dialogo para confirmación de guardado
     */
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
                        db.insertRuta(codigo,nombre); // Almacenar ruta en la base de datos
                        for (String tag: PntsTagRuta) {
                            db.insertTagRUT(tag,codigo); // Almacenar tag y asignarlo a la ruta creada
                        }
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

    /*
        Metodo que crea un ID para la ruta
     */
    private String crearCodigoRuta(String nombre){
        String fecha = new SimpleDateFormat("ddMMyyyy").format(new Date());
        Random randomGenerator = new Random();
        int RndNum = randomGenerator.nextInt(1000);

        return nombre+"_"+fecha+"_"+RndNum;
    }

    /*
        Dialogo para indicarle al usaurio que acerque un Tag de NFC para
        escribirle y almacenarlo en la ruta
     */
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (dialog) {

            GPS_Tracker gps_tracker = new GPS_Tracker(this);
            String codigo = String.valueOf(codigoTag)+"_"+ // Contador de tags en ruta
                    ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID))+"_"+ // Mac de tag
                    gps_tracker.getLongitude()+"_"+ // Latitud actual (ubicacion del tag)
                    gps_tracker.getLatitude(); // Latitud actual

            PntsTagRuta.add(codigo); // Guardar tag
            nfcController.write(intent, codigo); // Escribir el codigo en el NFC
            listView.setAdapter(adapter); // Actualizar ListView
            cdd.dismiss(); // Cerrar dialog
            dialog = false;
        }
    }

    /*
        Metodo para convertir los bytes de la mac del TAG NFC a string
     */
    private String ByteArrayToHexString(byte [] inarray) {
        int i, j, in;
        String [] hex = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
        String out= "";

        for(j = 0 ; j < inarray.length ; ++j)
        {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
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
}