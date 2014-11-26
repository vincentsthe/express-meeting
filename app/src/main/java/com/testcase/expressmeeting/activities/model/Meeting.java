package com.testcase.expressmeeting.activities.model;

import android.util.Log;

import java.io.Serializable;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Meeting implements Serializable {

    private int id;
    private String name;
    private String description;
    private Double latitude;
    private Double longitude;
    private int open;
    private Calendar time;
    private String location;
    private int organizer_id;

    public Meeting(int id, String name, String description, Double latitude, Double longitude, int open, String time, String location, int organizer_id) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.open = open;
        try {
            this.time = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            this.time.setTime(sdf.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.location = location;
        this.organizer_id = organizer_id;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public boolean isOpen() {
        return (this.open==1);
    }

    public int getOrganizer_id() {
        return this.organizer_id;
    }

    public int getDay() {
        return this.time.get(Calendar.DAY_OF_MONTH);        //example: 1, 14, 31
    }

    public String getMonth() {
        return (new DateFormatSymbols()).getMonths()[this.time.get(Calendar.MONTH)];
    }

    public int getYear() {
        return this.time.get(Calendar.YEAR);
    }

    public int getHour() {
        return this.time.get(Calendar.HOUR_OF_DAY);
    }

    public int getMinute() {
        return this.time.get(Calendar.MINUTE);
    }

    public String getDateString() {
        return (Integer.toString(this.getDay()) + " " + this.getMonth() + " " + Integer.toString(this.getYear()));
    }

    public String getTimeString() {
        return (Integer.toString(this.getHour()) + ":" + Integer.toString(this.getMinute()));
    }

    public String getFullString() {
        return (this.getDateString() + " " + this.getTimeString());
    }

    public long getDayLeft() {
        Date now = new Date();
        long diff = this.time.getTime().getTime() - now.getTime();

        return (diff/(1000 * 60 * 60 * 24));
    }

    public String getLocation() {
        return this.location;
    }
}
