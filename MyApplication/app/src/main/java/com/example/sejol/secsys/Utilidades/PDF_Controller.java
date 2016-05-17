package com.example.sejol.secsys.Utilidades;

/**
 * Created by sejol on 2/5/2016.
 */

import android.os.Environment;

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

public class PDF_Controller {

    private static Font Titulo = new Font(Font.FontFamily.TIMES_ROMAN, 25,
            Font.BOLD);
    private static Font Subtitulo = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font TxtRojo = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    private static Font TxtNegro = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL);

    public PDF_Controller() {

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

        File myFile = new File(docFolder + timeStamp + ".pdf");

        OutputStream output = new FileOutputStream(myFile);

        //Step 1
        Document document = new Document();

        //Step 2
        PdfWriter.getInstance(document, output);

        //Step 3
        document.open();

        //Step 4 Add content
        document.add(new Paragraph("Titulo", Titulo));
        document.add(new Paragraph("   Subtitulo ", Subtitulo));
        document.add(new Paragraph("Teléfono: 2240-8645 / Fax: 2240-2642", TxtNegro));
        document.add(new Paragraph("Cédula Jueridica: 3-001-051820-23", TxtNegro));
        document.add(new Paragraph("Correo: fiscalia@ingagr.or.cr", TxtNegro));
        document.add(new Paragraph("Apdo. 281-100", TxtNegro));

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

}
