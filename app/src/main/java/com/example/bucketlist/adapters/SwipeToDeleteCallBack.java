package com.example.bucketlist.adapters;

import android.graphics.Canvas;
import android.graphics.Path;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bucketlist.model.ItemAdapter;
import com.example.bucketlist.model.OnItemDelete;
import com.example.bucketlist.model.OnItemDeleteFireBase;

import static android.content.ContentValues.TAG;

public class SwipeToDeleteCallBack extends ItemTouchHelper.SimpleCallback {

    private ItemAdapter itemAdapter;
    private OnItemDeleteFireBase itemDeleteFireBase;

    public SwipeToDeleteCallBack(ItemAdapter adapter, OnItemDeleteFireBase itemDelete) {
        super(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT);
        this.itemAdapter = adapter;
        this.itemDeleteFireBase = itemDelete;
    }

    @Override
    public int getSwipeDirs(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return super.getSwipeDirs(recyclerView, viewHolder);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return true;
    }


    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        if (direction == ItemTouchHelper.LEFT)
        itemAdapter.deleteItem(position,  viewHolder , new OnItemDelete() {
            @Override
            public void refreshFragment() {
                itemAdapter.notifyDataSetChanged();
                itemDeleteFireBase.onItemDelete();
            }
        });
        else  itemAdapter.moveItem(position,viewHolder);

    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            if (viewHolder instanceof RecyclerAdapterAchieved.ViewHolder) {
                final View foregroundView = ((RecyclerAdapterAchieved.ViewHolder) viewHolder).viewForeground;
                getDefaultUIUtil().onSelected(foregroundView);
            } else if (viewHolder instanceof RecyclerAdapterDream.ContentViewHolder) {
                final View foregroundView = ((RecyclerAdapterDream.ContentViewHolder) viewHolder).viewForeground;
                getDefaultUIUtil().onSelected(foregroundView);
            }
        }
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
                                RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                int actionState, boolean isCurrentlyActive) {
        if (viewHolder instanceof RecyclerAdapterAchieved.ViewHolder) {
            final View foregroundView = ((RecyclerAdapterAchieved.ViewHolder) viewHolder).viewForeground;
            getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                    actionState, isCurrentlyActive);
        } else if (viewHolder instanceof RecyclerAdapterDream.ContentViewHolder) {
            final View foregroundView = ((RecyclerAdapterDream.ContentViewHolder) viewHolder).viewForeground;
            getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                    actionState, isCurrentlyActive);
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof RecyclerAdapterAchieved.ViewHolder) {
            final View foregroundView = ((RecyclerAdapterAchieved.ViewHolder) viewHolder).viewForeground;
            getDefaultUIUtil().clearView(foregroundView);
        } else if (viewHolder instanceof RecyclerAdapterDream.ContentViewHolder) {
            final View foregroundView = ((RecyclerAdapterDream.ContentViewHolder) viewHolder).viewForeground;
            getDefaultUIUtil().clearView(foregroundView);
        }
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        Log.d(TAG, "onChildDraw: " + dX);


        if (dX < 400 && dX > -400) {
            if (viewHolder instanceof RecyclerAdapterAchieved.ViewHolder) {
                final View foregroundView = ((RecyclerAdapterAchieved.ViewHolder) viewHolder).viewForeground;
                getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                        actionState, isCurrentlyActive);
            } else if (viewHolder instanceof RecyclerAdapterDream.ContentViewHolder) {
                final View foregroundView = ((RecyclerAdapterDream.ContentViewHolder) viewHolder).viewForeground;
                getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                        actionState, isCurrentlyActive);
            }
//            onSwiped(viewHolder,ItemTouchHelper.LEFT);

        }
    }

//    @Override
//    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//        listener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
//    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    public interface RecyclerItemTouchHelperListener {
        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
    }
}
