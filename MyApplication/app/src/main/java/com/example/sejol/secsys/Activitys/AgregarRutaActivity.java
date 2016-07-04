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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sejol.secsys.Adapters.TagListViewAdapter;
import com.example.sejol.secsys.Clases.Ruta;
import com.example.sejol.secsys.Clases.Tag;
import com.example.sejol.secsys.R;
import com.example.sejol.secsys.Utilidades.GPS_Tracker;
import com.example.sejol.secsys.Utilidades.NFC_Controller;
import com.example.sejol.secsys.Utilidades.SQLite_Controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class AgregarRutaActivity extends AppCompatActivity {

    ArrayList<Tag> PntsTagRuta = new ArrayList<>(); // Putos en el recorrido de la ruta
    SQLite_Controller db; // Clase para accesar a la base de datos

    TagListViewAdapter adapter; // Adapter para el listview

    private NFC_Controller nfcController; // Controlador de nfc
    private ListView listView; // Lista para mostrar los puntos en la ruta
    private EditText txtNombre; // Nombre para el recorrido
    private EditText txtVueltas; // Numero de vueltas a la ronda

    private boolean dialog = false;

    int codigoTag = 0; // Contador de puntos

    EsperarTagDialogClass cdd; //

    String codigoRuta; // Codigo o id de la ruta a asignar a los puntos

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
                cdd = new EsperarTagDialogClass(AgregarRutaActivity.this); // En espera de NFC
                cdd.show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new SQLite_Controller(this); // Inicializar la base de datos
        txtNombre = (EditText) findViewById(R.id.txtAddRutNombre); // Nombre de la ruta (editor)
        txtVueltas = (EditText)findViewById(R.id.txtNumVueltas); // Numero de vuelta (editor)
        listView = (ListView) findViewById(R.id.listViewAR); // Lista de puntos
        adapterSet();
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
    private void adapterSet() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                //Toast.makeText(view.getContext(),"List item "+position,Toast.LENGTH_SHORT).show();
                final PopupMenu popup = new PopupMenu(view.getContext(), listView);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.opciones_tag, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.modificar:
                                Toast.makeText(AgregarRutaActivity.this,"Pronto",Toast.LENGTH_LONG).show();
                                return true;
                            case R.id.borrar:
                                db.borrarRuta(PntsTagRuta.get(position).getCodigo());
                                PntsTagRuta.remove(position);
                                adapter.notifyDataSetChanged();
                                return true;
                        }
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });
        adapter = new TagListViewAdapter(PntsTagRuta, AgregarRutaActivity.this);
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
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String nombre = txtNombre.getText().toString();
                        String vueltas = txtVueltas.getText().toString();
                        codigoRuta = crearCodigoRuta(nombre);
                        ArrayList<Ruta> rutas = db.getRutas();
                        for (Ruta ruta : rutas) {
                            if (ruta.getNombre().equals(getIntent().getExtras().getString("ruta"))) {
                                db.borrarRuta(ruta.getNombre());
                            }
                        }
                        db.insertRuta(codigoRuta, nombre, vueltas); // Almacenar ruta en la base de datos
                        for (Tag tag : PntsTagRuta) {
                            db.insertTagRUT(
                                    tag.getCodigo(),
                                    tag.getNombre(),
                                    Arrays.asList(tag.getCodigo().split("_")).get(1),
                                    codigoRuta); // Almacenar tag y asignarlo a la ruta creada
                        }
                        Intent localIntent = new Intent();
                        localIntent.putExtra("ruta",nombre);
                        setResult(1, localIntent);
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    /*
        Metodo que crea un ID para la ruta
     */
    private String crearCodigoRuta(String nombre){
        String fecha = new SimpleDateFormat("dd/MM/yy_HH:mm:ss").format(new Date());
        return nombre+"_"+fecha;
    }

    /*
        Dialogo para indicarle al usaurio que acerque un Tag de NFC para
        escribirle y almacenarlo en la ruta
     */
    public class EsperarTagDialogClass extends Dialog implements android.view.View.OnClickListener {
        TextView textView1;

        public EsperarTagDialogClass(Activity a) {
            super(a);
            dialog = true;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.popup_esperando_tag);

            codigoTag++;

            textView1 = (TextView) findViewById(R.id.dialogo);
            textView1.setText("Esperando TAG...");
        }

        @Override
        public void onClick(View v) {

        }
    }

    public class IngresarNombreDelTagDialogClass extends Dialog implements android.view.View.OnClickListener {
        TextView textView1;

        public IngresarNombreDelTagDialogClass(Activity a) {
            super(a);
            dialog = true;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.popup_nombre_tag);

            final EditText nombre = (EditText) findViewById(R.id.popTxtTagNombre);
            ImageButton ok = (ImageButton) findViewById(R.id.popBtnOk);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Tag)(PntsTagRuta.get(PntsTagRuta.size() - 1))).setNombre(nombre.getText().toString());
                    listView.setAdapter(adapter); // Actualizar ListView
                    dismiss();
                }
            });
        }

        @Override
        public void onClick(View v) {

        }
    }

    /*
    Detector de tags nfc
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (dialog) {
            GPS_Tracker gps_tracker = new GPS_Tracker(this);
            String codigo =
                    String.valueOf(codigoTag) + "_" + // Contador de tags en ruta
                            ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)) + "_" + // Mac de tag
                            gps_tracker.getLongitude() + "_" + // Latitud actual (ubicacion del tag)
                            gps_tracker.getLatitude(); // Latitud actual
            Tag nuevoTag = new Tag();
            nuevoTag.setCodigo(codigo);
            PntsTagRuta.add(nuevoTag); // Guardar tag
            nfcController.write(intent, codigo); // Escribir el codigo en el NFC
            //listView.setAdapter(adapter); // Actualizar ListView
            cdd.dismiss(); // Cerrar dialog
            dialog = false;

            IngresarNombreDelTagDialogClass INTDC = new IngresarNombreDelTagDialogClass(AgregarRutaActivity.this); // En espera de NFC
            INTDC.show();
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
        getMenuInflater().inflate(R.menu.menu_guardar_ruta, menu);
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