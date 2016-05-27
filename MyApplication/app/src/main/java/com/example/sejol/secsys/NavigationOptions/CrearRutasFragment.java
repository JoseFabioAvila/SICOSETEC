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
import com.example.sejol.secsys.R;

import java.util.ArrayList;

/**
 * Created by fabio on 21/5/2016.
 */
public class CrearRutasFragment extends Fragment {

    private String saludo;
    private CardView cardView1;
    private CardView cardView2;

    private ListView listView;

    ArrayList<String> arrayList = new ArrayList<String>();


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

        arrayList.add("Hola 0");
        arrayList.add("Hola 1");
        arrayList.add("Hola 2");
        arrayList.add("Hola 3");
        arrayList.add("Hola 4");
        arrayList.add("Hola 5");
        arrayList.add("Hola 6");
        arrayList.add("Hola 7");
        arrayList.add("Hola 8");
        arrayList.add("Hola 9");




        saludo = "Toas mop!!!";
    }
    //////////////////////Segundo en ejecucion///////////////////////

    /////////////////////Crea la vista////////////////////////////////
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_crear_rutas, container, false);

        listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,arrayList));

        //se pueden meter acciones de los elementos del fragment
        cardView1 = (CardView) view.findViewById(R.id.card_view);
        cardView2 = (CardView) view.findViewById(R.id.card_view2);
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Toast.makeText(view.getContext(),"card view agregar",Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(getActivity(),AgregarRutaActivity2.class);
                //startActivity(intent);

                Intent intent = new Intent(getActivity(), AgregarRutaActivity2.class);
                //((MainActivity) getActivity()).startActivity(intent);
                //startActivity(intent);
                //CrearRutasFragment.this.startActivity(intent);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getActivity().startActivity(intent);*/
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