package com.testcase.expressmeeting.activities.element;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.testcase.expressmeeting.R;
import com.testcase.expressmeeting.activities.model.Meeting;

/**
 * Created by vincentsthe on 26/11/14.
 */
public class MeetingListAdapter extends ArrayAdapter<Meeting> {
    private final Context context;
    private final Meeting[] listMeeting;

    public MeetingListAdapter(Context context, Meeting[] values) {
        super(context, R.layout.menu_layout, values);
        this.context = context;
        this.listMeeting = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_meeting_layout, parent, false);

        TextView dayText = (TextView) rowView.findViewById(R.id.day);
        TextView monthText = (TextView) rowView.findViewById(R.id.month);
        TextView titleText = (TextView) rowView.findViewById(R.id.title);
        TextView dayLeftText = (TextView) rowView.findViewById(R.id.day_left);
        Log.i("asdasd", "check");

        dayText.setText(Integer.toString(listMeeting[position].getDay()));
        monthText.setText(listMeeting[position].getMonth());
        titleText.setText(listMeeting[position].getName());
        dayLeftText.setText(Long.toString(listMeeting[position].getDayLeft()) + " days left.");

        return rowView;
    }
}
