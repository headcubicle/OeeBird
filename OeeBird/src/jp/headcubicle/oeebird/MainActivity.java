package jp.headcubicle.oeebird;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.concurrent.ExecutionException;

public class MainActivity extends Activity {

    /** Twitter. */
    private Twitter twitter = null;
    /** リクエストトークン. */
    private RequestToken requestToken = null;
    /** アクセストークン. */
    private AccessToken accessToken = null;
    /** アプリケーション認証用PIN. */
    private String pin = null;
    /** Replyを送るTwitterユーザ. */
    private String targetTwitterUser = null;
    /** Replyを送るTweetに含まれるキーワード. */
    private String targetTweetKeyword = null;
    /** replyの内容. */
    private String replyText = null;
    /** 末尾. */
    private String tailText = null;
    
    /** サービス起動ボタン. */
    private Button launchButton = null;
    
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
        
        // Replyを送るTwitterユーザ
        EditText targetTwitterUserEdit = (EditText) findViewById(R.id.target_twitter_user);
        targetTwitterUser = sharedPreferences.getString(OeeBirdResource.targetTwitterUser, "");
        targetTwitterUserEdit.setText(targetTwitterUser);
        // Replyを送るTweetに含まれるキーワード
        EditText targetTweetKeywordEdit = (EditText) findViewById(R.id.target_tweet_keyword);
        targetTweetKeyword = sharedPreferences.getString(OeeBirdResource.targetTweetKeyword, "");
        targetTweetKeywordEdit.setText(targetTweetKeyword);
        // Replyの内容
        EditText replyTextEdit = (EditText) findViewById(R.id.reply_text);
        replyText = sharedPreferences.getString(OeeBirdResource.replyText, "");
        replyTextEdit.setText(replyText);
        // 末尾
        EditText tailTextEdit = (EditText) findViewById(R.id.tail_text);
        tailText = sharedPreferences.getString(OeeBirdResource.tailText, "");
        tailTextEdit.setText(tailText);
        
        // サービス起動ボタン
        launchButton = (Button) findViewById(R.id.button_launch);
        
        // Twitterインスタンスを生成する。
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(OeeBirdResource.consumerKey);
        builder.setOAuthConsumerSecret(OeeBirdResource.consumerSecret);
        Configuration configuration = builder.build();
        TwitterFactory factory = new TwitterFactory(configuration);
        twitter = factory.getInstance();
        
