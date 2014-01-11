package com.example.likeRSS.activities;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import com.example.likeRSS.R;
import com.example.likeRSS.fragments.RSSListNewsFragment;


/**
 * Created with IntelliJ IDEA.
 * User: Ruslik
 * Date: 07.11.13
 * Time: 14:01
 * To change this template use File | Settings | File Templates.
 */
public class RSSListActivity extends ActionBarActivity {

    RSSListNewsFragment rssListNewsFragment;
    FragmentTransaction transaction;
    public static final String LOG_ID = "my log";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((getResources().getBoolean(R.bool.tablet)) && (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)) {

            setContentView(R.layout.initlayout_for_tabletview);

        } else {
            setContentView(R.layout.initlayout);
            transaction = getSupportFragmentManager().beginTransaction();
            rssListNewsFragment = new RSSListNewsFragment();
            transaction.replace(R.id.containerFrame, rssListNewsFragment).commit();
        }


    }

}
