package jp.headcubicle.oeebird;

/**
 * 定数クラス
 */
public class OeeBirdResource {
	/** SharedPreferences: Replyを送るTwitterユーザ */
	public static String targetTwitterUser = "TARGET_TWITTER_USER";
	/** SharedPreferences: Replyを送るTweetに含まれるキーワード */
	public static String targetTweetKeyword = "TARGET_TWEET_KEYWORD";
	/** SharedPreferences: Replyの内容 */
	public static String replyText = "REPLY_TEXT";
	/** SharedPreferences: 末尾 */
	public static String tailText = "TAIL_TEXT";
	
	/** アクセストークンファイル名 */
	public static String accessTokenFileName = "access_token.dat";
	
	/** OAuth認証用ConsumerKey */
	public static String consumerKey = "DmINyUJz1obXoLqutRjYw";
	
	/** OAuth認証用ConsumerSecret */
	public static String consumerSecret = "ztuiAa6urhBYdCSbZoZ08byrc0Z6SeKSTfiTpr47w";

	/** IntentExtra: Replyを送るTwitterユーザ */
	public static String extraTargetTwitterUser = "jp.headcubicle.oeebird.intent.targetTwitterUser";
	/** IntentExtra: Replyを送るTweetに含まれるキーワード */
	public static String extraTargetTweetKeyword = "jp.headcubicle.oeebird.intent.targetTweetKeyword";
	/** IntentExtra: Replyの内容 */
	public static String extraReplyText = "jp.headcubicle.oeebird.intent.replyText";
	/** IntentExtra: 末尾 */
	public static String extraTailText = "jp.headcubicle.oeebird.intent.tailText";
	/** IntentExtra: アクセストークン */
	public static String extraAccessToken = "jp.headcubicle.oeebird.intent.accessToken";
}
