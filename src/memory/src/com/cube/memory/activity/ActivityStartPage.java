package com.cube.memory.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.cube.memory.phone.R;
import com.cube.memory.setup.ModuleManager;

public class ActivityStartPage extends ActivityBase{
	private Handler mHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_start_page);
		this.gotoHomePageLater();
		
		ModuleManager.setup(this.getApplicationContext());
	}
	
	private void gotoHomePageLater(){
		mHandler = new Handler();
		mHandler.postDelayed(new Runnable(){
			@Override
			public void run() {
				gotoHomePage();
			}
			
		}, 1000);
	}
	
	private void gotoHomePage(){
		Navigator.startHomePage(this);
		this.finish();
	}
}
