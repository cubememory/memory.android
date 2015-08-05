package com.cube.memory.entity;

public class EntityVideo extends EntityMedia{
	private String date;
	
	public EntityVideo(String date, long mediaID, String uri){
		super(mediaID, uri);
		this.date = date;
	}
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
}
