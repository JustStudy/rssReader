package com.example.likeRSS.forNet;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import com.example.likeRSS.JustLikeNotification;
import com.example.likeRSS.R;

import java.util.Timer;

/**
 * Created with IntelliJ IDEA.
 * User: Ruslik
 * Date: 14.11.13
 * Time: 19:00
 * To change this template use File | Settings | File Templates.
 */
public class MySimpleService extends Service {
    public static int NOTIFY_ID_onCreate = 101;
    public static String title = null;
    NotificationManager nm;
    Timer timer = new Timer();
    JustLikeTask justLikeTask;
    Context context;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        timer.cancel();
        new JustLikeNotification(nm, context, R.drawable.stop_service_button, "Онлайн мониторинг новостей отключено.", 23);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (title == null) {
            title = intent.getStringExtra("Title item");
        }

        if (JustLikeTask.title == null) {
            justLikeTask = new JustLikeTask(getApplication().getApplicationContext(), nm, title);
        } else {
            justLikeTask = new JustLikeTask(getApplication().getApplicationContext(), nm);
        }

        timer.schedule(justLikeTask, 0, 900000);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {

        context = getApplication().getApplicationContext();
        nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent();
        PendingIntent contentIntent1 = PendingIntent.getActivity(context,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder onCreateBuilder =
                new NotificationCompat.Builder(this)
                        .setContentIntent(contentIntent1)
                        .setSmallIcon(R.drawable.rss_start_service)

                        .setTicker("Service started!")
                        .setWhen(System.currentTimeMillis())
                        .setAutoCancel(false)

                        .setContentTitle("RSS сервис запущен!")

                        .setContentText("Service check RSS news every 15min!");

        Notification n = onCreateBuilder.getNotification();
        n.defaults = Notification.DEFAULT_SOUND;

        nm.notify(NOTIFY_ID_onCreate, n);
        super.onCreate();

    }
}
