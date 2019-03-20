package com.homecareplus.app.homecareplus.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.homecareplus.app.homecareplus.R;
import com.homecareplus.app.homecareplus.model.Client;
import com.homecareplus.app.homecareplus.viewmodel.ClientInformationViewModel;

public class ClientInformationFragment extends Fragment
{
    private Client client;
    private TextView clientNameTextView;
    private TextView clientAddressTextView;
    private TextView clientPhoneNumberTextView;
    private TextView clientGenderTextView;
    private TextView clientAdditionalInfoTextView;
    private TextView clientEmergenceyContactTextView;
    private TextView clientEmergenceyContactNumbTextView;
    private TextView clientHealthCardTextView;

    private ClientInformationViewModel viewModel;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ClientInformationActivity activity = (ClientInformationActivity) getActivity();
        if (activity != null)
        {
            this.client = activity.getClient();
        }
        return inflater.inflate(R.layout.fragment_client_info, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        viewModel = ViewModelProviders.of(this).get(ClientInformationViewModel.class);
        viewModel.init(client);

        clientNameTextView = view.findViewById(R.id.clientNameTextView);
        clientAddressTextView = view.findViewById(R.id.clientAddressTextView);
        clientPhoneNumberTextView = view.findViewById(R.id.clientPhoneNumberTextView);
        clientGenderTextView = view.findViewById(R.id.clientGenderTextView);
        clientAdditionalInfoTextView = view.findViewById(R.id.clientAdditionalIInfoTextView);
        clientEmergenceyContactTextView = view.findViewById(R.id.clientEmergencyContactName);
        clientEmergenceyContactNumbTextView = view.findViewById(R.id.clientEmergencyContactNumber);
        clientHealthCardTextView = view.findViewById(R.id.clientHealthCardTextView);

        viewModel.getClientData().observe(this, new Observer<Client>()
        {
            @Override
            public void onChanged(Client client)
            {
                setClientInformation(client);
            }
        });

    }

    private void setClientInformation(Client client)
    {
        clientNameTextView.setText(client.getFullName());
        clientAddressTextView.setText(client.getAddress());
        clientPhoneNumberTextView.setText(client.getPhoneNumber());
        clientGenderTextView.setText(client.getGender());
        clientAdditionalInfoTextView.setText(client.getAdditionalInfo());
        clientHealthCardTextView.setText(client.getHealthCardNumber());
        clientEmergenceyContactTextView.setText(client.getEmergencyContactName());
        clientEmergenceyContactNumbTextView.setText(client.getEmergencyContactNumber());
    }


    @Override
    public void onResume()
    {
        super.onResume();
    }

}
