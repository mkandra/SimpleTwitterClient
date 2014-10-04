package com.codepath.apps.basictwitter.models;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;

public class User extends Model implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7815730823885250549L;
	private long uid;
	private String name;
	private String screenName;
	private String profileImageUrl;
	private byte[] byteImage;
	private int tweets_count;
	private int followers_count;
	private int following_count;
	private String tagline;

	public static User fromJson(JSONObject jsonObject) {
		User user = new User();
		// Extract values from the json to populate the member variables
		try {
			user.name = jsonObject.getString("name");
			user.uid = jsonObject.getLong("id");
			user.screenName = jsonObject.getString("screen_name");
			user.profileImageUrl = jsonObject.getString("profile_image_url");
			user.tweets_count = jsonObject.getInt("statuses_count");
			user.followers_count = jsonObject.getInt("followers_count");
			user.following_count = jsonObject.getInt("friends_count");
			user.tagline = jsonObject.getString("description");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return user;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	public String getName() {
		return name;
	}

	public long getUid() {
		return uid;
	}

	public String getScreenName() {
		return screenName;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public String getTagline() {
		return tagline;
	}

	public int getFollowersCount() {
		return followers_count;
	}

	public int getFollowingCount() {
		return following_count;
	}

	public int getTweetsCount() {
		return tweets_count;
	}

	public void setTweets_count(int tweets_count) {
		this.tweets_count = tweets_count;
	}

	public void setFollowers_count(int followers_count) {
		this.followers_count = followers_count;
	}

	public void setFollowing_count(int following_count) {
		this.following_count = following_count;
	}

	public void setTagline(String tagline) {
		this.tagline = tagline;
	}

	public byte[] getByteImage() {
		return byteImage;
	}

	public void setByteImage(byte[] byteImage) {
		this.byteImage = byteImage;
	}
}
