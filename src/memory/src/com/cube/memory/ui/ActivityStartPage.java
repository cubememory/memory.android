package com.cube.memory.ui;

import android.content.Intent;
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
		Intent intent = new Intent(this, ActivityHomePage.class);
		this.startActivity(intent);
		this.finish();
		//for test git
		//for test git 2
	}
}
