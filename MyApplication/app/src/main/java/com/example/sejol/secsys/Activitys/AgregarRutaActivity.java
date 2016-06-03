package com.example.sejol.secsys.Activitys;

import android.app.PendingIntent;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sejol.secsys.R;
import com.example.sejol.secsys.Utilidades.NFC_Controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AgregarRutaActivity extends AppCompatActivity {

    List rondas = new ArrayList<>();

    ArrayAdapter<String> adapter;

    public static final String TAG = "NfcDemo";

    private TextView mTextView;
    private NFC_Controller nfcController;
    private ListView listView;

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
                Intent intent = new Intent(getApplicationContext(), CrearRondaActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTextView = (TextView) findViewById(R.id.textViewAR);

        listView = (ListView) findViewById(R.id.listViewAR);
        adapterSet();

        nfcController = new NFC_Controller(this,2);

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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        nfcController.leerPunto(intent, mTextView, listView, rondas, adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        nfcController.enableForegroundDispatchSystem();
    }

    @Override protected void onPause() {
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

}
