package com.homecareplus.app.homecareplus.activity;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.homecareplus.app.homecareplus.R;
import com.homecareplus.app.homecareplus.adapter.AppointmentSection;
import com.homecareplus.app.homecareplus.adapter.CustomSectionedAdapter;
import com.homecareplus.app.homecareplus.contract.MainAppointmentsContract;
import com.homecareplus.app.homecareplus.model.Appointment;
import com.homecareplus.app.homecareplus.model.Employee;
import com.homecareplus.app.homecareplus.presenter.MainAppointmentPresenter;

import java.util.ArrayList;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class MainActivity extends AppCompatActivity implements MainAppointmentsContract.view
{
    private RecyclerView recyclerView;
    private MainAppointmentPresenter presenter;
    private CustomSectionedAdapter adapter;
    private TextView employeeNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.employeeNameTextView = findViewById(R.id.employeeNameTextView);

        presenter = new MainAppointmentPresenter(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);

        adapter = new CustomSectionedAdapter();

        presenter.fetchAppointments();

        this.recyclerView = findViewById(R.id.client_appointment_RecyclerView);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.cell_divider));
        this.recyclerView.addItemDecoration(divider);
        this.recyclerView.setAdapter(adapter);
    }

    @Override
    public void displayAppointment(Appointment appointment)
    {
        adapter.addAppointment(appointment);
    }

    @Override
    public void displayEmployeeInfo(Employee employee)
    {

    }
}
