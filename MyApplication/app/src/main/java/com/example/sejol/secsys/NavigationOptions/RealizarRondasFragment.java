package com.example.sejol.secsys.NavigationOptions;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.sejol.secsys.Clases.Reporte;
import com.example.sejol.secsys.Clases.Ronda;
import com.example.sejol.secsys.Clases.Ruta;
import com.example.sejol.secsys.Clases.Tag;
import com.example.sejol.secsys.Clases.Usuario;
import com.example.sejol.secsys.Popup.PopupGuardarRonda;
import com.example.sejol.secsys.Popup.PopupReportarAnomalia;
import com.example.sejol.secsys.Popup.PopupSeleccionarRuta;
import com.example.sejol.secsys.R;
import com.example.sejol.secsys.Utilidades.Email_Controller;
import com.example.sejol.secsys.Utilidades.NFC_Controller;
import com.example.sejol.secsys.Utilidades.PDF_Controller;
import com.example.sejol.secsys.Utilidades.SQLite_Controller;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class RealizarRondasFragment extends Fragment implements LocationListener {

    View v;
    private GoogleMap mMap;
    private LocationManager locationManager;
    LatLng UserLatLng;


    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;

    SQLite_Controller db; // Base de datos
    ArrayList<Tag> puntosPorRecorrer; // Tag de la ruta seleccionada
    ArrayList<Marker> punterosEnMapa = new ArrayList<>(); // Marker de los tag de la ruta seleccionada
    Tag[] estadoDeRonda; // Estado del recorrido de la ruta --> Tag de la ronda
    Ronda ronda; // Ronda --> Conjunto de tags
    Usuario usuario;
    Marker MarkerReporte;
    ArrayList<Reporte> reportes = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_realizar_rutas, container, false);

        db = new SQLite_Controller(v.getContext());

        usuario = (Usuario) getArguments().getSerializable("usuario");

        // Boton para seleccionar una ruta
        ImageButton btnSeleccionarRuta = (ImageButton) v.findViewById(R.id.btnMapSelecRuta);
        btnSeleccionarRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(v.getContext(), PopupSeleccionarRuta.class);
                startActivityForResult(i, 1);
            }
        });

        // Button for positioning on actual position
        setUpMapIfNeeded();
        //Agregar Click Listener
        addClickListener();
        //Activar Gestos del MApa
        activateGestures();

        // Ubicacion de de la camara en el mapa donde se encuentra el usuario
        mMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapRR)).getMap();
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(v.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(v.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);

        return v;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == 1) { // Volver del seleccionar ruta
                mMap.clear(); // Limpiar el mapa
                Ruta ruta = (Ruta) data.getSerializableExtra("ruta"); // get ruta
                ronda = crearIdRonda(ruta);  // Construi ronda a partir de la ruta seleccionada

                puntosPorRecorrer = db.getTagsDeRutaPorRuta(ruta.getCodigo()); // Get tag de la ruta
                estadoDeRonda = new Tag[puntosPorRecorrer.size()]; // Crear lista de estados para la ronda

                displayMarkerPuntosPorRecorrer();// Mostrar lista de tag en el mapa
            }else if(requestCode == 2){ // Volver luego de seleccionar guardar
                Bundle b = data.getExtras();
                ronda.setNombre((String) b.get("nombre"));
                guardarRonda(); // Guardar ronda en bd
                new PDF_Controller(
                        ronda,
                        usuario,
                        db.getTagsDeRondaPorCodigo(ronda.getCodigo()),
                        db.getRepsDeRondaPorCodigo(ronda.getCodigo()));
                new Email_Controller(
                        v.getContext(),
                        ronda,
                        usuario,
                        db.getTagsDeRondaPorCodigo(ronda.getCodigo()),
                        db.getRepsDeRondaPorCodigo(ronda.getCodigo()));
            }else if(requestCode == 3){ // Volver luego de realizar un reporte
                Bundle b = data.getExtras();
                String titulo = b.get("titulo").toString();
                String descripcion = b.get("descripcion").toString();
                String hora = new SimpleDateFormat("HH:mm:ss").format(new Date());

                Reporte nuevoReporte = new Reporte(titulo,descripcion,hora);
                reportes.add(nuevoReporte);
                BitmapDescriptor icon =
                        BitmapDescriptorFactory.fromResource(R.drawable.map_icon_notification);
                mMap.addMarker(new MarkerOptions().position(MarkerReporte.getPosition()).draggable(true).icon(icon));
                MarkerReporte.remove();
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////   Controladores de ronda
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public Ronda getRonda() {
        return ronda;
    }

    /*
    Constructor de ronda a partir de un ruta seleccionada
     */
    private Ronda crearIdRonda(Ruta ruta) {

        Ronda ronda = new Ronda();
        String fecha = new SimpleDateFormat("dd/MM/yy HH:mm:ss").format(new Date()); // Fecha de cración de la ronda

        ronda.setCodigo(ruta.getCodigo() + fecha);
        ronda.setNombre("");
        ronda.setFecha(fecha);
        ronda.setRuta(ruta.getCodigo());

        return ronda;
    }

    /*
    Metodo para guardar la ronda del sistema en la base de datos
     */
    private void guardarRonda(){
        // Almacenar ronda en la base de datos
        // GUardar ronda
        db.insertRonda(ronda.getCodigo(), ronda.getNombre(), ronda.getFecha(),ronda.getRuta(), usuario.getUsuario());
        // Guardar estado de la ronda (Ountos recorridos y no recorridos)
        for (int i = 0; i < puntosPorRecorrer.size(); i++) {
            if (estadoDeRonda[i] != null)
                db.insertTagRND(
                        estadoDeRonda[i].getCodigo(),
                        estadoDeRonda[i].getHora(),
                        estadoDeRonda[i].getRonda()); // Almacenar tag y asignarlo a la ruta creada
            else
                db.insertTagRND(
                        puntosPorRecorrer.get(i).getCodigo(),
                        " No realizado",
                        puntosPorRecorrer.get(i).getRonda()); // Almacenar tag y asignarlo a la ruta creada
        }
        //GUardar reportes
        db.insertListaReportes(reportes,ronda);
    }

    /*
    Actualiza el mapa luego de pasar por un tag
     */
    public void ActualizarRonda(Tag lectura) {
        for (int i = 0; i < puntosPorRecorrer.size(); i++) {
            if (puntosPorRecorrer.get(i).getCodigo().equals(lectura.getCodigo())) {

                Tag nuevoTag = new Tag();
                String fecha = new SimpleDateFormat("dd/MM/yy HH:mm:ss").format(new Date());

                nuevoTag.setCodigo(lectura.getCodigo() + "_" + fecha);
                nuevoTag.setRonda(ronda.getCodigo());
                nuevoTag.setHora(fecha);

                estadoDeRonda[i] = nuevoTag;
                BitmapDescriptor icon =
                        BitmapDescriptorFactory.fromResource(R.drawable.map_icon_recorrido);
                punterosEnMapa.get(i).setIcon(icon);
            }
        }
    }

    /*
    Carga en el mapa todos lo tag de una ruta
     */
    private void displayMarkerPuntosPorRecorrer() {
        for (Tag tag : puntosPorRecorrer) {
            String[] tagData = tag.getCodigo().split("_");
            double lng = Double.parseDouble(tagData[2]);
            double lat = Double.parseDouble(tagData[3]);
            LatLng ll = new LatLng(lat, lng);

            BitmapDescriptor icon =
                    BitmapDescriptorFactory.fromResource(R.drawable.map_icon_sinrecorrer);
            punterosEnMapa.add(
                    mMap.addMarker(new MarkerOptions()
                            .position(ll)
                            .draggable(true)
                            .title(tagData[0])
                            .icon(icon)));
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////   Menu
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_realizar_ronda, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_guardar_ronda:
                Intent i = new Intent(v.getContext(), PopupGuardarRonda.class);
                i.putExtra("pntPorRecorrer",puntosPorRecorrer);
                i.putExtra("estadoRonda",estadoDeRonda);
                startActivityForResult(i,2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////   Configurar mapa
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapRR))
                    .getMap();

            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        // El objeto GoogleMap ha sido referenciado correctamente ahora podemos manipular sus propiedades
        // Seteamos el tipo de mapa
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Activamos la capa o layer MyLocation
        if (ActivityCompat.checkSelfPermission(v.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(v.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }

        });

        //Este evento se ejecuta cuando le dan click a la ventana de información del Marker
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                marker.remove();
            }
        });
    }

    // ---------------------------------------------------------------------------------------------
    // ----------------------------   Cambios de posicion      -------------------------------------
    // ---------------------------------------------------------------------------------------------

    @Override
    public void onLocationChanged(Location location) {
        UserLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(UserLatLng, 10);

        mMap.animateCamera(cameraUpdate);
        if (ActivityCompat.checkSelfPermission(v.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(v.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
    }

    // ---------------------------------------------------------------------------------------------
    // ----------------------------   Marcadores o punteros    -------------------------------------
    // ---------------------------------------------------------------------------------------------

    // Crear un marcador
    public void addClickListener(){
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(MarkerReporte != null)
                    MarkerReporte.remove();

                MarkerReporte = mMap.addMarker(new MarkerOptions().position(latLng).draggable(true).title("Remover"));
                Intent i = new Intent(v.getContext(), PopupReportarAnomalia.class);
                startActivityForResult(i,3);
            }
        });
    }



    // ---------------------------------------------------------------------------------------------
    // ------------------------------   Configurar gestos     --------------------------------------
    // ---------------------------------------------------------------------------------------------

    public void activateGestures(){
        UiSettings mUisettings = mMap.getUiSettings();

        mUisettings.setRotateGesturesEnabled(true);
        mUisettings.setScrollGesturesEnabled(true);
        mUisettings.setTiltGesturesEnabled(true);
        mUisettings.setZoomControlsEnabled(true);
        mUisettings.setZoomGesturesEnabled(true);
    }

}