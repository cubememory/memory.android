package com.cube.memory.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import com.cube.memory.phone.R;

public class FragmentLocal extends Fragment {
	private TabHost mTabHost;
	private ViewPager mViewPager;
	private PagerAdapterLocal mPagerAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View localView =  inflater.inflate(R.layout.fragment_local, container, false);

		/*setup the tab host*/
		mTabHost = (TabHost)localView.findViewById(android.R.id.tabhost);
		mTabHost.setup();
		
		/*setup the view pager*/
		mViewPager = (ViewPager)localView.findViewById(R.id.pager_local);
		mPagerAdapter = new PagerAdapterLocal(this.getActivity(), mTabHost, this.getChildFragmentManager());
		mViewPager.setAdapter(mPagerAdapter);
		
		return localView;
	}
	
	private class PagerAdapterLocal extends FragmentPagerAdapter{
		private Context mContext;
		private TabHost mTabHost;
		private List<Fragment> mPagers = new ArrayList<Fragment>();
		
		public PagerAdapterLocal(Context context, TabHost tabhost, FragmentManager manager){
			super(manager);
			mContext = context;
			mTabHost = tabhost;
			
			this.initPagers(mContext);
		}
		
		private void initPagers(Context context){
			/*init the photo pager*/
			Fragment fragmentPhoto = Fragment.instantiate(context, FragmentPhoto.class.getName());
			mPagers.add(fragmentPhoto);
			mTabHost.addTab(mTabHost.newTabSpec("photo").setIndicator(mContext.getText(R.string.tab_name_photo)).setContent(new EmptyTabFactory(mContext)));
			
			/*init the video pager*/
			Fragment fragmentVideo = Fragment.instantiate(context, FragmentVideo.class.getName());
			mPagers.add(fragmentVideo);
			mTabHost.addTab(mTabHost.newTabSpec("video").setIndicator(mContext.getText(R.string.tab_name_video)).setContent(new EmptyTabFactory(mContext)));
		}
		
		@Override
		public Fragment getItem(int pos) {
			return mPagers.get(pos);
		}

		@Override
		public int getCount() {
			return mPagers.size();
		}	
	}
	
	private class EmptyTabFactory implements TabHost.TabContentFactory{
		private Context mContext;
		
		public EmptyTabFactory(Context context){
			mContext = context;
		}
		
		@Override
		public View createTabContent(String tag) {
			View view = new View(mContext);
			view.setMinimumHeight(0);
			view.setMinimumWidth(0);
			return view;
		}
		
	}
}
