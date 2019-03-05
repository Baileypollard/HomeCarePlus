package com.homecareplus.app.homecareplus.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.homecareplus.app.homecareplus.R;

public class AppointmentHeaderViewHolder extends RecyclerView.ViewHolder
{
    private TextView headerTitle;

    public AppointmentHeaderViewHolder(View view)
    {
        super(view);

        headerTitle = view.findViewById(R.id.headerDateTextView);
    }


    public void setHeaderTitle(String name)
    {
        headerTitle.setText(name);
    }

}