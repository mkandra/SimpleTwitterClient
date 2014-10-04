package com.codepath.apps.basictwitter.helpers;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.models.User;

public class MySQLiteHelper extends SQLiteOpenHelper {
	// Logcat tag
	private static final String TAG = "MySQLiteHelper";

	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "tweets_users.db";

	@Override
	public void onCreate(SQLiteDatabase db) {
		// creating required tables
		db.execSQL(UserDataSource.DATABASE_CREATE);
		db.execSQL(TweetDataSource.DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");
		// on upgrade drop older tables
		db.execSQL("DROP TABLE IF EXISTS " + UserDataSource.TABLE_USERS);
		db.execSQL("DROP TABLE IF EXISTS " + TweetDataSource.TABLE_TWEETS);

		// create new tables
		onCreate(db);
	}

	// closing database
	public void closeDB() {
		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null && db.isOpen())
			db.close();
	}

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void createUserEntry(User user) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = UserDataSource.populateValues(user);

		db.insert(UserDataSource.TABLE_USERS, null, values);
	}

	public void createTweetEntry(Tweet tweet) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = TweetDataSource.populateValues(tweet);

		db.insert(TweetDataSource.TABLE_TWEETS, null, values);
	}

	public User getUserById(long uid) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + UserDataSource.TABLE_USERS
				+ " WHERE " + UserDataSource.COLUMN_USER_UID + " = " + uid;

		Cursor c = db.rawQuery(selectQuery, null);

		if (c != null)
			c.moveToFirst();

		User user = UserDataSource.cursorToUser(c);

		return user;
	}

	/*
	 * getting all users
	 */
	public List<User> getAllUsers() {
		List<User> users = new ArrayList<User>();
		String selectQuery = "SELECT  * FROM " + UserDataSource.TABLE_USERS;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				User user = UserDataSource.cursorToUser(c);

				// adding to list
				users.add(user);
			} while (c.moveToNext());
		}

		return users;
	}

	public Tweet getTweetById(long uid) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TweetDataSource.TABLE_TWEETS
				+ " WHERE " + TweetDataSource.KEY_ID + " = " + uid;

		Cursor c = db.rawQuery(selectQuery, null);

		if (c != null)
			c.moveToFirst();

		Tweet tweet = TweetDataSource.cursorToTweet(c);

		User user = getUserById(c.getLong(c
				.getColumnIndex(TweetDataSource.COLUMN_USER_UID)));
		tweet.setUser(user);

		return tweet;
	}

	/*
	 * getting all tweets
	 */
	public List<Tweet> getAllTweets() {
		List<Tweet> tweets = new ArrayList<Tweet>();
		String selectQuery = "SELECT  * FROM " + TweetDataSource.TABLE_TWEETS;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Tweet tweet = TweetDataSource.cursorToTweet(c);

				User user = getUserById(c.getLong(c
						.getColumnIndex(TweetDataSource.COLUMN_USER_UID)));
				tweet.setUser(user);

				// adding to list
				tweets.add(tweet);
			} while (c.moveToNext());
		}

		return tweets;
	}

	public void deleteAllTweets() {
		SQLiteDatabase db = this.getWritableDatabase();

		db.execSQL("delete from " + UserDataSource.TABLE_USERS);
		db.execSQL("delete from " + TweetDataSource.TABLE_TWEETS);
	}
}
