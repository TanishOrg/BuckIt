package com.example.bucketlist.model;

import android.app.Dialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bucketlist.R;

/**
 * Abstract class for Bucket Item adapters
 * @param <ViewHolder>
 */

public abstract class ItemAdapter<ViewHolder extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<ViewHolder> {

    protected abstract void updateData(BucketItems items, final ViewHolder holder, final int position);
    protected abstract void bindHolder(final ViewHolder holder, final BucketItems items, final int position);

    public abstract void deleteItem(int position, ViewHolder viewHolder, OnItemDelete onItemDelete);

    public abstract void moveItem(int position, ViewHolder viewHolder);
}
