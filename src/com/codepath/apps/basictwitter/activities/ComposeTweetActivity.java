package com.codepath.apps.basictwitter.activities;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.TwitterApplication;
import com.codepath.apps.basictwitter.TwitterClient;
import com.codepath.apps.basictwitter.helpers.Utils;
import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ComposeTweetActivity extends Activity {
	private TwitterClient client;
	private ImageView ivUserProfileImage;
	private TextView tvUserProfileName;
	private TextView tvUserScreenName;
	private EditText etTweet;
	private TextView tvNumCharsLeft;
	private Tweet tweetToReply;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose_tweet);
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.argb(255, 85, 172, 238))); // Twitter
																	// Colour
																	// Palette

		User auth_user = Utils.getAuthUser(this);
		
		tweetToReply = (Tweet) getIntent().getSerializableExtra("tweetToReply");

		client = TwitterApplication.getRestClient();

		ivUserProfileImage = (ImageView) findViewById(R.id.ivUserProfileImage);
		tvUserProfileName = (TextView) findViewById(R.id.tvUserProfileName);
		tvUserScreenName = (TextView) findViewById(R.id.tvUserScreenName);
		etTweet = (EditText) findViewById(R.id.etTweet);

		if (tweetToReply != null) {
			etTweet.setText("@" + tweetToReply.getUser().getScreenName() + " ");
			etTweet.setSelection(etTweet.getText().length());
		}

		etTweet.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				int numChars = s.toString().length();

				tvNumCharsLeft.setText(String.valueOf(100 - numChars));

				if (numChars > 100) {
					tvNumCharsLeft.setTextColor(Color.RED);
				} else {
					tvNumCharsLeft.setTextColor(Color.WHITE);
				}

			}

		});

		ivUserProfileImage.setImageResource(android.R.color.transparent);
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(auth_user.getProfileImageUrl(),
				ivUserProfileImage);

		tvUserProfileName.setText(auth_user.getName());
		tvUserScreenName.setText("@" + auth_user.getScreenName());
		etTweet.setSelection(etTweet.getText().length());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		tvNumCharsLeft = new TextView(this);
		tvNumCharsLeft.setText("100");
		tvNumCharsLeft.setTextColor(Color.DKGRAY);
		tvNumCharsLeft.setPadding(5, 0, 5, 0);
		tvNumCharsLeft.setTypeface(null, Typeface.BOLD);
		tvNumCharsLeft.setTextSize(14);
		menu.add(Menu.NONE, 0, Menu.NONE, "charWords")
				.setActionView(tvNumCharsLeft)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		getMenuInflater().inflate(R.menu.compose_menu, menu);

		return true;
	}

	public void onTweetSubmit(MenuItem item) {
		String tweetToPost = etTweet.getText().toString();

		if (!Utils.isNetworkAvailable(this)) {
			Toast.makeText(this, "No Internet connection!", Toast.LENGTH_SHORT)
					.show();
		}

		if (tweetToPost.length() > 100) {
			Toast.makeText(this, "Limit text to 100 characters",
					Toast.LENGTH_SHORT).show();
			return;
		}

		if (tweetToReply != null) {
			
			client.postReplyTweet(tweetToReply.getUid(), tweetToPost, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject jsonObject) {
					Toast.makeText(ComposeTweetActivity.this, "Reply posted",
							Toast.LENGTH_SHORT).show();
					Tweet tweet = Tweet.fromJson(jsonObject);

					Intent data = new Intent();
					data.putExtra("repliedTweet", tweet);
					setResult(RESULT_OK, data);
					finish();
				}

				@Override
				public void onFailure(Throwable e, String s) {
					Toast.makeText(ComposeTweetActivity.this,
							"Failed to reply! Try again",
							Toast.LENGTH_SHORT).show();
				}
			});
		} else {
			client.postTweet(tweetToPost, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject jsonObject) {
					Toast.makeText(ComposeTweetActivity.this, "Posted Tweet",
							Toast.LENGTH_SHORT).show();
					Tweet tweet = Tweet.fromJson(jsonObject);

					Intent data = new Intent();
					data.putExtra("postedTweet", tweet);
					setResult(RESULT_OK, data);
					finish();
				}

				@Override
				public void onFailure(Throwable e, String s) {
					Toast.makeText(ComposeTweetActivity.this,
							"Failed to post the tweet! Try again",
							Toast.LENGTH_SHORT).show();
				}
			});
		}
	}
}
