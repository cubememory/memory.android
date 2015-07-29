package com.cube.sdk.util;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

public class CScanner {
	public static class BaseItem{
		public int id;
		public int count;
	}
	
	public static class MediaItem extends BaseItem{
		public String name;
		public String title;
		public String path;
		public String type;
		
		public int isDrm;
		
		public long size;
		
		public int width;
		public int height;
		
		public int latitude;
		public int longitude;
		
		public long createTime;
		public long modifyTime;

	}
	
	public static class Image{
		public static List<Gallery> scan(Context context){
			List<Gallery> galleries = new ArrayList<Gallery>();
			galleries.add(scan(context, MediaStore.Images.Media.INTERNAL_CONTENT_URI));
			galleries.add(scan(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
			return galleries;
		}
		
		@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
		public static Gallery scan(Context context, Uri uri){
			Gallery gallery = new Gallery(uri);
			
			ContentResolver resolver = context.getContentResolver();
			Cursor cursor = resolver.query(uri, null, null, null, null);
			while(cursor.moveToNext()){
				Item item = new Item();
				item.name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME));
				item.path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
				item.type = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.MIME_TYPE));
				item.size = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.ImageColumns.SIZE));
				
				if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
					item.width = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.WIDTH));
					item.height = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.HEIGHT));
				}
				
				item.latitude = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.LATITUDE));
				item.longitude = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.LONGITUDE));
				
				item.createTime = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_ADDED));
				item.modifyTime = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_MODIFIED));
				
				gallery.addItem(item);
			}
			
			return gallery;		
		}
		
		public static class Gallery{
			private Uri uri;
			private List<Item> items = new ArrayList<Item>();
			
			public Gallery(Uri uri){
				this.uri = uri;
			}
			
			public Uri getUri(){
				return this.uri;
			}
			
			public List<Item> getItems(){
				return this.items;
			}
			
			public void addItem(Item item){
				this.items.add(item);
			}
		}
		
		public static class Item{
			public String name;
			public String path;
			public String type;
			public long size;
			
			public int width;
			public int height;
			
			public int latitude;
			public int longitude;
			
			public long createTime;
			public long modifyTime;
		}
	}
	
	public static class Video{
		public static List<Gallery> scan(Context context){
			List<Gallery> galleries = new ArrayList<Gallery>();
			galleries.add(scan(context, MediaStore.Video.Media.INTERNAL_CONTENT_URI));
			galleries.add(scan(context, MediaStore.Video.Media.EXTERNAL_CONTENT_URI));
			return galleries;
		}
		
		@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
		public static Gallery scan(Context context, Uri uri){
			Gallery gallery = new Gallery(uri);
			
			ContentResolver resolver = context.getContentResolver();
			Cursor cursor = resolver.query(uri, null, null, null, null);
			while(cursor.moveToNext()){
				ImageItem item = new ImageItem();
				//item.setName(cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.)));
				item.setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)));
				item.setType(cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.MIME_TYPE)));
				item.setSize(cursor.getLong(cursor.getColumnIndex(MediaStore.Images.ImageColumns.SIZE)));
				
				if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
					item.setWidth(cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.WIDTH)));
					item.setHeight(cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.HEIGHT)));
				}else{
					item.setWidth(0);
					item.setHeight(0);
				}
				
				item.setLatitude(cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.LATITUDE)));
				item.setLongitude(cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.LONGITUDE)));
				
				item.setCreateTime(cursor.getLong(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_ADDED)));
				item.setModifyTime(cursor.getLong(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_MODIFIED)));
				
				gallery.addItem(item);
			}
			
			return gallery;		
		}
		
		public static class Gallery{
			private Uri uri;
			private List<ImageItem> items = new ArrayList<ImageItem>();
			
			public Gallery(Uri uri){
				this.uri = uri;
			}
			
			public Uri getUri(){
				return this.uri;
			}
			
			public List<ImageItem> getItems(){
				return this.items;
			}
			
			public void addItem(ImageItem item){
				this.items.add(item);
			}
		}
		
		public static class ImageItem{
			private String name;
			private String path;
			private String type;
			private long size;
			
			private int width;
			private int height;
			
			private int latitude;
			private int longitude;
			
			private long createTime;
			private long modifyTime;
			
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
			public String getType() {
				return type;
			}
			public void setType(String type) {
				this.type = type;
			}
			public long getSize() {
				return size;
			}
			public void setSize(long size) {
				this.size = size;
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
			}
			public long getModifyTime() {
				return modifyTime;
			}
			public void setModifyTime(long modifyTime) {
				this.modifyTime = modifyTime;
			}
		}		
	}
}
