package com.homecareplus.app.homecareplus.activity;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.homecareplus.app.homecareplus.BuildConfig;
import com.homecareplus.app.homecareplus.R;
import com.homecareplus.app.homecareplus.adapter.AppointmentSection;
import com.homecareplus.app.homecareplus.adapter.CustomSectionedAdapter;
import com.homecareplus.app.homecareplus.callback.ItemTouchHelperCallback;
import com.homecareplus.app.homecareplus.contract.AppointmentRowOnClickListener;
import com.homecareplus.app.homecareplus.model.Appointment;
import com.homecareplus.app.homecareplus.model.AppointmentSectionModel;
import com.homecareplus.app.homecareplus.model.Client;
import com.homecareplus.app.homecareplus.util.GPSTracker;
import com.homecareplus.app.homecareplus.util.SharedPreference;
import com.homecareplus.app.homecareplus.viewmodel.LoginViewModel;
import com.homecareplus.app.homecareplus.viewmodel.MainActivityViewModel;
import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;

import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private RecyclerView recyclerView;
    private CustomSectionedAdapter adapter;
    private TextView employeeNameTextView;
    private ItemTouchHelperExtension extension;
    private ItemTouchHelperExtension.Callback callback;
    private MainActivityViewModel mainActivityViewModel;
    private Observer<String> employeeNameObs;
    private Observer<List<AppointmentSectionModel>> appointmentSectionObs;

    private TextView noAppointmentsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GPSTracker.getInstance(this).startLocationScanning(this);

        this.employeeNameTextView = findViewById(R.id.employeeNameTextView);
        this.noAppointmentsTextView = findViewById(R.id.noAppointmentsTextView);

        Toolbar toolbar = findViewById(R.id.custom_toolbar);
        toolbar.findViewById(R.id.logout_imageView).setVisibility(View.VISIBLE);

        setSupportActionBar(toolbar);

        this.adapter = new CustomSectionedAdapter(new AppointmentRowOnClickListener()
        {
            @Override
            public void onItemClick(View v, Appointment appointment)
            {
                //Start the client appointment activity here
                Intent intent = new Intent(MainActivity.this, AppointmentActivity.class);
                intent.putExtra(SharedPreference.KEY_APPOINTMENT, appointment);
                startActivity(intent);
            }

            @Override
            public void onCallClicked(Appointment appointment)
            {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + appointment.getClientPhoneNumber().replace("-", "")));
                startActivity(callIntent);
            }

            @Override
            public void onClientInfoClicked(Client client)
            {
                Intent intent = new Intent(MainActivity.this, ClientInformationActivity.class);
                intent.putExtra(SharedPreference.KEY_CLIENT, client);
                startActivity(intent);
            }
        });
        this.callback = new ItemTouchHelperCallback();
        this.extension = new ItemTouchHelperExtension(callback);

        String employeeId = SharedPreference.getSharedInstance(getApplicationContext()).getEmployeeId();

        this.recyclerView = findViewById(R.id.client_appointment_RecyclerView);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.cell_divider));
        this.recyclerView.addItemDecoration(divider);
        this.recyclerView.setAdapter(adapter);
        this.extension.attachToRecyclerView(recyclerView);
        this.adapter.setItemTouchHelperExtension(extension);

        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        mainActivityViewModel.init(employeeId);

        appointmentSectionObs = new Observer<List<AppointmentSectionModel>>()
        {
            @Override
            public void onChanged(List<AppointmentSectionModel> appointmentSections)
            {
                if (appointmentSections.isEmpty()) {
                    noAppointmentsTextView.setVisibility(View.VISIBLE);
                } else {
                    noAppointmentsTextView.setVisibility(View.GONE);
                }

                for (AppointmentSectionModel a: appointmentSections)
                {
                    adapter.displayAppointmentSection(a);
                }
            }
        };
        mainActivityViewModel.getAppointmentSections().observe(this, appointmentSectionObs);

        mainActivityViewModel.getLogoutData().observe(this, new Observer<Boolean>()
        {
            @Override
            public void onChanged(Boolean logout)
            {
                if (logout)
                {
                    startLoginActivity();
                }
            }
        });

        employeeNameObs = new Observer<String>()
        {
            @Override
            public void onChanged(String name)
            {
                employeeNameTextView.setText(name);
            }
        };
        mainActivityViewModel.getEmployeeName().observe(this, employeeNameObs);
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    public void onClickLogout(View v)
    {

        mainActivityViewModel.getAppointmentSections().removeObserver(appointmentSectionObs);
        mainActivityViewModel.getEmployeeName().removeObserver(employeeNameObs);
        mainActivityViewModel.logout();
    }


    public Activity getActivity()
    {
        return this;
    }


    public void startLoginActivity()
    {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
