package jp.headcubicle.oeebird;

import twitter4j.DirectMessage;
import twitter4j.Query;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.UserStreamListener;
import twitter4j.auth.AccessToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

/**
 * Tweet用サービス
 */
public class TweetService extends Service {

	/** Replyを送るTwitterユーザ */
	private String targetTwitterUser = null;
	/** Replyを送るTweetに含まれるキーワード */
	private String targetTweetKeyword = null;
	/** replyの内容 */
	private String replyText = null;
	/** 末尾 */
	private String tailText = null;
	/** アクセストークン */
	private AccessToken accessToken = null;
	
	/** */
	private TwitterStream twitterStream = null;

	UserStreamListener userStreamListener = new UserStreamListener() {

		@Override
		public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
			Log.d("onDeletionNotice", "Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
		}

		@Override
		public void onScrubGeo(long userId, long upToStatusId) {
			Log.d("onScrubGeo", "Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
		}

		@Override
		public void onStallWarning(StallWarning warning) {
			Log.d("onStallWarning", "Got stall warning:" + warning);
		}

		@Override
		public void onStatus(Status status) {
			Log.d("onStatus", "Status: " + status);
			// 対象ユーザが特定のTweetをした場合にReplyを送る。
			if(status.getUser().getName().equals(targetTwitterUser)) {
				if(status.getText().contains(targetTweetKeyword)) {
					Twitter twitter = TwitterFactory.getSingleton();
					replyText += tailText;
					try {
						twitter.updateStatus("@" + targetTwitterUser + " " + replyText);
					} catch (TwitterException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

		@Override
		public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
			Log.d("onTrackLimitationNotice", "Got track limitation notice:" + numberOfLimitedStatuses);
		}

		@Override
		public void onException(Exception e) {
			e.printStackTrace();
		}

		@Override
		public void onBlock(User arg0, User arg1) {
			// TODO Auto-generated method stub			
		}

		@Override
		public void onDeletionNotice(long arg0, long arg1) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onDirectMessage(DirectMessage arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onFavorite(User arg0, User arg1, Status arg2) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onFollow(User arg0, User arg1) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onFriendList(long[] arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onUnblock(User arg0, User arg1) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onUnfavorite(User arg0, User arg1, Status arg2) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onUnfollow(User arg0, User arg1) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onUserListCreation(User arg0, UserList arg1) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onUserListDeletion(User arg0, UserList arg1) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onUserListMemberAddition(User arg0, User arg1, UserList arg2) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onUserListMemberDeletion(User arg0, User arg1, UserList arg2) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onUserListSubscription(User arg0, User arg1, UserList arg2) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onUserListUnsubscription(User arg0, User arg1, UserList arg2) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onUserListUpdate(User arg0, UserList arg1) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onUserProfileUpdate(User arg0) {
			// TODO Auto-generated method stub
		}
	};
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * サービス起動
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("TweetService", "onStartCommand");
		
		// Reply送信先ユーザ
		targetTwitterUser = intent.getStringExtra(OeeBirdResource.extraTargetTwitterUser);
		// Reply送信先Tweetに含まれるキーワード
		targetTweetKeyword = intent.getStringExtra(OeeBirdResource.extraTargetTweetKeyword);
		// Relpy内容
		replyText = intent.getStringExtra(OeeBirdResource.extraReplyText);
		// 末尾
		tailText = intent.getStringExtra(OeeBirdResource.extraTailText);
		// アクセストークン
		accessToken = (AccessToken) intent.getSerializableExtra(OeeBirdResource.extraAccessToken);
		
		Configuration configuration = new ConfigurationBuilder().setOAuthConsumerKey(OeeBirdResource.consumerKey)
																.setOAuthConsumerSecret(OeeBirdResource.consumerSecret)
																.build();

		twitterStream = new TwitterStreamFactory(configuration).getInstance();
		twitterStream.setOAuthAccessToken(accessToken);
		twitterStream.addListener(userStreamListener);
		// Streamの読み込みを開始する。
		twitterStream.user();
		
		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * サービス停止
	 */
	@Override
	public void onDestroy() {
		Log.d("TweetService", "onDestroy");
		
		// Streamの読み込みを停止する。
		twitterStream.shutdown();
		
		super.onDestroy();
	}
}
