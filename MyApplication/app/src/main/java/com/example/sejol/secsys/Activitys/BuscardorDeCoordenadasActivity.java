package com.example.sejol.secsys.Activitys;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.sejol.secsys.Clases.Tag;
import com.example.sejol.secsys.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class BuscardorDeCoordenadasActivity extends FragmentActivity implements LocationListener {

    private GoogleMap mMap;
    private LocationManager locationManager;
    LatLng UserLatLng;
    Marker MarkerLatLng;
    Button btnListo;

    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscardor_de_coordenadas);

        btnListo = (Button)findViewById(R.id.btnListo);
        btnListo.setVisibility(View.VISIBLE);
        btnListo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent localIntent = new Intent();
                localIntent.putExtra("lat", String.valueOf(MarkerLatLng.getPosition().latitude));
                localIntent.putExtra("lng", String.valueOf(MarkerLatLng.getPosition().longitude));
                setResult(100, localIntent);
                finish();
            }
        });

        // Button for positioning on actual position
        setUpMapIfNeeded();
        //Agregar Click Listener
        addClickListener();
        //Activar Gestos del MApa
        activateGestures();

        // Ubicacion de de la camara en el mapa donde se encuentra el usuario
        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);

        double lat = getIntent().getDoubleExtra("lat",0);
        double lng = getIntent().getDoubleExtra("lng",0);
        LatLng ll = new LatLng(lat,lng);
        MarkerLatLng = mMap.addMarker(new MarkerOptions()
                .position(ll).draggable(true).title("Remover"));
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////   Configurar mapa
    ///////////////////////////////////////////////////////////////////////////////////////////////
    private void setUpMapIfNeeded() {
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap(); // Check if we were successful in obtaining the map.

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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(getApplicationContext(), "" +
                        "Latitud: "+MarkerLatLng.getPosition().latitude+
                        "\nLongitud: "+MarkerLatLng.getPosition().longitude, Toast.LENGTH_LONG).show();
                return false;
            }

        });

        //Este evento se ejecuta cuando le dan click a la ventana de información del Marker
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                marker.remove();
                btnListo.setVisibility(View.INVISIBLE);
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

                btnListo.setVisibility(View.VISIBLE);
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
