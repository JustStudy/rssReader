package com.example.likeRSS.twitter;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import twitter4j.TwitterException;

/**
 * Created with IntelliJ IDEA.
 * User: Ruslik
 * Date: 10.12.13
 * Time: 10:37
 * To change this template use File | Settings | File Templates.
 */
public class SendTweetTask extends AsyncTask<Void, Void, Void> {
    private String message;
    private Handler handler;

    public SendTweetTask(String message, Handler handler) {
        this.message = message;
        this.handler = handler;
    }

    @Override
    protected Void doInBackground(Void... params) {
        String result;
//опять же пользуемся статическим полем twitter из Вашего активити
        if (TweetChatActivity.twitter != null) {
            try {
                TweetChatActivity.twitter.updateStatus(message);
                result = "Your message has been sent";
            } catch (TwitterException e) {
                result = "Error sending message";
            }
            if (handler != null) {
                Message msg = new Message();
                msg.obj = result;
                handler.sendMessage(msg);
            }
        }

        return null;
    }
}
