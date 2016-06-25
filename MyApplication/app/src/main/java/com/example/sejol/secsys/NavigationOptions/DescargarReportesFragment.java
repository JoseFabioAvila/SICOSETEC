package com.example.sejol.secsys.NavigationOptions;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.sejol.secsys.Clases.Ronda;
import com.example.sejol.secsys.Clases.Usuario;
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

    public DescargarReportesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_descargar_reportes, container, false);

        usuario = (Usuario) getArguments().getSerializable("usuario");

        db = new SQLite_Controller(view.getContext());
        final ArrayList<Ronda> rondas = db.getRondas(usuario);
        ArrayList<String> nombresRondas = new ArrayList<>();
        for(Ronda ronda:rondas){
            nombresRondas.add(ronda.getNombre());
        }

        lvRondas = (ListView) view.findViewById(R.id.lvRondas);
        lvRondas.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,nombresRondas));
        lvRondas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                Toast.makeText(view.getContext(), "List item " + position, Toast.LENGTH_SHORT).show();

                //Toast.makeText(view.getContext(),"List item "+position,Toast.LENGTH_SHORT).show();
                final PopupMenu popup = new PopupMenu(view.getContext(), lvRondas);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.opciones_imp_ronda, popup.getMenu());

                final int pos = position;

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.ver:
                                Intent i = new Intent(view.getContext(),PopupVerReporte.class);
                                i.putExtra("ronda",rondas.get(pos));
                                i.putExtra("usuario",usuario);
                                i.putExtra("lstTags",db.getTagsDeRondaPorCodigo(rondas.get(pos).getCodigo()));
                                i.putExtra("lstRep",db.getRepsDeRondaPorCodigo(rondas.get(pos).getCodigo()));
                                startActivityForResult(i,100);
                                return true;
                            case R.id.pdf:
                                PDF_Controller pdf_controller = new PDF_Controller(
                                        rondas.get(pos),
                                        usuario,
                                        db.getTagsDeRondaPorCodigo(rondas.get(pos).getCodigo()),
                                        db.getRepsDeRondaPorCodigo(rondas.get(pos).getCodigo()));
                                return true;
                            case R.id.borrarRonda:
                                db.borrarRonda(rondas.get(pos).getCodigo());
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
