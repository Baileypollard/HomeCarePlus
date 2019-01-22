package com.homecareplus.app.homecareplus.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.homecareplus.app.homecareplus.activity.AppointmentInfoTabFragment;
import com.homecareplus.app.homecareplus.activity.AppointmentMapTabFragment;

public class AppointmentTabAdapter extends FragmentStatePagerAdapter
{
    private int numberOfTabs;

    public AppointmentTabAdapter(FragmentManager fm, int numberOfTabs)
    {
        super(fm);
        this.numberOfTabs = numberOfTabs;
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position)
        {
            case 0:
                return new AppointmentInfoTabFragment();
            case 1:
                return new AppointmentMapTabFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount()
    {
        return this.numberOfTabs;
    }
}