package com.journaldev.elasticsearch.bean;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Conference {
	
	String id;
	
	String hashtag;
	
	String personne;
	
	String date;
	
	String tweetText;
	
	String Tweet;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getHashtag() {
		return hashtag;
	}
	public void setHashtag(String hashtag) {
		this.hashtag = hashtag;
	}
	public String getPersonne() {
		return personne;
	}
	public void setPersonne(String personne) {
		this.personne = personne;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTweetText() {
		return tweetText;
	}
	public void setTweetText(String tweetText) {
		this.tweetText = tweetText;
	}
	public String getTweet() {
		return Tweet;
	}
	public void setTweet(String tweet) {
		Tweet = tweet;
	}
	
	
	

    
}
