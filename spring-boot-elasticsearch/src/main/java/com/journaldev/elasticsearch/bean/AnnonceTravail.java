package com.journaldev.elasticsearch.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AnnonceTravail {
	
	String id;
	
	String poste;
	
	String datePublication;
	
	String tweetText;
	
	String Tweet;
	
	String location;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPoste() {
		return poste;
	}

	public void setPoste(String poste) {
		this.poste = poste;
	}

	public String getDatePublication() {
		return datePublication;
	}

	public void setDatePublication(String datePublication) {
		this.datePublication = datePublication;
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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	
}
