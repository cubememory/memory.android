package com.cube.memory.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.cube.memory.entity.EntityPhotoRecord;
import com.cube.memory.loader.LoaderPhotoRecord;
import com.cube.memory.widget.ViewPhotoRecordItem;

public class AdapterPhotoRecord extends ArrayAdapter<EntityPhotoRecord>{
	private LoaderPhotoRecord mLoaderPhotoRecord;
	
	public AdapterPhotoRecord(Context context, LoaderPhotoRecord loaderPhotoRecord) {
		super(context, 0);
		mLoaderPhotoRecord = loaderPhotoRecord;
	}
	
	public void addPhotoRecords(List<EntityPhotoRecord> records){
		//set the records with new data
		this.addAll(records);
	}
	
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.i("memory", "position: "+position);
		View view = null;
		if(convertView != null){
			view = convertView;
		}else{
			view = new ViewPhotoRecordItem(parent.getContext(), this.getItem(position));
		}
		
		if(position+2 > this.getCount()){
			mLoaderPhotoRecord.forceLoad();
		}
		return view;
	}
	
	
}

