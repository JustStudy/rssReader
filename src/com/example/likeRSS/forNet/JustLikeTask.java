package com.example.likeRSS.forNet;

import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;
import com.example.likeRSS.JustLikeNotification;
import com.example.likeRSS.R;
import com.example.likeRSS.RssSingleItem;
import com.example.likeRSS.activities.RSSListActivity;

import java.util.ArrayList;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

/**
 * Created with IntelliJ IDEA.
 * User: Ruslik
 * Date: 17.11.13
 * Time: 13:06
 * To change this template use File | Settings | File Templates.
 */
public class JustLikeTask extends TimerTask {
    public static String title;
    Context context;
    NotificationManager nm;
    private int Id_notify = 153;

    JustLikeTask(Context context, NotificationManager nm) {
        this.context = context;
        this.nm = nm;
    }

    JustLikeTask(Context context, NotificationManager nm, String title) {
        this.context = context;
        this.nm = nm;
        this.title = title;
    }

    @Override
    public void run() {
        ArrayList<RssSingleItem> rssItems = new ArrayList<RssSingleItem>();
        Log.e(RSSListActivity.LOG_ID, "in run thread");

        String Url = "http://newsru.com/plain/rss/txt_all.xml";
        try {
            rssItems = new MyAsyncTask().execute(Url).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (!title.contentEquals(rssItems.get(0).getTitle())) {
            setItemTitle(rssItems.get(0).getTitle());
            new JustLikeNotification(nm, context, R.drawable.rss_new_found, rssItems.get(0).getTitle(), RSSListActivity.class, Id_notify);

        }

    }

    private void setItemTitle(String s) {
        title = s;
    }
}
