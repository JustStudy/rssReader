package com.example.likeRSS.fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.likeRSS.DBContentProvider;
import com.example.likeRSS.R;
import com.example.likeRSS.RssDBHelper;
import com.example.likeRSS.RssSingleItem;
import com.example.likeRSS.activities.FavoriteNewsActivity;
import com.example.likeRSS.activities.RssItemDisplayActivity;
import com.example.likeRSS.forNet.MyAsyncTask;
import com.example.likeRSS.forNet.MySimpleService;
import com.example.likeRSS.vkSources.MainActivity;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class RSSListNewsFragment extends Fragment {

    SQLiteDatabase db;
    Intent intent;
    String[] RSSItemsTitlesArray;
    ListView RSSList = null;
    public static ArrayList<RssSingleItem> rssItems = new ArrayList<RssSingleItem>();
    String Url = "http://newsru.com/plain/rss/txt_all.xml";
    private Menu menuActionBar;
    int icon_id = 0;
    Cursor cursor = null;
    RssDBHelper rssDBHelper;
    private int positionListItem = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        getActivity().getMenuInflater().inflate(R.menu.action_bar_menu, menu);

        if ((getResources().getBoolean(R.bool.tablet)) && (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)) {
            rssDBHelper = new RssDBHelper(getActivity());
            db = rssDBHelper.getReadableDatabase();
            // String k = rssItems.get(0).getTitle();

            String[] fromArr = {RssDBHelper.DB_COLUMN_TITLE};
            cursor = db.query(RssDBHelper.DB_TABLE, fromArr, null, null, null, null, null);

            if (cursor.moveToFirst()) {
                int indexTitle = cursor.getColumnIndex(RssDBHelper.DB_COLUMN_TITLE);


                do {

                    if (cursor.getString(indexTitle).equals(rssItems.get(0).getTitle())) {
                        icon_id = R.drawable.add_db;
                        break;
                    } else icon_id = R.drawable.del_db;
                } while (cursor.moveToNext());
            } else icon_id = R.drawable.del_db;
            cursor.close();
            menu.findItem(R.id.like_unlike_new).setIcon(icon_id);
            menu.getItem(2).setIcon(icon_id);


        }


        menuActionBar = menu;
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.launch_facebook:
                startActivity(new Intent(getActivity(), MainActivity.class));

                return true;

            case R.id.start_service:
                getActivity().startService(new Intent(getActivity(), MySimpleService.class).putExtra("Title item", rssItems.get(0).getTitle()));

                return true;
            case R.id.stop_service:
                getActivity().stopService(new Intent(getActivity(), MySimpleService.class));

                return true;
            case R.id.go_to_db_news:
                startActivity(new Intent(getActivity(), FavoriteNewsActivity.class));
                return true;
            case R.id.like_unlike_new: {
                rssDBHelper = new RssDBHelper(getActivity());

                ContentValues contentValues = new ContentValues();
                if (icon_id == R.drawable.del_db) {
                    String dbItemTitle = rssItems.get(positionListItem).getTitle();
                    contentValues.put(RssDBHelper.DB_COLUMN_TITLE, dbItemTitle);

                    String desc = rssItems.get(positionListItem).getDescription();

                    contentValues.put(RssDBHelper.DB_COLUMN_TEXT_NEW, desc);
                    String category = rssItems.get(positionListItem).getCategory();
                    contentValues.put(RssDBHelper.DB_COLUMN_CATEGORY, category);
                    getActivity().getContentResolver().insert(DBContentProvider.DB_CONTENT_URI, contentValues);

                    icon_id = R.drawable.add_db;
                    item.setIcon(icon_id);

                } else {
                    String controlTitle = rssItems.get(positionListItem).getTitle();
                    String[] arrayColumns = {RssDBHelper.DB_COLUMN_ID, RssDBHelper.DB_COLUMN_TITLE};
                    cursor = db.query(RssDBHelper.DB_TABLE, arrayColumns, null, null, null, null, null);
                    if (cursor.moveToFirst()) {
                        int indexTitle = cursor.getColumnIndex(RssDBHelper.DB_COLUMN_TITLE);
                        do {

                            if (cursor.getString(indexTitle).equals(controlTitle)) {
                                int columnIndex = cursor.getColumnIndex(RssDBHelper.DB_COLUMN_ID);
                                int del_id = cursor.getInt(columnIndex);
                                Uri uri = Uri.parse(DBContentProvider.DB_CONTENT_URI + "/"
                                        + del_id);
                                getActivity().getContentResolver().delete(uri, null, null);
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
        return super.onOptionsItemSelected(item);
    }


    public void takeRSSHTML() throws ExecutionException, InterruptedException {
        rssItems = new MyAsyncTask().execute(Url).get();
    }


    void transformForItem(ArrayList<RssSingleItem> c) {
        RSSItemsTitlesArray = new String[c.size()];
        for (int i = 0; i < c.size(); i++) {
            RSSItemsTitlesArray[i] = c.get(i).getTitle();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewRSSListNewsFragment = inflater.inflate(R.layout.rsslist, container, false);

        RSSList = (ListView) viewRSSListNewsFragment.findViewById(R.id.rssListView);
        try {
            takeRSSHTML();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        transformForItem(rssItems);
        if ((getResources().getBoolean(R.bool.tablet)) && (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)) {
            firstInit();
        }

        RSSList.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.onerssitem, RSSItemsTitlesArray));
        RSSList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if ((getResources().getBoolean(R.bool.tablet)) && (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)) {
                    getActivity().getIntent().putExtra("rssItem", transformToString(rssItems.get(position)));
                    positionListItem = position;
                    getActivity().setTitle(rssItems.get(position).getTitle());
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.containerForDetailNew, new RSSItemDisplayerFragment()).commit();

                    Cursor cursor = null;
                    String controlTitle = rssItems.get(position).getTitle();

                    String[] arrayColumn = {RssDBHelper.DB_COLUMN_TITLE};
                    cursor = getActivity().getContentResolver().query(DBContentProvider.DB_CONTENT_URI, arrayColumn, null, null, null);

                    // cursor=db.rawQuery("SELECT title FROM rssnewstable");
                    if (cursor.moveToFirst()) {
                        int columnIndex = cursor.getColumnIndex(RssDBHelper.DB_COLUMN_TITLE);


                        do {
                            if (cursor.getString(columnIndex).equals(controlTitle)) {
                                icon_id = R.drawable.add_db;
                                break;
                            } else icon_id = R.drawable.del_db;
                        } while (cursor.moveToNext());
                    } else icon_id = R.drawable.del_db;

                    cursor.close();
                    getActivity().getIntent().putExtra("id icon", icon_id);
                    menuActionBar.findItem(R.id.like_unlike_new).setIcon(icon_id);

                } else {
                    intent = new Intent(getActivity(), RssItemDisplayActivity.class);
                    intent.putExtra("rssItem", transformToString(rssItems.get(position)));
                    getActivity().startActivity(intent);
                }
            }
        });
        setHasOptionsMenu(true);
        return viewRSSListNewsFragment;
    }

    private String[] transformToString(RssSingleItem rssSingleItem) {
        String[] strings = new String[]{rssSingleItem.getTitle(), rssSingleItem.getDescription(), rssSingleItem.getCategory()};
        return strings;
    }

    private void firstInit() {
        getActivity().getIntent().putExtra("rssItem", transformToString(rssItems.get(0)));
        getActivity().setTitle(rssItems.get(0).getTitle());
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.containerForDetailNew, new RSSItemDisplayerFragment()).commit();
    }


}
