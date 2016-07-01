package com.example.sejol.secsys.Popup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sejol.secsys.Adapters.ReportesListViewAdapter;
import com.example.sejol.secsys.Adapters.TagsRondaListViewAdapter;
import com.example.sejol.secsys.Clases.Reporte;
import com.example.sejol.secsys.Clases.Ronda;
import com.example.sejol.secsys.Clases.Tag;
import com.example.sejol.secsys.Clases.Usuario;
import com.example.sejol.secsys.R;
import com.example.sejol.secsys.Utilidades.SQLite_Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PopupVerReporte extends AppCompatActivity {

    Ronda ronda;
    Usuario usuario;
    ArrayList<Tag> lstTags;
    ArrayList<Reporte> lstReportes;
    SQLite_Controller bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_ver_reporte);

        bd = new SQLite_Controller(this);
        // Get intent
        Intent intent = getIntent();
        ronda = (Ronda) intent.getSerializableExtra("ronda");
        usuario = (Usuario)intent.getSerializableExtra("usuario");
        lstTags = (ArrayList<Tag>)intent.getSerializableExtra("lstTags");
        lstReportes = (ArrayList<Reporte>)intent.getSerializableExtra("lstRep");
        // Get view components
        TextView txtoficial = (TextView)findViewById(R.id.poptxtOficial);
        TextView txtronda = (TextView)findViewById(R.id.poptxtRonda);
        TextView txtruta = (TextView)findViewById(R.id.poptxtRuta);
        TextView txtfecha = (TextView)findViewById(R.id.poptxtFecha);
        TextView txthora = (TextView)findViewById(R.id.poptxtHora);
        // Lista de tags de la ronda
        ListView lvTags = (ListView)findViewById(R.id.poplvTags);
        lvTags.setAdapter(new TagsRondaListViewAdapter(listaDeEstadoDeRonda(),this));
        justifyListViewHeightBasedOnChildren(lvTags);
        // Lista de reportes
        ListView lvReportes = (ListView)findViewById(R.id.poplvReportes);
        ReportesListViewAdapter adapterReportes = new ReportesListViewAdapter(lstReportes,this);
        lvReportes.setAdapter(adapterReportes);
        justifyListViewHeightBasedOnChildren(lvReportes);
        // Datos generales
        txtoficial.setText(usuario.getNombre());
        txtronda.setText(ronda.getNombre());
        txtruta.setText(Arrays.asList(ronda.getRuta().split("_")).get(0));
        List<String> dataFecha = Arrays.asList(ronda.getFecha().split(" "));
        txtfecha.setText(dataFecha.get(0));
        txthora.setText(dataFecha.get(1));
        // Tamaño del popup
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int width = localDisplayMetrics.widthPixels;
        int height = localDisplayMetrics.heightPixels;
        getWindow().setLayout((int)(0.70 * width), (int)(0.60 * height));
    }

    /*
    Configura el alto del Listview para que se base en la suma del alto de los elementos en la lista
    eliminando el scrollview
     */
    public void justifyListViewHeightBasedOnChildren (ListView listView) {

        ListAdapter adapter = listView.getAdapter();

        if (adapter == null) {
            return;
        }
        ViewGroup vg = listView;
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, vg);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(par);
        listView.requestLayout();
    }

    public ArrayList listaDeEstadoDeRonda(){
        ArrayList<Tag> lstTagsRuta = bd.getTagsDeRuta(ronda.getRuta());
        ArrayList<Tag> lstTagsRonda;
        ArrayList<String> lstHoras = new ArrayList<>();
        for(Tag tag:lstTagsRuta){
            lstTagsRonda = bd.getTagsDeRondaConMac(ronda.getCodigo(),tag.getMac());
            String horas = "";
            for(Tag tag2:lstTagsRonda){
                if(horas.equals("")){
                    horas = tag2.getHora();
                }else{
                    horas = horas + "\n" + tag2.getHora();
                }

            }
            lstHoras.add(horas);
        }
        return lstHoras;
    }
}
