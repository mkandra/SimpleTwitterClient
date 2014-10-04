package com.codepath.apps.basictwitter.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;

import com.codepath.apps.basictwitter.models.User;

public class Utils {

	public static Boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null
				&& activeNetworkInfo.isConnectedOrConnecting();
	}

	// getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
	public static String getRelativeTimeAgo(String rawJsonDate) {
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

	public static void storeAuthUser(Context context, User auth_user) {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor edit = pref.edit();
		edit.putString("auth_user_uid", String.valueOf(auth_user.getUid()));
		edit.putString("auth_user_name", auth_user.getName());
		edit.putString("auth_user_screen_name", auth_user.getScreenName());
		edit.putString("auth_user_profile_image_url",
				auth_user.getProfileImageUrl());
		edit.commit();
	}

	public static User getAuthUser(Context context) {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		String auth_user_id = pref.getString("auth_user_uid", "");
		String auth_user_name = pref.getString("auth_user_name", "");
		String auth_user_screen_name = pref.getString("auth_user_screen_name",
				"");
		String auth_user_profile_image_url = pref.getString(
				"auth_user_profile_image_url", "");

		User auth_user = new User();
		auth_user.setUid(Long.parseLong(auth_user_id));
		auth_user.setName(auth_user_name);
		auth_user.setScreenName(auth_user_screen_name);
		auth_user.setProfileImageUrl(auth_user_profile_image_url);

		return auth_user;
	}



}
