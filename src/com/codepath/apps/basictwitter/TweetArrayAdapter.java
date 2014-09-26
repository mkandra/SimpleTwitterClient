package com.codepath.apps.basictwitter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.basictwitter.models.Tweet;
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

	public TweetArrayAdapter(Context context, List<Tweet> tweets) {
		super(context, 0, tweets);
		imageLoader= ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		//TimelineActivity.imageLoader.init(ImageLoaderConfiguration.createDefault(getContext()));
		//ImageLoader imageLoader = ImageLoader.getInstance();
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

		// Populate views with tweet data
		imageLoader.displayImage(tweet.getUser().getProfileImageUrl(),
				viewHolder.ivProfileImage);
		viewHolder.tvUserName.setText(tweet.getUser().getScreenName());
		viewHolder.tvBody.setText(tweet.getBody());
		String timeStamp = tweet.getCreatedAt();
		String timeStampFormatted = getRelativeTimeAgo(timeStamp);
		viewHolder.tvTimeStamp.setText(timeStampFormatted);
		return convertView;
	}

	// getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
	public String getRelativeTimeAgo(String rawJsonDate) {
		String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
		SimpleDateFormat sf = new SimpleDateFormat(twitterFormat,
				Locale.ENGLISH);
		sf.setLenient(true);

		String relativeDate = "";
		try {
			long dateMillis = sf.parse(rawJsonDate).getTime();
			relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
					System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS)
					.toString();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return relativeDate;
	}

}
