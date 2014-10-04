package com.codepath.apps.basictwitter.helpers;

import android.content.ContentValues;
import android.database.Cursor;

import com.codepath.apps.basictwitter.models.Tweet;

public class TweetDataSource {
	public static final String TABLE_TWEETS = "Tweets";

	public static final String KEY_ID = "_id";
	public static final String COLUMN_UID = "uid";
	public static final String COLUMN_TWEET_BODY = "body";
	public static final String COLUMN_CREATED_AT = "create_at";
	public static final String COLUMN_USER_UID = "user_uid";

	// Table Create Statement
	public static final String DATABASE_CREATE = "create table " + TABLE_TWEETS
			+ "(" + KEY_ID + " integer primary key autoincrement, "
			+ COLUMN_UID + " integer, " + COLUMN_TWEET_BODY + " text, "
			+ COLUMN_CREATED_AT + " text, " + COLUMN_USER_UID + " integer);";

	public static String[] allColumns = { TweetDataSource.COLUMN_UID,
			TweetDataSource.COLUMN_TWEET_BODY,
			TweetDataSource.COLUMN_CREATED_AT, TweetDataSource.COLUMN_USER_UID };

	public static ContentValues populateValues(Tweet tweet) {
		ContentValues values = new ContentValues();
		values.put(TweetDataSource.COLUMN_UID, tweet.getUid());
		values.put(TweetDataSource.COLUMN_TWEET_BODY, tweet.getBody());
		values.put(TweetDataSource.COLUMN_CREATED_AT, tweet.getCreatedAt());
		values.put(TweetDataSource.COLUMN_USER_UID, tweet.getUser().getUid());

		return values;
	}

	public static Tweet cursorToTweet(Cursor c) {
		Tweet tweet = new Tweet();

		tweet.setUid(c.getLong(c.getColumnIndex(TweetDataSource.COLUMN_UID)));
		tweet.setBody(c.getString(c
				.getColumnIndex(TweetDataSource.COLUMN_TWEET_BODY)));
		tweet.setCreatedAt(c.getString(c
				.getColumnIndex(TweetDataSource.COLUMN_CREATED_AT)));

		return tweet;
	}

}
