package com.example.likeRSS.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.TextView;
import com.example.likeRSS.DBContentProvider;
import com.example.likeRSS.R;
import com.example.likeRSS.RssDBHelper;
import com.example.likeRSS.RssSingleItem;
import com.example.likeRSS.activities.FavoriteNewsActivity;

/**
 * Created with IntelliJ IDEA.
 * User: Ruslik
 * Date: 24.11.13
 * Time: 17:23
 * To change this template use File | Settings | File Templates.
 */
public class FavoriteItemNewsFragment extends Fragment {
    private RssSingleItem oneRSSNewsItem;
    Cursor cursor;
    TextView titleTextView;
    TextView descriptionTextView;
    TextView categoryTextView;


    public void getSomeIntent() {
        oneRSSNewsItem = new RssSingleItem();
        oneRSSNewsItem.setTitle(getActivity().getIntent().getStringExtra("favorite_title"));
        oneRSSNewsItem.setDescription(getActivity().getIntent().getStringExtra("favorite_description"));
        oneRSSNewsItem.setCategory(getActivity().getIntent().getStringExtra("favorite_category"));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_del_db_favorite_new, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        RssDBHelper rssDBHelper = new RssDBHelper(getActivity());
        SQLiteDatabase db = rssDBHelper.getReadableDatabase();
        String[] arrayColumns = {RssDBHelper.DB_COLUMN_ID, RssDBHelper.DB_COLUMN_TITLE};
        cursor = db.query(RssDBHelper.DB_TABLE, arrayColumns, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int indexTitle = cursor.getColumnIndex(RssDBHelper.DB_COLUMN_TITLE);
            do {

                if (cursor.getString(indexTitle).equals(oneRSSNewsItem.getTitle())) {
                    int columnIndex = cursor.getColumnIndex(RssDBHelper.DB_COLUMN_ID);
                    int del_id = cursor.getInt(columnIndex);
                    Uri uri = Uri.parse(DBContentProvider.DB_CONTENT_URI + "/"
                            + del_id);
                    getActivity().getContentResolver().delete(uri, null, null);
                    break;
                }
            } while (cursor.moveToNext());
        }
        db.close();
        getActivity().finish();
        getActivity().startActivity(new Intent(getActivity(), FavoriteNewsActivity.class));
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View favoriteItemViewFragment = inflater.inflate(R.layout.rss_item_displayer_layout, container, false);

        titleTextView = (TextView) favoriteItemViewFragment.findViewById(R.id.titleRssItem);
        descriptionTextView = (TextView) favoriteItemViewFragment.findViewById(R.id.textRssItem);
        categoryTextView = (TextView) favoriteItemViewFragment.findViewById(R.id.categoryRssItem);

        getSomeIntent();


        titleTextView.setText(oneRSSNewsItem.getTitle());
        descriptionTextView.setText(oneRSSNewsItem.getDescription());
        categoryTextView.setText(oneRSSNewsItem.getCategory());
        return favoriteItemViewFragment;
    }
}
