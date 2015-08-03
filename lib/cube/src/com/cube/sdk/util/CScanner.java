/**
 * wrap the media scanner for image/video/audio/...
 * @author wruibo
 */
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
		//unique row id for the item
		public int _id;
		//the count of rows in the directory
		public int _count;
	}
	
	public static class MediaItem extends BaseItem{
		//display name of the file
		public String name;
		//the title of the content
		public String title;
		//file path of the media
		public String path;
		//mime type of the media file
		public String mimeType;
		//whether the media file is drm-protected
		public int isDrm;
		//size of the file in bytes
		public long size;
		
		//the width for the media(image/video) in pixels
		public int width;
		//the height for the media(image/video) in pixels		
		public int height;

		//unix time stamp in million seconds for the media file add to the media store
		public long createTime;
		//unix time stamp in million seconds for the media file last modified
		public long modifiedTime;

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
				item._id = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID));
				item._count = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns._COUNT));
				
				item.name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME));
				item.title = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.TITLE));
				item.path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
				item.mimeType = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.MIME_TYPE));
				item.isDrm = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.IS_PRIVATE));
				item.size = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.ImageColumns.SIZE));
				if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
					item.width = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.WIDTH));
					item.height = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.HEIGHT));
				}
				item.createTime = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_ADDED));
				item.modifiedTime = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_MODIFIED));
				
				item.description = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DESCRIPTION));
				item.picasaID = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.PICASA_ID));
				item.isPrivate = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.IS_PRIVATE));
				item.dateTaken = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_TAKEN));
				item.orientation = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.ORIENTATION));
				item.latitude = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.LATITUDE));
				item.longitude = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.LONGITUDE));
				item.miniThumbMagic = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.MINI_THUMB_MAGIC));
				item.bucketID = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.BUCKET_ID));
				item.bucketDisplayName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME));
				
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
		
		public static class Item extends MediaItem{
			//the description of the image
			public String description;
			//the picasa id of the image
			public String picasaID;
			//whether the video should be published as public or private
			public int isPrivate;
			//unix time stamp in million seconds of the time that the image was taken
			public long dateTaken;
			//the orientation for the image expressed as degrees. only degrees 0, 90, 180, 270 will work.
			public int orientation;
			
			//the longitude where the image was captured
			public double longitude;
			//the latitude where the image was captured
			public double latitude;
			
            //the mini thumb id.
            public int miniThumbMagic;
            
            //the bucket id of the image. This is a read-only property that is automatically computed from the DATA column.
            public String bucketID;
            
            //The bucket display name of the image. This is a read-only property that is automatically computed from the DATA column.
            public String bucketDisplayName;
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
				Item item = new Item();
				item._id = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.VideoColumns._ID));
				item._count = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.VideoColumns._COUNT));
				
				item.name = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DISPLAY_NAME));
				item.title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.TITLE));
				item.path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA));
				item.mimeType = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.MIME_TYPE));
				item.isDrm = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.VideoColumns.IS_PRIVATE));
				item.size = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.VideoColumns.SIZE));
				if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
					item.width = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.VideoColumns.WIDTH));
					item.height = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.VideoColumns.HEIGHT));
				}
				item.createTime = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATE_ADDED));
				item.modifiedTime = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATE_MODIFIED));
				
				item.duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DURATION));
				item.artist = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.ARTIST));
				item.album = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.ALBUM));
				item.resolution = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.RESOLUTION));
				item.description = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DESCRIPTION));
				item.isPrivate = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.VideoColumns.IS_PRIVATE));
				item.tags = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.TAGS));
				item.category = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.CATEGORY));
				item.language = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.LANGUAGE));
				item.dateTaken = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATE_TAKEN));
				item.latitude = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.VideoColumns.LATITUDE));
				item.longitude = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.VideoColumns.LONGITUDE));
				item.miniThumbMagic = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.VideoColumns.MINI_THUMB_MAGIC));
				item.bucketID = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.BUCKET_ID));
				item.bucketDisplayName = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME));
				item.bookmark = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.VideoColumns.BOOKMARK));
				
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
		
		public static class Item extends MediaItem{
			//the duration of the video file, in ms
			public long duration;
			//the artist who created the video file, if any
			public String artist;
			//the album the video file is from, if any
			public String album;
			//the resolution of the video file, formatted as "XxY"
			public String resolution;			
			//the description of the video
			public String description;
			//whether the video should be published as public or private
			public int isPrivate;
			//the user-added tags associated with a video
			public String tags;
			//the YouTube category of the video
			public String category;
			//The language of the video
			public String language;
			
			//unix time stamp in million seconds of the time that the image was taken
			public long dateTaken;
			
			//the longitude where the image was captured
			public double longitude;
			//the latitude where the image was captured
			public double latitude;
			
            //the mini thumb id.
            public int miniThumbMagic;
            //the bucket id of the image. This is a read-only property that is automatically computed from the DATA column.
            public String bucketID;
            //The bucket display name of the image. This is a read-only property that is automatically computed from the DATA column.
            public String bucketDisplayName;
            /**
             * The bookmark for the video. Time in ms. Represents the location in the video that the
             * video should start playing at the next time it is opened. If the value is null or
             * out of the range 0..DURATION-1 then the video should start playing from the
             * beginning.
             */
            public long bookmark;
		}
	}
}
