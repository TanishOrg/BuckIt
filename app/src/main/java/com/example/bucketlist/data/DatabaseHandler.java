package com.example.bucketlist.data;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;


import com.example.bucketlist.constants.Constants;
import com.example.bucketlist.model.BucketItems;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String TAG = "Database Handler";
    private final Context context;

    public DatabaseHandler(@Nullable Context context) {
        super(context, Constants.DB_NAME,null, Constants.DB_VERSION);
        this.context  = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE " + Constants.TABLE_NAME + "("
                + Constants.KEY_ID + " INTEGER PRIMARY KEY,"
                + Constants.KEY_ITEM_TITLE + " TEXT,"
                + Constants.KEY_ITEM_INFO + " TEXT,"
                + Constants.KEY_ITEM_CATEGORY + " TEXT,"
                + Constants.KEY_ITEM_ACHIEVED + " BOOLEAN,"
                + Constants.KEY_DATE_NAME + " LONG,"
                + Constants.KEY_ITEM_VISIBILITY + " BOOLEAN,"
                + Constants.KEY_DATA_DEADLINE + " STRING"
                + ")";

        sqLiteDatabase.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+Constants.TABLE_NAME);
        Log.d(TAG, "onUpgrade: ");

        onCreate(sqLiteDatabase);

    }

    //crud operation
    public void addItem(BucketItems items) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_ITEM_TITLE, items.getTitle());
        values.put(Constants.KEY_ITEM_CATEGORY, items.getCategory());
        values.put(Constants.KEY_ITEM_INFO,items.getInfo());
        values.put(Constants.KEY_ITEM_ACHIEVED, items.isAchieved());
        values.put(Constants.KEY_DATE_NAME, System.currentTimeMillis());
        values.put(Constants.KEY_ITEM_VISIBILITY,items.isPrivate());
        values.put(Constants.KEY_DATA_DEADLINE,items.getDeadline());


        db.insert(Constants.TABLE_NAME,null,values);
        Log.d(TAG, "addItem: "
                + items.toString());
        Log.d(TAG, "addItem: "  + items.isPrivate());
    }

    public List<BucketItems> getAllItems() {
        SQLiteDatabase db = this.getReadableDatabase();

        List<BucketItems> items = new ArrayList<>();

        Cursor cursor = db.query(
                Constants.TABLE_NAME,
                new String[] {Constants.KEY_ID,
                        Constants.KEY_ITEM_TITLE,
                        Constants.KEY_ITEM_ACHIEVED,
                        Constants.KEY_ITEM_INFO,
                        Constants.KEY_ITEM_CATEGORY,
                        Constants.KEY_DATE_NAME,
                        Constants.KEY_DATA_DEADLINE,
                        Constants.KEY_ITEM_VISIBILITY,
                },
                null,
                null,
                null,
                null,
                Constants.KEY_DATE_NAME + " DESC"
        );

        if (cursor.moveToFirst()) {
            do {
                DateFormat dateFormat = DateFormat.getDateInstance();
                String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME))).getTime());

                BucketItems item = this.getNewItem(cursor);
                items.add(item);
            }while (cursor.moveToNext());
        }
        return items;
    }

    private BucketItems getNewItem(Cursor cursor) {

        DateFormat dateFormat = DateFormat.getDateInstance();
        String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME))).getTime());

        Log.d(TAG, "getNewItem: "+ cursor.getString(cursor.getColumnIndex(Constants.KEY_ITEM_VISIBILITY)));
        return new BucketItems(
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))),
                cursor.getString(cursor.getColumnIndex(Constants.KEY_ITEM_CATEGORY)),
                cursor.getString(cursor.getColumnIndex(Constants.KEY_ITEM_INFO)),
                cursor.getString(cursor.getColumnIndex(Constants.KEY_ITEM_TITLE)),
                (Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ITEM_VISIBILITY)))) == 0 ? false: true,
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ITEM_ACHIEVED))) == 0? false: true,
                formattedDate,
                cursor.getString(cursor.getColumnIndex(Constants.KEY_DATA_DEADLINE))
        );

    }

    //TODO Add update items
    //TODO Add Delete items
    //TODO getItemCount

    public int getItemsCount() {
        String countQuery = "SELECT * FROM " + Constants.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery,null);

        return cursor.getCount();
    }

    public void deleteItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(
                Constants.TABLE_NAME,
                Constants.KEY_ID + "=?",
                new String[]{String.valueOf(id)}
        );

        db.close();
    }
    public void updateItem(BucketItems item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_ITEM_TITLE, item.getTitle());
        values.put(Constants.KEY_ITEM_INFO,item.getInfo());
        values.put(Constants.KEY_ITEM_CATEGORY, item.getCategory());
        values.put(Constants.KEY_ITEM_ACHIEVED, item.isAchieved());
        values.put(Constants.KEY_DATE_NAME, System.currentTimeMillis());
        //update item
        db.update(Constants.TABLE_NAME,values,
                Constants.KEY_ID + "=?",
                new String[]{String.valueOf(item.getId())}
        );

    }

    public void updateCategory(int id, boolean marked) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_ITEM_ACHIEVED, marked);

        db.update(Constants.TABLE_NAME,
                values,
                Constants.KEY_ID + "=?",
                new String[]{String.valueOf(id)}
        );
    }
}
