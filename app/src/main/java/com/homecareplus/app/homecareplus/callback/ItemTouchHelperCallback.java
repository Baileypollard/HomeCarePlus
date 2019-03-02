package com.homecareplus.app.homecareplus.callback;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import com.homecareplus.app.homecareplus.viewholder.AppointmentHeaderViewHolder;
import com.homecareplus.app.homecareplus.viewholder.AppointmentRowViewHolder;
import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;

public class ItemTouchHelperCallback extends ItemTouchHelperExtension.Callback
{

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder)
    {
        if (viewHolder instanceof AppointmentHeaderViewHolder)
        {
            return 0;
        }
        return makeMovementFlags(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target)
    {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction)
    {

    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive)
    {
        if (viewHolder instanceof AppointmentRowViewHolder)
        {
            ((AppointmentRowViewHolder) viewHolder).getAppointmentRow().setTranslationX(dX);
        }
    }
}