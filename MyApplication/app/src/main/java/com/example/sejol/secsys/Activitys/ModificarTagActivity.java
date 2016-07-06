package com.example.sejol.secsys.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.sejol.secsys.Clases.Reporte;
import com.example.sejol.secsys.Clases.Ruta;
import com.example.sejol.secsys.Clases.Tag;
import com.example.sejol.secsys.R;
import com.example.sejol.secsys.Utilidades.Email_Controller;
import com.example.sejol.secsys.Utilidades.GPS_Controller;
import com.example.sejol.secsys.Utilidades.PDF_Controller;
import com.example.sejol.secsys.Utilidades.SQLite_Controller;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.itextpdf.text.Paragraph;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ModificarTagActivity extends AppCompatActivity {

    Tag tag;
    EditText txtLat, txtLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_tag);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        tag = (Tag) intent.getSerializableExtra("tag");

        final EditText txtNombre = (EditText)findViewById(R.id.txtMdfTagNombre);
        txtLat    = (EditText)findViewById(R.id.txtMdfTagLat);
        txtLng    = (EditText)findViewById(R.id.txtMdfTagLng);

        txtNombre.setText(tag.getNombre());
        final List<String> tagCodigo = Arrays.asList(tag.getCodigo().split("_"));
        txtLat.setText(tagCodigo.get(3));
        txtLng.setText(tagCodigo.get(2));

        RadioGroup radioGroup = (RadioGroup)findViewById(R.id.rdGroupCoor);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch(checkedId)
                {
                    case R.id.rndActCoord:
                        GPS_Controller gps = new GPS_Controller(ModificarTagActivity.this);
                        txtLat.setText(String.valueOf(gps.getLatitude()));
                        txtLng.setText(String.valueOf(gps.getLongitude()));
                        break;
                    case R.id.rndBscCoord:
                        Intent i = new Intent(ModificarTagActivity.this, BuscardorDeCoordenadasActivity.class);
                        i.putExtra("lat",Double.valueOf(txtLat.getText().toString()));
                        i.putExtra("lng",Double.valueOf(txtLng.getText().toString()));
                        startActivityForResult(i,100);
                        break;
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String codigoV = tag.getCodigo();
                List<String> oldCode = Arrays.asList(tag.getCodigo().split("_"));
                tag.setCodigo(oldCode.get(0)+"_"+ oldCode.get(1)+"_"+txtLng.getText().toString()+"_"+txtLat.getText().toString());
                tag.setNombre(txtNombre.getText().toString());
                SQLite_Controller db = new SQLite_Controller(ModificarTagActivity.this);
                db.updateTag(codigoV,tag);

                Intent localIntent = new Intent();
                localIntent.putExtra("tag",tag);
                setResult(1000, localIntent);
                finish();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == 100) {
                Bundle b = data.getExtras();
                txtLat.setText((String) b.get("lat"));
                txtLng.setText((String) b.get("lng"));
            }
        }
    }
}
