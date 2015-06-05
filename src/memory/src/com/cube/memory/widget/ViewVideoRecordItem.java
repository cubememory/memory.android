package com.cube.memory.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cube.memory.entity.EntityPhotoRecord;
import com.cube.memory.phone.R;

public class ViewVideoRecordItem extends RelativeLayout{
	private TextView mDateTextView;
	private TextView mWeatherTextView;
	private ImageView mPhotoImageView;
	
	public ViewVideoRecordItem(Context context, EntityPhotoRecord record) {
		super(context);
		
		/*inflate the layout xml for view record item*/
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_photo_record_item, this);
		
		/*find the relate view items from layout xml*/
		mDateTextView = (TextView)findViewById(R.id.text_record_date);
		mWeatherTextView = (TextView)findViewById(R.id.text_record_weather);
		mPhotoImageView = (ImageView)findViewById(R.id.image_record_photo);
	
		mDateTextView.setText(record.getDate());
		mWeatherTextView.setText(record.getWeather());
		mPhotoImageView.setImageResource(R.drawable.sample_photo);
	}
}
