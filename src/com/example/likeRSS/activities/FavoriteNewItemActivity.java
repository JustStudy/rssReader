package com.example.likeRSS.activities;


import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import com.example.likeRSS.R;
import com.example.likeRSS.fragments.FavoriteItemNewsFragment;

/**
 * Created with IntelliJ IDEA.
 * User: Ruslik
 * Date: 23.11.13
 * Time: 21:51
 * To change this template use File | Settings | File Templates.
 */
public class FavoriteNewItemActivity extends ActionBarActivity {

    FragmentTransaction fragmentTransaction;
    FavoriteItemNewsFragment favoriteItemNewsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.initlayout);
        getSupportActionBar().setTitle("Favorite News");
        favoriteItemNewsFragment = new FavoriteItemNewsFragment();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.containerFrame, favoriteItemNewsFragment).commit();

    }


}
