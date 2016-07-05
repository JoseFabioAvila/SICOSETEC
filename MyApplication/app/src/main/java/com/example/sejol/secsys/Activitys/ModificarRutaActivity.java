package com.example.sejol.secsys.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.sejol.secsys.Adapters.TagListViewAdapter;
import com.example.sejol.secsys.Clases.Ruta;
import com.example.sejol.secsys.Clases.Tag;
import com.example.sejol.secsys.R;
import com.example.sejol.secsys.Utilidades.SQLite_Controller;

import java.util.ArrayList;

public class ModificarRutaActivity extends AppCompatActivity {

    SQLite_Controller db;
    Ruta ruta;
    ListView lvTags;
    TagListViewAdapter adapter; // Adapter para el listview
    ArrayList<Tag> PntsTagRuta = new ArrayList<>(); // Putos en el recorrido de la ruta

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_ruta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new SQLite_Controller(this);
        Intent intent = getIntent();
        ruta = (Ruta) intent.getSerializableExtra("ruta");

        final EditText txtNombre = (EditText) findViewById(R.id.txtMdfRutNombre);
        txtNombre.setText(ruta.getNombre());
        final EditText txtVuelta = (EditText) findViewById(R.id.txtMdfNumVueltas);
        txtVuelta.setText(ruta.getVueltas());
        lvTags    = (ListView) findViewById(R.id.LvMdfTagRut);
        AdapterSet();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ruta.setNombre(txtNombre.getText().toString());
                ruta.setVueltas(txtVuelta.getText().toString());
                db.updateRuta(ruta);
                finish();
            }
        });
    }

    /*
        Metodo para configurar el adapter para la lista de puntos en la ruta
     */
    private void AdapterSet() {
        lvTags.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                //Toast.makeText(view.getContext(),"List item "+position,Toast.LENGTH_SHORT).show();
                final PopupMenu popup = new PopupMenu(view.getContext(), lvTags);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.opciones_tag, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.modificar:
                                Intent i = new Intent(ModificarRutaActivity.this, ModificarTagActivity.class);
                                i.putExtra("tag",PntsTagRuta.get(position));
                                startActivityForResult(i,1000);
                                return true;
                            case R.id.borrar:
                                db.borrarTagDeRuta(PntsTagRuta.get(position).getCodigo());
                                PntsTagRuta.remove(position);
                                adapter.notifyDataSetChanged();
                                return true;
                        }
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });
        PntsTagRuta = db.getTagsDeRuta(ruta.getCodigo());
        adapter = new TagListViewAdapter(PntsTagRuta, ModificarRutaActivity.this);
        lvTags.setAdapter(adapter);
    }
}
