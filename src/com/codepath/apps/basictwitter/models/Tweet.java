package com.codepath.apps.basictwitter.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.query.Select;

public class Tweet extends Model implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5779887379453615990L;
	private long uid;
	private String body;
	private String createdAt;
	private User user;

	public static Tweet fromJson(JSONObject jsonObject) {
		Tweet tweet = new Tweet();
		// Extract values from the json to populate the member variables
		try {
			tweet.body = jsonObject.getString("text");
			tweet.uid = jsonObject.getLong("id");
			tweet.createdAt = jsonObject.getString("created_at");
			tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return tweet;
	}

	public String getBody() {
		return body;
	}

	public long getUid() {
		return uid;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public User getUser() {
		return user;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
		ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject tweetJson = null;
			try {
				tweetJson = jsonArray.getJSONObject(i);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}

			Tweet tweet = Tweet.fromJson(tweetJson);
			if (tweet != null) {
				tweets.add(tweet);
			}
		}

		return tweets;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getBody() + " - " + getUser().getScreenName();
	}

	public static List<Tweet> getFromDB() {
		return new Select().from(Tweet.class).execute();
	}

}
