package com.cube.memory.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.cube.memory.phone.R;

public class ActivityFullScreenPhoto extends Activity {
	private ImageView mImageView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_full_screen_photo);
		
		int resID = getIntent().getIntExtra("res_id", R.drawable.image_sample_photo);
		
		mImageView = (ImageView)findViewById(R.id.image_full_screen_photo);
		mImageView.setScaleType(ScaleType.CENTER_CROP);
		mImageView.setImageResource(resID);
	}

}
