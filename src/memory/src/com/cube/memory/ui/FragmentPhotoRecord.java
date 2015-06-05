package com.cube.memory.ui;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.Menu;
import android.view.MenuInflater;

import com.cube.memory.adapter.AdapterPhotoRecord;
import com.cube.memory.entity.EntityPhotoRecord;
import com.cube.memory.loader.LoaderPhotoRecord;

public class FragmentPhotoRecord extends ListFragment implements LoaderCallbacks<List<EntityPhotoRecord>> {
	//adapter for holding the photo record items
	private AdapterPhotoRecord mAdapterPhotoRecord;
	//loader for photo records
	private LoaderPhotoRecord mLoaderPhotoRecord;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		this.setEmptyText("Nothing");
		this.setHasOptionsMenu(true);

		/* initialize the loader */
		mLoaderPhotoRecord = new LoaderPhotoRecord(this.getActivity());
		this.getLoaderManager().initLoader(0, null, this);
		
		/* set the list adapter */
		mAdapterPhotoRecord = new AdapterPhotoRecord(this.getActivity(), mLoaderPhotoRecord);
		this.setListAdapter(mAdapterPhotoRecord);
		
		this.setListShown(false);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.add("Search");
	}

	@Override
	public Loader<List<EntityPhotoRecord>> onCreateLoader(int id, Bundle args) {
		return mLoaderPhotoRecord;
	}

	@Override
	public void onLoadFinished(Loader<List<EntityPhotoRecord>> loader, List<EntityPhotoRecord> data) {
		mAdapterPhotoRecord.addPhotoRecords(data);
		mAdapterPhotoRecord.notifyDataSetChanged();
		if(this.isResumed()){
			setListShown(true);
		}else{
			setListShownNoAnimation(true);
		}
	}

	@Override
	public void onLoaderReset(Loader<List<EntityPhotoRecord>> loader) {
		mAdapterPhotoRecord.clear();
	}
}