package com.testcase.expressmeeting.activities;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
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
import android.view.ViewDebug;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TimePicker;

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

import java.util.Calendar;
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

    public void showDatePickerDialog(View v) {
        // Process to get Current Date
        Calendar c = Calendar.getInstance();
        int mYear, mMonth, mDay;
        final Button btn = (Button) findViewById(R.id.dates);

        if (btn.getText().toString().equals("Date")) {
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
        }
        else {
            String[] temp = btn.getText().toString().split("-");
            mYear = Integer.parseInt(temp[0]);
            mMonth = Integer.parseInt(temp[1])-1;
            mDay = Integer.parseInt(temp[2]);
        }

        // Launch Date Picker Dialog
        DatePickerDialog dpd = new DatePickerDialog(this,
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    // Display Selected date in textbox
                    btn.setText(year + "-" + ((monthOfYear<9)?"0":"") + (monthOfYear + 1) + "-" + ((dayOfMonth<10)?"0":"") + dayOfMonth);
                }
            }, mYear, mMonth, mDay);
        dpd.show();
    }

    public void showTimePickerDialog(View v) {
        // Process to get Current Time
        Calendar c = Calendar.getInstance();
        int mHour, mMinute;
        final Button btn = (Button) findViewById(R.id.times);

        if (btn.getText().toString().equals("Time")) {
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
        }
        else {
            String[] temp = btn.getText().toString().split(":");
            mHour = Integer.parseInt(temp[0]);
            mMinute = Integer.parseInt(temp[1]);
        }

        // Launch Time Picker Dialog
        TimePickerDialog tpd = new TimePickerDialog(this,
            new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    // Display Selected time in textbox
                    btn.setText(((hourOfDay<10)?"0":"") + hourOfDay + ":" + ((minute<10)?"0":"") + minute);
                }
            }, mHour, mMinute, true);
        tpd.show();
    }
}
