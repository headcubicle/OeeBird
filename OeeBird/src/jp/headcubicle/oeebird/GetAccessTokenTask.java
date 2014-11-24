package jp.headcubicle.oeebird;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.RequestToken;
import android.os.AsyncTask;

/**
 * アクセストークン取得用タスク
 */
public class GetAccessTokenTask extends AsyncTask<Void, Void, GetAccessTokenResult> {
    
    /** Twitter */
    private Twitter twitter = null;
    
    /** リクエストトークン */
    private RequestToken requestToken = null;
    
    /** 実行結果 */
    private GetAccessTokenResult result = null;
    
    /** PIN */
    private String pin = null;
    
    /**
     * コンストラクタ
     * @param main 呼び出し元Activity
     * @param requestToken リクエストトークン
     * @param pin pin
     */
    public GetAccessTokenTask(Twitter twitter, RequestToken requestToken, String pin) {
        super();
        this.twitter = twitter;
        this.requestToken = requestToken;
        this.pin = pin;
    }

    @Override
    protected GetAccessTokenResult doInBackground(Void... params) {

        result = new GetAccessTokenResult();
        
        try {
            if(pin.length() > 0) {
                result.setAccessToken(twitter.getOAuthAccessToken(requestToken, pin));
            } else {
                result.setAccessToken(twitter.getOAuthAccessToken());
            }
        } catch (TwitterException e) {
            result.setTwitterException(e);
            e.printStackTrace();
        }

        return result;
    }
}
