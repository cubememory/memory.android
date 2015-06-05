package com.cube.memory.loader;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.cube.memory.entity.EntityPhotoRecord;

public class LoaderPhotoRecord extends AsyncTaskLoader<List<EntityPhotoRecord>> {
	
	public LoaderPhotoRecord(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<EntityPhotoRecord> loadInBackground() {
		List<EntityPhotoRecord> records = new ArrayList<EntityPhotoRecord>();
		for (int i = 0; i < 20; i++) {
			EntityPhotoRecord record = new EntityPhotoRecord();
			record.setDate("2015.12.12");
			record.setPhoto("http://a.hiphotos.baidu.com/image/w%3D400/sign=687d90fec1fdfc03e578e2b8e43e87a9/4afbfbedab64034fd871a406adc379310a551d75.jpg");
			record.setWeather("天气-晴");
			records.add(record);
		}

		return records;
	}

	@Override
	public void onCanceled(List<EntityPhotoRecord> data) {
		// TODO Auto-generated method stub
		super.onCanceled(data);
	}

	@Override
	public void deliverResult(List<EntityPhotoRecord> data) {
		// TODO Auto-generated method stub
		super.deliverResult(data);
	}

	@Override
	protected void onAbandon() {
		// TODO Auto-generated method stub
		super.onAbandon();
	}

	@Override
	protected void onReset() {
		// TODO Auto-generated method stub
		super.onReset();
	}

	@Override
	protected void onStartLoading() {
		forceLoad();
	}

	@Override
	protected void onStopLoading() {
		// TODO Auto-generated method stub
		super.onStopLoading();
	}

}
