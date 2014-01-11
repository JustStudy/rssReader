package com.example.likeRSS;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.example.likeRSS.activities.RSSListActivity;

/**
 * Created with IntelliJ IDEA.
 * User: Ruslik
 * Date: 27.11.13
 * Time: 22:20
 * To change this template use File | Settings | File Templates.
 */
public class DBContentProvider extends ContentProvider {
    RssDBHelper rssDBHelper;
    SQLiteDatabase db;
    static final String AUTHORITY = "com.example.likeRSS.DBContentProvider";
    static final String TABLE_PATH = "original";
    static final int URI_ON_ID = 10;
    static final int URI_ALL_TABLE = 1;
    public static final Uri DB_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + TABLE_PATH);

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);


    static {


        uriMatcher.addURI(AUTHORITY, TABLE_PATH, URI_ALL_TABLE);
        uriMatcher.addURI(AUTHORITY, TABLE_PATH + "/#", URI_ON_ID);

    }


    @Override
    public boolean onCreate() {
        Log.d(RSSListActivity.LOG_ID, "on Create");
        rssDBHelper = new RssDBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        try {
            db = rssDBHelper.getWritableDatabase();
        } catch (SQLiteException ex) {
            db = rssDBHelper.getReadableDatabase();
        }
        int uriType = uriMatcher.match(uri);
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        switch (uriType) {
            case URI_ALL_TABLE:
                break;
            case URI_ON_ID:
                // adding the ID to the original query
                queryBuilder.appendWhere(RssDBHelper.DB_COLUMN_ID + "="
                        + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        // checkColumns(projection);
        Cursor cursor = null;
        queryBuilder.setTables(RssDBHelper.DB_TABLE);
        cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        //  String ret = getContext().getContentResolver().getType(TAKE_FOR_ID);
        //  Log.e(RSSListActivity.LOG_ID,"ret value = "+ret);
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = uriMatcher.match(uri);
        db = rssDBHelper.getWritableDatabase();
        int rowsDeleted = 0;
        long id = 0;
        switch (uriType) {
            case URI_ALL_TABLE:
                id = db.insert(RssDBHelper.DB_TABLE, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return Uri.parse(AUTHORITY + "/" + TABLE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = uriMatcher.match(uri);
        db = rssDBHelper.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
            case URI_ALL_TABLE:
                rowsDeleted = db.delete(RssDBHelper.DB_TABLE, selection,
                        selectionArgs);
                break;
            case URI_ON_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = db.delete(RssDBHelper.DB_TABLE,
                            RssDBHelper.DB_COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = db.delete(RssDBHelper.DB_TABLE,
                            RssDBHelper.DB_COLUMN_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int uriType = uriMatcher.match(uri);
        db = rssDBHelper.getWritableDatabase();
        int rowsUpdated = 0;
        switch (uriType) {
            case URI_ALL_TABLE:
                rowsUpdated = db.update(RssDBHelper.DB_TABLE,
                        values,
                        selection,
                        selectionArgs);
                break;
            case URI_ON_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = db.update(RssDBHelper.DB_TABLE,
                            values,
                            RssDBHelper.DB_COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = db.update(RssDBHelper.DB_TABLE,
                            values,
                            RssDBHelper.DB_COLUMN_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
