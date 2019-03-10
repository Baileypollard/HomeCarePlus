package com.homecareplus.app.homecareplus.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.homecareplus.app.homecareplus.R;
import com.homecareplus.app.homecareplus.contract.AppointmentMapContract;
import com.homecareplus.app.homecareplus.model.Appointment;
import com.homecareplus.app.homecareplus.viewmodel.AppointmentMapViewModel;

public class AppointmentMapTabFragment extends Fragment implements OnMapReadyCallback
{
    private GoogleMap googleMap;
    private Appointment appointment;
    private AppointmentMapViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        AppointmentActivity activity = (AppointmentActivity) getActivity();
        if (activity != null)
        {
            this.appointment = activity.getAppointment();
        }
        return inflater.inflate(R.layout.fragment_appointment_map, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        viewModel = ViewModelProviders.of(this).get(AppointmentMapViewModel.class);
        viewModel.init(appointment.getAddress(), getResources().getString(R.string.google_api_key));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.appointmentMap);
        if (mapFragment != null)
        {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        this.googleMap = googleMap;
        viewModel.getLatLngData().observe(this, new Observer<LatLng>()
        {
            @Override
            public void onChanged(@Nullable LatLng latLng)
            {
                displayMarkerOnMaps(latLng);
            }
        });
    }

    public void displayMarkerOnMaps(LatLng latLng)
    {
        googleMap.addMarker(new MarkerOptions().position(latLng).title("Appointment Marker"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
    }

}