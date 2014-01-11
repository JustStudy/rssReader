package com.example.likeRSS.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.likeRSS.DBContentProvider;
import com.example.likeRSS.R;
import com.example.likeRSS.RssDBHelper;
import com.example.likeRSS.RssSingleItem;
import com.example.likeRSS.activities.FavoriteNewItemActivity;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Ruslik
 * Date: 24.11.13
 * Time: 9:58
 * To change this template use File | Settings | File Templates.
 */
public class FavoriteNewsFragment extends Fragment {
    RssDBHelper rssDBHelper;

    ListView favoriteNewsList = null;
    ArrayList<RssSingleItem> FavoriteNewsItems = new ArrayList<RssSingleItem>();
    ArrayList<String> newsDBList = new ArrayList<String>();
    ArrayList<Integer> arrayListInt = new ArrayList<Integer>();
    ArrayList<RssSingleItem> justRssDBArray = new ArrayList<RssSingleItem>();
    Cursor cursor = null;
    Intent intent;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View viewFavoriteNewsFragment = inflater.inflate(R.layout.rss_list_db, container, false);

        favoriteNewsList = (ListView) viewFavoriteNewsFragment.findViewById(R.id.rssListViewDB);

        rssDBHelper = new RssDBHelper(getActivity().getApplicationContext());
        SQLiteDatabase db = rssDBHelper.getReadableDatabase();

        String[] titleArr = {RssDBHelper.DB_COLUMN_ID, RssDBHelper.DB_COLUMN_TITLE};

        cursor = getActivity().getContentResolver().query(DBContentProvider.DB_CONTENT_URI, null, null, null, null);

        if (cursor.moveToFirst()) {

            do {
                justRssDBArray.add(new RssSingleItem(cursor.getString(1), cursor.getString(2), cursor.getString(3)));
                newsDBList.add(cursor.getString(1));
                arrayListInt.add(cursor.getInt(0));

                cursor.getString(cursor.getColumnIndex(RssDBHelper.DB_COLUMN_TITLE));

            } while (cursor.moveToNext());
        }
        if ((getResources().getBoolean(R.bool.tablet)) && (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)) {
            getActivity().getIntent().putExtra("favorite_title", justRssDBArray.get(0).getTitle());
            getActivity().getIntent().putExtra("favorite_description", justRssDBArray.get(0).getDescription());
            getActivity().getIntent().putExtra("favorite_category", justRssDBArray.get(0).getCategory());
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.containerForDetailNewDB, new FavoriteItemNewsFragment()).commit();
        }
        cursor.close();
        db.close();

        favoriteNewsList.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.onerssitem, newsDBList));
        favoriteNewsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ((getResources().getBoolean(R.bool.tablet)) && (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)) {

                    getActivity().getIntent().putExtra("favorite_title", justRssDBArray.get(position).getTitle());
                    getActivity().getIntent().putExtra("favorite_description", justRssDBArray.get(position).getDescription());
                    getActivity().getIntent().putExtra("favorite_category", justRssDBArray.get(position).getCategory());

                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.containerForDetailNewDB, new FavoriteItemNewsFragment()).commit();
                } else {
                    intent = new Intent(getActivity(), FavoriteNewItemActivity.class);
                    intent.putExtra("favorite_title", justRssDBArray.get(position).getTitle());
                    intent.putExtra("favorite_description", justRssDBArray.get(position).getDescription());
                    intent.putExtra("favorite_category", justRssDBArray.get(position).getCategory());
                    getActivity().finish();
                    startActivity(intent);
                }
            }
        });
        return viewFavoriteNewsFragment;
    }


}
