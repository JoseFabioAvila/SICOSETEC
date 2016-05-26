package com.example.sejol.secsys.Activitys;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sejol.secsys.R;

public class CrearRondaActivity extends AppCompatActivity {

    EditText cod;
    Button btnCrear;
    boolean permitir = false;
    private NfcAdapter mNfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_ronda);

        cod = (EditText) findViewById(R.id.editText);
        btnCrear = (Button) findViewById(R.id.btnGrabar);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter == null) {
            Toast.makeText(this, "Su dispositivo no soporta la tecnologia NFC", Toast.LENGTH_LONG).show();
            finish();
            return;

        }

        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cod.getText().equals("")){
                    Toast. makeText(getApplicationContext(), "Antes ingre el codigo" , Toast. LENGTH_LONG ).show();
                    permitir = false;
                }
                else{
                    permitir = true;
                }
            }
        });


    }

    private void enableForegroundDispatchSystem(){
        Intent intent = new Intent(this,CrearRondaActivity.class) .addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        IntentFilter[] intentFilters = new IntentFilter[]{};
        mNfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);
    }

    private void disableForegroundDispatchSystem(){
        mNfcAdapter.disableForegroundDispatch(this);
    }

    public void write(Intent intent , String code){
        if (intent.hasExtra(NfcAdapter. EXTRA_TAG ))
        {
            Tag tag = intent.getParcelableExtra(NfcAdapter. EXTRA_TAG );
            NdefMessage ndefMessage = createNdefMessage(code);
            writeNdefMessage(tag , ndefMessage);
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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String code = cod.getText().toString();
        if(permitir == true) {
            write(intent,code);

            /*

            * Despues de agregar el codigo al NFC
            * Aqui se agregara a la base de datos dummy la posicion del mapa y el codigo del tag;
            *
            * */

            Toast.makeText(getApplicationContext(),code,Toast.LENGTH_SHORT).show();
            permitir = false;
        }
    }

    private NdefMessage createNdefMessage(String contect){
        NdefRecord ndefRecord = NdefRecord.createUri(contect);
        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{ndefRecord});
        return ndefMessage;
    }

    private void writeNdefMessage(Tag tag , NdefMessage ndefMessage) {
        try {
            if (tag == null ){
                Toast. makeText(this, "Tab object error" , Toast. LENGTH_LONG ).show();
                return;
            }
            Ndef ndef = Ndef. get(tag);
            if (ndef == null ){
                //Format tag with de ndef format and writes the message
                formatTag(tag , ndefMessage);
            } else {
                ndef.connect();
                if (!ndef.isWritable()){
                    Toast. makeText ( this , "Tag is not writeable" , Toast. LENGTH_LONG ).show();
                    ndef.close();
                    return;
                }
                ndef.writeNdefMessage(ndefMessage);
                ndef.close();
                Toast. makeText ( this , "Tag writen!" , Toast. LENGTH_LONG ).show();
            }
        } catch (Exception e){
            Log. e("formatTag", e.getMessage());
        }
    }

    private void formatTag(Tag tag , NdefMessage ndefMessage){
        try {
            NdefFormatable ndefFormatable = NdefFormatable. get(tag);
            if (ndefFormatable == null ){
                Toast. makeText ( this , "Tag is not ndef fomatable" , Toast. LENGTH_LONG ).show();
                return;
            }
            ndefFormatable.connect();
            ndefFormatable.format(ndefMessage);
            ndefFormatable.close();
            Toast. makeText ( this , "Tag format OK" , Toast. LENGTH_LONG ).show();
        } catch (Exception e){
            Log. e ( "formatTag" , e.getMessage());
        }
    }
}
