package com.codepath.apps.basictwitter.activities;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.TwitterApplication;
import com.codepath.apps.basictwitter.fragments.UserTimelineFragment;
import com.codepath.apps.basictwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		Intent i = getIntent();

		if (i.hasExtra("user")) {
			User user = (User) i.getSerializableExtra("user");
			getActionBar().setTitle("@" + user.getScreenName());
			populateProfileHeader(user);
			setupUserTimelineFragment(user);
		} else {
			loadProfileInfo();
		}

	}

	private void loadProfileInfo() {
		TwitterApplication.getRestClient().getAuthenticatedUser(
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject json) {
						User u = User.fromJson(json);
						getActionBar().setTitle("@" + u.getScreenName());
						populateProfileHeader(u);
						setupUserTimelineFragment(u);
					}

				});

	}

	private void populateProfileHeader(User u) {
		TextView tvName = (TextView) findViewById(R.id.tvName);
		TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
		TextView tvNumTweets = (TextView) findViewById(R.id.tvNumTweets);
		TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
		TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
		ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
		tvName.setText(u.getName());
		tvTagline.setText(u.getTagline());
		tvNumTweets.setText(u.getTweetsCount() + " Tweets");
		tvFollowers.setText(u.getFollowersCount() + " Followers");
		tvFollowing.setText(u.getFollowingCount() + " Following");
		ImageLoader.getInstance().displayImage(u.getProfileImageUrl(),
				ivProfileImage);
	}

	private void setupUserTimelineFragment(User user) {
		UserTimelineFragment fragmentUserTimeline = UserTimelineFragment
				.newInstance(user);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.flTimelineContainer, fragmentUserTimeline);
		ft.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
