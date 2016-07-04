package com.example.sejol.secsys.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sejol.secsys.Clases.Tag;
import com.example.sejol.secsys.R;
import com.itextpdf.text.Paragraph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jose on 7/1/16.
 */
public class TagListViewAdapter extends BaseAdapter {
    Context ctx;
    ArrayList<Tag> lsttags = new ArrayList();
    LayoutInflater mInflater;

    public TagListViewAdapter(ArrayList<Tag> lst, Context ctx)
    {
        this.mInflater = LayoutInflater.from(ctx);
        this.lsttags = lst;
        this.ctx = ctx;
    }

    public int getCount()
    {
        return this.lsttags.size();
    }

    public Object getItem(int pos)
    {
        return this.lsttags.get(pos);
    }

    public long getItemId(int pos)
    {
        return 0;
    }

    public View getView(int pos, View view, ViewGroup paramViewGroup)
    {
        View v = this.mInflater.inflate(R.layout.list_item_tagview, null);

        TextView tagNombre  = (TextView)v.findViewById(R.id.lv_tag);
        TextView tagPos = (TextView)v.findViewById(R.id.lv_hora);

        //Tag tag = lsttags.get(pos);
        //List<String> codeData = Arrays.asList(tag.getCodigo().split("_"));
        List<String> codeData = Arrays.asList(lsttags.get(pos).getCodigo().split("_"));
        tagNombre.setText(lsttags.get(pos).getNombre() + ": ");
        tagPos.setText("    Latitud : " + codeData.get(3) + "\n    Longitud: " + codeData.get(2));

        return v;
    }
}
