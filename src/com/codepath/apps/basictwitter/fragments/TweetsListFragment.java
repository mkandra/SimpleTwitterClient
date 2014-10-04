package com.codepath.apps.basictwitter.fragments;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.TwitterApplication;
import com.codepath.apps.basictwitter.TwitterClient;
import com.codepath.apps.basictwitter.activities.DetailedTweetActivity;
import com.codepath.apps.basictwitter.activities.ProfileActivity;
import com.codepath.apps.basictwitter.adapters.TweetArrayAdapter;
import com.codepath.apps.basictwitter.adapters.TweetArrayAdapter.OnTweetClickListener;
import com.codepath.apps.basictwitter.helpers.MySQLiteHelper;
import com.codepath.apps.basictwitter.listeners.EndlessScrollListener;
import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

public abstract class TweetsListFragment extends Fragment implements
		OnTweetClickListener {

	public abstract void refreshTimelines(TwitterClient client,
			long firstTweetUid);

	private ArrayList<Tweet> tweets;
	private TweetArrayAdapter aTweets;
	private ListView lvTweets;
	private TwitterClient client;
	private SwipeRefreshLayout swipeContainer;
	private MySQLiteHelper dbHelper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Non-view initialization
		tweets = new ArrayList<Tweet>();
		aTweets = new TweetArrayAdapter(getActivity(), tweets, this);
		dbHelper = new MySQLiteHelper(getActivity());
		client = TwitterApplication.getRestClient();
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	};

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// Inflate the layout
		View v = inflater.inflate(R.layout.fragment_tweets_list, container,
				false);
		// Assign the view references
		lvTweets = (ListView) v.findViewById(R.id.lvTweets);
		lvTweets.setAdapter(aTweets);

		lvTweets.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent i = new Intent(parent.getContext(),
						DetailedTweetActivity.class);
				Tweet tweet = tweets.get(position);
				i.putExtra("detailTweet", tweet);
				startActivity(i);
			}
		});

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

		swipeContainer = (SwipeRefreshLayout) v
				.findViewById(R.id.swipeContainer);
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
		return v;
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
			List<Tweet> tweetsFromDB = dbHelper.getAllTweets();

			if (tweetsFromDB.size() > 0) {
				aTweets.addAll(tweetsFromDB);
				Toast.makeText(getActivity(),
						"Cached tweets. No internet connection",
						Toast.LENGTH_SHORT).show();
				return;
			}
			Toast.makeText(getActivity(), "No internet connection",
					Toast.LENGTH_SHORT).show();
		} else {
			client.getHomeTimeline(offset, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONArray json) {
					// Log.d("DEBUG", json.toString());
					ArrayList<Tweet> tweets = Tweet.fromJSONArray(json);
					aTweets.addAll(tweets);

					for (Tweet tweet : tweets) {
						dbHelper.createUserEntry(tweet.getUser());
						dbHelper.createTweetEntry(tweet);
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

	private void refreshTimeline(long firstTweetUid) {
		if (!isNetworkAvailable()) {
			Toast.makeText(getActivity(), "No internet connection",
					Toast.LENGTH_SHORT).show();
		} else {
			refreshTimelines(client, firstTweetUid);
		}
	}

	private Boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getActivity()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null
				&& activeNetworkInfo.isConnectedOrConnecting();
	}

	// return the adapter to the activity
	// public TweetArrayAdapter getAdapter() {
	// return aTweets;
	// }

	// Delegate the adding to the internal adapter
	public void addAll(ArrayList<Tweet> tweets) {
		aTweets.addAll(tweets);
	}

	public void addAll(List<Tweet> tweets) {
		aTweets.addAll(tweets);
	}

	public void insert(Tweet tweet, int i) {
		aTweets.insert(tweet, i);

	}

	public void notifyDataSetChanged() {
		aTweets.notifyDataSetChanged();

	}

	public List<Tweet> getAllTweets() {
		return dbHelper.getAllTweets();
	}

	public void deleteAllTweets() {
		dbHelper.deleteAllTweets();
	}

	public void createUserEntry(User user) {
		dbHelper.createUserEntry(user);
	}

	public void createTweetEntry(Tweet tweet) {
		dbHelper.createTweetEntry(tweet);
	}

	@Override
	public void onProfileImageClick(User user) {
		Intent i = new Intent(getActivity(), ProfileActivity.class);
		i.putExtra("user", user);
		startActivity(i);
	}
}
