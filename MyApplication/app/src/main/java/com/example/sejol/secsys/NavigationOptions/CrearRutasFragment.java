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

import com.example.sejol.secsys.Activitys.AgregarRutaActivity;
import com.example.sejol.secsys.Clases.Ruta;
import com.example.sejol.secsys.R;
import com.example.sejol.secsys.Utilidades.SQLite_Controller;

import java.util.ArrayList;

/**
 * Created by fabio on 21/5/2016.
 */
public class CrearRutasFragment extends Fragment {

    private CardView cardView1;
    private CardView cardView2;

    private ListView listView;

    private SQLite_Controller db;


    //////////////////////Primero en ejecucion///////////////////////
    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            this.listener = (FragmentActivity) context;
        }
    }*/
    //////////////////////Primero en ejecucion///////////////////////

    //////////////////////Segundo en ejecucion///////////////////////
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    /////////////////////Crea la vista////////////////////////////////
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_crear_rutas, container, false);

        db = new SQLite_Controller(view.getContext());
        ArrayList<Ruta> rutas = db.getRutas();
        ArrayList<String> nombresRutas = new ArrayList<>();
        for(Ruta ruta:rutas){
            nombresRutas.add(ruta.getNombre());
        }

        listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,nombresRutas));

        //se pueden meter acciones de los elementos del fragment
        cardView1 = (CardView) view.findViewById(R.id.card_view);
        cardView2 = (CardView) view.findViewById(R.id.card_view2);
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AgregarRutaActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(view.getContext(),"List item "+position,Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    //////////////////para menu//////////////////////////////////
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.action_hola:
                Toast.makeText(getContext(),"jajajaja",Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //////////////////para menu//////////////////////////////////


    //////////////////////metodos de clase///////////////////////
    public CrearRutasFragment() {
        // Required empty public constructor
    }
    //////////////////////metodos de clase///////////////////////
}