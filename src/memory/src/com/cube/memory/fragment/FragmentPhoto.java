package com.cube.memory.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cube.memory.adapter.AdapterPage;
import com.cube.memory.entity.EntityPhoto;
import com.cube.memory.phone.R;

public class FragmentPhoto extends ListFragment {
	private AdapterPhotos mAdapterPhotos;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_photo, container);
		
		mAdapterPhotos = new AdapterPhotos(this.getActivity());
		
		this.setListAdapter(mAdapterPhotos);
		
		return view;
	}
		
	private class AdapterPhotos extends AdapterPage<EntityPhoto>{
		private Context mContext;

		public AdapterPhotos(Context context) {
			this.mContext = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return null;
		}
	}
}
