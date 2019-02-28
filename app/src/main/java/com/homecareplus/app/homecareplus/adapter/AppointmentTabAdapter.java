package com.homecareplus.app.homecareplus.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.homecareplus.app.homecareplus.activity.AppointmentInfoTabFragment;
import com.homecareplus.app.homecareplus.activity.AppointmentMapTabFragment;

import java.util.ArrayList;
import java.util.List;

public class AppointmentTabAdapter extends FragmentStatePagerAdapter
{
    List<Fragment> mFragmentList;

    public AppointmentTabAdapter(FragmentManager fm)
    {
        super(fm);
        mFragmentList = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position)
    {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount()
    {
        return mFragmentList.size();
    }

    public void addFragement(Fragment fragment)
    {
        mFragmentList.add(fragment);
    }
}