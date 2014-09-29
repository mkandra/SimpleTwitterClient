SimpleTwitterClient
===================

A simple Twitter client that supports viewing a Twitter timeline and composing a new tweet.

Time Spent: 16 Hrs

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
* [x] Optional: Advanced: User can refresh tweets timeline by pulling down to refresh.
* [x] Optional: User can tap a tweet to display a "detailed" view of that tweet.
* [x] Optional: User can select "reply" from detail view to respond to a tweet.
* [x] Optional: User can open the twitter app offline and see last loaded tweets.
	- offline support is buggy. Need to debug this.
* [x] Optional: Some tweaking of colors and icons.	

Walkthrough of above user stories:

![Video Walkthrough](SimpleTwitterClientDemo.gif)

GIF created with [LiceCap](http://www.cockos.com/licecap/).
