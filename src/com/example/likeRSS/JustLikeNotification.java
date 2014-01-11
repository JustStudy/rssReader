package com.example.likeRSS;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;


/**
 * Created with IntelliJ IDEA.
 * User: Ruslik
 * Date: 15.11.13
 * Time: 0:13
 * To change this template use File | Settings | File Templates.
 */
public class JustLikeNotification extends Notification {

    public JustLikeNotification(NotificationManager nm, Context context, int id_icon, String ContentText, Class c, int NOTIFY_ID) {
        nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent1 = new Intent(context, c);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, notificationIntent1,
                PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentIntent(contentIntent)
                .setContentIntent(contentIntent)
                .setSmallIcon(id_icon)
                        // текст в строке состояния
                .setTicker("")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(false)
                        // Заголовок уведомления
                .setContentTitle("Найдено новую новость")
                .setContentText(ContentText); // Текст уведомленимя
        Notification notificationCompat = new Notification();
        notificationCompat.defaults = Notification.DEFAULT_SOUND;
        notificationCompat = builder.getNotification();
        notificationCompat.defaults = Notification.DEFAULT_SOUND;
        nm.notify(NOTIFY_ID, notificationCompat);


    }

    public JustLikeNotification(NotificationManager nm, Context context, int id_icon, String ContentText, int NOTIFY_ID) {
        nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(id_icon)
                // текст в строке состояния
                .setTicker("Service stoped!")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                        // Заголовок уведомления
                .setContentTitle("RSS сервис остановлен")
                .setContentText(ContentText); // Текст уведомленимя
        Notification notificationCompat = new Notification();
        notificationCompat.defaults = Notification.DEFAULT_SOUND;
        notificationCompat = builder.getNotification();
        nm.notify(NOTIFY_ID, notificationCompat);


    }
}
