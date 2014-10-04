SimpleTwitterClient
===================

A simple Twitter client using fragments.

Time Spent: 15 Hrs

Completed user stories:
* [x] Required: User can sign in to Twitter using OAuth login.
* [x] Required: User can view the tweets from their home timeline.
	- User is displayed the username, name, and body for each tweet
	- User is displayed the relative timestamp for each tweet
	- User can view more tweets as they scroll with infinite pagination
	- Optional: Links in tweets are clickable and will launch the web browser.
* [x] Required: User can compose a new tweet
	- User can click a “Compose” icon in the Action Bar on the top right
	- User can then enter a new tweet and post this to twitter
	- User is taken back to home timeline with new tweet visible in timeline
	- Optional: User can see a counter with total number of characters left for tweet
* [x] Required: User can switch between Timeline and Mention views using tabs
        - User can view their home timeline tweets.
        - User can view the recent mentions of their username.
        - User can do infinite scroll in these lists.
* [x] Required: User can navigate to view their own profile
        - User can see picture, tagline, # of followers, # of following, and tweets on their profile.
* [x] Required: User can see another user's profile. by clicking on their profile image.
        - User can see picture, tagline, # of followers, # of following, and tweets of that user.
        - Profile view includes that user's timeline
* [x] Optional: Advanced: User can refresh tweets timeline by pulling down to refresh.
* [x] Optional: User can tap a tweet to display a "detailed" view of that tweet.
* [x] Optional: User can select "reply" from detail view to respond to a tweet.
* [x] Optional: Some tweaking of colors and icons.
* [x] Optional: User can open the twitter app offline and see last loaded tweets.
* [x] Optional: Used SQLiteOpenHelper for data persistence.

Walkthrough of above user stories:

![Video Walkthrough](SimpleTwitterClientV2Demo.gif)

GIF created with [LiceCap](http://www.cockos.com/licecap/).
