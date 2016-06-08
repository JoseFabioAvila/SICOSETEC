package com.example.sejol.secsys.Activitys;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sejol.secsys.R;
import com.example.sejol.secsys.Utilidades.NFC_Controller;

public class CrearRondaActivity extends FragmentActivity {

    EditText cod;
    Button btnCrear;
    boolean permitir = false;
    private NFC_Controller nfcController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_ronda);

        cod = (EditText) findViewById(R.id.editText);
        btnCrear = (Button) findViewById(R.id.btnGrabar);

        nfcController = new NFC_Controller(this, 1);

        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cod.getText().equals("")) {
                    Toast.makeText(getApplicationContext(), "Antes ingre el codigo", Toast.LENGTH_LONG).show();
                    permitir = false;
                } else {
                    permitir = true;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        nfcController.enableForegroundDispatchSystem();
    }

    @Override
    protected void onPause() {
        super.onPause();
        nfcController.disableForegroundDispatchSystem();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        nfcController.escribir(permitir, cod, intent);
        //finish();
    }
}