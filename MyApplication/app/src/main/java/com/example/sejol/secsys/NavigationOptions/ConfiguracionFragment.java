package com.example.sejol.secsys.NavigationOptions;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sejol.secsys.Activitys.LoginActivity;
import com.example.sejol.secsys.Adapters.DoubleLineListViewAdapter;
import com.example.sejol.secsys.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfiguracionFragment extends Fragment {

    View v;

    public ConfiguracionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_configuracion, container, false);

        ListView lv = (ListView) v.findViewById (R.id.listViewConf);
        ArrayList<String> lstTitulos = new ArrayList<>(), lstSubtitulos = new ArrayList<>();
        lstTitulos.add("Nombre de usuario"); lstSubtitulos.add("Presionar para cambiar el nombre de usuario utilizado");
        lstTitulos.add("Cambiar correo"); lstSubtitulos.add("Presionar para cambiar el correo utilizado");
        lstTitulos.add("Cambiar contrasela"); lstSubtitulos.add("Presionar para cambiar la contraseña de usario utilizada");
        lstTitulos.add("Modificar reseptores"); lstSubtitulos.add("Correos a los que se envian los reportes diarios");
        lstTitulos.add("Cerrar sesión"); lstSubtitulos.add("");
        DoubleLineListViewAdapter dlAdapter = new DoubleLineListViewAdapter(lstTitulos,lstSubtitulos,v.getContext());
        lv.setAdapter(dlAdapter);
        lv.setDescendantFocusability(ListView.FOCUS_BLOCK_DESCENDANTS);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(v.getContext(),"funciona",Toast.LENGTH_LONG).show();
                opciones(position);
            }
        });
        return v;
    }

    private void opciones(int op){
        switch (op){
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                v.getContext().getSharedPreferences("Login", 0).edit().clear().commit();
                Intent i = new Intent(v.getContext(), LoginActivity.class);
                startActivity(i);
                break;
        }
    }

}
