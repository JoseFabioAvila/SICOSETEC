package com.example.sejol.secsys.NavigationOptions;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sejol.secsys.Activitys.LoginActivity;
import com.example.sejol.secsys.Activitys.MainActivity;
import com.example.sejol.secsys.Adapters.DoubleLineListViewAdapter;
import com.example.sejol.secsys.Clases.Tag;
import com.example.sejol.secsys.Clases.Usuario;
import com.example.sejol.secsys.Popup.PopupConfiguracionDeCorreos;
import com.example.sejol.secsys.R;
import com.example.sejol.secsys.Utilidades.SQLite_Controller;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfiguracionFragment extends Fragment {

    View v;
    SQLite_Controller db;
    Usuario usuario;

    public ConfiguracionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_configuracion, container, false);
        db = new SQLite_Controller(v.getContext());
        usuario = (Usuario) getArguments().getSerializable("usuario");

        ListView lv = (ListView) v.findViewById (R.id.listViewConf);
        ArrayList<String> lstTitulos = new ArrayList<>(), lstSubtitulos = new ArrayList<>();
        lstTitulos.add("Nombre de usuario"); lstSubtitulos.add("Presionar para cambiar el nombre de usuario utilizado");
        lstTitulos.add("Cambiar correo"); lstSubtitulos.add("Presionar para cambiar el correo utilizado");
        lstTitulos.add("Cambiar contrasela"); lstSubtitulos.add("Presionar para cambiar la contraseña de usario utilizada");
        lstTitulos.add("Modificar reseptores"); lstSubtitulos.add("Correos a los que se envian los reportes diarios");
        lstTitulos.add("Cerrar sesión"); lstSubtitulos.add("");
        DoubleLineListViewAdapter dlAdapter = new DoubleLineListViewAdapter(lstTitulos,lstSubtitulos,v.getContext());
        lv.setAdapter(dlAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                opciones(position);
            }
        });
        return v;
    }

    private void opciones(int op){
        Intent i;
        IngresarTextoDialogClass INTDC;
        switch (op){
            case 0:
                INTDC = new IngresarTextoDialogClass(v.getContext(),0); // En espera de NFC
                INTDC.show();
                break;
            case 1:
                INTDC = new IngresarTextoDialogClass(v.getContext(),1); // En espera de NFC
                INTDC.show();
                break;
            case 2:
                INTDC = new IngresarTextoDialogClass(v.getContext(),2); // En espera de NFC
                INTDC.show();
                break;
            case 3:
                i = new Intent(v.getContext(), PopupConfiguracionDeCorreos.class);
                startActivityForResult(i,3);
                break;
            case 4:
                v.getContext().getSharedPreferences("Login", 0).edit().clear().commit();
                i = new Intent(v.getContext(), LoginActivity.class);
                startActivity(i);
                break;
        }
    }

    public class IngresarTextoDialogClass extends Dialog {
        int op = 0;

        public IngresarTextoDialogClass(Context a, int op) {
            super(a);
            this.op = op;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.popup_nombre_tag);

            final EditText nombre = (EditText) findViewById(R.id.popTxtTagNombre);
            ImageButton ok = (ImageButton) findViewById(R.id.popBtnOk);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (op){
                        case 0:
                            usuario.setNombre(nombre.getText().toString());
                            db.updateUsuario(usuario);
                            break;
                        case 1:
                            String temp = usuario.getUsuario();
                            usuario.setUsuario(nombre.getText().toString());
                            db.updateUsuario(temp,usuario);
                            break;
                        case 2:
                            usuario.setContraseña(nombre.getText().toString());
                            db.updateUsuario(usuario);
                            break;
                    }
                    dismiss();
                }
            });
        }
    }
}