        // アクセストークンを読み込む。
        try {
            FileInputStream fis = openFileInput(OeeBirdResource.accessTokenFileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            accessToken = (AccessToken) ois.readObject();
            ois.close();
        } catch (FileNotFoundException e) {
            // ファイルが存在しない場合、アクセストークンをnullとする。
            accessToken = null;
            // サービス起動ボタンを無効とする。
            launchButton.setEnabled(false);
        } catch (StreamCorruptedException e) {
            Toast.makeText(this, R.string.error_stream_corrupted_exception, Toast.LENGTH_LONG)
                .show();
            e.printStackTrace();
        } catch (IOException e) {
            Toast.makeText(this, R.string.error_io_exception, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            Toast.makeText(this, R.string.error_class_not_found_exception, Toast.LENGTH_LONG)
                .show();
            e.printStackTrace();
        }
        
        // アクセストークンがある場合、Twitterインスタンスに設定する。
        if (null != accessToken) {
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
     * 設定ボタンタップ.
     */
    public void onClickSettings(View view) {
        // 設定を保存する。
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Editor editor = sharedPreferences.edit();

        // Replyを送るTwitterユーザ
        EditText targetTwitterUserEdit = (EditText) findViewById(R.id.target_twitter_user);
        editor.putString(OeeBirdResource.targetTwitterUser,
                        targetTwitterUserEdit.getText().toString());
        // Replyを送るTweetに含まれるキーワード
        EditText targetTweetKeywordEdit = (EditText) findViewById(R.id.target_tweet_keyword);
        editor.putString(OeeBirdResource.targetTweetKeyword,
                        targetTweetKeywordEdit.getText().toString());
        // Replyの内容
        EditText replyTextEdit = (EditText) findViewById(R.id.reply_text);
        editor.putString(OeeBirdResource.replyText, replyTextEdit.getText().toString());
        // 末尾
        EditText tailTextEdit = (EditText) findViewById(R.id.tail_text);
        editor.putString(OeeBirdResource.tailText, tailTextEdit.getText().toString());
        
        // コミット
        editor.commit();
        
        // アクセストークンが保存されていない場合、OAuth認証を行う。
        if (null == accessToken) {
            GetRequestTokenTask getRequestTokenTask = new GetRequestTokenTask(twitter);
            GetRequestTokenResult result = null;
            
            if (null == requestToken) {
                try {
                    result = getRequestTokenTask.execute().get();
                    if (result.getRequestToken() != null) {
                        requestToken = result.getRequestToken();                        
                    } else if (result.getTwitterException() != null) {
                        Toast.makeText(this,
                                    getString(R.string.error_get_request_token_failed)
                                    + result.getTwitterException().getStatusCode(),
                                    Toast.LENGTH_LONG).show();
                    }
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                } catch (ExecutionException e1) {
                    e1.printStackTrace();
                }
            }
            
            if (requestToken != null) {
                // ブラウザ起動
                Uri authenticationUri = Uri.parse(requestToken.getAuthenticationURL());
                Intent intent = new Intent(Intent.ACTION_VIEW, authenticationUri);
                startActivity(intent);
                
                // PIN入力ダイアログを表示する。
                DialogFragment dialogFragment = new PinDialogFragment();
                dialogFragment.show(getFragmentManager(), "pin");
            }
        }
    }
    
    /**
     * 起動ボタンをタップ.
     */
    public void onClickLaunch(View view) throws TwitterException {
        // サービス起動
        LaunchTweetServiceTask launchTask = new LaunchTweetServiceTask(this,
                                                                    targetTwitterUser,
                                                                    targetTweetKeyword,
                                                                    replyText,
                                                                    tailText);
        launchTask.execute();
        
        // サービス起動ボタンを無効にする。
        launchButton.setEnabled(false);
    }
    
    /**
     * 停止ボタンをタップ.
     */
    public void onClickStop(View view) {
        // サービス停止
        stopService(new Intent(MainActivity.this, TweetService.class));
        // サービス起動ボタンを有効にする。
        launchButton.setEnabled(true);
    }
    
    /**
     * PIN入力ダイアログ.
     */
    public static class PinDialogFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            
            LayoutInflater inflater = (LayoutInflater)getActivity()
                                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View content = inflater.inflate(R.layout.dialog_pin, null);
            
            builder.setTitle(R.string.label_my_twitter_pin);
            builder.setView(content);
            builder.setPositiveButton(R.string.button_label_pin_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // PINを取得する。
                    EditText pinEdit = (EditText) content.findViewById(R.id.my_twitter_pin);
                    MainActivity main = (MainActivity) PinDialogFragment.this.getActivity();
                    String pin = pinEdit.getText().toString();
                    
                    // アクセストークンを取得する。
                    if (!pin.isEmpty()) {
                        GetAccessTokenTask getAccessTokenTask = new GetAccessTokenTask(main.getTwitter(),
                                                                                    main.getRequestToken(),
                                                                                    pin);
                        try {
                            GetAccessTokenResult result = getAccessTokenTask.execute().get();
                            if (result.getAccessToken() != null) {
                                main.setAccessToken(result.getAccessToken());
                                // サービス起動ボタンを有効にする。
                                main.launchButton.setEnabled(true);
                            } else if (result.getTwitterException() != null) {
                                Toast.makeText(main, main.getString(R.string.error_get_access_token_failed)
                                                                    + result.getTwitterException().getStatusCode(),
                                                                    Toast.LENGTH_LONG).show();
                            }
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        } catch (ExecutionException e1) {
                            e1.printStackTrace();
                        }
                        
                        // アクセストークンを保存する。
                        try {
                            FileOutputStream fos = main.openFileOutput(OeeBirdResource.accessTokenFileName,
                                                                    MODE_PRIVATE);
                            ObjectOutputStream oos = new ObjectOutputStream(fos);
                            oos.writeObject(main.getAccessToken());
                            oos.close();
                        } catch (FileNotFoundException e) {
                            Toast.makeText(main,
                                            R.string.error_file_not_found_exception,
                                            Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        } catch (IOException e) {
                            Toast.makeText(main,
                                            R.string.error_io_exception,
                                            Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
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
     * PINを取得する.
     * @return PIN
     */
    public String getPin() {
        return pin;
    }

    /**
     * PINを設定する.
     * @param pin PIN
     */
    public void setPin(String pin) {
        this.pin = pin;
    }

    /**
     * リクエストトークンを取得する.
     * @return リクエストトークン
     */
    public RequestToken getRequestToken() {
        return requestToken;
    }

    /**
     * リクエストトークンを設定する.
     * @param requestToken リクエストトークン
     */
    public void setRequestToken(RequestToken requestToken) {
        this.requestToken = requestToken;
    }

    /**
     * アクセストークンを取得する.
     * @return アクセストークン
     */
    public AccessToken getAccessToken() {
        return accessToken;
    }

    /**
     * アクセストークンを設定する.
     * @param accessToken アクセストークン
     */
    public void setAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * Twitterを取得する.
     * @return Twitter
     */
    public Twitter getTwitter() {
        return twitter;
    }

    /**
     * Twitterを設定する.
     * @param twitter Twitter
     */
    public void setTwitter(Twitter twitter) {
        this.twitter = twitter;
    }
}
