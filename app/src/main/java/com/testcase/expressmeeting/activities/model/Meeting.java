package com.testcase.expressmeeting.activities.model;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    public static Meeting[] getMockData() {
        Meeting[] meetings = new Meeting[5];
        meetings[0] = new Meeting(1, "Hackaton", "Be there at 7AM", 21.123123, 21.123123, 0, "2014-11-27 07:00:00", "Binus", 0);
        meetings[1] = new Meeting(2, "Reunion", "We will meet up in front of restaurant.", 21.123123, 21.123123, 0, "2014-11-28 07:00:00", "East Restaurant", 0);
        meetings[2] = new Meeting(5, "Startup Asia Jakarta", "Don't miss the first day!", 21.123123, 21.123123, 0, "2014-11-29 07:00:00", "Mandiri Tower", 1);
        meetings[3] = new Meeting(7, "Tech In Asia", "Don't miss our event!", 21.123123, 21.123123, 0, "2014-11-30 07:00:00", "Jakarta Area", 0);
        meetings[4] = new Meeting(9, "Annual Meeting", "Remember to bring your invitation.", 21.123123, 21.123123, 0, "2014-12-02 07:00:00", "West Restaurant", 1);
        return meetings;
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
