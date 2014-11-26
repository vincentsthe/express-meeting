package com.testcase.expressmeeting.activities.drawer;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.testcase.expressmeeting.R;
import com.testcase.expressmeeting.activities.DetailMeetingActivity;
import com.testcase.expressmeeting.activities.MainActivity;
import com.testcase.expressmeeting.activities.element.MenuListAdapter;

public class SidebarDrawer {

    private ActionBarActivity activity;
    private Toolbar mToolbar;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    public SidebarDrawer(ActionBarActivity activity, Toolbar toolbar) {
        this.activity = activity;
        this.mToolbar = toolbar;
    }

    public void setupDrawer() {
        mDrawerLayout = (DrawerLayout) this.activity.findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) this.activity.findViewById(R.id.left_drawer);
        String[] menus = new String[]{"Home", "Profile", "Meetings", "Friends", "Notification"};
        MenuListAdapter menuListAdapter = new MenuListAdapter(this.activity, menus);
        mDrawerList.setAdapter(menuListAdapter);

        mDrawerToggle = new ActionBarDrawerToggle(this.activity, mDrawerLayout, mToolbar, R.drawable.ic_launcher, R.drawable.ic_launcher) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if(position == 0) {
                    Intent intent = new Intent(activity.getApplicationContext(), MainActivity.class);
                    activity.startActivity(intent);
                } else {
                    Intent intent = new Intent(activity.getApplicationContext(), DetailMeetingActivity.class);
                    activity.startActivity(intent);
                }
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

}
