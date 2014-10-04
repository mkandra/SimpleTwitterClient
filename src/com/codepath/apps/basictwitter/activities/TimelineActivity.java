package com.codepath.apps.basictwitter.activities;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.fragments.HomeTimelineFragment;
import com.codepath.apps.basictwitter.fragments.MentionsTimelineFragment;
import com.codepath.apps.basictwitter.helpers.Utils;
import com.codepath.apps.basictwitter.listeners.FragmentTabListener;
import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.models.User;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TimelineActivity extends FragmentActivity {
	public static final int COMPOSE_TWEET_ACTIVITY_REQUEST_CODE = 10;

	public static ImageLoader imageLoader = ImageLoader.getInstance();

	// private MySQLiteHelper dbHelper;

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

		setupTabs();

		// dbHelper = new MySQLiteHelper(getApplicationContext());

	}

	private void setupTabs() {
		ActionBar actionBar = getActionBar();

		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);

		Tab tab1 = actionBar
				.newTab()
				.setText("Home")
				.setIcon(R.drawable.ic_home)
				.setTag("HomeTimelineFragment")
				.setTabListener(
						new FragmentTabListener<HomeTimelineFragment>(
								R.id.flContainer, this, "home",
								HomeTimelineFragment.class));
		actionBar.addTab(tab1);
		actionBar.selectTab(tab1);

		Tab tab2 = actionBar
				.newTab()
				.setText("Mentions")
				.setIcon(R.drawable.ic_mentions)
				.setTag("MentionsTimelineFragment")
				.setTabListener(
						new FragmentTabListener<MentionsTimelineFragment>(
								R.id.flContainer, this, "mentions",
								MentionsTimelineFragment.class));

		actionBar.addTab(tab2);
	}

	// Append more data into the adapter
	public void customLoadMoreDataFromApi(long offset) {
		// This method probably sends out a network request and appends new data
		// items to your adapter.
		// Use the offset value and add it as a parameter to your API request to
		// retrieve paginated data.
		// Deserialize API response and then construct new objects to append to
		// the adapter
		// populateTimeline(offset);
	}

	public void onProfileView(MenuItem mi) {
		Intent i = new Intent(this, ProfileActivity.class);
		startActivity(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.timeline, menu);
		return true;
	}

	public void onComposeTweet(MenuItem item) {
		Intent i = new Intent(this, ComposeTweetActivity.class);
		User auth_user = Utils.getAuthUser(this);
		i.putExtra("auth_user", auth_user);
		startActivityForResult(i, COMPOSE_TWEET_ACTIVITY_REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == COMPOSE_TWEET_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				Tweet tweetPosted = (Tweet) data
						.getSerializableExtra("postedTweet");
				HomeTimelineFragment fragmentHomeTimeline = (HomeTimelineFragment) getSupportFragmentManager()
						.findFragmentByTag("home");
				fragmentHomeTimeline.insert(tweetPosted, 0);
			}
		}
	}
}
