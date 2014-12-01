package jp.headcubicle.oeebird;

import twitter4j.TwitterException;
import twitter4j.auth.RequestToken;

public class GetRequestTokenResult {

    /** リクエストトークン. */
    private RequestToken requestToken = null;
    
    /** 例外. */
    private TwitterException twitterException = null;

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
     * 例外を取得する.
     * @return 例外
     */
    public TwitterException getTwitterException() {
        return twitterException;
    }

    /**
     * 例外を設定する.
     * @param twitterException 例外
     */
    public void setTwitterException(TwitterException twitterException) {
        this.twitterException = twitterException;
    }
}
