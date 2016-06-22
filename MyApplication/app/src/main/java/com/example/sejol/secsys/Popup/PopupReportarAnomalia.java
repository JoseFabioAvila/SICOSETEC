package com.example.sejol.secsys.Popup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.example.sejol.secsys.R;

public class PopupReportarAnomalia extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_reportar_anomalia);

        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int width = localDisplayMetrics.widthPixels;
        int height = localDisplayMetrics.heightPixels;
        getWindow().setLayout((int)(0.70 * width), (int)(0.60 * height));
    }
}
