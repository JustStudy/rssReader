package com.example.likeRSS.twitter;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public abstract class HttpBaseThread extends Thread {

    private static final String TAG = "HttpBaseThread";

    // Set the timeout in milliseconds until a connection is established.
    private static final int timeoutConnection = 5000 * 2;

    // Set the default socket timeout (SO_TIMEOUT)
    // in milliseconds which is the timeout for waiting for data.
    private static final int timeoutSocket = 5000 * 2;

    //thread execution statuses
    public static final int STATUS_PENDING = 0;
    public static final int STATUS_RUNNING = 1;
    public static final int STATUS_FINISHED = 2;
    //task execution results
    public static final int TASK_STATUS_OK = 0;
    public static final int TASK_STATUS_ERROR = 1;
    public static final int TASK_STATUS_TIMEOUT = 2;

    private int threadStatus = STATUS_PENDING;
    protected int taskStatus = TASK_STATUS_OK;

    private Handler handler;

    public HttpBaseThread(Handler handler) {
        this.handler = handler;
    }

    public synchronized int getStatus() {
        return threadStatus;
    }

    @Override
    public synchronized void start() {
        if (getStatus() == STATUS_PENDING) {
            synchronized (this) {
                threadStatus = STATUS_RUNNING;
            }
        }
        super.start();
    }

    @Override
    public void run() {
        doWork();

        synchronized (this) {
            threadStatus = STATUS_FINISHED;
        }

        if (handler != null) {
            Message msg = new Message();
            msg.obj = this;
            handler.dispatchMessage(msg);
        }
    }

    public void runSynchronously() {
        doWork();
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public Handler getHandler() {
        return handler;
    }

    protected DefaultHttpClient getDefaultHttpClient() {
        HttpParams httpParameters = new BasicHttpParams();

        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

        return new DefaultHttpClient(httpParameters);
    }

    protected HttpGet createHttpGetRequest(String requestUrl) {
        Log.d(TAG, "Request url: " + requestUrl);
        HttpGet request = new HttpGet(requestUrl);

        return request;
    }

    protected HttpPost createHttpPostRequest(String requestUrl) {
        Log.d(TAG, "Request url: " + requestUrl);
        HttpPost request = new HttpPost(requestUrl);

        return request;
    }

    protected abstract void doWork();

    public abstract void publishResult();
}
