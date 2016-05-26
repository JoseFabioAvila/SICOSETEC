package com.example.sejol.secsys.Activitys;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.sejol.secsys.Adapters.FragmentDrawer;
import com.example.sejol.secsys.NavigationOptions.CrearRutasFragment;
import com.example.sejol.secsys.NavigationOptions.DescargarReportesFragment;
import com.example.sejol.secsys.NavigationOptions.RealizarRutasFragment;
import com.example.sejol.secsys.R;

public class MainActivity
        extends AppCompatActivity
        implements  FragmentDrawer.FragmentDrawerListener{

    private static String TAG = MainActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_main);
        mToolbar.setTitle(TAG);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Setting navigation drawer
        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
        displayView(0); //Display option 0 o the navigation drawer
    }



    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new RealizarRutasFragment();
                title = getString(R.string.title_adm_realizar);
                break;
            case 1:
                fragment = new CrearRutasFragment();
                title = getString(R.string.title_adm_crear);
                break;
            case 2:
                fragment = new DescargarReportesFragment();
                title = getString(R.string.title_adm_descargar);
                break;
            case 3:
                finish();
                break;
            default:
                break;
        }

        // Do fragment display transaction
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }
}
