package com.example.sejol.secsys.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sejol.secsys.Clases.Reporte;
import com.example.sejol.secsys.Clases.Tag;
import com.example.sejol.secsys.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jose on 6/21/16.
 */
public class ReportesListViewAdapter extends BaseAdapter {
    Context ctx;
    ArrayList<Reporte> lstReportes = new ArrayList();
    LayoutInflater mInflater;

    public ReportesListViewAdapter(ArrayList<Reporte> lst,Context ctx)
    {
        this.lstReportes = lst;
        this.mInflater = LayoutInflater.from(ctx);
        this.ctx = ctx;
    }

    public int getCount()
    {
        return this.lstReportes.size();
    }

    public Object getItem(int paramInt)
    {
        return this.lstReportes.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
        return 0L;
    }

    public View getView(int pos, View view, ViewGroup paramViewGroup)
    {

        if (view == null) {
            view = this.mInflater.inflate(R.layout.list_item_reporte, null);
        }

        TextView repNum = (TextView)view.findViewById(R.id.lvNum);
        TextView repAnomalia  = (TextView)view.findViewById(R.id.lvAnomalia);
        TextView repDescripcion = (TextView)view.findViewById(R.id.lvDescripcion);
        TextView repHora = (TextView)view.findViewById(R.id.lvHora);

        Reporte reporte = lstReportes.get(pos);

        repNum.setText          ("Reporte "+(pos+1));
        repAnomalia.setText     ("Anomalía: "+reporte.getAnomalia());
        repDescripcion.setText  ("Detalle:  "+reporte.getDescripcion());
        repHora.setText         ("Hora:     "+reporte.getHora());

        return view;
    }
}
