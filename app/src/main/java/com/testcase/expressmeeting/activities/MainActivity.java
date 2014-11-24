package com.testcase.expressmeeting.activities;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.testcase.expressmeeting.R;


public class MainActivity extends ActionBarActivity implements LocationListener {

    private static final String MAP_TAG = "GMap";

    private Location currentLoc;

    private Toolbar mToolbar;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupActionBar();
        setupDrawer();
        if (savedInstanceState == null) {
            setupMap();
        }

        setupLocation();
    }

    @Override
    protected void onStart() {
        super.onStart();

        configMap();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLoc = location;
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

    private void setupActionBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void setupDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.drawable.ic_launcher, R.drawable.ic_launcher) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void setupLocation() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        currentLoc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (currentLoc == null) {
            currentLoc = new Location("coba");
            currentLoc.setLatitude(0f);
            currentLoc.setLongitude(0f);
        }
    }

    private void setupMap() {
        GoogleMapOptions googleMapOptions = new GoogleMapOptions();
        googleMapOptions.zoomControlsEnabled(false);

        MapFragment mapFragment = MapFragment.newInstance(googleMapOptions);

        getFragmentManager().beginTransaction()
            .add(R.id.container, mapFragment, MAP_TAG)
            .commit();
    }

    private void configMap() {
        mMap = ((MapFragment)getFragmentManager().findFragmentByTag(MAP_TAG)).getMap();
        mMap.setMyLocationEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude()), 10.0f));
    }
}
