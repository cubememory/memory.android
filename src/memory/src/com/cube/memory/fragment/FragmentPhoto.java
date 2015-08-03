package com.cube.memory.fragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cube.memory.phone.R;
import com.cube.memory.widget.ViewSection;

public class FragmentPhoto extends ListFragment {
	private AdapterPhotos mAdapterPhotos;
	private Handler mHandler;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_photo, container, false);
		
		mAdapterPhotos = new AdapterPhotos(this.getActivity());
		this.setListAdapter(mAdapterPhotos);
		
		
		/*
		if(Looper.myLooper() != null){
			mHandler = new Handler(mAdapterPhotos);
		}
		this.getImages(mHandler);
		*/
		return view;
	}
	
	
	
	private void getImages(final Handler handler){
		new Thread(new Runnable(){			
			@Override
			public void run() {
				List<CubeMediaScanner.Image> images = CubeMediaScanner.getImages(FragmentPhoto.this.getActivity());
				handler.sendMessage(handler.obtainMessage(0, images));
			}
			
		}).start();
	}
	
	private class AdapterPhotos extends BaseAdapter implements Handler.Callback{
		private Context mContext;
		
		public AdapterPhotos(Context context) {
			this.mContext = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewSection vib = new ViewSection(mContext);
			
			vib.setData(new Date(System.currentTimeMillis()), 200, this.getSampleImages());
			
			return vib;
		}
		
		private List<String> getSampleImages(){
			List<String> images = new ArrayList<String>();
			for(int i=0; i<13; i++){
				images.add("file://system/media/Pre-loaded/Pictures/Picture_01_Shark.jpg");
			}
			return images;
		}

		@Override
		public boolean handleMessage(Message msg) {
			/*List<CubeMediaScanner.Image> images = (List<CubeMediaScanner.Image>)msg.obj;
			this.addAlbums(images);
			this.mKeys = new ArrayList<String>();
			
			Iterator<String> iter = this.mAlbums.keySet().iterator();
			while(iter.hasNext()){
				this.mKeys.add(iter.next());
			}
			
			this.notifyDataSetChanged();
			*/
			return false;
		}
		
		public void addAlbums(List<CubeMediaScanner.Image> images){	
		/*	for(CubeMediaScanner.Image image: images){
				if(!this.mAlbums.containsKey(image.getDate())){
					List<EntityPhoto> photos = new ArrayList<EntityPhoto>();
					mAlbums.put(image.getDate(), photos);
				}
				mAlbums.get(image.getDate()).add(new EntityPhoto(image.getDate(), image.getPath()));
			}
			
			this.mKeys = new ArrayList<String>();
			
			Iterator<String> iter = this.mAlbums.keySet().iterator();
			while(iter.hasNext()){
				this.mKeys.add(iter.next());
			}*/
		}

		@Override
		public int getCount() {
			return 16;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
	}
}
