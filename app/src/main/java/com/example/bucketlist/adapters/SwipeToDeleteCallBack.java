package com.example.bucketlist.adapters;

import android.util.Log;

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
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        itemAdapter.deleteItem(position,  viewHolder , new OnItemDelete() {
            @Override
            public void refreshFragment() {
                itemAdapter.notifyDataSetChanged();
                itemDeleteFireBase.onItemDelete();
            }
        });

    }
}
