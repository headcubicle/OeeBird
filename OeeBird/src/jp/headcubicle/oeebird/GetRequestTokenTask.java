package jp.headcubicle.oeebird;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;
import android.os.AsyncTask;

/**
 * リクエストトークン取得用タスク
 */
public class GetRequestTokenTask extends AsyncTask<Void, Void, RequestToken> {

	@Override
	protected RequestToken doInBackground(Void... params) {
		Twitter twitter = TwitterFactory.getSingleton();
		twitter.setOAuthConsumer(OeeBirdResource.consumerKey, OeeBirdResource.consumerSecret);
		RequestToken requestToken = null;

		try {
			requestToken = twitter.getOAuthRequestToken();
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return requestToken;
	}
}
