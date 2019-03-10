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
import com.homecareplus.app.homecareplus.model.Client;
import com.homecareplus.app.homecareplus.presenter.ClientPreviousAppointmentPresenter;
import com.homecareplus.app.homecareplus.util.SharedPreference;

public class ClientInformationActivity extends AppCompatActivity
{
    private ViewPager mainViewPager;
    private BottomNavigationView navigationView;
    private Client client;
    private ClientPreviousAppointmentPresenter previousAppointmentPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_client_details);

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

        this.client = (Client) getIntent().getSerializableExtra(SharedPreference.KEY_CLIENT);

        this.mainViewPager = findViewById(R.id.mainViewPager);
        this.navigationView = findViewById(R.id.bottom_navigation);

        ClientPreviousAppointmentsFragment previousAppointmentsFragment = new ClientPreviousAppointmentsFragment();
        ClientInformationFragment clientInformationFragment = new ClientInformationFragment();

        this.previousAppointmentPresenter = new ClientPreviousAppointmentPresenter();
        this.previousAppointmentPresenter.setView(previousAppointmentsFragment);

        FragmentTabAdapter pagerAdapter = new FragmentTabAdapter(getSupportFragmentManager());

        pagerAdapter.addFragment(clientInformationFragment);
        pagerAdapter.addFragment(previousAppointmentsFragment);

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
                            case R.id.actionPreviousApp:
                                mainViewPager.setCurrentItem(1);
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
                } else
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

    public Client getClient()
    {
        return client;
    }
}
