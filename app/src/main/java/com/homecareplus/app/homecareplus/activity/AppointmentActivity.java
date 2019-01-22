package com.homecareplus.app.homecareplus.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.homecareplus.app.homecareplus.R;
import com.homecareplus.app.homecareplus.adapter.AppointmentTabAdapter;
import com.homecareplus.app.homecareplus.contract.AppointmentInformationContract;
import com.homecareplus.app.homecareplus.contract.AppointmentMapContract;
import com.homecareplus.app.homecareplus.model.Appointment;
import com.homecareplus.app.homecareplus.presenter.AppointmentInfoPresenter;
import com.homecareplus.app.homecareplus.presenter.AppointmentMapPresenter;
import com.homecareplus.app.homecareplus.util.SharedPreference;


public class AppointmentActivity extends AppCompatActivity
{
    private ViewPager mainViewPager;
    private TabLayout mainTabLayout;
    private Appointment appointment;
    private AppointmentInformationContract.presenter appInfoPresenter;
    private AppointmentMapContract.presenter appMapPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_layout);

        Toolbar toolbar = findViewById(R.id.custom_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);

        toolbar.findViewById(R.id.emptyBackButtonHolder).setVisibility(View.GONE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });


        this.appInfoPresenter = new AppointmentInfoPresenter();
        this.appMapPresenter = new AppointmentMapPresenter();

        this.appointment = (Appointment) getIntent().getSerializableExtra(SharedPreference.KEY_APPOINTMENT);
        this.mainViewPager = findViewById(R.id.mainViewPager);
        this.mainTabLayout = findViewById(R.id.appointmentTabLayout);


        AppointmentMapTabFragment mapTabFragment = new AppointmentMapTabFragment();
        AppointmentInfoTabFragment appointmentInfoTabFragment = new AppointmentInfoTabFragment();

        this.appInfoPresenter.setView(appointmentInfoTabFragment);
        this.appMapPresenter.setView(mapTabFragment);

        AppointmentTabAdapter pagerAdapter = new AppointmentTabAdapter(getSupportFragmentManager());

        pagerAdapter.addFragement(appointmentInfoTabFragment);
        pagerAdapter.addFragement(mapTabFragment);

        this.mainViewPager.setAdapter(pagerAdapter);
        this.mainViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mainTabLayout));

        this.mainTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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

    public Appointment getAppointment()
    {
        return this.appointment;
    }
}
