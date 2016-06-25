package com.example.sejol.secsys.Utilidades;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;

/**
 * Created by jose on 6/24/16.
 */
public class Email_Controller {

    private String correo, documento;
    private Context ctx;

    public Email_Controller(String correo, String documento, Context context) {
        this.correo = correo;
        this.documento = documento;
        this.ctx = context;
    }

    public void enviarCorreo(){
        File path = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), "Secsys");
        File filelocation = new File(path, "Reporte: Ronda de prueba 2.pdf");
        Uri uri = Uri.fromFile(filelocation);

        Intent i = new Intent(Intent.ACTION_SEND);
        //i.setType("message/rfc822");
        i.setType("vnd.android.cursor.dir/email");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"gamboavargasjosepablo@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
        i.putExtra(Intent.EXTRA_TEXT   , "body of email");
        i.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            ctx.startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ctx, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
