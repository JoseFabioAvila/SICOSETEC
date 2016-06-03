package com.example.sejol.secsys.NavigationOptions;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sejol.secsys.R;
import com.example.sejol.secsys.Utilidades.NFC_Controller;

/**
 * A simple {@link Fragment} subclass.
 */
public class RealizarRutasFragment extends Fragment {

    public TextView textView;

    public RealizarRutasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_realizar_rutas, container, false);
        textView = (TextView) view.findViewById(R.id.fralgo);
        return view;
    }

    public TextView getTexView(){
        return textView;
    }
}
