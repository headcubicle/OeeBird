package jp.headcubicle.oeebird;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

/**
 * Tweet用サービス
 */
public class TweetService extends Service {

	/** Replyを送るTwitterアカウント */
	private String targetTwitterAccount = null;
	/** replyの内容 */
	private String replyText = null;
	/** 末尾 */
	private String tailText = null;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * サービス起動
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("TweetService", "onStartCommand");
		
		targetTwitterAccount = intent.getStringExtra("jp.headcubicle.oeebird.intent.targetTwitterAccount");
		replyText = intent.getStringExtra("jp.headcubicle.oeebird.intent.replyText");
		tailText = intent.getStringExtra("jp.headcubicle.oeebird.intent.tailText");

		TweetTask tweetTask = new TweetTask();
		tweetTask.execute("@" + targetTwitterAccount + " " + replyText + tailText);
		
		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * サービス停止
	 */
	@Override
	public void onDestroy() {
		Log.d("TweetService", "onDestroy");
		super.onDestroy();
	}
	
	/**
	 * Tweet用タスク
	 */
	public class TweetTask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			Twitter twitter = TwitterFactory.getSingleton();

			try {
				twitter.updateStatus(params[0]);
			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}
	}
}
