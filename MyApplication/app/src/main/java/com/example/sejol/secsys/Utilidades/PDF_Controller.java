package com.example.sejol.secsys.Utilidades;

/**
 * Created by sejol on 2/5/2016.
 */

import android.content.Context;
import android.os.Environment;

import com.example.sejol.secsys.Clases.Reporte;
import com.example.sejol.secsys.Clases.Ronda;
import com.example.sejol.secsys.Clases.Tag;
import com.example.sejol.secsys.Clases.Usuario;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PDF_Controller {

    private static Font Titulo = new Font(Font.FontFamily.TIMES_ROMAN, 25,
            Font.BOLD);
    private static Font Subtitulo = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font TxtNegro = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL);

    private Ronda ronda;
    private Usuario usuario;
    private ArrayList<Tag> puntos;
    private ArrayList<Reporte> reportes;
    private SQLite_Controller db;

    public PDF_Controller(Ronda rnd, Usuario usr, ArrayList<Tag> pnt, ArrayList<Reporte> rep, Context ctx) {
        this.ronda = rnd;
        this.usuario = usr;
        this.puntos = pnt;
        this.reportes = rep;
        db = new SQLite_Controller(ctx);
        createPdf();
    }

    public void createPdf(){
        try {
            print();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public void print()throws FileNotFoundException, DocumentException {
        File path = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), "Secsys");
        if (!path.exists()) {
            path.mkdir();
        }
        File docFolder = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS+"/Secsys"), "Reporte");

        //Create time stamp
        Date date = new Date() ;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);

        //File myFile = new File(docFolder + timeStamp + ".pdf");
        File myFile = new File(docFolder+": "+ronda.getNombre() + ".pdf");

        OutputStream output = new FileOutputStream(myFile);

        //Step 1
        Document document = new Document();

        //Step 2
        PdfWriter.getInstance(document, output);

        //Step 3
        document.open();

        //Step 4 Add content
        document.add(new Paragraph("Reporte de ronda", Titulo));
        document.add(new Paragraph("", Titulo));
        document.add(new Paragraph("   Oficial: " + usuario.getNombre(), Subtitulo));
        document.add(new Paragraph("   Ronda:   " + ronda.getNombre() , Subtitulo));
        document.add(new Paragraph("   Ruta:    " + ronda.getRuta() , Subtitulo));
        List<String> dataFecha = Arrays.asList(ronda.getFecha().split(" "));
        document.add(new Paragraph("   Fecha:   " + dataFecha.get(0)  , Subtitulo));
        document.add(new Paragraph("   Inicio:  " + dataFecha.get(1)  , Subtitulo));

        document.add(new Paragraph("Detalle de la ronda", Titulo));
        document.add(new Paragraph("", Titulo));
        document.add(new Paragraph("Ronda realizada: ", Subtitulo));
        document.add(new Paragraph("", Subtitulo));
        for (Tag pnt: db.getTagsDeRuta(ronda.getRuta())) {
            List<String> codeData = Arrays.asList(pnt.getCodigo().split("_"));
            document.add(new Paragraph("Punto " + codeData.get(0), Subtitulo));
            document.add(new Paragraph("    Latitud : " + codeData.get(3) + "    Longitud: " + codeData.get(2), TxtNegro));
            document.add(new Paragraph("    Hora:     " + getHoras(pnt)  , TxtNegro));
            document.add(new Paragraph("", Titulo));
        }

        document.add(new Paragraph("Anomalías reportadas: ", Subtitulo));
        document.add(new Paragraph("", Subtitulo));
        for (int i = 0; i < reportes.size();i++) {
            document.add(new Paragraph("Reporte " + (i+1), Subtitulo));
            document.add(new Paragraph("    Anomalía:    " + reportes.get(i).getAnomalia(), TxtNegro));
            document.add(new Paragraph("    Descripcion: " + reportes.get(i).getDescripcion(), TxtNegro));
            document.add(new Paragraph("    Hora:        " + reportes.get(i).getHora() , TxtNegro));
            document.add(new Paragraph("", Titulo));
        }

        //Step 5: Close the document
        document.close();
    }

    // iText allows to add metadata to the PDF which can be viewed in your Adobe
    // Reader
    // under File -> Properties
    private static void addMetaData(Document pdf) {
        pdf.addTitle("My first PDF");
        pdf.addSubject("Using iText");
        pdf.addKeywords("Java, PDF, iText");
        pdf.addAuthor("Lars Vogel");
        pdf.addCreator("Lars Vogel");
    }

    private String getHoras(Tag tag){
        ArrayList<Tag> lstTagsRonda;
        lstTagsRonda = db.getTagsDeRondaConMac(ronda.getCodigo(),tag.getMac());
        String horas = "";
        for(Tag tag2:lstTagsRonda){
            if(horas.equals("")){
                horas = tag2.getHora();
            }else{
                horas = horas + "\n                " + tag2.getHora();
            }
        }
        return horas;
    }
}
