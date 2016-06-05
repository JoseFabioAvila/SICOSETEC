package com.example.sejol.secsys.Activitys;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.google.android.gms.maps.model.LatLng;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.HashMap;

public class AgregarRutaActivity extends AppCompatActivity {

    List rondas = new ArrayList<>();

    ArrayAdapter<String> adapter;

    public static final String TAG = "NfcDemo";

    private TextView mTextView;
    private NFC_Controller nfcController;
    private ListView listView;

    private boolean dialog = false;
    int codigoTag = 0;
    CustomDialogClass cdd;

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
                /*Intent intent = new Intent(getApplicationContext(), CrearRondaActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);*/

                cdd = new CustomDialogClass(AgregarRutaActivity.this);
                cdd.show();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTextView = (TextView) findViewById(R.id.textViewAR);

        listView = (ListView) findViewById(R.id.listViewAR);
        adapterSet();

        nfcController = new NFC_Controller(this, 2);

        if (!nfcController.mNfcAdapter.isEnabled()) {
            mTextView.setText("Lectura NFC desactivado");
        } else {
            mTextView.setText("Lectura NFC activado");
        }

        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTextView.getText() == "Lectura NFC activado") {
                    mTextView.setText("Lectura NFC desactivado");
                } else {
                    mTextView.setText("Lectura NFC activado");
                }
            }
        });
    }

    public void adapterSet() {
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, rondas);
        listView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed()
    {
        alerta();
    }

    public void alerta()
    {
        new AlertDialog.Builder(this)
                .setTitle("Atención!")
                .setMessage("¿Desea crear esta ruta?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                })
                //.setIcon(R.drawable.warning_icon)
                .show();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (dialog) {
            GPS_Tracker gps_tracker = new GPS_Tracker(this);
            nfcController.write(intent, String.valueOf(codigoTag)+" "+gps_tracker.getLongitude()+" "+gps_tracker.getLatitude(), listView, rondas, adapter);
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
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_creacion_de_rondas) {
            Intent intent = new Intent(this,CrearRondaActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    public class CustomDialogClass extends Dialog implements android.view.View.OnClickListener {

        public Dialog d;
        public EditText editText;
        TextView textView1;
        public Button yes, no;

        public CustomDialogClass(Activity a) {
            super(a);
            dialog = true;
            // TODO Auto-generated constructor stub
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.write_tag);

            codigoTag++;

            textView1 = (TextView) findViewById(R.id.dialogo);
            textView1.setText("Esperando TAG...");
        }

        @Override
        public void onClick(View v) {

        }
    }
}

            /*yes = (Button) findViewById(R.id.btn_yes);
            no = (Button) findViewById(R.id.btn_no);
            editText = (EditText) findViewById(R.id.codigo);

            editText.addTextChangedListener(new TextWatcher()
            {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after)
                {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count)
                {
                    if(s.toString().equals(""))
                    {
                        codigoTag = "";
                    }
                    else
                    {
                        codigoTag = s.toString();
                    }
                }

                @Override
                public void afterTextChanged(Editable s)
                {
                }
            });

            //yes.setOnClickListener(this);
            no.setOnClickListener(this);

        }



        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_no:
                    dialog = false;
                    dismiss();
                    break;
                default:
                    break;
            }
            dismiss();
        }*/

