package com.homecareplus.app.homecareplus.activity;

import android.content.Intent;
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

import com.homecareplus.app.homecareplus.R;
import com.homecareplus.app.homecareplus.adapter.CustomSectionedAdapter;
import com.homecareplus.app.homecareplus.callback.ItemTouchHelperCallback;
import com.homecareplus.app.homecareplus.contract.AppointmentRowOnClickListener;
import com.homecareplus.app.homecareplus.contract.MainAppointmentsContract;
import com.homecareplus.app.homecareplus.model.Appointment;
import com.homecareplus.app.homecareplus.model.Employee;
import com.homecareplus.app.homecareplus.presenter.MainAppointmentPresenter;
import com.homecareplus.app.homecareplus.util.SharedPreference;
import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;

public class MainActivity extends AppCompatActivity implements MainAppointmentsContract.view
{
    private RecyclerView recyclerView;
    private MainAppointmentPresenter presenter;
    private CustomSectionedAdapter adapter;
    private TextView employeeNameTextView;
    private ItemTouchHelperExtension extension;
    private ItemTouchHelperExtension.Callback callback;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.employeeNameTextView = findViewById(R.id.employeeNameTextView);

        this.presenter = new MainAppointmentPresenter(this);

        Toolbar toolbar = findViewById(R.id.custom_toolbar);
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
        });
        this.callback = new ItemTouchHelperCallback();
        this.extension = new ItemTouchHelperExtension(callback);

        this.presenter.fetchAppointments();
        this.presenter.fetchEmployeeName(SharedPreference.getSharedInstance(getApplicationContext()).getEmployeeId());

        this.recyclerView = findViewById(R.id.client_appointment_RecyclerView);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.cell_divider));
        this.recyclerView.addItemDecoration(divider);
        this.recyclerView.setAdapter(adapter);
        this.extension.attachToRecyclerView(recyclerView);
        this.adapter.setmItemTouchHelperExtension(extension);
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void displayAppointment(Appointment appointment)
    {
        adapter.addAppointment(appointment);
    }

    @Override
    public void displayEmployeeName(String name)
    {
        employeeNameTextView.setText(name);
    }


    public void onClickLogout(View v)
    {
        presenter.logout();
    }

    @Override
    public void startLoginActivity()
    {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
