package com.cube.memory.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public class CubeInflater {
	public static void inflate(Context context, int resource, ViewGroup root){
		/*inflate the layout xml for view record item*/
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(resource, root);		
	}
	
	public static void inflate(Context context, int resource, ViewGroup root, boolean attachToRoot){
		/*inflate the layout xml for view record item*/
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(resource, root, attachToRoot);		
	}
}
