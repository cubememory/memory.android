package com.cube.memory.ui;

import android.os.Bundle;
import android.os.Handler;

import com.cube.memory.phone.R;

public class ActivityStartPage extends ActivityBase{
	private Handler mHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_start_page);
		this.gotoHomePageLater();
	}
	
	private void gotoHomePageLater(){
		mHandler = new Handler();
		mHandler.postDelayed(new Runnable(){
			@Override
			public void run() {
				gotoHomePage();
			}
			
		}, 3000);
	}
	
	private void gotoHomePage(){
		
	}
}
