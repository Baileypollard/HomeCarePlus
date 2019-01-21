package com.homecareplus.app.homecareplus.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class AppointmentHeaderViewHolder extends RecyclerView.ViewHolder
{
    private TextView headerTitle;

    public AppointmentHeaderViewHolder(View view)
    {
        super(view);

        headerTitle = view.findViewById(com.homecareplus.app.homecareplus.R.id.headerDateTextView);
    }


    public void setHeaderTitle(String name)
    {
        headerTitle.setText(name);
    }

}
