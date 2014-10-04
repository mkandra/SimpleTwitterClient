package com.codepath.apps.basictwitter.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.helpers.Utils;
import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.models.User;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class TweetArrayAdapter extends ArrayAdapter<Tweet> {
	private ImageLoader imageLoader;

	private static class ViewHolder {
		ImageView ivProfileImage;
		TextView tvUserName;
		TextView tvBody;
		TextView tvTimeStamp;
	}

	private OnTweetClickListener listener;

	public interface OnTweetClickListener {
		public void onProfileImageClick(User user);
		// public void onFavoriteClick(int pos, boolean isChecked);
		// public void onReplyClick(Tweet replyTweet);
	}

	public TweetArrayAdapter(Context context, List<Tweet> tweets,
			OnTweetClickListener listener) {
		super(context, 0, tweets);
		this.listener = listener;
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		// TimelineActivity.imageLoader.init(ImageLoaderConfiguration.createDefault(getContext()));
		// ImageLoader imageLoader = ImageLoader.getInstance();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Get the data item for position
		Tweet tweet = getItem(position);

		ViewHolder viewHolder;

		// Find or inflate the template
		if (convertView == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.tweet_item, parent, false);
			// Find the views within template
			viewHolder.ivProfileImage = (ImageView) convertView
					.findViewById(R.id.ivProfileImage);
			viewHolder.tvUserName = (TextView) convertView
					.findViewById(R.id.tvUserName);
			viewHolder.tvBody = (TextView) convertView
					.findViewById(R.id.tvBody);
			viewHolder.tvTimeStamp = (TextView) convertView
					.findViewById(R.id.tvTimeStamp);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.ivProfileImage.setImageResource(android.R.color.transparent);

		if (!Utils.isNetworkAvailable(getContext())) {
			// byte[] byteImage = tweet.getUser().getByteImage();
			// viewHolder.ivProfileImage.setImageBitmap(BitmapFactory
			// .decodeByteArray(byteImage, 0, byteImage.length));
		} else {
			// Populate views with tweet data
			imageLoader.displayImage(tweet.getUser().getProfileImageUrl(),
					viewHolder.ivProfileImage);
		}
		viewHolder.ivProfileImage.setTag(tweet.getUser());
		viewHolder.ivProfileImage
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						User user = (User) v.getTag();
						listener.onProfileImageClick(user);
					}
				});
		viewHolder.tvUserName.setText(tweet.getUser().getScreenName());
		viewHolder.tvBody.setText(tweet.getBody());
		// Linkify.addLinks(viewHolder.tvBody, Linkify.ALL);
		// viewHolder.tvBody.setMovementMethod(LinkMovementMethod.getInstance());
		String timeStamp = tweet.getCreatedAt();
		String timeStampFormatted = Utils.getRelativeTimeAgo(timeStamp);
		viewHolder.tvTimeStamp.setText(timeStampFormatted);
		return convertView;
	}

}
