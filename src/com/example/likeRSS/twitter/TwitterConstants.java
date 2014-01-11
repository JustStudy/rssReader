package com.example.likeRSS.twitter;

/**
 * Created with IntelliJ IDEA.
 * User: Ruslik
 * Date: 09.12.13
 * Time: 21:57
 * To change this template use File | Settings | File Templates.
 */
public class TwitterConstants {
    public static final String CONSUMER_KEY = "jwDxSYAcy3gcOjQrc6R7gw";
    public static final String CONSUMER_SECRET = "Cr6ByTzvWjmBXjUU2aw3l0hNKWTR1igqIMsJlzWYhe0";

    public static final String REQUEST_URL = "https://api.twitter.com/oauth/request_token";
    public static final String ACCESS_URL = "https://api.twitter.com/oauth/access_token";
    public static final String AUTHORIZE_URL = "https://api.twitter.com/oauth/authorize";

    public static final String OAUTH_CALLBACK_SCHEME = "appfortwitter";
    public static final String OAUTH_CALLBACK_HOST = "callback";
    public static final String OAUTH_CALLBACK_URL = OAUTH_CALLBACK_SCHEME + "://" + OAUTH_CALLBACK_HOST;
}
