package com.example.likeRSS.fragments;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.likeRSS.R;

/**
 * Created with IntelliJ IDEA.
 * User: Ruslik
 * Date: 11.11.13
 * Time: 12:12
 * To change this template use File | Settings | File Templates.
 */
public class RSSItemDisplayerFragment extends Fragment {
    String[] onNewArray;
    SharedPreferences shareNew;
    TextView titleItemRss;
    TextView textItemRss;


    TextView categoryItemRss;


    public void getSomeIntent() {
        onNewArray = getActivity().getIntent().getStringArrayExtra("rssItem");
        if (!getResources().getBoolean(R.bool.tablet) || (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)) {
            getActivity().setTitle(onNewArray[0]);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewRssItemFragment = inflater.inflate(R.layout.rss_item_displayer_layout, container, false);
        titleItemRss = (TextView) viewRssItemFragment.findViewById(R.id.titleRssItem);
        textItemRss = (TextView) viewRssItemFragment.findViewById(R.id.textRssItem);

        categoryItemRss = (TextView) viewRssItemFragment.findViewById(R.id.categoryRssItem);

        getSomeIntent();

        titleItemRss.setText(onNewArray[0]);
        textItemRss.setText(onNewArray[1]);
        categoryItemRss.setText("Категория: " + onNewArray[2]);


        return viewRssItemFragment;
    }
}
