package com.example.sejol.secsys.NavigationOptions;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sejol.secsys.Activitys.MainActivity;
import com.example.sejol.secsys.Clases.Ronda;
import com.example.sejol.secsys.Clases.Ruta;
import com.example.sejol.secsys.Clases.Usuario;
import com.example.sejol.secsys.R;
import com.example.sejol.secsys.Utilidades.SQLite_Controller;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class DescargarReportesFragment extends Fragment {

    Usuario usuario;
    private CardView cardViewAgragarRonda;
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
        ArrayList<Ronda> rondas = db.getRondas(usuario);
        ArrayList<String> nombresRondas = new ArrayList<>();
        for(Ronda ronda:rondas){
            nombresRondas.add(ronda.getNombre());
        }

        lvRondas = (ListView) view.findViewById(R.id.lvRondas);
        lvRondas.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,nombresRondas));
        lvRondas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(view.getContext(),"List item "+position,Toast.LENGTH_SHORT).show();
            }
        });

        cardViewAgragarRonda = (CardView) view.findViewById(R.id.card_view_agregarRonda);
        cardViewAgragarRonda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(intent);
            }
        });
        return view;
    }
}
