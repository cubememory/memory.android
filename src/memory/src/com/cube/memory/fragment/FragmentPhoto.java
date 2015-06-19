package com.cube.memory.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cube.memory.entity.EntityPhoto;
import com.cube.memory.phone.R;
import com.cube.memory.util.CubeMediaScanner;
import com.cube.memory.widget.ViewImageBlock;
import com.cube.memory.widget.ViewImageBlock.EntityImageBlock;

public class FragmentPhoto extends ListFragment {
	private AdapterPhotos mAdapterPhotos;
	private Handler mHandler;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_photo, container, false);
		
		mAdapterPhotos = new AdapterPhotos(this.getActivity());
		this.setListAdapter(mAdapterPhotos);
		if(Looper.myLooper() != null){
			mHandler = new Handler(mAdapterPhotos);
		}
		this.getImages(mHandler);
		
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
		
		private List<String> mKeys = new ArrayList<String>();
		private Map<String, List<EntityPhoto>> mAlbums = new HashMap<String, List<EntityPhoto>>();

		public AdapterPhotos(Context context) {
			this.mContext = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			String date = mKeys.get(position);
			
			EntityImageBlock eib = new EntityImageBlock();
			eib.setTextLeft(date);
			eib.setTextCenter(String.valueOf(mAlbums.get(date).size()));
			eib.setTextRight("more");
			eib.setListImages(mAlbums.get(date));
			
			ViewImageBlock vib = new ViewImageBlock(mContext, eib);
			
			return vib;
		}

		@Override
		public boolean handleMessage(Message msg) {
			List<CubeMediaScanner.Image> images = (List<CubeMediaScanner.Image>)msg.obj;
			this.addAlbums(images);
			this.mKeys = new ArrayList<String>();
			
			Iterator<String> iter = this.mAlbums.keySet().iterator();
			while(iter.hasNext()){
				this.mKeys.add(iter.next());
			}
			
			this.notifyDataSetChanged();
			
			return false;
		}
		
		private void addAlbums(List<CubeMediaScanner.Image> images){	
			for(CubeMediaScanner.Image image: images){
				if(!this.mAlbums.containsKey(image.getDate())){
					List<EntityPhoto> photos = new ArrayList<EntityPhoto>();
					mAlbums.put(image.getDate(), photos);
				}
				mAlbums.get(image.getDate()).add(new EntityPhoto(image.getDate(), image.getPath()));
			}
		}

		@Override
		public int getCount() {
			return mAlbums.size();
		}

		@Override
		public Object getItem(int position) {
			return mAlbums.get(mKeys.get(position));
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
	}
}
