package com.cube.memory.adapter;

import java.util.ArrayList;
import java.util.List;

import android.widget.BaseAdapter;

public abstract class AdapterPage<T> extends BaseAdapter{
	/*objects in the adapter*/
	private List<T> mObjects = new ArrayList<T>();
	
	public AdapterPage(){
	}
	
	public synchronized void add(T object){
		this.mObjects.add(object);
	}
	
	public synchronized void add(List<T> objects){
		this.mObjects.addAll(objects);
	}

	@Override
	public int getCount() {
		return mObjects.size();
	}

	@Override
	public Object getItem(int position) {
		if(position < mObjects.size()){
			return mObjects.get(position);
		}else{
			return null;
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}	
}

