package com.example.sejol.secsys.NavigationOptions;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.sejol.secsys.Activitys.AgregarRutaActivity;
import com.example.sejol.secsys.Activitys.MainActivity;
import com.example.sejol.secsys.Activitys.ModificarRutaActivity;
import com.example.sejol.secsys.Adapters.TagListViewAdapter;
import com.example.sejol.secsys.Clases.Ruta;
import com.example.sejol.secsys.Clases.Tag;
import com.example.sejol.secsys.R;
import com.example.sejol.secsys.Utilidades.SQLite_Controller;

import java.util.ArrayList;

/**
 * Created by fabio on 21/5/2016.
 */
public class CrearRutasFragment extends Fragment {

    private View view;
    private CardView cardView1;
    private CardView cardView2;

    private ListView listView;
    private SQLite_Controller db;
    private ArrayList<Ruta> rutas;
    private ArrayList<String> nombresRutas;
    private ArrayAdapter<String> adapter;

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
        view = inflater.inflate(R.layout.fragment_crear_rutas, container, false);

        db = new SQLite_Controller(view.getContext());
        rutas = db.getRutas();
        nombresRutas = new ArrayList<>();
        for(Ruta ruta:rutas){
            nombresRutas.add(ruta.getNombre());
        }
        adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,nombresRutas);
        listView = (ListView) view.findViewById(R.id.listView);

        listView.setAdapter(adapter);

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
                startActivityForResult(intent,1);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {

                //Toast.makeText(view.getContext(),"List item "+position,Toast.LENGTH_SHORT).show();
                final PopupMenu popup = new PopupMenu(view.getContext(), listView);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.opciones_ruta, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.ver:
                                (new VerRutaDialogClass(view.getContext(),position)).show();
                                return true;
                            case R.id.modificar:
                                Intent intent = new Intent(getActivity(), ModificarRutaActivity.class);
                                intent.putExtra("ruta",rutas.get(position));
                                startActivity(intent);
                                return true;
                            case R.id.borrar:
                                db.borrarRuta(rutas.get(position).getCodigo());
                                nombresRutas.remove(position);
                                rutas.remove(position);
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Bundle b = data.getExtras();
            nombresRutas.add((String) b.get("ruta"));
            adapter.notifyDataSetChanged();
        }
    }


    public class VerRutaDialogClass extends Dialog implements android.view.View.OnClickListener {
        int pos;
        public VerRutaDialogClass(Context a, int pos) {
            super(a);
            this.pos = pos;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.popup_ver_ruta);

            TextView nom = (TextView) findViewById(R.id.popRutNom);
            TextView vueltas = (TextView) findViewById(R.id.popRutVults);
            ListView tags = (ListView) findViewById(R.id.popLvRutTags);

            nom.setText(nom.getText().toString() + rutas.get(pos).getNombre());
            vueltas.setText(vueltas.getText().toString() + rutas.get(pos).getVueltas());
            TagListViewAdapter adapter = new TagListViewAdapter(db.getTagsDeRuta(rutas.get(pos).getCodigo()), view.getContext());
            tags.setAdapter(adapter);
        }

        @Override
        public void onClick(View v) {

        }
    }
}