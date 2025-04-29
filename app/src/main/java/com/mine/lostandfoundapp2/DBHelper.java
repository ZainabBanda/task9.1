package com.mine.lostandfoundapp2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME    = "LostAndFound.db";
    private static final int    DATABASE_VERSION = 3;

    private static final String TABLE_NAME = "items";
    private static final String COL_ID     = "ID";
    private static final String COL_NAME   = "ITEM_NAME";
    private static final String COL_DESC   = "DESCRIPTION";
    private static final String COL_DATE   = "DATE_REPORTED";
    private static final String COL_LOC    = "LOCATION";
    private static final String COL_PHONE  = "PHONE";
    private static final String COL_STATUS = "STATUS";
    private static final String COL_LAT    = "LATITUDE";
    private static final String COL_LNG    = "LONGITUDE";

    /** Constructor */
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
            "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID     + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME   + " TEXT, " +
                COL_DESC   + " TEXT, " +
                COL_DATE   + " TEXT, " +
                COL_LOC    + " TEXT, " +
                COL_PHONE  + " TEXT, " +
                COL_STATUS + " TEXT, " +
                COL_LAT    + " REAL, " +
                COL_LNG    + " REAL" +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        if (oldV < 2) {
            db.execSQL("ALTER TABLE " + TABLE_NAME +
                " ADD COLUMN " + COL_PHONE + " TEXT");
        }
        if (oldV < 3) {
            db.execSQL("ALTER TABLE " + TABLE_NAME +
                " ADD COLUMN " + COL_LAT + " REAL");
            db.execSQL("ALTER TABLE " + TABLE_NAME +
                " ADD COLUMN " + COL_LNG + " REAL");
        }
    }

    /** Insert with geo */
    public boolean insertItem(String name,
                              String desc,
                              String date,
                              String loc,
                              String phone,
                              String status,
                              double latitude,
                              double longitude) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME,   name);
        cv.put(COL_DESC,   desc);
        cv.put(COL_DATE,   date);
        cv.put(COL_LOC,    loc);
        cv.put(COL_PHONE,  phone);
        cv.put(COL_STATUS, status);
        cv.put(COL_LAT,    latitude);
        cv.put(COL_LNG,    longitude);
        long id = db.insert(TABLE_NAME, null, cv);
        return id != -1;
    }

    /** Fetch all rows */
    public List<Item> getAllItems() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(
            TABLE_NAME, null, null, null, null, null,
            COL_ID + " DESC"
        );
        List<Item> out = new ArrayList<>();
        while (c.moveToNext()) {
            out.add(new Item(
                c.getInt   (c.getColumnIndexOrThrow(COL_ID)),
                c.getString(c.getColumnIndexOrThrow(COL_NAME)),
                c.getString(c.getColumnIndexOrThrow(COL_DESC)),
                c.getString(c.getColumnIndexOrThrow(COL_DATE)),
                c.getString(c.getColumnIndexOrThrow(COL_LOC)),
                c.getString(c.getColumnIndexOrThrow(COL_PHONE)),
                c.getString(c.getColumnIndexOrThrow(COL_STATUS)),
                c.getDouble(c.getColumnIndexOrThrow(COL_LAT)),
                c.getDouble(c.getColumnIndexOrThrow(COL_LNG))
            ));
        }
        c.close();
        return out;
    }

    /** Fetch one by ID */
    public Item getItemById(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(
            TABLE_NAME, null,
            COL_ID + " = ?",
            new String[]{ String.valueOf(id) },
            null, null, null
        );
        Item item = null;
        if (c.moveToFirst()) {
            item = new Item(
                c.getInt   (c.getColumnIndexOrThrow(COL_ID)),
                c.getString(c.getColumnIndexOrThrow(COL_NAME)),
                c.getString(c.getColumnIndexOrThrow(COL_DESC)),
                c.getString(c.getColumnIndexOrThrow(COL_DATE)),
                c.getString(c.getColumnIndexOrThrow(COL_LOC)),
                c.getString(c.getColumnIndexOrThrow(COL_PHONE)),
                c.getString(c.getColumnIndexOrThrow(COL_STATUS)),
                c.getDouble(c.getColumnIndexOrThrow(COL_LAT)),
                c.getDouble(c.getColumnIndexOrThrow(COL_LNG))
            );
        }
        c.close();
        return item;
    }

    /** Delete by ID */
    public boolean removeItem(int id) {
        return getWritableDatabase()
            .delete(TABLE_NAME, COL_ID + "=?",
                new String[]{ String.valueOf(id) }) > 0;
    }
}
