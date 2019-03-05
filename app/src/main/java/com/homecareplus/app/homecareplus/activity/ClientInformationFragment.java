package com.homecareplus.app.homecareplus.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.homecareplus.app.homecareplus.R;
import com.homecareplus.app.homecareplus.model.Client;

public class ClientInformationFragment extends Fragment
{
    private Client client;

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

    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

}
