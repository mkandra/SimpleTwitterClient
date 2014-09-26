package com.codepath.apps.basictwitter.models;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "Users")
public class User extends Model implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -7815730823885250549L;
	@Column(name = "remote_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private long uid;
    @Column(name = "Name")
	private String name;
    @Column(name = "screen_name")
	private String screenName;
    @Column(name = "profile_image_url")
	private String profileImageUrl;
	
    
	public static User fromJson(JSONObject jsonObject) {
		User user = new User();
		// Extract values from the json to populate the member variables
		try {
			user.name = jsonObject.getString("name");
			user.uid = jsonObject.getLong("id");
			user.screenName = jsonObject.getString("screen_name");
			user.profileImageUrl = jsonObject.getString("profile_image_url");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return user;
	}

	public static User getAuthUser() {
        // This is how you execute a query
        return new Select()
          .from(User.class)
          .where("auth_user = ?", 1)
          .executeSingle();
	}
	
	public String getName() {
		return name;
	}

	public long getUid() {
		return uid;
	}

	public String getScreenName() {
		return screenName;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

}
