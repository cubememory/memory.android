package com.cube.memory.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import com.cube.memory.entity.EntityPhotoRecord;
import com.cube.memory.phone.R;
import com.cube.memory.ui.ActivityFullScreenPhoto;

public class ViewPhotoRecordItem extends RelativeLayout{
	private TextView mDateTextView;
	private TextView mWeatherTextView;
	private ImageView mPhotoImageView;
	
	public ViewPhotoRecordItem(Context context, EntityPhotoRecord record) {
		super(context);
		/*inflate the layout xml for view record item*/
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_photo_record_item, this);
		
		/*find the relate view items from layout xml*/
		mDateTextView = (TextView)findViewById(R.id.text_record_date);
		mWeatherTextView = (TextView)findViewById(R.id.text_record_weather);
		mPhotoImageView = (ImageView)findViewById(R.id.image_record_photo);
	
		/*set the content for every view item*/
		mDateTextView.setText(record.getDate());
		mWeatherTextView.setText(record.getWeather());
		mPhotoImageView.setImageResource(R.drawable.image_sample_photo);
		
		mPhotoImageView.setOnClickListener(new PhotoImageOnClickListener(context, R.drawable.image_sample_photo));
	}
	
	public static class PhotoImageOnClickListener implements View.OnClickListener{
		private Context mContext;
		private int resID;
		
		public PhotoImageOnClickListener(Context context, int resID){
			this.mContext = context;
			this.resID = resID;
		}
		
		@Override
		public void onClick(View v) {
			Toast.makeText((Activity)this.mContext, "click: "+this.resID, Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(mContext, ActivityFullScreenPhoto.class);
			intent.putExtra("res_id", resID);
			mContext.startActivity(intent);
		}
		
	}
}
