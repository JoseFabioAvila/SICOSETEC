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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by jose on 6/24/16.
 */
public class Email_Controller {

    private SQLite_Controller db;
    private Context ctx;

    public Email_Controller(Context context, Ronda ronda, Usuario usuario, ArrayList<Tag> tagsDeRondaPorCodigo, ArrayList<Reporte> repsDeRondaPorCodigo) {
        this.ctx = context;
        db = new SQLite_Controller(ctx);
        enviarCorreo(ronda, usuario, tagsDeRondaPorCodigo, repsDeRondaPorCodigo);
    }

    public void enviarCorreo(Ronda ronda, Usuario usuario, ArrayList<Tag> tagsDeRondaPorCodigo, ArrayList<Reporte> repsDeRondaPorCodigo){
        File path = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), "Secsys");
        File filelocation = new File(path, "Reporte: "+ronda.getNombre()+".pdf");
        Uri uri = Uri.fromFile(filelocation);

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        //i.setType("vnd.android.cursor.dir/email");

        String[] lstCorreos = new String[db.getCorreos().size()];
        lstCorreos = db.getCorreos().toArray(lstCorreos);
        i.putExtra(Intent.EXTRA_EMAIL  , lstCorreos);
        i.putExtra(Intent.EXTRA_SUBJECT, "Reporte de ronda de seguridad de " +usuario.getNombre() );
        i.putExtra(Intent.EXTRA_TEXT   , "Ronda de seguridad:"+
                "   \nOficial:    " + usuario.getNombre()+
                "   \nRuta:       " + ronda.getRuta() +
                "   \nRonda:    "   + ronda.getNombre()+
                "   \nIniciada:   " + (Arrays.asList(ronda.getFecha().split(" ")).get(1))+
                "   \nFinalizada: " + (new SimpleDateFormat("HH:mm:ss").format(new Date())));
        i.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            ctx.startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ctx, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
