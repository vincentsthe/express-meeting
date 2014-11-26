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
import android.widget.TextView;

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
import com.testcase.expressmeeting.activities.model.Meeting;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;


public class DetailMeetingActivity extends ActionBarActivity {

    private static final String MAP_TAG = "GMap";

//    private static final String ACTIVITY_TAG = "CreateMeetingActivity";

    private LatLng currentLoc;

    private Toolbar mToolbar;

    private GoogleMap mMap;

    private Meeting meeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_meeting);

        setupActionBar();
        setupMap();
//        Log.i(ACTIVITY_TAG, currentLoc.latitude+" "+currentLoc.longitude);
//        Log.i(ACTIVITY_TAG,getCompleteAddressString(currentLoc.latitude, currentLoc.longitude));

        SidebarDrawer sidebarDrawer = new SidebarDrawer(this, this.mToolbar);
        sidebarDrawer.setupDrawer();

        Intent intent = getIntent();
        this.meeting = (Meeting) intent.getSerializableExtra("MEETING_DETAIL");
    }

    @Override
    protected void onStart() {
        super.onStart();

        addMeetingDetail();
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
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 18.0f));

        MarkerOptions markerOpt = new MarkerOptions().position(currentLoc);
        mMap.addMarker(markerOpt);
    }

    private void addMeetingDetail() {
        TextView label;

        //name
        label = (TextView)findViewById(R.id.name);
        label.setText(this.meeting.getName());

        //datetime
        label = (TextView)findViewById(R.id.datetime);
        label.setText(this.meeting.getFullString());

        //organizer
        label = (TextView)findViewById(R.id.organizer);
        label.setText("Mirza Widihananta");

        //location
        currentLoc = new LatLng(-6.878944, 107.6172301);
        label = (TextView)findViewById(R.id.location);
        label.setText(this.meeting.getLocation());

        //description
        label = (TextView)findViewById(R.id.description);
        label.setText(this.meeting.getDescription());
    }
}
