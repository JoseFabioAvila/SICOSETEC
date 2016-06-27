package com.example.sejol.secsys.Utilidades;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import com.example.sejol.secsys.Clases.Reporte;
import com.example.sejol.secsys.Clases.Ronda;
import com.example.sejol.secsys.Clases.Tag;
import com.example.sejol.secsys.Clases.Usuario;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by jose on 6/24/16.
 */
public class Email_Controller {

    private SQLite_Controller db;
    private Context ctx;

    public Email_Controller(Context context, Ronda ronda, Usuario usuario, ArrayList<Tag> tagsDeRondaPorCodigo, ArrayList<Reporte> repsDeRondaPorCodigo) {
        this.ctx = context;
        db = new SQLite_Controller(ctx);
    }

    public void enviarCorreo(Ronda ronda, Usuario usuario){
        File path = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), "Secsys");
        File filelocation = new File(path, "Reporte: "+ronda.getNombre()+".pdf");
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
