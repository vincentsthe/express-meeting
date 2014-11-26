package com.testcase.expressmeeting.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.testcase.expressmeeting.R;
import com.testcase.expressmeeting.activities.drawer.SidebarDrawer;
import com.testcase.expressmeeting.activities.element.MeetingListAdapter;
import com.testcase.expressmeeting.activities.model.Meeting;

public class ListMeetingActivity extends ActionBarActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_meeting);

        setupActionBar();
        SidebarDrawer sidebarDrawer = new SidebarDrawer(this, this.mToolbar);
        sidebarDrawer.setupDrawer();

        ListView listView = (ListView) findViewById(R.id.list_meeting);
        final Meeting[] listMeeting = Meeting.getMockData();
        MeetingListAdapter meetingListAdapter = new MeetingListAdapter(this, listMeeting);
        listView.setAdapter(meetingListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(getApplicationContext(), DetailMeetingActivity.class);
                intent.putExtra("MEETING_DETAIL", listMeeting[position]);
                startActivity(intent);
            }
        });
    }

    private void setupActionBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_meeting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
