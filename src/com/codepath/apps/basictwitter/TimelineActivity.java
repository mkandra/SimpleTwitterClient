package com.codepath.apps.basictwitter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
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
	private SwipeRefreshLayout swipeContainer;

	public static ImageLoader imageLoader = ImageLoader.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		getActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.argb(255, 85, 172, 238))); // Twitter
																	// Colour
																	// Palette
		getActionBar().setTitle(
				Html.fromHtml("<font color='#ffffff'>Home </font>"));

		auth_user = (User) getIntent().getSerializableExtra("auth_user");

		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(this);
		Editor edit = pref.edit();
		edit.putString("auth_user_uid", String.valueOf(auth_user.getUid()));
		edit.putString("auth_user_name", auth_user.getName());
		edit.putString("auth_user_screen_name", auth_user.getScreenName());
		edit.putString("auth_user_profile_image_url",
				auth_user.getProfileImageUrl());
		edit.commit();

		client = TwitterApplication.getRestClient();

		lvTweets = (ListView) findViewById(R.id.lvTweets);
		tweets = new ArrayList<Tweet>();
		aTweets = new TweetArrayAdapter(this, tweets);
		lvTweets.setAdapter(aTweets);

		lvTweets.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent i = new Intent(TimelineActivity.this,
						DetailedTweetActivity.class);
				Tweet tweet = tweets.get(position);
				i.putExtra("detailTweet", tweet);
				startActivity(i);
			}

		});

		populateTimeline(0);

		lvTweets.setOnScrollListener(new EndlessScrollListener() {

			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				// Triggered only when new data needs to be appended to the list
				// Add whatever code is needed to append new items to your
				// AdapterView
				if (!aTweets.isEmpty()) {
					Tweet lastTweet = aTweets.getItem(aTweets.getCount() - 1);
					long lastTweetUid = lastTweet.getUid();
					// customLoadMoreDataFromApi(page);
					customLoadMoreDataFromApi(lastTweetUid - 1);
				}
			}

		});

		swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
		// Setup refresh listener which triggers new data loading
		swipeContainer.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// Your code to refresh the list here.
				// Make sure you call swipeContainer.setRefreshing(false)
				// once the network request has completed successfully.
				if (!aTweets.isEmpty()) {
					Tweet firstTweet = aTweets.getItem(0);
					long firstTweetUid = firstTweet.getUid();
					refreshTimeline(firstTweetUid);
				}
				// Now we call setRefreshing(false) to signal refresh has
				// finished
				swipeContainer.setRefreshing(false);
			}

		});

		// Configure the refreshing colors
		swipeContainer.setColorSchemeResources(
				android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
	}

	private void refreshTimeline(long firstTweetUid) {
		if (!isNetworkAvailable()) {
			Toast.makeText(TimelineActivity.this, "No internet connection",
					Toast.LENGTH_SHORT).show();
		} else {
			client.refreshHomeTimeline(firstTweetUid,
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(JSONArray json) {
							// Log.d("DEBUG", json.toString());
							ArrayList<Tweet> tweets = Tweet.fromJSONArray(json);

							if (!tweets.isEmpty()) {
								// append new data
								for (int i = 0; i < tweets.size(); i++) {
									aTweets.insert(tweets.get(i), i);
								}

								// Save Data
								new Delete().from(Tweet.class).execute();
								new Delete().from(User.class).execute();

								ActiveAndroid.beginTransaction();
								try {
									for (Tweet tweet : tweets) {
										tweet.getUser().save();
										tweet.save();
									}
									ActiveAndroid.setTransactionSuccessful();
								} catch (Exception e) {
									e.printStackTrace();
								} finally {
									ActiveAndroid.endTransaction();
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
		if (!isNetworkAvailable()) {
			List<Tweet> tweetsFromDB = Tweet.getFromDB();
			if (tweetsFromDB.size() > 0) {
				aTweets.addAll(tweetsFromDB);
				Toast.makeText(TimelineActivity.this,
						"Cached tweets. No internet connection",
						Toast.LENGTH_SHORT).show();
				return;
			}
			Toast.makeText(TimelineActivity.this, "No internet connection",
					Toast.LENGTH_SHORT).show();
		} else {
			client.getHomeTimeline(offset, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONArray json) {
					// Log.d("DEBUG", json.toString());
					ArrayList<Tweet> tweets = Tweet.fromJSONArray(json);
					aTweets.addAll(tweets);

					// Save Data
					new Delete().from(Tweet.class).execute();
					new Delete().from(User.class).execute();

					ActiveAndroid.beginTransaction();
					try {
						for (Tweet tweet : tweets) {
							tweet.getUser().save();
							tweet.save();
						}
						ActiveAndroid.setTransactionSuccessful();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						ActiveAndroid.endTransaction();
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

	private Boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null
				&& activeNetworkInfo.isConnectedOrConnecting();
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
				Tweet tweetPosted = (Tweet) data
						.getSerializableExtra("postedTweet");
				aTweets.insert(tweetPosted, 0);
				aTweets.notifyDataSetChanged();
			}
		}
	}
}
