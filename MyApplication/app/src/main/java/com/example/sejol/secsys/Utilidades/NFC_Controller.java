package com.example.sejol.secsys.Utilidades;

import android.app.Activity;
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

import com.example.sejol.secsys.Activitys.AgregarRutaActivity;
import com.example.sejol.secsys.Activitys.MainActivity;
import com.example.sejol.secsys.Activitys.ModificarRutaActivity;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.List;

/**
 * Created by sejol on 17/5/2016.
 */
public class NFC_Controller {
    public NfcAdapter mNfcAdapter;
    private Context context;
    int opcion;

    // Opcion  == 1
    public NFC_Controller(MainActivity context) {

        this.context = context;
        opcion = 1;
        mNfcAdapter = NfcAdapter.getDefaultAdapter(context);
    }
    // Opcion  == 2
    public NFC_Controller(AgregarRutaActivity context) {
        this.context = context;
        opcion = 2;
        mNfcAdapter = NfcAdapter.getDefaultAdapter(context);
    }
    // Opcion  == 3
    public NFC_Controller(ModificarRutaActivity context) {
        this.context = context;
        opcion = 3;
        mNfcAdapter = NfcAdapter.getDefaultAdapter(context);
    }

    public NFC_Controller(Context context){
        mNfcAdapter = NfcAdapter.getDefaultAdapter(context);
    }

    public void enableForegroundDispatchSystem(){
        Intent intent = null;
        if      (opcion == 1){
            intent = new Intent(context, MainActivity.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        }else if(opcion == 2){
            intent = new Intent(context, AgregarRutaActivity.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        }else if(opcion == 3){
            intent = new Intent(context, ModificarRutaActivity.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        IntentFilter[] intentFilters = new IntentFilter[]{};
        mNfcAdapter.enableForegroundDispatch((Activity) context, pendingIntent, intentFilters, null);
    }

    public void disableForegroundDispatchSystem() {
        mNfcAdapter.disableForegroundDispatch((Activity) context);
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
    /*
    public String leerPunto(Intent intent){
        if (intent.hasExtra(NfcAdapter.EXTRA_TAG)) {
            Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
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
    }*/
    public String leerPunto(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs = null;
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            }
            return buildTagViews(msgs);
        }
        return "";
    }

    private String buildTagViews(NdefMessage[] msgs) {
        if (msgs == null || msgs.length == 0)
            return "";

        String text = "";
        //  String tagId = new String(msgs[0].getRecords()[0].getType());
        byte[] payload = msgs[0].getRecords()[0].getPayload();
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16"; // Get the Text Encoding
        int languageCodeLength = payload[0] & 0063; // Get the Language Code, e.g. "en"
        // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");

        try {
            // Get the Text
            text = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        } catch (UnsupportedEncodingException e) {
            Log.e("UnsupportedEncoding", e.toString());
        }

        return text;
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
