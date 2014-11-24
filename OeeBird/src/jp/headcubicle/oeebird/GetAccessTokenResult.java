package jp.headcubicle.oeebird;

import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;

/**
 * アクセストークン取得用タスクの実行結果
 */
public class GetAccessTokenResult {

    /** アクセストークン */
    private AccessToken accessToken = null;
    
    /** 例外 */
    private TwitterException twitterException = null;

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
     * 例外を取得する。
     * @return 例外
     */
    public TwitterException getTwitterException() {
        return twitterException;
    }

    /**
     * 例外を設定する。
     * @param e
     */
    public void setTwitterException(TwitterException twitterException) {
        this.twitterException = twitterException;
    }
}
