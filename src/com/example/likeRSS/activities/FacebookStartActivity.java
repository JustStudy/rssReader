package com.example.likeRSS.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.likeRSS.ConstantsForProg;
import com.example.likeRSS.R;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;

import java.io.IOException;

/**
 * Created by Ruslik on 11.01.14.
 */
public class FacebookStartActivity extends Activity {
    private Facebook facebook = new Facebook(ConstantsForProg.FACEBOOK_APP_ID);
    private AsyncFacebookRunner mAsyncRunner;
    private SharedPreferences mPrefs;
    EditText messageFacebook;
    Button btnPostToWall;
    Button btnFaceBookLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_facebook);
        btnPostToWall = (Button) findViewById(R.id.button_post);
        messageFacebook = (EditText) findViewById(R.id.message);
        btnFaceBookLogin = (Button) findViewById(R.id.button_facebook_login);
        btnFaceBookLogin.setVisibility(View.VISIBLE);
        btnPostToWall.setVisibility(View.INVISIBLE);
        messageFacebook.setVisibility(View.INVISIBLE);
        mAsyncRunner = new AsyncFacebookRunner(facebook);
        btnFaceBookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginToFacebook();
            }
        });
        btnPostToWall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                postFacebookToWall(messageFacebook.getText().toString());
                finish();

            }
        });

    }


    public void postFacebookToWall(String message) {

        Bundle bundle = new Bundle();
        bundle.putString("message", message);
        try {
            facebook.request("me/feed", bundle, "POST");
            Toast.makeText(getApplicationContext(), "Successfully posted", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
        }


    }


    public void loginToFacebook() {
        SharedPreferences sharedPreferences = getSharedPreferences("shareOnVk", MODE_WORLD_WRITEABLE);
        messageFacebook.setText(sharedPreferences.getString("news", " "));
        mPrefs = getPreferences(MODE_PRIVATE);
        String access_token = mPrefs.getString("access_token", null);
        long expires = mPrefs.getLong("access_expires", 0);

        if (access_token != null) {
            facebook.setAccessToken(access_token);
            Log.d("FB Sessions", "" + facebook.isSessionValid());
            // Making post to wall visible
            btnPostToWall.setVisibility(View.VISIBLE);
            messageFacebook.setVisibility(View.VISIBLE);
            btnFaceBookLogin.setVisibility(View.INVISIBLE);
        }

        if (expires != 0) {
            facebook.setAccessExpires(expires);
        }

        if (!facebook.isSessionValid()) {
            facebook.authorize(this,
                    new String[]{"email", "publish_stream"},
                    new Facebook.DialogListener() {

                        @Override
                        public void onCancel() {
                            // Function to handle cancel event
                        }

                        @Override
                        public void onComplete(Bundle values) {
                            // Function to handle complete event
                            // Edit Preferences and update facebook acess_token
                            SharedPreferences.Editor editor = mPrefs.edit();
                            editor.putString("access_token",
                                    facebook.getAccessToken());
                            editor.putLong("access_expires",
                                    facebook.getAccessExpires());
                            editor.commit();

                            btnPostToWall.setVisibility(View.VISIBLE);
                            messageFacebook.setVisibility(View.VISIBLE);
                            btnFaceBookLogin.setVisibility(View.INVISIBLE);

                        }

                        @Override
                        public void onError(DialogError error) {
                            // Function to handle error

                        }

                        @Override
                        public void onFacebookError(FacebookError fberror) {
                            // Function to handle Facebook errors

                        }

                    });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebook.authorizeCallback(requestCode, resultCode, data);
    }


}
