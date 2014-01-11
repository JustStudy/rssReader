package com.example.likeRSS.twitter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DatabaseMgr {

    private static final String TAG = "DatabaseMgr";

    private static DatabaseMgr instance = null;

    private SQLiteDatabase db = null;

    public static synchronized DatabaseMgr getInstance() {
        if (instance == null)
            instance = new DatabaseMgr();
        return instance;
    }

    private DatabaseMgr() {
        //
    }

    public void connect(Context context) {
        if (db != null)
            return;
        db = (new CacheDbHelper(context)).getWritableDatabase();
    }

    public byte[] getCachedImage(String url) {
        if (url == null)
            return null;
        //
        byte[] result = null;
        Cursor c = db.rawQuery("select image from image_cache where (url = ?)",
                new String[]{url});
        if (c != null) {
            if (c.getCount() > 0) {
                c.moveToFirst();
                result = c.getBlob(0);
            }
            c.close();
        }
        return result;
    }

    public boolean isCachedImageExists(String url) {
        if (url == null)
            return false;
        //
        boolean result = false;
        Cursor c = db.rawQuery(
                "select count(*) from image_cache where (url = ?)",
                new String[]{url});
        if (c != null) {
            if (c.getCount() > 0) {
                c.moveToFirst();
                result = c.getInt(0) > 0;
            }
            c.close();
        }
        return result;
    }

    public void putCachedImage(String url, byte[] data) {
        if (url == null || isCachedImageExists(url))
            return;
        if (data == null)
            return;
        //
        ContentValues cv = new ContentValues();
        cv.put("url", url);
        cv.put("image", data);
        db.insert("image_cache", null, cv);
        Log.d(TAG, "Image added to cache [" + url + "]");
    }
}
