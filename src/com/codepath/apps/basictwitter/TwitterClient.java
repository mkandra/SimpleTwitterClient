package com.codepath.apps.basictwitter;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change
																				// this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change
																			// this,
																			// base
																			// API
																			// URL
	public static final String REST_CONSUMER_KEY = "imQmNu4DPOjLCitZAA4wnBs0M"; // Change
																				// this
	public static final String REST_CONSUMER_SECRET = "2RiQaDlRpFbwxDGj8YSiN6SypltJ1WxbWoET3uC65GUZ3XM6OD"; // Change
																											// this
	public static final String REST_CALLBACK_URL = "oauth://cpbasictweets"; // Change
																			// this
																			// (here
																			// and
																			// in
																			// manifest)

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY,
				REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	public void getHomeTimeline(long offset, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count", "20");
		if (offset != 0)
			params.put("max_id", String.valueOf(offset));

		client.get(apiUrl, params, handler);
	}

	public void refreshHomeTimeline(long sinceId,
			AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count", "20");
		params.put("since_id", String.valueOf(sinceId));

		client.get(apiUrl, params, handler);
	}

	public void getAuthenticatedUser(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("account/verify_credentials.json");
		client.get(apiUrl, null, handler);
	}

	public void postTweet(String tweet, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("/statuses/update.json");

		RequestParams params = new RequestParams();
		params.put("status", tweet);

		client.post(apiUrl, params, handler);
	}

	public void postReplyTweet(long uid, String replyTweet,
			AsyncHttpResponseHandler jsonHttpResponseHandler) {
		String apiUrl = getApiUrl("/statuses/update.json");

		RequestParams params = new RequestParams();
		params.put("status", replyTweet);
		params.put("in_reply_to_status_id", String.valueOf(uid));

		client.post(apiUrl, params, jsonHttpResponseHandler);
	}

	public void getMentionsTimeline(long offset,
			AsyncHttpResponseHandler jsonHttpResponseHandler) {
		String apiUrl = getApiUrl("statuses/mentions_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count", "20");
		if (offset != 0)
			params.put("max_id", String.valueOf(offset));

		client.get(apiUrl, params, jsonHttpResponseHandler);
	}

	public void getUserTimeline(String screenName,
			AsyncHttpResponseHandler jsonHttpResponseHandler) {
		String apiUrl = getApiUrl("statuses/user_timeline.json");
		RequestParams params = new RequestParams();
		params.put("screen_name", screenName);
		client.get(apiUrl, params, jsonHttpResponseHandler);
	}

	public void refreshMentionsTimeline(long sinceId,
			AsyncHttpResponseHandler jsonHttpResponseHandler) {
		String apiUrl = getApiUrl("statuses/mentions_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count", "20");
		params.put("since_id", String.valueOf(sinceId));

		client.get(apiUrl, params, jsonHttpResponseHandler);

	}

	// CHANGE THIS
	// DEFINE METHODS for different API endpoints here
	/*
	 * public void getInterestingnessList(AsyncHttpResponseHandler handler) {
	 * String apiUrl =
	 * getApiUrl("?nojsoncallback=1&method=flickr.interestingness.getList"); //
	 * Can specify query string params directly or through RequestParams.
	 * RequestParams params = new RequestParams(); params.put("format", "json");
	 * client.get(apiUrl, params, handler); }
	 */

	/*
	 * 1. Define the endpoint URL with getApiUrl and pass a relative path to the
	 * endpoint i.e getApiUrl("statuses/home_timeline.json"); 2. Define the
	 * parameters to pass to the request (query or body) i.e RequestParams
	 * params = new RequestParams("foo", "bar"); 3. Define the request method
	 * and make a call to the client i.e client.get(apiUrl, params, handler);
	 * i.e client.post(apiUrl, params, handler);
	 */
}