package jp.headcubicle.oeebird;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import android.os.AsyncTask;

/**
 * リクエストトークン取得用タスク
 */
public class GetRequestTokenTask extends AsyncTask<Void, Void, GetRequestTokenResult> {

    /** Twitter */
    Twitter twitter = null;
    
    /**
     * コンストラクタ
     * @param main　呼び出し元のActivity
     */
    public GetRequestTokenTask(Twitter twitter) {
        super();
        this.twitter = twitter;
    }

    @Override
    protected GetRequestTokenResult doInBackground(Void... params) {
        
        GetRequestTokenResult result = new GetRequestTokenResult();

        try {
            result.setRequestToken(twitter.getOAuthRequestToken());
        } catch (TwitterException e) {
            result.setTwitterException(e);
            e.printStackTrace();
        }

        return result;
    }
}
