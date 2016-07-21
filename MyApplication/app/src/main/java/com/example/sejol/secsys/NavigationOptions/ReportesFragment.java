package com.example.sejol.secsys.NavigationOptions;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.sejol.secsys.Clases.Ronda;
import com.example.sejol.secsys.Clases.Usuario;
import com.example.sejol.secsys.Popup.PopupVerReporte;
import com.example.sejol.secsys.R;
import com.example.sejol.secsys.Utilidades.PDF_Controller;
import com.example.sejol.secsys.Utilidades.SQLite_Controller;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportesFragment extends Fragment {

    private View view;
    private boolean undo = false;
    private CaldroidFragment caldroidFragment;
    private CaldroidFragment dialogCaldroidFragment;
    private ArrayList<Ronda> rondas;
    private Usuario usuario;
    private SQLite_Controller db;
    private ListView lvRondas;
    private ArrayList<String> listaRondas = new ArrayList<>(); // Lista de los nombres de las rondas
    private ArrayAdapter<String> lRondasAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_reportes, container, false);
        db = new SQLite_Controller(view.getContext());
        usuario = (Usuario) getArguments().getSerializable("usuario"); // Usuario logueado
        lRondasAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, listaRondas);
        setCalendar();
        setListView();

        return view;
    }

    private void setListView() {
        lvRondas = (ListView) view.findViewById(R.id.lvRondas); // Lista para desplrear las rondas
        lvRondas.setAdapter(lRondasAdapter);
        lvRondas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

                //Toast.makeText(view.getContext(),"List item "+position,Toast.LENGTH_SHORT).show();
                final PopupMenu popup = new PopupMenu(view.getContext(), lvRondas);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.opciones_ronda, popup.getMenu());

                final int pos = position; // Posicion numerica del objeto de la lista seleccionado
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.ver:
                                Intent i = new Intent(view.getContext(),PopupVerReporte.class);
                                i.putExtra("ronda",rondas.get(pos));
                                i.putExtra("usuario",usuario);
                                i.putExtra("lstTags",db.getTagsDeRonda(rondas.get(pos).getCodigo()));
                                i.putExtra("lstRep",db.getRepsDeRonda(rondas.get(pos).getCodigo()));
                                startActivityForResult(i,100);
                                return true;
                            case R.id.pdf:
                                new PDF_Controller(
                                        rondas.get(pos),
                                        usuario,
                                        db.getTagsDeRonda(rondas.get(pos).getCodigo()),
                                        db.getRepsDeRonda(rondas.get(pos).getCodigo()),
                                        view.getContext());
                                return true;
                            case R.id.borrarRonda:
                                db.borrarRonda(rondas.get(pos).getCodigo());
                                listaRondas.remove(pos);
                                lRondasAdapter.notifyDataSetChanged();
                                return true;
                        }
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });
    }

    private void setCalendar() {
        final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
        // Setup caldroid fragment
        // **** If you want normal CaldroidFragment, use below line ****
        caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
        args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);

        caldroidFragment.setArguments(args);
        cargarReportes();
        //setCustomResourceForDates();

        // Attach to the activity
        FragmentTransaction t = getFragmentManager().beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
        t.commit();

        // Setup listener
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                String fecha = new SimpleDateFormat("dd/MM/yy").format(date);
                listaRondas.clear();
                for (int i = 0; i < rondas.size(); i++) {
                    String temp = (Arrays.asList(rondas.get(i).getFecha().split(" "))).get(0);
                    if(fecha.equals(temp)){
                        listaRondas.add(rondas.get(i).getNombre());
                    }
                }
                lRondasAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChangeMonth(int month, int year) {
                String text = "month: " + month + " year: " + year;
            }

            @Override
            public void onLongClickDate(Date date, View view) {
            }

            @Override
            public void onCaldroidViewCreated() {
                if (caldroidFragment.getLeftArrowButton() != null) {
                    Toast.makeText(view.getContext(),
                            "Caldroid view is created", Toast.LENGTH_SHORT)
                            .show();
                }
            }

        };

        // Setup Caldroid
        caldroidFragment.setCaldroidListener(listener);
    }

    private void cargarReportes(){
        rondas = db.getRondas(usuario); // recoger rondas del usuario
        for (int i = 0; i < rondas.size(); i++) {
            String startDateString = rondas.get(i).getFecha();
            DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
            Date fechaRonda = null;
            try {
                fechaRonda = df.parse(startDateString);
                if(rondas.get(i).getCompleta().equals("completada"))
                    setCompleto(fechaRonda);
                else
                    setIncompleto(fechaRonda);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void setCompleto(Date date){
        Date blueDate = date;
        ColorDrawable blue = new ColorDrawable(Color.GREEN);
        caldroidFragment.setBackgroundDrawableForDate(blue, blueDate);
        caldroidFragment.setTextColorForDate(R.color.white, blueDate);
    }

    private void setIncompleto(Date date){
        Date blueDate = date;
        ColorDrawable blue = new ColorDrawable(Color.RED);
        caldroidFragment.setBackgroundDrawableForDate(blue, blueDate);
        caldroidFragment.setTextColorForDate(R.color.white, blueDate);
    }

    /*
   Configura el alto del Listview para que se base en la suma del alto de los elementos en la lista
   eliminando el scrollview
    */
    public void justifyListViewHeightBasedOnChildren (ListView listView) {

        ListAdapter adapter = listView.getAdapter();

        if (adapter == null) {
            return;
        }
        ViewGroup vg = listView;
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, vg);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(par);
        listView.requestLayout();
    }
}
