package com.example.sejol.secsys.NavigationOptions;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;

import com.example.sejol.secsys.Clases.Ronda;
import com.example.sejol.secsys.Clases.Usuario;
import com.example.sejol.secsys.Popup.PopupConfiguracionDeCorreos;
import com.example.sejol.secsys.Popup.PopupVerReporte;
import com.example.sejol.secsys.R;
import com.example.sejol.secsys.Utilidades.PDF_Controller;
import com.example.sejol.secsys.Utilidades.SQLite_Controller;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class DescargarReportesFragment extends Fragment {

    Usuario usuario;
    private ListView lvRondas;
    private SQLite_Controller db;
    View view;

    public DescargarReportesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_descargar_reportes, container, false);


        CardView cardView1 = (CardView) view.findViewById(R.id.card_view);
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(view.getContext(), PopupConfiguracionDeCorreos.class);
                startActivity(i);
            }
        });

        usuario = (Usuario) getArguments().getSerializable("usuario"); // Usuario logueado

        db = new SQLite_Controller(view.getContext());
        final ArrayList<Ronda> rondas = db.getRondas(usuario); // recoger rondas del usuario
        final ArrayList<String> nombresRondas = new ArrayList<>(); // Lista de los nombres de las rondas
        for(Ronda ronda:rondas){
            nombresRondas.add(ronda.getNombre()); // add nombre de ronda
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,nombresRondas);
        lvRondas = (ListView) view.findViewById(R.id.lvRondas); // Lista para desplrear las rondas
        lvRondas.setAdapter(adapter);
        lvRondas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

                //Toast.makeText(view.getContext(),"List item "+position,Toast.LENGTH_SHORT).show();
                final PopupMenu popup = new PopupMenu(view.getContext(), lvRondas);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.opciones_imp_ronda, popup.getMenu());

                final int pos = position; // Posicion numerica del objeto de la lista seleccionado
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.ver:
                                Intent i = new Intent(view.getContext(),PopupVerReporte.class);
                                i.putExtra("ronda",rondas.get(pos));
                                i.putExtra("usuario",usuario);
                                i.putExtra("lstTags",db.getTagsDeRonda(rondas.get(pos).getCodigo()));
                                i.putExtra("lstRep",db.getRepsDeRondaPorCodigo(rondas.get(pos).getCodigo()));
                                startActivityForResult(i,100);
                                return true;
                            case R.id.pdf:
                                PDF_Controller pdf_controller = new PDF_Controller(
                                        rondas.get(pos),
                                        usuario,
                                        db.getTagsDeRonda(rondas.get(pos).getCodigo()),
                                        db.getRepsDeRondaPorCodigo(rondas.get(pos).getCodigo()));
                                return true;
                            case R.id.borrarRonda:
                                db.borrarRonda(rondas.get(pos).getCodigo());
                                final ArrayList<Ronda> rondas = db.getRondas(usuario); // recoger rondas del usuario
                                nombresRondas.remove(pos);
                                adapter.notifyDataSetChanged();
                                return true;
                        }
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });
        return view;
    }
}
