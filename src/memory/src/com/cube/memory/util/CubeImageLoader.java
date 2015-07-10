package com.cube.memory.util;

import android.content.Context;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class CubeImageLoader {
	
	public static void init(Context context){
		ImageLoader.getInstance().init(new ImageLoaderConfiguration.Builder(context).build());
	}
	
	public static void display(String uri, ImageView imageView){
		ImageLoader.getInstance().displayImage(uri, imageView);
	}
}
