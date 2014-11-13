package jp.headcubicle.oeebird;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Tweetサービス起動用タスク
 */
public class LaunchTweetServiceTask extends AsyncTask<Void, Void, Void> {

	/** Activity */
	private MainActivity mainActivity = null;
	/** Replyを送るTwitterアカウント */
	private String targetTwitterAccount = null;
	/** replyの内容 */
	private String replyText = null;
	/** 末尾 */
	private String tailText = null;

	/**
	 * コンストラクタ
	 * @param mainActivity Activyt
	 * @param targetTwitterAccount Reply送信先Twitterアカウント
	 * @param replyText Replyの内容
	 * @param tailText 末尾
	 */
	public LaunchTweetServiceTask(MainActivity mainActivity,
			String targetTwitterAccount, String replyText, String tailText) {
		super();
		this.mainActivity = mainActivity;
		this.targetTwitterAccount = targetTwitterAccount;
		this.replyText = replyText;
		this.tailText = tailText;
	}

	/**
	 * Tweetサービスを起動する。
	 */
	@Override
	protected Void doInBackground(Void... params) {
		// サービス起動
		Intent intent = new Intent(mainActivity, TweetService.class);
		intent.putExtra("jp.headcubicle.oeebird.intent.targetTwitterAccount", targetTwitterAccount);
		intent.putExtra("jp.headcubicle.oeebird.intent.replyText", replyText);
		intent.putExtra("jp.headcubicle.oeebird.intent.tailText", tailText);
		Log.d("doInBackground", "@" + targetTwitterAccount + " " + replyText + tailText);
		mainActivity.startService(intent);

		return null;
	}
}
