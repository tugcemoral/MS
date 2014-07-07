package com.instalogin.dao;

public class InstagramUser {
	
	private String id;
	private String username;
	private String fullname; 
	private String profilePicLink;
	private String website;
	private String bio;
	
	public InstagramUser(String id, String username, String fullname,
			String profilePicLink, String website, String bio) {
		this.id = id;
		this.username = username;
		this.fullname = fullname;
		this.profilePicLink = profilePicLink;
		this.website = website;
		this.bio = bio;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getProfilePicLink() {
		return profilePicLink;
	}

	public void setProfilePicLink(String profilePicLink) {
		this.profilePicLink = profilePicLink;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}



}