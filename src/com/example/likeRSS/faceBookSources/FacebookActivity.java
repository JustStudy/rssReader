package com.example.likeRSS.faceBookSources;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.example.likeRSS.R;
import com.nostra13.socialsharing.common.AuthListener;
import com.nostra13.socialsharing.facebook.FacebookFacade;


public class FacebookActivity extends Activity {

    private FacebookFacade facebook;
    private FacebookEventObserver facebookEventObserver;
    private EditText messageView;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ac_facebook);

        facebook = new FacebookFacade(this, Constants.FACEBOOK_APP_ID);
        facebookEventObserver = FacebookEventObserver.newInstance();

        messageView = (EditText) findViewById(R.id.message);

        Button postButton = (Button) findViewById(R.id.button_post);

        sharedPreferences = getSharedPreferences("shareOnVk", MODE_WORLD_WRITEABLE);
        String s = sharedPreferences.getString("news", " ");
        messageView.setText(s);


        postButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (facebook.isAuthorized()) {
                    publishMessage();
                    finish();
                } else {
                    // Start authentication dialog and publish message after successful authentication
                    facebook.authorize(new AuthListener() {
                        @Override
                        public void onAuthSucceed() {
                            publishMessage();
                            finish();
                        }

                        @Override
                        public void onAuthFail(String error) { // Do noting
                        }
                    });
                }
            }
        });

    }

    private void publishMessage() {
        facebook.publishMessage(messageView.getText().toString());
    }


    @Override
    public void onStart() {
        super.onStart();
        facebookEventObserver.registerListeners(this);
        if (!facebook.isAuthorized()) {
            facebook.authorize();
        }
    }

    @Override
    public void onStop() {
        facebookEventObserver.unregisterListeners();
        super.onStop();
    }


}
