package com.example.likeRSS.twitter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CacheDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mivotvcache.db";
    private static final int DATABASE_VERSION = 1;

    public CacheDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table image_cache (id integer primary key, url string not null, image blob)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        if (newV > oldV) {
            db.execSQL("drop table if exists image_cache");
            onCreate(db);
        }
    }
}
