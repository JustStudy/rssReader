package com.example.likeRSS.twitter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.example.likeRSS.R;

/**
 * Created with IntelliJ IDEA.
 * User: Ruslik
 * Date: 10.12.13
 * Time: 11:04
 * To change this template use File | Settings | File Templates.
 */
public class TwitterDialog extends Dialog {

    private static final FrameLayout.LayoutParams FILL = new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.FILL_PARENT,
            ViewGroup.LayoutParams.FILL_PARENT);

    private TweetChatActivity parent;
    private FrameLayout mContent;
    private LinearLayout webViewContainer;
    private WebView mWebView;
    private ImageView mCrossImage;
    private ProgressDialog mSpinner;
    private String mUrl;

    public TwitterDialog(Context context, String url) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        parent = (TweetChatActivity) context;

        mUrl = url;

        mSpinner = new ProgressDialog(getContext());
        mSpinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mSpinner.setMessage("Loading...");

        setContentView(R.layout.twitter_web_dialog);
        mContent = (FrameLayout) findViewById(R.id.twitter_web_view_content);


        mContent.setPadding(15, 15, 15, 15);

        webViewContainer = (LinearLayout) findViewById(R.id.twitter_web_view_container);
        mCrossImage = (ImageView) findViewById(R.id.twitter_close_web_dialog);

        mCrossImage.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                TwitterDialog.this.dismiss();
                if (parent.progressDialog != null
                        && parent.progressDialog.isShowing()) {
                    parent.progressDialog.dismiss();
                }
            }
        });
        int margin = mCrossImage.getDrawable().getIntrinsicWidth() / 2;
        mCrossImage.setVisibility(View.INVISIBLE);
        setUpWebView(margin);
    }

    @Override
    protected void onStop() {
        if (parent.progressDialog != null && parent.progressDialog.isShowing()) {
            parent.progressDialog.dismiss();
        }
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (parent.progressDialog != null && parent.progressDialog.isShowing()) {
            parent.progressDialog.dismiss();
        }
    }

    private void setUpWebView(int margin) {
        mWebView = new WebView(parent);
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setWebViewClient(new TwitterDialog.TwWebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(mUrl);
        mWebView.setLayoutParams(FILL);

        webViewContainer.setPadding(margin, margin, margin, margin);
        webViewContainer.addView(mWebView);
    }

    private class TwWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d("Twitter-WebView", "Redirect URL: " + url);

            boolean isDenied = false;
            try {
                Uri uri = Uri.parse(url);
                String param = uri.getQuery();
                String name = param.split("=")[0];
                if ("denied".equals(name)) {
                    isDenied = true;
                }
            } catch (Exception e) {
            }

            TwitterDialog.this.dismiss();
            if (!isDenied) {
                getContext().startActivity(
                        new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            } else {
                if (parent.progressDialog != null
                        && parent.progressDialog.isShowing()) {
                    parent.progressDialog.dismiss();
                }
            }
            return true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.d("Twitter-WebView", "Webview loading URL: " + url);
            super.onPageStarted(view, url, favicon);
            mSpinner.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mSpinner.dismiss();

            mContent.setBackgroundColor(Color.TRANSPARENT);
            mWebView.setVisibility(View.VISIBLE);
            mCrossImage.setVisibility(View.VISIBLE);
        }
    }

}
