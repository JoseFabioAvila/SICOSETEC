package com.example.sejol.secsys.Activitys;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.example.sejol.secsys.Utilidades.GPS_Controller;
import com.example.sejol.secsys.Utilidades.NFC_Controller;
import com.example.sejol.secsys.Utilidades.SQLite_Controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class ModificarRutaActivity extends AppCompatActivity {

    private SQLite_Controller db;

    private NFC_Controller nfcController; // Controlador de nfc
    private boolean dialog = false;
    int codigoTag = 0; // Contador de puntos
    EsperarTagDialogClass cdd;

    Ruta ruta;
    ListView lvTags;
    TagListViewAdapter adapter; // Adapter para el listview
    ArrayList<Tag> PntsTagRuta = new ArrayList<>(); // Putos en el recorrido de la ruta

    private EditText txtNombre;
    private EditText txtVuelta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_ruta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new SQLite_Controller(this);
        Intent intent = getIntent();
        ruta = (Ruta) intent.getSerializableExtra("ruta");

        txtNombre = (EditText) findViewById(R.id.txtMdfRutNombre);
        txtNombre.setText(ruta.getNombre());
        txtVuelta = (EditText) findViewById(R.id.txtMdfNumVueltas);
        txtVuelta.setText(ruta.getVueltas());
        lvTags    = (ListView) findViewById(R.id.LvMdfTagRut);
        AdapterSet();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cdd = new EsperarTagDialogClass(ModificarRutaActivity.this); // En espera de NFC
                cdd.show();
            }
        });

        nfcController = new NFC_Controller(this);

        if (!nfcController.mNfcAdapter.isEnabled()) { // Estado del NFC en el telefono
            Toast.makeText(this,"Lectura NFC desactivado",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this,"Lectura NFC activado",Toast.LENGTH_SHORT).show();
        }
    }

    /*
        Metodo para configurar el adapter para la lista de puntos en la ruta
     */
    private void AdapterSet() {
        lvTags.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                //Toast.makeText(view.getContext(),"List item "+position,Toast.LENGTH_SHORT).show();
                final PopupMenu popup = new PopupMenu(view.getContext(), lvTags);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.opciones_tag, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.modificar:
                                Intent i = new Intent(ModificarRutaActivity.this, ModificarTagActivity.class);
                                i.putExtra("tag",PntsTagRuta.get(position));
                                PntsTagRuta.remove(position);
                                startActivityForResult(i,1000);
                                return true;
                            case R.id.borrar:
                                db.borrarTagDeRuta(PntsTagRuta.get(position).getCodigo());
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
        PntsTagRuta = db.getTagsDeRuta(ruta.getCodigo());
        adapter = new TagListViewAdapter(PntsTagRuta, ModificarRutaActivity.this);
        lvTags.setAdapter(adapter);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PntsTagRuta.add((Tag)data.getSerializableExtra("tag"));
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_guardar_ruta, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_guardar_ruta:
                alerta();
                break;
            default:
                break;
        }

        return true;
    }

    /*
        Mostrar dialogo para confirmación de guardado
     */
    private void alerta()
    {
        new AlertDialog.Builder(this)
                .setTitle("Atención!")
                .setMessage("¿Desea guardar los cambios?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ruta.setNombre(txtNombre.getText().toString());
                        ruta.setVueltas(txtVuelta.getText().toString());
                        db.updateRuta(ruta);
                        for (Tag tag : PntsTagRuta) {
                            db.insertTagRUT(
                                    tag.getCodigo(),
                                    tag.getNombre(),
                                    Arrays.asList(tag.getCodigo().split("_")).get(1),
                                    ruta.getCodigo()); // Almacenar tag y asignarlo a la ruta creada
                        }

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
    /*
        Dialogo para indicarle al usaurio que ingrese el nombre del tag seleccionado para
        escribirle y almacenarlo en la ruta
     */
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
                    lvTags.setAdapter(adapter); // Actualizar ListView
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
            GPS_Controller gps_controller = new GPS_Controller(this);
            String codigo =
                    String.valueOf(codigoTag) + "_" + // Contador de tags en ruta
                            ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)) + "_" + // Mac de tag
                            gps_controller.getLongitude() + "_" + // Latitud actual (ubicacion del tag)
                            gps_controller.getLatitude(); // Latitud actual
            Tag nuevoTag = new Tag();
            nuevoTag.setCodigo(codigo);
            PntsTagRuta.add(nuevoTag); // Guardar tag
            nfcController.write(intent, codigo); // Escribir el codigo en el NFC
            //listView.setAdapter(adapter); // Actualizar ListView
            cdd.dismiss(); // Cerrar dialog
            dialog = false;

            IngresarNombreDelTagDialogClass INTDC = new IngresarNombreDelTagDialogClass(ModificarRutaActivity.this); // En espera de NFC
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

}
