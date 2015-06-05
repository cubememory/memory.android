package com.cube.memory.entity;

import android.net.Uri;

public class EntityPhotoRecord {
	private String date;
	private String photo;
	private String weather;
	private String description;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getPhoto() {
		return photo;
	}

	public Uri getPhotoURI() {
		return Uri.parse(photo);
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getWeather() {
		return weather;
	}

	public Uri getWeatherURI() {
		return Uri.parse(weather);
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
