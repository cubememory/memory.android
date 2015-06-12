package com.cube.memory.util;

import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

public class CubeImageLoader {
	
	
	public static void display(String uri, ImageView imageView){
		ImageLoader.getInstance().displayImage(uri, imageView);
	}
}
