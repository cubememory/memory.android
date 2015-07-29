package com.cube.memory.util;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.cube.sdk.util.CFormat;

public class CubeMediaScanner {
	public static List<Image> getImages(Context context){
		List<Image> images = new ArrayList<Image>();
		
		images.addAll(getImages(context, MediaStore.Images.Media.INTERNAL_CONTENT_URI));
		images.addAll(getImages(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
		
		return images;
	}
	
	@SuppressLint("InlinedApi") 
	public static List<Image> getImages(Context context, Uri uri){
		List<Image> images = new ArrayList<Image>();
		
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(uri, null, null, null, null);
		
		while(cursor.moveToNext()){
			Image image = new Image();
			image.setType(cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.MIME_TYPE)));
			image.setName(cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME)));
			image.setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)));
			image.setWidth(cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.WIDTH)));
			image.setHeight(cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.HEIGHT)));
			image.setSize(cursor.getLong(cursor.getColumnIndex(MediaStore.Images.ImageColumns.SIZE)));
			
			image.setLatitude(cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.LATITUDE)));
			image.setLongitude(cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.LONGITUDE)));
			
			image.setCreateTime(cursor.getLong(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_ADDED)));
			image.setModifyTime(cursor.getLong(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_MODIFIED)));
			
			images.add(image);
		}
		
		return images;
	}
		
	public static class Image{
		private String type;
		private String name;
		private String path;
		private int width;
		private int height;
		private long size;
		
		private int latitude;
		private int longitude;
		
		private long createTime;
		private long modifyTime;
		
		private String date;
		
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getPath() {
			return path;
		}
		public void setPath(String path) {
			this.path = path;
		}
		public int getWidth() {
			return width;
		}
		public void setWidth(int width) {
			this.width = width;
		}
		public int getHeight() {
			return height;
		}
		public void setHeight(int height) {
			this.height = height;
		}
		public long getSize() {
			return size;
		}
		public void setSize(long size) {
			this.size = size;
		}
		public int getLatitude() {
			return latitude;
		}
		public void setLatitude(int latitude) {
			this.latitude = latitude;
		}
		public int getLongitude() {
			return longitude;
		}
		public void setLongitude(int longitude) {
			this.longitude = longitude;
		}
		public long getCreateTime() {
			return createTime;
		}
		public void setCreateTime(long createTime) {
			this.createTime = createTime;
			this.date = CFormat.parseDate("yyyyMMdd", createTime);
		}
		public long getModifyTime() {
			return modifyTime;
		}
		public void setModifyTime(long modifyTime) {
			this.modifyTime = modifyTime;
		}
		public String getDate() {
			return date;
		}
		public void setDate(String date) {
			this.date = date;
		}
	}
}
