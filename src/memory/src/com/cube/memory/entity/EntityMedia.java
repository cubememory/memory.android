package com.cube.memory.entity;

public class EntityMedia {
	//media(image or video) id in the media store
	private long mediaID;
	
	//original file uri of the media
	private String uri;
	
	public EntityMedia(){
		
	}
	
	public EntityMedia(long mediaID, String uri){
		this.mediaID = mediaID;
		this.uri = uri;
	}
	
	public long getMediaID() {
		return mediaID;
	}

	public void setMediaID(long mediaID) {
		this.mediaID = mediaID;
	}
	
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
}
