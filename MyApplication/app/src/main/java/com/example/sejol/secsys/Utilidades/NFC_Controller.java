package com.example.sejol.secsys.Utilidades;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sejol.secsys.Activitys.AgregarRutaActivity;
import com.example.sejol.secsys.Activitys.CrearRondaActivity;
import com.example.sejol.secsys.Activitys.MainActivity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by sejol on 17/5/2016.
 */
public class NFC_Controller {
    public NfcAdapter mNfcAdapter;

    CrearRondaActivity context1;
    AgregarRutaActivity context2;
    MainActivity context3;
    Context context;
    int opcion;

    public NFC_Controller(MainActivity context, int opcion) {

        this.context = context;
        this.context3 = (MainActivity)context;

        mNfcAdapter = NfcAdapter.getDefaultAdapter(context);
        this.opcion = opcion;
    }
    public NFC_Controller(CrearRondaActivity context, int opcion) {

        this.context = context;
        this.context1 = (CrearRondaActivity)context;

        mNfcAdapter = NfcAdapter.getDefaultAdapter(context);
        this.opcion = opcion;
    }
    public NFC_Controller(AgregarRutaActivity context, int opcion) {

        this.context = context;
        this.context2 = context;

        mNfcAdapter = NfcAdapter.getDefaultAdapter(context);
        this.opcion = opcion;
    }

    public NFC_Controller(Context context){
        mNfcAdapter = NfcAdapter.getDefaultAdapter(context);
    }

    public void enableForegroundDispatchSystem(){
        Intent intent;
        if(opcion == 1) {
            intent = new Intent(context1, CrearRondaActivity.class ).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
            PendingIntent pendingIntent = PendingIntent.getActivity(context1, 0, intent, 0);
            IntentFilter[] intentFilters = new IntentFilter[]{};
            mNfcAdapter.enableForegroundDispatch(context1, pendingIntent, intentFilters, null);
        }
        if(opcion == 2){
            intent = new Intent(context2, AgregarRutaActivity.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
            PendingIntent pendingIntent = PendingIntent.getActivity(context2, 0, intent, 0);
            IntentFilter[] intentFilters = new IntentFilter[]{};
            mNfcAdapter.enableForegroundDispatch(context2, pendingIntent, intentFilters, null);
        }
        else{
            intent = new Intent(context3, MainActivity.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
            PendingIntent pendingIntent = PendingIntent.getActivity(context3, 0, intent, 0);
            IntentFilter[] intentFilters = new IntentFilter[]{};
            mNfcAdapter.enableForegroundDispatch(context3, pendingIntent, intentFilters, null);
        }
    }

    public void disableForegroundDispatchSystem() {
        if(opcion == 1) {
            mNfcAdapter.disableForegroundDispatch(context1);
        }
        if(opcion == 2) {
            mNfcAdapter.disableForegroundDispatch(context2);
        }
        else {
            mNfcAdapter.disableForegroundDispatch(context3);
        }
    }

    public void escribir(boolean permitir, EditText cod, Intent intent){
        String code = cod.getText().toString();
        if(permitir == true) {
            write(intent,code);
            /*
            * Despues de agregar el codigo al NFC
            * Aqui se agregara a la base de datos dummy la posicion del mapa y el codigo del tag;
            *
            * */
            //Toast.makeText(context,code,Toast.LENGTH_SHORT).show();
            permitir = false;
        }
    }

    public void write(Intent intent , String code){
        if (intent.hasExtra(NfcAdapter. EXTRA_TAG ))
        {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            NdefMessage ndefMessage = createNdefMessage(code);
            writeNdefMessage(tag, ndefMessage);
        }
    }

    public void write(Intent intent , String code, ListView listView, List rondas, ArrayAdapter<String> adapter){
        if (intent.hasExtra(NfcAdapter. EXTRA_TAG ))
        {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            NdefMessage ndefMessage = createNdefMessage(code);
            writeNdefMessage(tag, ndefMessage);
            rondas.add(code);
            listView.setAdapter(adapter);
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
                //Toast. makeText(context, "Tab object error" , Toast. LENGTH_LONG ).show();
                return;
            }
            Ndef ndef = Ndef. get(tag);
            if (ndef == null ){
                //Format tag with de ndef format and writes the message
                formatTag(tag , ndefMessage);
            } else {
                ndef.connect();
                if (!ndef.isWritable()){
                    //Toast. makeText ( context , "Tag is not writeable" , Toast. LENGTH_LONG ).show();
                    ndef.close();
                    return;
                }
                ndef.writeNdefMessage(ndefMessage);
                ndef.close();
                //Toast. makeText ( context , "Tag writen!" , Toast. LENGTH_LONG ).show();
            }
        } catch (Exception e){
            Log. e("formatTag", e.getMessage());
        }
    }

    private void formatTag(Tag tag , NdefMessage ndefMessage){
        try {
            NdefFormatable ndefFormatable = NdefFormatable. get(tag);
            if (ndefFormatable == null ){
                //Toast. makeText ( context , "Tag is not ndef fomatable" , Toast. LENGTH_LONG ).show();
                return;
            }
            ndefFormatable.connect();
            ndefFormatable.format(ndefMessage);
            ndefFormatable.close();
            //Toast. makeText ( context , "Tag format OK" , Toast. LENGTH_LONG ).show();
        } catch (Exception e){
            Log. e ( "formatTag" , e.getMessage());
        }
    }



    public void leerPunto(Intent intent,  TextView mTextView, ListView listView, List rondas, ArrayAdapter adapter){
        if (intent.hasExtra(NfcAdapter.EXTRA_TAG)) {
            Parcelable[] parcelables = intent. getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if(parcelables != null && parcelables.length > 0)
            {
                if(mTextView.getText() == "Lectura NFC activado") {
                    readTextFromMessage((NdefMessage) parcelables[0], listView, rondas, adapter);
                }else{
                    //Toast.makeText(context, "Lectura desactivada", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                //Toast.makeText(context, "No se econtro mensaje de la etiqueta!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String leerPunto(Intent intent){
        if (intent.hasExtra(NfcAdapter.EXTRA_TAG)) {
            Parcelable[] parcelables = intent. getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if(parcelables != null && parcelables.length > 0)
            {
                return readTextFromMessage((NdefMessage) parcelables[0]);
            }
            else
            {
                //Toast.makeText(context, "No se econtro mensaje de la etiqueta!", Toast.LENGTH_SHORT).show();
                return "";
            }
        }
        return "";
    }

    private void readTextFromMessage(NdefMessage ndefMessage,ListView listView, List rondas, ArrayAdapter adapter) {
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
        }else{
            //Toast.makeText(context, "No hay datos en la etiqueta!", Toast.LENGTH_SHORT).show();
        }
    }

    private String readTextFromMessage(NdefMessage ndefMessage) {
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

            //Toast.makeText(context,tagContent + " - Fecha: " + fecha + ", Hora: " + hora,Toast.LENGTH_LONG).show();
            return tagContent + " - Fecha: " + fecha + ", Hora: " + hora;
        }else{
            //Toast.makeText(context, "No hay datos en la etiqueta!", Toast.LENGTH_SHORT).show();
            return "";
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

}
