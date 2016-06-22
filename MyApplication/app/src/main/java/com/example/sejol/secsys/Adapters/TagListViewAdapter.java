package com.example.sejol.secsys.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sejol.secsys.Clases.Tag;
import com.example.sejol.secsys.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jose on 6/21/16.
 */
public class TagListViewAdapter extends BaseAdapter {
    Context ctx;
    ArrayList<Tag> lsttags = new ArrayList();
    ArrayList<String> seleccionados = new ArrayList<>();
    LayoutInflater mInflater;

    public TagListViewAdapter(ArrayList<Tag> lst,Context ctx)
    {
        this.lsttags = lst;
        this.mInflater = LayoutInflater.from(ctx);
        this.ctx = ctx;
    }

    public int getCount()
    {
        return this.lsttags.size();
    }

    public Object getItem(int paramInt)
    {
        return this.lsttags.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
        return 0L;
    }

    public View getView(int pos, View view, ViewGroup paramViewGroup)
    {

        if (view == null) {
            view = this.mInflater.inflate(R.layout.list_item_tagview, null);
        }

        TextView tagPos  = (TextView)view.findViewById(R.id.lv_tag);
        TextView tagHora = (TextView)view.findViewById(R.id.lv_hora);

        Tag tag = lsttags.get(pos);

        List<String> codeData = Arrays.asList(tag.getCodigo().split("_"));
        tagPos.setText("Punto " + codeData.get(0));
        tagHora.setText(tag.getHora());

        return view;
    }
}
