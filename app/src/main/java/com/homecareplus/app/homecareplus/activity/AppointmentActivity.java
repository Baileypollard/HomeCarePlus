package com.homecareplus.app.homecareplus.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.homecareplus.app.homecareplus.R;
import com.homecareplus.app.homecareplus.adapter.FragmentTabAdapter;
import com.homecareplus.app.homecareplus.contract.AppointmentHoursContract;
import com.homecareplus.app.homecareplus.contract.AppointmentInformationContract;
import com.homecareplus.app.homecareplus.model.Appointment;
import com.homecareplus.app.homecareplus.presenter.AppointmentHoursPresenter;
import com.homecareplus.app.homecareplus.presenter.AppointmentInfoPresenter;
import com.homecareplus.app.homecareplus.util.SharedPreference;


public class AppointmentActivity extends AppCompatActivity
{
    private ViewPager mainViewPager;
    private BottomNavigationView navigationView;
    private Appointment appointment;
    private AppointmentInformationContract.presenter appInfoPresenter;
    private FragmentTabAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
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

        this.appointment = (Appointment) getIntent().getSerializableExtra(SharedPreference.KEY_APPOINTMENT);
        this.mainViewPager = findViewById(R.id.mainViewPager);
        this.navigationView = findViewById(R.id.bottom_navigation);

        AppointmentInfoTabFragment appointmentInfoTabFragment = new AppointmentInfoTabFragment();
        AppointmentHoursTabFragment appointmentHoursTabFragment = new AppointmentHoursTabFragment();
        AppointmentMapTabFragment mapTabFragment = new AppointmentMapTabFragment();

        this.appInfoPresenter.setView(appointmentInfoTabFragment);

        this.pagerAdapter = new FragmentTabAdapter(getSupportFragmentManager());

        this.pagerAdapter.addFragment(appointmentInfoTabFragment);
        this.pagerAdapter.addFragment(appointmentHoursTabFragment);
        this.pagerAdapter.addFragment(mapTabFragment);

        this.mainViewPager.setAdapter(pagerAdapter);

        navigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener()
                {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item)
                    {
                        switch (item.getItemId())
                        {
                            case R.id.actionAppInfo:
                                mainViewPager.setCurrentItem(0);
                                break;
                            case R.id.actionCompleteApp:
                                mainViewPager.setCurrentItem(1);
                                break;
                            case R.id.actionAppMap:
                                mainViewPager.setCurrentItem(2);
                                break;
                        }
                        return false;
                    }
                });

        mainViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            private MenuItem prevMenuItem;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {

            }

            @Override
            public void onPageSelected(int position)
            {
                if (prevMenuItem != null)
                {
                    prevMenuItem.setChecked(false);
                }
                else
                {
                    navigationView.getMenu().getItem(0).setChecked(false);
                }

                navigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = navigationView.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state)
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
