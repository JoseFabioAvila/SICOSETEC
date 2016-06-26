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
import android.widget.PopupMenu;
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


    //////////////////////Segundo en ejecucion///////////////////////
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    //////////////////////Segundo en ejecucion///////////////////////

    /////////////////////Crea la vista////////////////////////////////
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_crear_rutas, container, false);

        db = new SQLite_Controller(view.getContext());
        ArrayList<Ruta> rutas = db.getRutas();
        final ArrayList<String> nombresRutas = new ArrayList<>();
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
                Bundle bundle = new Bundle();
                bundle.putString("ruta","");
                Intent intent = new Intent(getActivity(), AgregarRutaActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                intent.putExtras(bundle);

                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {

                //Toast.makeText(view.getContext(),"List item "+position,Toast.LENGTH_SHORT).show();
                final PopupMenu popup = new PopupMenu(view.getContext(), listView);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.opciones_crear_ruta, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.modificar:
                                Bundle bundle = new Bundle();
                                bundle.putString("ruta", nombresRutas.get(position));
                                Intent intent = new Intent(getActivity(), AgregarRutaActivity.class);

                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                intent.putExtras(bundle);

                                startActivity(intent);
                                return true;
                            case R.id.borrar:
                                db.borrarRuta(nombresRutas.get(position));
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
    /////////////////////Crea la vista////////////////////////////////

    ////////////////////Despues de crear la vista////////////////////
    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {


    }
    ////////////////////Despues de crear la vista////////////////////

    ////////////////////Forma segura de buscar cosas en el activity padre////////////////////
    /*@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }*/
    ////////////////////Forma segura de buscar cosas en el activity padre////////////////////

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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}