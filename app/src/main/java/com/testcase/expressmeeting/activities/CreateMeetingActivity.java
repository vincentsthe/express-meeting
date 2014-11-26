package com.testcase.expressmeeting.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.testcase.expressmeeting.R;
import com.testcase.expressmeeting.activities.drawer.SidebarDrawer;

import java.util.List;
import java.util.Locale;


public class CreateMeetingActivity extends ActionBarActivity {

    private static final String MAP_TAG = "GMap";

//    private static final String ACTIVITY_TAG = "CreateMeetingActivity";

    private LatLng currentLoc;

    private String currentAddress;

    private Toolbar mToolbar;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_meeting);

        setupActionBar();
        Bundle extras = getIntent().getExtras();
        currentLoc = (LatLng)extras.get("position");
        currentAddress = getCompleteAddressString(currentLoc.latitude, currentLoc.longitude);
        EditText et= (EditText) findViewById(R.id.location);
        et.setHint(currentAddress);
        setupMap();

        SidebarDrawer sidebarDrawer = new SidebarDrawer(this, this.mToolbar);
        sidebarDrawer.setupDrawer();
//        Log.i(ACTIVITY_TAG, currentLoc.latitude+" "+currentLoc.longitude);
//        Log.i(ACTIVITY_TAG,getCompleteAddressString(currentLoc.latitude, currentLoc.longitude));
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

    private void setupActionBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void setupMap() {
        GoogleMapOptions googleMapOptions = new GoogleMapOptions();
        googleMapOptions.zoomControlsEnabled(false);
        googleMapOptions.scrollGesturesEnabled(false);
        googleMapOptions.rotateGesturesEnabled(false);

        MapFragment mapFragment = MapFragment.newInstance(googleMapOptions);

        getFragmentManager().beginTransaction()
                .add(R.id.map_container, mapFragment, MAP_TAG)
                .commit();
    }

    private void configMap() {
        mMap = ((MapFragment)getFragmentManager().findFragmentByTag(MAP_TAG)).getMap();
        mMap.setMyLocationEnabled(false);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 17.0f));

        MarkerOptions markerOpt = new MarkerOptions().position(currentLoc);
        final Marker marker = mMap.addMarker(markerOpt);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                currentLoc = point;
                marker.setPosition(currentLoc);
                mMap.animateCamera(CameraUpdateFactory.newLatLng(currentLoc));
                currentAddress = getCompleteAddressString(currentLoc.latitude, currentLoc.longitude);
                EditText et= (EditText) findViewById(R.id.location);
                et.setHint(currentAddress);
            }
        });
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
//                Log.i(ACTIVITY_TAG, returnedAddress.toString());

                StringBuilder strReturnedAddress = new StringBuilder("");

                int i;
                for (i = 0; i < returnedAddress.getMaxAddressLineIndex()-1; i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(", ");
                }
                String last = returnedAddress.getAddressLine(i);
                String[] lastAddress = last.split(",");
                strReturnedAddress.append(lastAddress[0]);
                strAdd = strReturnedAddress.toString();
//                Log.w("My Current loction address", "" + strReturnedAddress.toString());
            } else {
//                Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
//            Log.w("My Current loction address", "Canont get Address!");
        }

        if (strAdd.length() == 0) strAdd = "Location";
        return strAdd;
    }
}
