package com.codepath.apps.basictwitter.fragments;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.basictwitter.TwitterApplication;
import com.codepath.apps.basictwitter.TwitterClient;
import com.codepath.apps.basictwitter.helpers.Utils;
import com.codepath.apps.basictwitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class HomeTimelineFragment extends TweetsListFragment {
	private TwitterClient client;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		client = TwitterApplication.getRestClient();

		populateTimeline(0);
	}

	public void populateTimeline(long offset) {
		if (!Utils.isNetworkAvailable(getActivity())) {
			List<Tweet> tweetsFromDB = getAllTweets();

			if (tweetsFromDB.size() > 0) {
				addAll(tweetsFromDB);
				// Toast.makeText(getContext(),
				// "Cached tweets. No internet connection",
				// Toast.LENGTH_SHORT).show();
				return;
			}
			// Toast.makeText(this, "No internet connection",
			// Toast.LENGTH_SHORT).show();
		} else {
			client.getHomeTimeline(offset, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONArray json) {
					// Log.d("DEBUG", json.toString());
					ArrayList<Tweet> tweets = Tweet.fromJSONArray(json);
					addAll(tweets);

					// Save Data
					deleteAllTweets();

					for (Tweet tweet : tweets) {
						createUserEntry(tweet.getUser());
						createTweetEntry(tweet);
					}

				}

				@Override
				public void onFailure(Throwable e, String s) {
					Log.d("DEBUG", e.toString());
					Log.d("DEBUG", s.toString());
				}
			});
		}
	}

	public void refreshTimelines(TwitterClient client, long firstTweetUid) {
		client.refreshHomeTimeline(firstTweetUid,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONArray json) {
						// Log.d("DEBUG", json.toString());
						ArrayList<Tweet> tweets = Tweet.fromJSONArray(json);

						if (!tweets.isEmpty()) {
							// append new data
							for (int i = 0; i < tweets.size(); i++) {
								insert(tweets.get(i), i);
							}

							// Save Data
							deleteAllTweets();

							for (Tweet tweet : tweets) {
								createUserEntry(tweet.getUser());
								createTweetEntry(tweet);
							}
						}
					}

					@Override
					public void onFailure(Throwable e, String s) {
						Log.d("DEBUG", e.toString());
						Log.d("DEBUG", s.toString());
					}
				});
	}

}
