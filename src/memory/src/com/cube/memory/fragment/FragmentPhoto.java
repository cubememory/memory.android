package com.cube.memory.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cube.memory.entity.EntityAlbum;
import com.cube.memory.entity.EntityPhoto;
import com.cube.memory.phone.R;
import com.cube.memory.widget.ViewPhotoSection;
import com.cube.sdk.task.CExecutor;
import com.cube.sdk.task.CHandler;
import com.cube.sdk.util.CFormat;
import com.cube.sdk.util.CScanner;
import com.cube.sdk.log.CLogger;

public class FragmentPhoto extends ListFragment {
	private final static String TAG = FragmentPhoto.class.getName();
	
	//adapter for photo list
	private AdapterPhotos mAdapterPhotos;
	//progress dialog for loading photo data
	private ProgressDialog progressDialog;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		/*inflate the fragment list view*/
		View view = inflater.inflate(R.layout.fragment_photo, container, false);
	
		/*start the progress dialog*/
		progressDialog = ProgressDialog.show(this.getActivity(), this.getString(R.string.tip_loading_title), this.getString(R.string.tip_loading_body));
				
		/*start the photo loading task*/
		CExecutor.getInstance().submit(mHandler);
		
		return view;
	}
	
	private CHandler mHandler = new CHandler(this.getActivity()){

		@Override
		public Object onProcess(Context context, Object obj) {
			/*scan the image store for galleries*/
			try{
				List<CScanner.Image.Gallery> galleries = CScanner.Image.scan(getActivity());
				
				/*translate to albums*/
				return toAlbums(galleries);
			}catch(Exception e){
				CLogger.e(TAG, e.getMessage(), e);
			}
			
			return null;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void onFinish(Context context, Object res) {
			
			/*initialize the list fragment adapter*/
			mAdapterPhotos = new AdapterPhotos(getActivity(), (List<EntityAlbum<EntityPhoto>>) res);
			setListAdapter(mAdapterPhotos);
			
			/*cancel the progress dialog*/
			progressDialog.cancel();
		}
		
		private List<EntityAlbum<EntityPhoto>> toAlbums(List<CScanner.Image.Gallery> galleries){
			/*albums after translated*/
			List<EntityAlbum<EntityPhoto>> albums = new ArrayList<EntityAlbum<EntityPhoto>>();
			
			try{
				/*map for the same date photo items*/
				Map<String, List<CScanner.Image.Item>> dateItems = new HashMap<String, List<CScanner.Image.Item>>();
				
				/*process each gallery(internal & external media storage)*/
				for(CScanner.Image.Gallery gallery: galleries){
					List<CScanner.Image.Item> items = gallery.getItems();
					for(CScanner.Image.Item item: items){
						String date = CFormat.parseDate("yyyymmdd", item.createTime*1000);
						if(!dateItems.containsKey(date)){
							/*new date items, create the relate item in the map*/
							List<CScanner.Image.Item> photos = new ArrayList<CScanner.Image.Item>();
							dateItems.put(date, photos);
						}
						/*add the item to relate date items*/
						dateItems.get(date).add(item);
					}
				}
				
				/*translate the map to albums*/
				for(Map.Entry<String, List<CScanner.Image.Item>> entry: dateItems.entrySet()){
					EntityAlbum<EntityPhoto> album = new EntityAlbum<EntityPhoto>();
					album.setName(entry.getKey());
					album.setTotal(entry.getValue().size());
					
					for(CScanner.Image.Item item: entry.getValue()){
						EntityPhoto photo = new EntityPhoto(entry.getKey(), item._id, "file://"+item.path);
						album.addItem(photo);
					}
					albums.add(album);
				}
			}catch(Exception e){
				CLogger.e(TAG, e.getMessage(), e);
			}
			return albums;
		}
	};
	
	private class AdapterPhotos extends BaseAdapter{
		private Context mContext;
		private List<EntityAlbum<EntityPhoto>> albums;
		
		public AdapterPhotos(Context context, List<EntityAlbum<EntityPhoto>> albums) {
			this.mContext = context;
			this.albums = albums;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewPhotoSection vib = new ViewPhotoSection(mContext);
			vib.setData(albums.get(position));
			return vib;
		}

		@Override
		public int getCount() {
			return this.albums.size();
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
