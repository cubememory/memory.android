package com.cube.memory.entity;

import java.util.ArrayList;
import java.util.List;

public class EntityVideos {
	private List<EntityVideo> videos = new ArrayList<EntityVideo>();
	
	public EntityVideos(){
		
	}
	
	public EntityVideos(List<EntityVideo> videos){
		this.setVideos(videos);
	}
	
	public void setVideos(List<EntityVideo> videos){
		this.videos.addAll(videos);
	}
	
	public List<EntityVideo> getVideos(){
		return this.videos;
	}
}
