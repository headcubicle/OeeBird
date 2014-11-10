package jp.headcubicle.oeebird;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

	// OAuth認証用URI文字列
	private String authenticationUriString = null;
	
	/**
	 * OAuth認証用URI文字列を取得する。
	 * @return OAuth認証用URI文字列
	 */
	public String getAuthenticationUriString() {
		return authenticationUriString;
	}

	/**
	 * OAuth認証用URI文字列を設定する。
	 * @param authenticationUriString OAuth認証用URI文字列
	 */
	public void setAuthenticationUriString(String authenticationUriString) {
		this.authenticationUriString = authenticationUriString;
	}
	
	public void copyAuthenticationUriString(String authenticationUriString) {
		this.authenticationUriString = new String(authenticationUriString);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// 天才が仕事しないせい。ヒルズに帰れ。
		try {
			Class.forName("android.os.AsyncTask");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// 設定値を読み込む。
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		// 自分のTwitterアカウント
		EditText myTwitterAccountEdit = (EditText) findViewById(R.id.my_twitter_account);
		myTwitterAccountEdit.setText(sharedPreferences.getString("MY_TWITTER_ACCOUNT", ""));
		// 自分のTwitterパスワード
		EditText myTwitterPasswordEdit = (EditText) findViewById(R.id.my_twitter_password);
		myTwitterPasswordEdit.setText(sharedPreferences.getString("MY_TWITTER_PASSWORD", ""));
		// Replyを送るTwitterアカウント
		EditText targetTwitterAccountEdit = (EditText) findViewById(R.id.target_twitter_account);
		targetTwitterAccountEdit.setText(sharedPreferences.getString("TARGET_TWITTER_ACCOUNT", ""));
		// Replyの内容
		EditText replyTextEdit = (EditText) findViewById(R.id.reply_text);
		replyTextEdit.setText(sharedPreferences.getString("REPLY_TEXT", ""));
		// 末尾
		EditText tailTextEdit = (EditText) findViewById(R.id.tail_text);
		tailTextEdit.setText(sharedPreferences.getString("TAIL_TEXT", ""));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * 設定ボタンタップ
	 */
	public void onClickSettings(View view) {
		// 設定を保存する。
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		Editor editor = sharedPreferences.edit();

		// 自分のTwitterアカウント
		EditText myTwitterAccountEdit = (EditText) findViewById(R.id.my_twitter_account);
		editor.putString("MY_TWITTER_ACCOUNT", myTwitterAccountEdit.getText().toString());
		// 自分のTwitterパスワード
		EditText myTwitterPasswordEdit = (EditText) findViewById(R.id.my_twitter_password);
		editor.putString("MY_TWITTER_PASSWORD", myTwitterPasswordEdit.getText().toString());
		// Replyを送るTwitterアカウント
		EditText targetTwitterAccountEdit = (EditText) findViewById(R.id.target_twitter_account);
		editor.putString("TARGET_TWITTER_ACCOUNT", targetTwitterAccountEdit.getText().toString());
		// Replyの内容
		EditText replyTextEdit = (EditText) findViewById(R.id.reply_text);
		editor.putString("REPLY_TEXT", replyTextEdit.getText().toString());
		// 末尾
		EditText tailTextEdit = (EditText) findViewById(R.id.tail_text);
		editor.putString("TAIL_TEXT", tailTextEdit.getText().toString());
		
		// コミット
		editor.commit();
		
		// OAuth認証
		AuthenticationTask authenticationTask = new AuthenticationTask();
		try {
			authenticationUriString = authenticationTask.execute().get();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		/*
		while(null == authenticationUriString) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		*/
		
		// ブラウザ起動
		Uri authenticationUri = Uri.parse(getAuthenticationUriString());
		Intent intent = new Intent(Intent.ACTION_VIEW, authenticationUri);
		startActivity(intent);
	}
	
	/**
	 * 起動ボタンをタップ
	 */
	public void onClickLaunch(View view) {
		
	}
	
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

			Log.d("doInBackground", requestToken.getAuthenticationURL());
			
			return requestToken.getAuthenticationURL();
		}
		
		/*
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			MainActivity.this.copyAuthenticationUriString(result);
			Log.d("onPostExecute", authenticationUriString);
		}
		*/
	}
}
