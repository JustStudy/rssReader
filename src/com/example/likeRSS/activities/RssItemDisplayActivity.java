package com.example.likeRSS.activities;


import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import com.example.likeRSS.DBContentProvider;
import com.example.likeRSS.R;
import com.example.likeRSS.RssDBHelper;
import com.example.likeRSS.fragments.RSSItemDisplayerFragment;
import com.example.likeRSS.twitter.TweetChatActivity;
import com.example.likeRSS.vkSources.MainActivity;

/**
 * Created with IntelliJ IDEA.
 * User: Ruslik
 * Date: 11.11.13
 * Time: 12:45
 * To change this template use File | Settings | File Templates.
 */
public class RssItemDisplayActivity extends ActionBarActivity {
    Menu RssItemDisplayMenu;
    Intent intent;
    RSSItemDisplayerFragment rssItemDisplayerFragment;
    FragmentTransaction fragmentTransaction;
    RssDBHelper rssDBHelper;
    SQLiteDatabase db;
    int icon_id;
    Cursor cursor = null;
    String[] oneNewsItem;


    private void shareOnSocialNet(int idButton) {
        SharedPreferences sharedPreferences = getSharedPreferences("shareOnVk", MODE_WORLD_WRITEABLE);
        if (idButton == R.id.shareOnFacebook) {
            sharedPreferences.edit().putString("news", oneNewsItem[1]).commit();
            //startActivity(new Intent(this, FacebookActivity.class));
            startActivity(new Intent(this, FacebookStartActivity.class));
        }

        if (idButton == R.id.shareOnTwitter) {
            sharedPreferences.edit().putString("news", oneNewsItem[0]).commit();
            startActivity(new Intent(this, TweetChatActivity.class));
        }

        if (idButton == R.id.shareOnVK) {
            sharedPreferences.edit().putString("news", oneNewsItem[1]).commit();
            startActivity(new Intent(this, MainActivity.class));
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rssDBHelper = new RssDBHelper(this);
        oneNewsItem = getIntent().getStringArrayExtra("rssItem");


        if ((getResources().getBoolean(R.bool.tablet)) && (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)) {
            intent = new Intent(this, RSSListActivity.class);
            startActivity(intent);
        }
        setContentView(R.layout.initlayout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        rssItemDisplayerFragment = new RSSItemDisplayerFragment();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.containerFrame, rssItemDisplayerFragment).commit();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.db_menu, menu);
        RssItemDisplayMenu = menu;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                db = rssDBHelper.getReadableDatabase();
                String controlTitle = oneNewsItem[0];
                String[] selectionColumns = {RssDBHelper.DB_COLUMN_TITLE};

                cursor = getContentResolver().query(DBContentProvider.DB_CONTENT_URI, selectionColumns, null, null, null);

                if (cursor.moveToFirst()) {
                    int indexTitle = cursor.getColumnIndex(RssDBHelper.DB_COLUMN_TITLE);

                    do {

                        if (cursor.getString(indexTitle).equals(controlTitle)) {
                            icon_id = R.drawable.add_db;
                            break;
                        } else icon_id = R.drawable.del_db;
                    } while (cursor.moveToNext());
                } else icon_id = R.drawable.del_db;
                cursor.close();

                RssItemDisplayMenu.getItem(0).setIcon(icon_id);
                db.close();
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        SQLiteDatabase db = rssDBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        if ((item.getItemId() == R.id.shareOnFacebook) | (item.getItemId() == R.id.shareOnTwitter) |
                (item.getItemId() == R.id.shareOnVK)) {
            shareOnSocialNet(item.getItemId());
        }
        switch (item.getItemId()) {

            case android.R.id.home: {
                finish();
                return true;
            }
            case R.id.like_unlike_new: {
                if (icon_id == R.drawable.del_db) {
                    contentValues.put("title", oneNewsItem[0]);
                    contentValues.put("description", oneNewsItem[1]);
                    contentValues.put("category", oneNewsItem[2]);
                    db.insert(RssDBHelper.DB_TABLE, null, contentValues);
                    icon_id = R.drawable.add_db;
                    item.setIcon(icon_id);
                    cursor.close();
                } else {

                    String[] arrayForSelection = {RssDBHelper.DB_COLUMN_ID, RssDBHelper.DB_COLUMN_TITLE};
                    cursor = db.query(RssDBHelper.DB_TABLE, arrayForSelection, null, null, null, null, null);
                    if (cursor.moveToFirst()) {
                        int f = cursor.getColumnIndex(RssDBHelper.DB_COLUMN_TITLE);
                        do {
                            if (cursor.getString(f).equals(oneNewsItem[0])) {
                                db.delete(RssDBHelper.DB_TABLE, "id = " + cursor.getInt(0), null);
                                icon_id = R.drawable.del_db;
                                item.setIcon(icon_id);
                                break;
                            }
                        } while (cursor.moveToNext());
                    }
                    cursor.close();
                }
                return true;
            }
        }
        db.close();
        return super.onOptionsItemSelected(item);
    }
}
