package com.codepath.apps.basictwitter;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.basictwitter.models.Tweet;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DetailedTweetActivity extends Activity {

	private Tweet detailedTweet;
	private ImageLoader imageLoader;
	private ImageView ivProfImage;
	private TextView tvUsersName;
	private TextView tvScreenName;
	private TextView tvTweetBody;
	private ImageView ivTweetReply;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detailed_tweet);
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.argb(255, 85, 172, 238))); // Twitter
																	// Colour
																	// Palette
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getActionBar().setCustomView(R.layout.detail_actvity_abs);

		detailedTweet = (Tweet) getIntent().getSerializableExtra("detailTweet");

		ivProfImage = (ImageView) findViewById(R.id.ivProfImage);
		tvUsersName = (TextView) findViewById(R.id.tvUsersName);
		tvScreenName = (TextView) findViewById(R.id.tvScreenName);
		tvTweetBody = (TextView) findViewById(R.id.tvTweetBody);

		// Populate views with tweet data
		imageLoader = ImageLoader.getInstance();
		ivProfImage.setImageResource(android.R.color.transparent);
		imageLoader.displayImage(detailedTweet.getUser().getProfileImageUrl(),
				ivProfImage);

		tvUsersName.setText(detailedTweet.getUser().getName());
		tvScreenName.setText("@" + detailedTweet.getUser().getScreenName());
		tvTweetBody.setText(detailedTweet.getBody());

		ivTweetReply = (ImageView) findViewById(R.id.ivTweetReply);

	}

	public void onReplyTweet(View v) {
		Intent i = new Intent(this, ComposeTweetActivity.class);
		i.putExtra("tweetToReply", detailedTweet);
		startActivity(i);
	}
}
