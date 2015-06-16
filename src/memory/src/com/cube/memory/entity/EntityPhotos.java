package com.cube.memory.entity;

import java.util.ArrayList;
import java.util.List;

public class EntityPhotos {
	private int total;
	private List<EntityAlbum> albums = new ArrayList<EntityAlbum>();

	public EntityPhotos() {

	}
	
	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
	
	public void addAlbum(String date, List<EntityPhoto> photos) {
		this.albums.add(new EntityAlbum(date, photos));
	}

	public void setAlbums(List<EntityAlbum> albums) {
		this.albums.addAll(albums);
	}

	public List<EntityAlbum> getAlbums() {
		return this.albums;
	}

	public static class EntityAlbum {
		private String date;
		private int total;
		private List<EntityPhoto> photos = new ArrayList<EntityPhoto>();

		public EntityAlbum(String date, List<EntityPhoto> photos) {
			this.setPhotos(photos);
		}

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public int getTotal() {
			return total;
		}

		public void setTotal(int total) {
			this.total = total;
		}

		public void setPhotos(List<EntityPhoto> photos) {
			this.photos.addAll(photos);
		}

		public List<EntityPhoto> getPhotos() {
			return this.photos;
		}
	}
}
