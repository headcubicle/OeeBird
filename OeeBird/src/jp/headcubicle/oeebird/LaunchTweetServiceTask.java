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
	/** Replyを送るTwitterユーザ */
	private String targetTwitterUser = null;
	/** Replyを送るTweetに含まれるキーワード */
	private String targetTweetKeyword = null;
	/** replyの内容 */
	private String replyText = null;
	/** 末尾 */
	private String tailText = null;

	/**
	 * コンストラクタ
	 * @param mainActivity Activity
	 * @param targetTwitterUser Reply送信先Twitterユーザ
	 * @param replyText Replyの内容
	 * @param tailText 末尾
	 */
	public LaunchTweetServiceTask(MainActivity mainActivity,
			String targetTwitterUser, String targetTweetKeyword, String replyText, String tailText) {
		super();
		this.mainActivity = mainActivity;
		this.targetTwitterUser = targetTwitterUser;
		this.targetTweetKeyword = targetTweetKeyword;
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
		intent.putExtra("jp.headcubicle.oeebird.intent.targetTwitterUser", targetTwitterUser);
		intent.putExtra("jp.headcubicle.oeebird.intent.targetTweetKeyword", targetTweetKeyword);
		intent.putExtra("jp.headcubicle.oeebird.intent.replyText", replyText);
		intent.putExtra("jp.headcubicle.oeebird.intent.tailText", tailText);
		intent.putExtra("jp.headcubicle.oeebird.intent.accessToken", mainActivity.getAccessToken());
		Log.d("doInBackground", "@" + targetTwitterUser + " " + replyText + tailText);
		mainActivity.startService(intent);

		return null;
	}
}
