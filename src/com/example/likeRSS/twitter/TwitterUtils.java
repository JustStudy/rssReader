package com.example.likeRSS.twitter;

import android.content.SharedPreferences;
import oauth.signpost.OAuth;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;


/**
 * Created with IntelliJ IDEA.
 * User: Ruslik
 * Date: 09.12.13
 * Time: 22:04
 * To change this template use File | Settings | File Templates.
 */
public class TwitterUtils {
    public static Twitter isAuthenticated(SharedPreferences prefs) {
        String token = prefs.getString(OAuth.OAUTH_TOKEN, "");
        String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");

        if (token == null || token.length() == 0 || secret == null
                || secret.length() == 0)
            return null;

        try {
            AccessToken a = new AccessToken(token, secret);
            Twitter twitter = new TwitterFactory().getInstance();
            twitter.setOAuthConsumer(TwitterConstants.CONSUMER_KEY,
                    TwitterConstants.CONSUMER_SECRET);
            twitter.setOAuthAccessToken(a);
            return twitter;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static void saveAccessToken(SharedPreferences prefs, AccessToken a) {
        final SharedPreferences.Editor edit = prefs.edit();
        edit.putString(OAuth.OAUTH_TOKEN, a.getToken());
        edit.putString(OAuth.OAUTH_TOKEN_SECRET, a.getTokenSecret());
        edit.commit();
    }
}
