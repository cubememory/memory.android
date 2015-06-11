package com.cube.memory.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;

import com.cube.memory.phone.R;

public class ActivityMain extends FragmentActivity {
	private FragmentTabHost mTabHost;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
		setContentView(R.layout.activity_home_page);
		
		mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
		
		mTabHost.addTab(mTabHost.newTabSpec("local").setIndicator(this.getText(R.string.tab_name_local)), FragmentPhotoRecord.class, null);
		mTabHost.addTab(mTabHost.newTabSpec("network").setIndicator(this.getText(R.string.tab_name_network)), FragmentPhotoRecord.class, null);
		mTabHost.addTab(mTabHost.newTabSpec("record").setIndicator(this.getText(R.string.tab_name_record)), FragmentPhotoRecord.class, null);
		mTabHost.addTab(mTabHost.newTabSpec("share").setIndicator(this.getText(R.string.tab_name_share)), FragmentPhotoRecord.class, null);
		mTabHost.addTab(mTabHost.newTabSpec("setting").setIndicator(this.getText(R.string.tab_name_setting)), FragmentPhotoRecord.class, null);
		
		mTabHost.setOnTabChangedListener(new MyOnTabChangeListener(this));
		mTabHost.setOnClickListener(new MyOnClickListener(this));
	}
	
	private static class MyOnTabChangeListener implements OnTabChangeListener{
		private Context mContext;
		
		public MyOnTabChangeListener(Context context){
			this.mContext = context;
		}

		@Override
		public void onTabChanged(String tabId) {
			if(tabId.equals("record")){
				Intent intent = new Intent(mContext, ActivityFullScreenPhoto.class);
				intent.putExtra("res_id", R.drawable.image_sample_photo);
				mContext.startActivity(intent);
			}
		}		
	}
	
	private static class MyOnClickListener implements OnClickListener{
		private Context mContext;
		public MyOnClickListener(Context context){
			this.mContext = context;
		}
		@Override
		public void onClick(View v) {
			Toast.makeText(mContext, "tab: clicked", Toast.LENGTH_SHORT).show();
		}
		
	}

}
