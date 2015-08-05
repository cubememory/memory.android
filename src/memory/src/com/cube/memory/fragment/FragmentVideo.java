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
import com.cube.memory.entity.EntityVideo;
import com.cube.memory.phone.R;
import com.cube.memory.widget.ViewVideoSection;
import com.cube.sdk.log.CLogger;
import com.cube.sdk.task.CExecutor;
import com.cube.sdk.task.CHandler;
import com.cube.sdk.util.CFormat;
import com.cube.sdk.util.CScanner;

public class FragmentVideo extends ListFragment {
	private final static String TAG = FragmentVideo.class.getName();

	// adapter for photo list
	private AdapterVideos mAdapterVideos;
	// progress dialog for loading photo data
	private ProgressDialog progressDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		/* inflate the fragment list view */
		View view = inflater.inflate(R.layout.fragment_video, container, false);

		/* start the progress dialog */
		progressDialog = ProgressDialog.show(this.getActivity(), this.getString(R.string.tip_loading_title), this.getString(R.string.tip_loading_body));

		/* start the photo loading task */
		CExecutor.getInstance().submit(mHandler);

		return view;
	}

	private CHandler mHandler = new CHandler(this.getActivity()) {

		@Override
		public Object onProcess(Context context, Object obj) {
			/* scan the image store for galleries */
			try {
				List<CScanner.Video.Gallery> galleries = CScanner.Video.scan(getActivity());

				/* translate to albums */
				return toAlbums(galleries);
			} catch (Exception e) {
				CLogger.e(TAG, e.getMessage(), e);
			}

			return null;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void onFinish(Context context, Object res) {

			/* initialize the list fragment adapter */
			mAdapterVideos = new AdapterVideos(getActivity(), (List<EntityAlbum<EntityVideo>>) res);
			setListAdapter(mAdapterVideos);

			/* cancel the progress dialog */
			progressDialog.cancel();
		}

		private List<EntityAlbum<EntityVideo>> toAlbums(List<CScanner.Video.Gallery> galleries) {
			/* albums after translated */
			List<EntityAlbum<EntityVideo>> albums = new ArrayList<EntityAlbum<EntityVideo>>();

			try {
				/* map for the same date photo items */
				Map<String, List<CScanner.Video.Item>> dateItems = new HashMap<String, List<CScanner.Video.Item>>();

				/* process each gallery(internal & external media storage) */
				for (CScanner.Video.Gallery gallery : galleries) {
					List<CScanner.Video.Item> items = gallery.getItems();
					for (CScanner.Video.Item item : items) {
						String date = CFormat.parseDate("yyyymmdd", item.createTime * 1000);
						if (!dateItems.containsKey(date)) {
							/* new date items, create the relate item in the map */
							List<CScanner.Video.Item> photos = new ArrayList<CScanner.Video.Item>();
							dateItems.put(date, photos);
						}
						/* add the item to relate date items */
						dateItems.get(date).add(item);
					}
				}

				/* translate the map to albums */
				for (Map.Entry<String, List<CScanner.Video.Item>> entry : dateItems.entrySet()) {
					EntityAlbum<EntityVideo> album = new EntityAlbum<EntityVideo>();
					album.setName(entry.getKey());
					album.setTotal(entry.getValue().size());

					for (CScanner.Video.Item item : entry.getValue()) {
						EntityVideo video = new EntityVideo(entry.getKey(), item._id, "file://" + item.path);
						album.addItem(video);
					}
					albums.add(album);
				}
			} catch (Exception e) {
				CLogger.e(TAG, e.getMessage(), e);
			}
			return albums;
		}
	};

	private class AdapterVideos extends BaseAdapter {
		private Context mContext;
		private List<EntityAlbum<EntityVideo>> albums;

		public AdapterVideos(Context context, List<EntityAlbum<EntityVideo>> albums) {
			this.mContext = context;
			this.albums = albums;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewVideoSection vib = new ViewVideoSection(mContext);
			vib.setData(albums.get(position));
			return vib;
		}

		@Override
		public int getCount() {
			return this.albums.size();
		}

		@Override
		public Object getItem(int position) {
			return this.albums.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
	}
}
