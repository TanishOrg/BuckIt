package com.example.bucketlist.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.example.bucketlist.constants.Constants;

@Entity(tableName = Constants.TABLE_BUCKET_LIST)
public class BucketList {
    @NonNull
    @ColumnInfo(name = Constants.bucket_list_item_name)
    private String itemName;

    private int id;

    private String dateItemAdded;

    private BucketList() {}




}
