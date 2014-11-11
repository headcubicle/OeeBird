package jp.headcubicle.oeebird;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

/**
 * OAuth認証用タスク
 */
public class AuthenticationTask extends AsyncTask<Void, Void, String> {

	@Override
	protected String doInBackground(Void... params) {
		Twitter twitter = TwitterFactory.getSingleton();
		twitter.setOAuthConsumer("DmINyUJz1obXoLqutRjYw", "ztuiAa6urhBYdCSbZoZ08byrc0Z6SeKSTfiTpr47w");
		RequestToken requestToken = null;

		try {
			requestToken = twitter.getOAuthRequestToken();
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return requestToken.getAuthenticationURL();
	}
}
