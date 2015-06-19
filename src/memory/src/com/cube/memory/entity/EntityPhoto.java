package com.cube.memory.entity;

public class EntityPhoto {
	private String date;
	private String uri;
	
	public EntityPhoto(String date, String uri){
		this.date = date;
		this.uri = uri;
	}
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
}
