package jp.headcubicle.oeebird;

import java.util.concurrent.ExecutionException;

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
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

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
		String authenticationUriString = null;
		try {
			authenticationUriString = authenticationTask.execute().get();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// ブラウザ起動
		Uri authenticationUri = Uri.parse(authenticationUriString);
		Intent intent = new Intent(Intent.ACTION_VIEW, authenticationUri);
		startActivity(intent);
		
		// PIN入力ダイアログを表示する。
		DialogFragment dialogFragment = new PinDialogFragment();
		dialogFragment.show(getFragmentManager(), "pin");
	}
	
	/**
	 * 起動ボタンをタップ
	 */
	public void onClickLaunch(View view) {
		
	}
	
	/**
	 * PIN入力ダイアログ
	 */
	public static class PinDialogFragment extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			
			LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View content = inflater.inflate(R.layout.dialog_pin, null);
			
			builder.setTitle(R.string.label_my_twitter_pin);
			builder.setView(content);
			builder.setPositiveButton(R.string.button_label_pin_ok, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			});
			
			builder.setNegativeButton(R.string.button_label_pin_cancel, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			});
			
			return builder.create();
		}
		
	}
}
