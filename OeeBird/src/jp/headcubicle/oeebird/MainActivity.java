package jp.headcubicle.oeebird;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.concurrent.ExecutionException;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

	/** リクエストトークン */
	private RequestToken requestToken = null;
	/** アクセストークン */
	private AccessToken accessToken = null;
	/** アプリケーション認証用PIN */
	private String pin = null;
	/** Replyを送るTwitterアカウント */
	private String targetTwitterAccount = null;
	/** replyの内容 */
	private String replyText = null;
	/** 末尾 */
	private String tailText = null;
	
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
		targetTwitterAccount = sharedPreferences.getString("TARGET_TWITTER_ACCOUNT", "");
		targetTwitterAccountEdit.setText(targetTwitterAccount);
		// Replyの内容
		EditText replyTextEdit = (EditText) findViewById(R.id.reply_text);
		replyText = sharedPreferences.getString("REPLY_TEXT", "");
		replyTextEdit.setText(replyText);
		// 末尾
		EditText tailTextEdit = (EditText) findViewById(R.id.tail_text);
		tailText = sharedPreferences.getString("TAIL_TEXT", "");
		tailTextEdit.setText(tailText);
		
		// アクセストークンを読み込む。
		try {
			FileInputStream fis = openFileInput("access_token.dat");
			ObjectInputStream ois = new ObjectInputStream(fis);
			accessToken = (AccessToken) ois.readObject();
			ois.close();
		} catch (FileNotFoundException e) {
			// ファイルが存在しない場合、アクセストークンをnullとする。
			accessToken = null;
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// アクセストークンがある場合、Twitterインスタンスに設定する。
		if (null != accessToken) {
			Twitter twitter = TwitterFactory.getSingleton();
			twitter.setOAuthConsumer("DmINyUJz1obXoLqutRjYw", "ztuiAa6urhBYdCSbZoZ08byrc0Z6SeKSTfiTpr47w");
			twitter.setOAuthAccessToken(accessToken);
		}
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
		
		// アクセストークンが保存されていない場合、OAuth認証を行う。
		if(null == accessToken) {
			GetRequestTokenTask getRequestTokenTask = new GetRequestTokenTask();
			try {
				requestToken = getRequestTokenTask.execute().get();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ExecutionException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			// ブラウザ起動
			Uri authenticationUri = Uri.parse(requestToken.getAuthenticationURL());
			Intent intent = new Intent(Intent.ACTION_VIEW, authenticationUri);
			startActivity(intent);
			
			// PIN入力ダイアログを表示する。
			DialogFragment dialogFragment = new PinDialogFragment();
			dialogFragment.show(getFragmentManager(), "pin");
		}
	}
	
	/**
	 * 起動ボタンをタップ
	 */
	public void onClickLaunch(View view) throws TwitterException {
		TweetTestTask testTask = new TweetTestTask();
		testTask.execute("@" + targetTwitterAccount + " " + replyText + tailText);
	}
	
	/**
	 * PIN入力ダイアログ
	 */
	public static class PinDialogFragment extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			
			LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View content = inflater.inflate(R.layout.dialog_pin, null);
			
			builder.setTitle(R.string.label_my_twitter_pin);
			builder.setView(content);
			builder.setPositiveButton(R.string.button_label_pin_ok, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// PINを取得する。
					EditText pinEdit = (EditText) content.findViewById(R.id.my_twitter_pin);
					//EditText pinEdit = (EditText) PinDialogFragment.this.getView().findViewById(R.id.my_twitter_pin);
					MainActivity main = (MainActivity) PinDialogFragment.this.getActivity();
					String pin = pinEdit.getText().toString();
					
					// アクセストークンを取得する。
				    GetAccessTokenTask getAccessTokenTask = new GetAccessTokenTask(main.getRequestToken(), pin);
				    try {
						main.setAccessToken(getAccessTokenTask.execute().get());
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ExecutionException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					// アクセストークンを保存する。
					try {
						FileOutputStream fos = main.openFileOutput("access_token.dat", MODE_PRIVATE);
						ObjectOutputStream oos = new ObjectOutputStream(fos);
						oos.writeObject(main.getAccessToken());
						oos.close();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			
			builder.setNegativeButton(R.string.button_label_pin_cancel, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// 特に何もしない。
				}
			});
			
			return builder.create();
		}
		
	}

	/**
	 * PINを取得する。
	 * @return PIN
	 */
	public String getPin() {
		return pin;
	}

	/**
	 * PINを設定する。
	 * @param pin
	 */
	public void setPin(String pin) {
		this.pin = pin;
	}

	/**
	 * リクエストトークンを取得する。
	 * @return リクエストトークン
	 */
	public RequestToken getRequestToken() {
		return requestToken;
	}

	/**
	 * リクエストトークンを設定する。
	 * @param requestToken リクエストトークン
	 */
	public void setRequestToken(RequestToken requestToken) {
		this.requestToken = requestToken;
	}

	/**
	 * アクセストークンを取得する。
	 * @return アクセストークン
	 */
	public AccessToken getAccessToken() {
		return accessToken;
	}

	/**
	 * アクセストークンを設定する。
	 * @param accessToken アクセストークン
	 */
	public void setAccessToken(AccessToken accessToken) {
		this.accessToken = accessToken;
	}
	
	/**
	 * Tweet用タスク
	 */
	public class TweetTestTask extends AsyncTask<String, Void, Void> {

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
