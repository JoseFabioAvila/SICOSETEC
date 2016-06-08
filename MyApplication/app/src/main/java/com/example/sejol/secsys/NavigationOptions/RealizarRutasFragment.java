package com.example.sejol.secsys.NavigationOptions;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.sejol.secsys.Clases.Ronda;
import com.example.sejol.secsys.Clases.Ruta;
import com.example.sejol.secsys.Clases.Tag;
import com.example.sejol.secsys.Popup.PopupSeleccionarRuta;
import com.example.sejol.secsys.R;
import com.example.sejol.secsys.Utilidades.NFC_Controller;
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
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class RealizarRutasFragment extends Fragment implements LocationListener {

    View v;
    private GoogleMap mMap;
    private LocationManager locationManager;
    LatLng UserLatLng;
    Marker MarkerLatLng;

    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;

    ImageButton btnSeleccionarRuta;

    SQLite_Controller db;
    ArrayList<Tag> puntosPorRecorrer;
    Ronda ronda;
    ArrayList<Tag> estadoDeRonda;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_realizar_rutas, container, false);

        db = new SQLite_Controller(v.getContext());

        // Boton para seleccionar una ruta
        btnSeleccionarRuta = (ImageButton)v.findViewById(R.id.btnMapSelecRuta);
        btnSeleccionarRuta.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(v.getContext(), PopupSeleccionarRuta.class);
                startActivityForResult(i,1);
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
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);

        return v;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mMap.clear();
        Ruta ruta = (Ruta)data.getSerializableExtra("ruta");
        puntosPorRecorrer = db.getTagsDeRuta(ruta.getCodigo());
        Toast.makeText(v.getContext(),ruta.getNombre(),Toast.LENGTH_SHORT).show();
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
                Toast.makeText(v.getContext(),"Holis",Toast.LENGTH_SHORT);
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
                if(MarkerLatLng != null)
                    MarkerLatLng.remove();

                MarkerLatLng = mMap.addMarker(new MarkerOptions()
                        .position(latLng).draggable(true).title("Remover"));
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        });
    }

    // ---------------------------------------------------------------------------------------------
    // ------------------------------   Configurar gestos     --------------------------------------
    // ---------------------------------------------------------------------------------------------


    /*
        EL API de Google Maps para Android viene con una serie de gestos ya predefinidos
        los cuales son configurables con la finalidad de dirigir la manera en la que el
        usuario interactúa con el mismo.
    */

    public void activateGestures(){
        UiSettings mUisettings = mMap.getUiSettings();

        mUisettings.setRotateGesturesEnabled(true);
        mUisettings.setScrollGesturesEnabled(true);
        mUisettings.setTiltGesturesEnabled(true);
        mUisettings.setZoomControlsEnabled(true);
        mUisettings.setZoomGesturesEnabled(true);
    }

}