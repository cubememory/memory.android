package com.cube.memory.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.widget.TabHost.OnTabChangeListener;

import com.cube.memory.fragment.FragmentLocal;
import com.cube.memory.phone.R;
import com.cube.memory.util.CubeUtils;

public class ActivityHomePage extends FragmentActivity {
	private FragmentTabHost mTabHost;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
		this.setContentView(R.layout.activity_home_page);
		this.setupTabs();
	}
	
	private void setupTabs(){
		mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
		
		mTabHost.addTab(mTabHost.newTabSpec("local").setIndicator(this.getText(R.string.tab_name_local)), FragmentLocal.class, null);
		mTabHost.addTab(mTabHost.newTabSpec("network").setIndicator(this.getText(R.string.tab_name_network)), FragmentLocal.class, null);
		mTabHost.addTab(mTabHost.newTabSpec("record").setIndicator(this.getText(R.string.tab_name_record)), FragmentLocal.class, null);
		mTabHost.addTab(mTabHost.newTabSpec("share").setIndicator(this.getText(R.string.tab_name_share)), FragmentLocal.class, null);
		mTabHost.addTab(mTabHost.newTabSpec("setting").setIndicator(this.getText(R.string.tab_name_setting)), FragmentLocal.class, null);
		
		mTabHost.setOnTabChangedListener(new MyOnTabChangeListener(this));
	}
	
	private static class MyOnTabChangeListener implements OnTabChangeListener{
		private Context mContext;
		
		public MyOnTabChangeListener(Context context){
			this.mContext = context;
		}

		@Override
		public void onTabChanged(String tabId) {
			if(tabId.equals("record")){
				CubeUtils.startCamera(mContext);
			}
		}		
	}
}
