package com.cube.memory.entity;

public class EntityPhoto extends EntityMedia{
	private String date;
	
	public EntityPhoto(String date, long mediaID, String uri){
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
