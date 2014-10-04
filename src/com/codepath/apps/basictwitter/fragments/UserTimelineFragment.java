package com.codepath.apps.basictwitter.fragments;

import org.json.JSONArray;

import android.os.Bundle;

import com.codepath.apps.basictwitter.TwitterApplication;
import com.codepath.apps.basictwitter.TwitterClient;
import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

public class UserTimelineFragment extends TweetsListFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		User user = (User) getArguments().getSerializable("user");
		TwitterApplication.getRestClient().getUserTimeline(
				user.getScreenName(), new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONArray jsonTweets) {
						addAll(Tweet.fromJSONArray(jsonTweets));
					}
				});
	}

	public static UserTimelineFragment newInstance(User user) {
		UserTimelineFragment fragmentUserTimeline = new UserTimelineFragment();
		Bundle args = new Bundle();
		args.putSerializable("user", user);
		fragmentUserTimeline.setArguments(args);
		return fragmentUserTimeline;
	}

	@Override
	public void refreshTimelines(TwitterClient client, long firstTweetUid) {
		// TODO Auto-generated method stub
		
	}
}
