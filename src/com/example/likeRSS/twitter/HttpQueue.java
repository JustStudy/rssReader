package com.example.likeRSS.twitter;

import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;

public class HttpQueue {

    private static HttpQueue instance = null;

    private ArrayList<HttpBaseThread> queue;

    private Handler savedHandler;

    private HttpQueue() {
        queue = new ArrayList<HttpBaseThread>();
    }

    public static synchronized HttpQueue getInstance() {
        if (instance == null) instance = new HttpQueue();
        return instance;
    }

    public synchronized void clearQueue() {
        if (queue.size() == 0) return;

        int pos = 0;
        while (pos < queue.size()) {
            HttpBaseThread task = queue.get(pos);
            if (task.getStatus() == HttpBaseThread.STATUS_PENDING) {
                queue.remove(pos);
            } else {
                pos++;
            }
        }
    }

    public void addTask(HttpBaseThread task) {
        synchronized (this) {
            if (queue.size() >= 1) queue.add(1, task);
            else queue.add(task);
        }

        runFirst();
    }

    private void runFirst() {
        boolean recursive = false;
        synchronized (this) {
            if (queue.size() != 0) {
                HttpBaseThread task = queue.get(0);
                if (task.getStatus() == HttpBaseThread.STATUS_PENDING) {
                    savedHandler = task.getHandler();
                    task.setHandler(handler);
                    task.start();
                } else if (task.getStatus() == HttpBaseThread.STATUS_FINISHED) {
                    queue.remove(0);
                    recursive = true;
                }
            }
        }

        if (recursive) runFirst();
    }

    private void taskFinished(HttpBaseThread task) {
        task.setHandler(savedHandler);
        task.publishResult();

        synchronized (this) {
            queue.remove(task);
        }

        runFirst();
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            taskFinished((HttpBaseThread) msg.obj);
        }
    };
}
