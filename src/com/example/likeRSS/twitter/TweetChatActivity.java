package com.example.likeRSS.twitter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.likeRSS.R;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import twitter4j.Twitter;


/**
 * Created with IntelliJ IDEA.
 * User: Ruslik
 * Date: 09.12.13
 * Time: 23:46
 * To change this template use File | Settings | File Templates.
 */
public class TweetChatActivity extends Activity implements View.OnClickListener {

    protected static final String TAG = "TweetChatActivity";


    private EditText chatTheme;

    public ProgressDialog progressDialog;
    public static Twitter twitter;
    public static OAuthConsumer consumer;
    public static OAuthProvider provider;

    public String twitterMessage;

    private Animation fadeIn, fadeOut;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.twitter_main);

        initLayout();
    }

    private void initLayout() {
        chatTheme = (EditText) findViewById(R.id.chat_theme);
        SharedPreferences sharedPreferences = getSharedPreferences("shareOnVk", MODE_WORLD_WRITEABLE);
        chatTheme.setText(sharedPreferences.getString("news", " "));


        findViewById(R.id.btn_push_on_wall_twitter).setOnClickListener(this);

        findViewById(R.id.twitter_sign_on_btn).setOnClickListener(this);

        fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        fadeIn.setAnimationListener(new Animation.AnimationListener() {

            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {

            }
        });
        fadeOut = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {

            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {

            }
        });
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_push_on_wall_twitter:

                EditText tweetBox1 = ((EditText) findViewById(R.id.chat_theme));
                String message1 = tweetBox1.getText().toString();
                if (message1 != null && !("".equals(message1))) {
                    Log.e("Good", "Was sended!");
                    sendTweet(message1);
                }

                tweetBox1.setText("");

                InputMethodManager imm1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(tweetBox1.getWindowToken(), 0);
                break;


            case R.id.twitter_sign_on_btn:
                if (isOnline()) {
                    if (twitter == null) {
                        signOnTwitter();

                    }
                } else {

                    Toast.makeText(this, R.string.network_unavailable, Toast.LENGTH_LONG).show();
                }
                break;

        }
    }

    /**
     * TWITTER CODE HERE
     */

    public void showTwitterDialog(String url) {
        new TwitterDialog(this, url).show();
    }

    public void signOnTwitter() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (twitter == null) {
            twitter = TwitterUtils.isAuthenticated(prefs);
        }

        if (twitter == null) {
            progressDialog = ProgressDialog.show(this, "", "Please wait");
            twitterMessage = null;
            Toast.makeText(this, "Please authorize this app!",
                    Toast.LENGTH_LONG).show();
            consumer = new CommonsHttpOAuthConsumer(
                    TwitterConstants.CONSUMER_KEY,
                    TwitterConstants.CONSUMER_SECRET);
            provider = new CommonsHttpOAuthProvider(
                    TwitterConstants.REQUEST_URL, TwitterConstants.ACCESS_URL,
                    TwitterConstants.AUTHORIZE_URL);
            new OAuthRequestTokenTask(this, consumer, provider, new Handler() {

                @Override
                public void handleMessage(Message msg) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                    Toast.makeText(
                            TweetChatActivity.this,
                            "Error during Twitter Authorization: "
                                    + "OAUth retrieve request token failed",
                            Toast.LENGTH_LONG).show();
                }

            }).execute();
        } else {
            ((ImageView) findViewById(R.id.twitter_sign_on_btn)).setImageResource(R.drawable.twitter_icon);
        }
    }


    public void sendTweet(String message) {
        Log.e("My logs", "In send tweet");
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);

        if (twitter == null) {
            twitter = TwitterUtils.isAuthenticated(prefs);
        }

        if (twitter != null) {
            new SendTweetTask(message, new Handler() {

                public void handleMessage(Message msg) {
                    if (msg != null && msg.obj != null)
                        Toast.makeText(TweetChatActivity.this,
                                (String) msg.obj, Toast.LENGTH_LONG).show();
                    //
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                }

            }).execute();
        } else {
            progressDialog = ProgressDialog.show(this, "", "Please wait");
            Toast.makeText(this, "Please authorize this app!",
                    Toast.LENGTH_LONG).show();
            consumer = new CommonsHttpOAuthConsumer(
                    TwitterConstants.CONSUMER_KEY,
                    TwitterConstants.CONSUMER_SECRET);
            provider = new CommonsHttpOAuthProvider(
                    TwitterConstants.REQUEST_URL, TwitterConstants.ACCESS_URL,
                    TwitterConstants.AUTHORIZE_URL);
            // store message for future use
            twitterMessage = message;
            new OAuthRequestTokenTask(this, consumer, provider, new Handler() {

                @Override
                public void handleMessage(Message msg) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                    Toast.makeText(
                            TweetChatActivity.this,
                            "Error during Twitter Authorization: "
                                    + "OAUth retrieve request token",
                            Toast.LENGTH_LONG).show();
                }

            }).execute();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent != null && intent.getData() != null) {
            Uri uri = intent.getData();
            if (uri != null
                    && uri.toString().startsWith(
                    TwitterConstants.OAUTH_CALLBACK_URL)) {
                new RetrieveAccessTokenTask(this, consumer, provider, twitterMessage, new Handler() {

                    @Override
                    public void handleMessage(Message msg) {
                        if (msg != null && msg.obj != null) {
                            if (msg.arg1 == RetrieveAccessTokenTask.RETRIEVAL_FAIL) {
                                Log.d(TAG, (String) msg.obj);
                                Toast.makeText(
                                        TweetChatActivity.this,
                                        "Failed to retrieve access token",
                                        Toast.LENGTH_LONG).show();
                            } else if (msg.arg1 == RetrieveAccessTokenTask.RETRIEVAL_SUCCESS) {
                                ((ImageView) findViewById(R.id.twitter_sign_on_btn)).setImageResource(R.drawable.twitter_icon);
                            }
                        }
                        //
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        }
                    }

                }).execute(uri);
            }
        } else {
            // HIDE progress dialog (can be visible after twitter authorization
            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        }
    }

    /**
     * END TWITTER CODE
     */

    public boolean wasOffline;

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            wasOffline = false;
            return true;
        }
        return false;
    }

}
