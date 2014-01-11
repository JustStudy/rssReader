package com.example.likeRSS.twitter;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;

public class GetImageThread extends HttpBaseThread {

    private static final String TAG = "GetImageThread";

    private String url;

    public GetImageThread(Handler handler, String url) {
        super(handler);
        this.url = url;
    }

    @Override
    protected void doWork() {
        if (DatabaseMgr.getInstance().isCachedImageExists(url)) {
            return;
        }

        DefaultHttpClient client = getDefaultHttpClient();
        HttpGet request = createHttpGetRequest(url);

        try {
            HttpResponse response = client.execute(request);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                ByteArrayOutputStream ostream = new ByteArrayOutputStream();
                response.getEntity().writeTo(ostream);
                if (ostream.size() != 0) {
                    byte[] data = ostream.toByteArray();
                    DatabaseMgr.getInstance().putCachedImage(url, data);
                } else {
                    taskStatus = TASK_STATUS_ERROR;
                    log("ostream.size() == 0");
                }
            } else {
                taskStatus = TASK_STATUS_ERROR;
                log("Error: http status code = " + response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            taskStatus = TASK_STATUS_ERROR;
            log(e.getMessage());
        }
    }

    private void log(String message) {
        if (message != null) Log.d(TAG, message);
    }

    @Override
    public void publishResult() {
        Handler handler = getHandler();
        Message msg = new Message();
        msg.arg1 = taskStatus;
        msg.obj = url;
        handler.sendMessage(msg);
    }
}
