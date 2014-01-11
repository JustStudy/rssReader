package com.example.likeRSS;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created with IntelliJ IDEA.
 * User: Ruslik
 * Date: 22.11.13
 * Time: 13:04
 * To change this template use File | Settings | File Templates.
 */
public class RssDBHelper extends SQLiteOpenHelper {
    private static String RSS_DATA_BASE_NAME = "DataBaseName.db";
    private static int DATA_BASE_VERSION = 1;
    public static final String DB_COLUMN_ID = "id";
    public static final String DB_COLUMN_TEXT_NEW = "description";
    public static final String DB_COLUMN_CATEGORY = "category";
    public static String DB_TABLE = "rssnewstable";
    public static final String DB_COLUMN_TITLE = "title";

    public RssDBHelper(Context context) {
        super(context, RSS_DATA_BASE_NAME, null, DATA_BASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + DB_TABLE + "(" + DB_COLUMN_ID + " integer primary key autoincrement, " + DB_COLUMN_TITLE + " text," + DB_COLUMN_TEXT_NEW + " text, " + DB_COLUMN_CATEGORY + " text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
