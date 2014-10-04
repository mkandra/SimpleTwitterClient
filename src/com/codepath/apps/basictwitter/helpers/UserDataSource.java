package com.codepath.apps.basictwitter.helpers;

import android.content.ContentValues;
import android.database.Cursor;

import com.codepath.apps.basictwitter.models.User;

public class UserDataSource {
	public static final String TABLE_USERS = "Users";

	public static final String KEY_ID = "_id";
	public static final String COLUMN_USER_UID = "user_uid";
	public static final String COLUMN_USER_NAME = "name";
	public static final String COLUMN_USER_SCREEN_NAME = "screen_name";
	public static final String COLUMN_USER_PROFILE_IMAGE_URL = "profile_image_url";
	// public static final String COLUMN_USER_PROFILE_IMAGE = "profile_image";
	public static final String COLUMN_USER_TWEETS_COUNT = "tweets_count";
	public static final String COLUMN_USER_FOLLOWERS_COUNT = "followers_count";
	public static final String COLUMN_USER_FOLLOWING_COUNT = "following_count";
	public static final String COLUMN_USER_TAGLINE = "tagline";

	// Table Create Statement
	public static final String DATABASE_CREATE = "create table " + TABLE_USERS
			+ "(" + KEY_ID + " integer primary key autoincrement, "
			+ COLUMN_USER_UID + " integer, " + COLUMN_USER_NAME + " text, "
			+ COLUMN_USER_SCREEN_NAME
			+ " text, "
			+ COLUMN_USER_PROFILE_IMAGE_URL
			+ " text, "
			// + COLUMN_USER_PROFILE_IMAGE + " BLOB, "
			+ COLUMN_USER_TWEETS_COUNT + " integer, "
			+ COLUMN_USER_FOLLOWERS_COUNT + " integer, "
			+ COLUMN_USER_FOLLOWING_COUNT + " integer, " + COLUMN_USER_TAGLINE
			+ " text);";

	public static String[] allColumns = {
			UserDataSource.COLUMN_USER_UID,
			UserDataSource.COLUMN_USER_NAME,
			UserDataSource.COLUMN_USER_SCREEN_NAME,
			UserDataSource.COLUMN_USER_PROFILE_IMAGE_URL,
			// UserDataSource.COLUMN_USER_PROFILE_IMAGE,
			UserDataSource.COLUMN_USER_TWEETS_COUNT,
			UserDataSource.COLUMN_USER_FOLLOWERS_COUNT,
			UserDataSource.COLUMN_USER_FOLLOWING_COUNT,
			UserDataSource.COLUMN_USER_TAGLINE };

	public static ContentValues populateValues(User user) {
		ContentValues values = new ContentValues();
		values.put(UserDataSource.COLUMN_USER_UID, user.getUid());
		values.put(UserDataSource.COLUMN_USER_NAME, user.getName());
		values.put(UserDataSource.COLUMN_USER_SCREEN_NAME, user.getScreenName());
		values.put(UserDataSource.COLUMN_USER_PROFILE_IMAGE_URL,
				user.getProfileImageUrl());
		// try {
		// FileInputStream instream = new FileInputStream(
		// user.getProfileImageUrl());
		// BufferedInputStream bif = new BufferedInputStream(instream);
		// byte[] byteImage1 = new byte[bif.available()];
		// bif.read(byteImage1);
		// values.put(UserDataSource.COLUMN_USER_PROFILE_IMAGE, byteImage1);
		// bif.close();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

		values.put(UserDataSource.COLUMN_USER_TWEETS_COUNT,
				user.getTweetsCount());
		values.put(UserDataSource.COLUMN_USER_FOLLOWERS_COUNT,
				user.getFollowersCount());
		values.put(UserDataSource.COLUMN_USER_FOLLOWING_COUNT,
				user.getFollowingCount());
		values.put(UserDataSource.COLUMN_USER_TAGLINE, user.getTagline());

		return values;
	}

	public static User cursorToUser(Cursor c) {
		User user = new User();

		user.setUid(c.getLong(c.getColumnIndex(UserDataSource.COLUMN_USER_UID)));
		user.setName(c.getString(c
				.getColumnIndex(UserDataSource.COLUMN_USER_NAME)));
		user.setScreenName(c.getString(c
				.getColumnIndex(UserDataSource.COLUMN_USER_SCREEN_NAME)));
		user.setProfileImageUrl(c.getString(c
				.getColumnIndex(UserDataSource.COLUMN_USER_PROFILE_IMAGE_URL)));
		// byte[] byteImage = c.getBlob(c
		// .getColumnIndex(UserDataSource.COLUMN_USER_PROFILE_IMAGE));
		// user.setByteImage(byteImage);
		user.setTweets_count(c.getInt(c
				.getColumnIndex(UserDataSource.COLUMN_USER_TWEETS_COUNT)));
		user.setFollowers_count(c.getInt(c
				.getColumnIndex(UserDataSource.COLUMN_USER_FOLLOWERS_COUNT)));
		user.setFollowing_count(c.getInt(c
				.getColumnIndex(UserDataSource.COLUMN_USER_FOLLOWING_COUNT)));
		user.setTagline(c.getString(c
				.getColumnIndex(UserDataSource.COLUMN_USER_TAGLINE)));

		return user;
	}
}
