package com.example.bucketlist.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.bucketlist.constants.Constants;
import com.example.bucketlist.model.BucketList;

import static android.content.ContentValues.TAG;

public class DatabaseHandler extends SQLiteOpenHelper {

    private Context context;

    public DatabaseHandler(@Nullable Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE " + Constants.TABLE_NAME + "("
                + Constants.KEY_ID + " INTEGER PRIMARY KEY,"
                + Constants.KEY_CATEGORY + " TEXT,"
                + Constants.KEY_TITLE + " TEXT,"
                + Constants.KEY_MARKER + " BOOLEAN,"
                + Constants.KEY_DATE_NAME + " LONG"
                + ")";

        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+Constants.TABLE_NAME);

        onCreate(sqLiteDatabase);

    }

    public void addItem(BucketList items) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_CATEGORY, items.getCategory());
        values.put(Constants.KEY_TITLE, items.getTitle());
        values.put(Constants.KEY_DATE_NAME, java.lang.System.currentTimeMillis());
        //insert
        db.insert(Constants.TABLE_NAME,null,values);
        Log.d(TAG, "addItems: ");
    }
}
