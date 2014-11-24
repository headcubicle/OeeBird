package jp.headcubicle.oeebird;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * リクエストトークン取得用タスク
 */
public class GetRequestTokenTask extends AsyncTask<Void, Void, GetRequestTokenResult> {

    /** Twitter */
    Twitter twitter = null;
    
    /** 呼び出し元のActivity */
    private MainActivity main = null;
    
    /**
     * コンストラクタ
     * @param main　呼び出し元のActivity
     */
    public GetRequestTokenTask(MainActivity main, Twitter twitter) {
        super();
        this.main = main;
        this.twitter = twitter;
    }

    @Override
    protected GetRequestTokenResult doInBackground(Void... params) {
//        Twitter twitter = TwitterFactory.getSingleton();
//
//        // ConsumerKeyとSecretが未設定の場合、設定する。
//        if (twitter.getConfiguration().getOAuthConsumerKey() == null ||
//                twitter.getConfiguration().getOAuthConsumerSecret() == null) {
//            twitter.setOAuthConsumer(OeeBirdResource.consumerKey, OeeBirdResource.consumerSecret);
//        }
        
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
