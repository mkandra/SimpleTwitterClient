package com.codepath.apps.basictwitter;

import java.util.ArrayList;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.codepath.apps.basictwitter.adapters.EndlessScrollListener;
import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TimelineActivity extends Activity {
	public static final int COMPOSE_TWEET_ACTIVITY_REQUEST_CODE = 10;
	private TwitterClient client;
	private ArrayList<Tweet> tweets;
	private ArrayAdapter<Tweet> aTweets;
	private ListView lvTweets;
	private User auth_user;

	public static ImageLoader imageLoader = ImageLoader.getInstance();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.argb(255, 85, 172, 238))); // Twitter Colour Palette
		getActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Home </font>"));
		
		auth_user = (User) getIntent().getSerializableExtra("auth_user");

		client = TwitterApplication.getRestClient();

		
		lvTweets = (ListView) findViewById(R.id.lvTweets);
		tweets = new ArrayList<Tweet>();
		aTweets = new TweetArrayAdapter(this, tweets);
		lvTweets.setAdapter(aTweets);
		
		populateTimeline(0);

		lvTweets.setOnScrollListener(new EndlessScrollListener() {

			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				// Triggered only when new data needs to be appended to the list
				// Add whatever code is needed to append new items to your
				// AdapterView
				long lastTweetUid = 0;
				if (!aTweets.isEmpty()) {
					Tweet lastTweet = aTweets.getItem(aTweets.getCount() - 1);
					lastTweetUid = lastTweet.getUid();
				}
				// customLoadMoreDataFromApi(page);
				customLoadMoreDataFromApi(lastTweetUid);
			}

		});

	}

	// Append more data into the adapter
	public void customLoadMoreDataFromApi(long offset) {
		// This method probably sends out a network request and appends new data
		// items to your adapter.
		// Use the offset value and add it as a parameter to your API request to
		// retrieve paginated data.
		// Deserialize API response and then construct new objects to append to
		// the adapter
		populateTimeline(offset);
	}

	public void populateTimeline(long offset) {
		client.getHomeTimeline(offset, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray json) {
				Log.d("DEBUG", json.toString());
				aTweets.addAll(Tweet.fromJSONArray(json));
			}

			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("DEBUG", e.toString());
				Log.d("DEBUG", s.toString());
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.timeline, menu);
		return true;
	}

	public void onComposeTweet(MenuItem item) {
		Intent i = new Intent(this, ComposeTweetActivity.class);
		i.putExtra("auth_user", auth_user);
		startActivityForResult(i, COMPOSE_TWEET_ACTIVITY_REQUEST_CODE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == COMPOSE_TWEET_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				Tweet tweetPosted = (Tweet) data.getSerializableExtra("postedTweet");
		        aTweets.insert(tweetPosted, 0);
		        aTweets.notifyDataSetChanged();
			}
		}
	}
}
