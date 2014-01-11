package com.example.likeRSS.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import com.example.likeRSS.R;
import com.example.likeRSS.fragments.FavoriteNewsFragment;

/**
 * Created with IntelliJ IDEA.
 * User: Ruslik
 * Date: 23.11.13
 * Time: 20:57
 * To change this template use File | Settings | File Templates.
 */
public class FavoriteNewsActivity extends ActionBarActivity {

    FavoriteNewsFragment favoriteNewsFragment;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((getResources().getBoolean(R.bool.tablet)) && (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)) {
            setContentView(R.layout.init_layout_for_db_tablet);

        } else {
            setContentView(R.layout.initlayout);
            favoriteNewsFragment = new FavoriteNewsFragment();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.containerFrame, favoriteNewsFragment).commit();
        }


    }
}
