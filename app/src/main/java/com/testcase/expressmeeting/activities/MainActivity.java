package com.testcase.expressmeeting.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
import com.testcase.expressmeeting.R;
import com.testcase.expressmeeting.activities.drawer.SidebarDrawer;
import com.testcase.expressmeeting.activities.element.MenuListAdapter;


public class MainActivity extends ActionBarActivity implements LocationListener {

    private static final String MAP_TAG = "GMap";

    private Location currentLoc;

    private GoogleMap mMap;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupActionBar();
//        setupDrawer();
        if (savedInstanceState == null) {
            setupMap();
        }

        SidebarDrawer sidebarDrawer = new SidebarDrawer(this, this.mToolbar);
        sidebarDrawer.setupDrawer();

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
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude()), 13.0f));
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng point) {
                mMap.animateCamera(CameraUpdateFactory.newLatLng(point));
                Intent intent = new Intent(getApplicationContext(), CreateMeetingActivity.class);
                intent.putExtra("position",point);
                startActivity(intent);
            }
        });
        setupFriends();
        setupMeeting();
    }

    private void setupFriends() {
        String username, name, lastLogin;
        IconGenerator factory = new IconGenerator(this);
        Bitmap icon;
        factory.setContentPadding(5, -5, 5, -3);
        factory.setStyle(IconGenerator.STYLE_GREEN);

        username = "vincentsthe";
        name = "Vincent Sebastian The";
        lastLogin = "25 November 2014";
        icon = factory.makeIcon(username);
        mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(icon))
                .position(new LatLng(currentLoc.getLatitude()+0.01, currentLoc.getLongitude()-0.005))
                .title(name)
                .snippet("Last Login : " + lastLogin)
        );

        username = "atnanahidiw";
        name = "Mirza Widihananta";
        lastLogin = "25 November 2014";
        icon = factory.makeIcon(username);
        mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromBitmap(icon))
                        .position(new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude()))
                        .title(name)
                        .snippet("Last Login : " + lastLogin)
        );
    }

    private void setupMeeting() {
        String date, name, location;
        IconGenerator factory = new IconGenerator(this);
        Bitmap icon;
        factory.setContentPadding(10, 2, 10, 2);
        factory.setStyle(IconGenerator.STYLE_BLUE);

        date = "25";
        name = "Kerja Bareng Hackathon";
        location = "Kosan Mirza";
        icon = factory.makeIcon(date);
        mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromBitmap(icon))
                        .position(new LatLng(currentLoc.getLatitude()+0.05, currentLoc.getLongitude()+0.05))
                        .title(name)
                        .snippet(location)
        );
    }
}
