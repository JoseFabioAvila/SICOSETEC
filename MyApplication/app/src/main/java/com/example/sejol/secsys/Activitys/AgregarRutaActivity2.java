package com.example.sejol.secsys.Activitys;
import com.example.sejol.secsys.R;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AgregarRutaActivity2 extends AppCompatActivity {

    List rondas = new ArrayList<>();

    ArrayAdapter<String> adapter;

    public static final String TAG = "NfcDemo";

    private TextView mTextView;
    private NfcAdapter mNfcAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_ruta_2);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);


        mTextView = (TextView) findViewById(R.id.textViewAR);

        listView = (ListView) findViewById(R.id.listViewAR);
        adapterSet();


        if (mNfcAdapter == null) {
            Toast.makeText(this, "Su dispositivo no soporta la tecnologia NFC", Toast.LENGTH_LONG).show();
            //finish();
            //return;

        }

        if (!mNfcAdapter.isEnabled()) {
            mTextView.setText("Lectura NFC desactivado");
        } else {
            mTextView.setText("Lectura NFC activado");
        }

        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTextView.getText() == "Lectura NFC activado"){
                    mTextView.setText("Lectura NFC desactivado");
                }
                else{
                    mTextView.setText("Lectura NFC activado");
                }
            }
        });

    }

    public void adapterSet() {
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, rondas);
        listView.setAdapter(adapter);
    }

    private void enableForegroundDispatchSystem(){
        Intent intent = new Intent(this,AgregarRutaActivity2.class) .addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        IntentFilter[] intentFilters = new IntentFilter[]{};
        mNfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);
    }

    private void disableForegroundDispatchSystem(){
        mNfcAdapter.disableForegroundDispatch(this);
    }

    private void readTextFromMessage(NdefMessage ndefMessage) {
        NdefRecord[] ndefRecords = ndefMessage.getRecords();
        if(ndefRecords != null && ndefRecords.length>0){
            NdefRecord ndefRecord = ndefRecords[0];
            String tagContent = getTextFromNdefRecord(ndefRecord);
            Calendar c = Calendar.getInstance();

            String fecha = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
            fecha +="/"+String.valueOf(c.get(Calendar.MONTH));
            fecha +="/"+String.valueOf(c.get(Calendar.YEAR));

            String hora = String.valueOf(c.get(Calendar.HOUR));
            hora +=":"+String.valueOf(c.get(Calendar.MINUTE));
            hora +=":"+String.valueOf(c.get(Calendar.SECOND));
            if(c.get(Calendar.AM_PM) == 1){
                hora +=" p.m";
            }
            else{
                hora +=" a.m";
            }

            rondas.add(tagContent + " - Fecha: " + fecha + ", Hora: " + hora);
            listView.setAdapter(adapter);
            //Toast.makeText(this, tagContent+" Fecha: "+fecha+", Hora: "+hora, Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "No hay datos en la etiqueta!", Toast.LENGTH_SHORT).show();
        }
    }

    public String getTextFromNdefRecord(NdefRecord ndefRecord) {
        String tagContent = null;
        try {
            byte[] payload = ndefRecord.getPayload();
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
            int languageSize = payload[0] & 0063;
            tagContent = new String(payload, languageSize + 1, payload.length - languageSize - 1, textEncoding);
        } catch (UnsupportedEncodingException e) {
            Log.e("getTextFromNdefRecord", e.getMessage(), e);
        } return tagContent;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent); if (intent.hasExtra(NfcAdapter.EXTRA_TAG)) {
            Parcelable[] parcelables = intent. getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if(parcelables != null && parcelables.length > 0)
            {
                if(mTextView.getText() == "Lectura NFC activado") {
                    readTextFromMessage((NdefMessage) parcelables[0]);
                }else{
                    //Toast.makeText(this, "Lectura desactivada", Toast.LENGTH_SHORT).show();
                }

            }
            else
            {
                Toast.makeText(this, "No se econtro mensaje de la etiqueta!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        enableForegroundDispatchSystem();
    }

    @Override protected void onPause() {
        super.onPause();
        disableForegroundDispatchSystem();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_creacion_de_rondas) {
            Intent intent = new Intent(this,CrearRondaActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
