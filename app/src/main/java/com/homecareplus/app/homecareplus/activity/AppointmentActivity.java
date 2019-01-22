package com.homecareplus.app.homecareplus.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.homecareplus.app.homecareplus.R;
import com.homecareplus.app.homecareplus.adapter.AppointmentTabAdapter;


public class AppointmentActivity extends AppCompatActivity
{
    private ViewPager mainViewPager;
    private TabLayout mainTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);

        mainViewPager = (ViewPager) findViewById(R.id.mainViewPager);
        mainTabLayout = (TabLayout) findViewById(R.id.appointmentTabLayout);

        AppointmentTabAdapter pagerAdapter = new AppointmentTabAdapter(getSupportFragmentManager(), mainTabLayout.getTabCount());
        mainViewPager.setAdapter(pagerAdapter);
        mainViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mainTabLayout));

        mainTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                mainViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab)
            {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab)
            {

            }
        });
    }

    public void onClickLogout(View v)
    {

    }
}
