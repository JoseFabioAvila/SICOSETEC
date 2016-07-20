package com.example.sejol.secsys.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sejol.secsys.Clases.Reporte;
import com.example.sejol.secsys.R;

import java.util.ArrayList;

/**
 * Created by jose on 6/21/16.
 */
public class DoubleLineListViewAdapter extends BaseAdapter {
    Context ctx;
    ArrayList<String> lstTitles = new ArrayList();
    ArrayList<String> lstSubtitles = new ArrayList();
    LayoutInflater mInflater;

    public DoubleLineListViewAdapter(ArrayList<String> lstTitles,ArrayList<String> lstSubtitles, Context ctx)
    {
        this.lstTitles = lstTitles;
        this.lstSubtitles = lstSubtitles;
        this.mInflater = LayoutInflater.from(ctx);
        this.ctx = ctx;
    }

    public int getCount()
    {
        return this.lstTitles.size();
    }

    public Object getItem(int paramInt)
    {
        return this.lstTitles.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
        return 0L;
    }

    public View getView(int pos, View view, ViewGroup paramViewGroup)
    {
        if (view == null) {
            view = this.mInflater.inflate(R.layout.list_item_configuracion, null);
        }
        ((TextView)view.findViewById(R.id.txtTitulo)).setText(lstTitles.get(pos));
        ((TextView)view.findViewById(R.id.txtSubtitulo)).setText(lstSubtitles.get(pos));

        return view;
    }
}
