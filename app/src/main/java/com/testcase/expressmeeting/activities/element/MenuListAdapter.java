package com.testcase.expressmeeting.activities.element;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.testcase.expressmeeting.R;

public class MenuListAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    public MenuListAdapter(Context context, String[] values) {
        super(context, R.layout.menu_layout, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.menu_layout, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.menu_text);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.menu_icon);
        textView.setText(values[position]);
        // Change the icon for Windows and iPhone
        if(position == 0) {
            imageView.setImageResource(R.drawable.map);
        } else if(position == 1) {
            imageView.setImageResource(R.drawable.user);
        } else if(position == 2) {
            imageView.setImageResource(R.drawable.planner);
        } else if(position == 3) {
            imageView.setImageResource(R.drawable.group);
        } else if(position == 4) {
            imageView.setImageResource(R.drawable.notif);
        } else {
            imageView.setImageResource(R.drawable.user);
        }

        return rowView;
    }
}
