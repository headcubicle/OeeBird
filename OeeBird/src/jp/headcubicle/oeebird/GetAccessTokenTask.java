package jp.headcubicle.oeebird;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import android.os.AsyncTask;

/**
 * アクセストークン取得用タスク
 */
public class GetAccessTokenTask  extends AsyncTask<Void, Void, AccessToken> {

	// リクエストトークン
	private RequestToken requestToken = null;
	
	// PIN
	private String pin = null;
	
	public GetAccessTokenTask(RequestToken requestToken, String pin) {
		super();
		this.requestToken = requestToken;
		this.pin = pin;
	}

	@Override
	protected AccessToken doInBackground(Void... params) {
		Twitter twitter = TwitterFactory.getSingleton();

		AccessToken accessToken = null;
		
		try {
			if(pin.length() > 0) {
				accessToken = twitter.getOAuthAccessToken(requestToken, pin);
			} else {
				accessToken = twitter.getOAuthAccessToken();
			}
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return accessToken;
	}

}
