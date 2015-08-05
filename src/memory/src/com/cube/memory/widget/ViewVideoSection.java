package com.cube.memory.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cube.memory.entity.EntityAlbum;
import com.cube.memory.entity.EntityVideo;
import com.cube.memory.phone.R;
import com.cube.memory.util.CubeInflater;
import com.cube.sdk.task.CExecutor;

public class ViewVideoSection extends RelativeLayout implements OnClickListener{
	private TextView mTextViewDate;
	private TextView mTextViewTotal;
	private TextView mTextViewMore;
	private GridView mGridViewImages;
	
	public ViewVideoSection(Context context) {
		super(context);
		init(context);
	}

	public ViewVideoSection(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public ViewVideoSection(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context){
		/*inflate the view with layout xml*/
		CubeInflater.inflate(context, R.layout.view_section, this);
		
		/*find all the view in the widget*/
		mTextViewDate = (TextView) this.findViewById(R.id.text_date);
		mTextViewTotal = (TextView) this.findViewById(R.id.text_total);
		mTextViewMore = (TextView) this.findViewById(R.id.text_more);
		mGridViewImages = (GridView) this.findViewById(R.id.grid_images);
		
		int ori = context.getResources().getConfiguration().orientation;
		if(ori == Configuration.ORIENTATION_LANDSCAPE)
			mGridViewImages.setNumColumns(6);
		else
			mGridViewImages.setNumColumns(4);
		
		mTextViewMore.setOnClickListener(this);
	}

	public void setData(EntityAlbum<EntityVideo> album){
		mTextViewDate.setText(album.getName());
		mTextViewTotal.setText(String.valueOf(album.getTotal()));
		mGridViewImages.setAdapter(new AdapterGridView(this.getContext(), album.getItems()));
	}
	
	private static class AdapterGridView extends BaseAdapter{
		private ContentResolver mResolver;
		private Context mContext;
		
		private List<EntityVideo> mItems = new ArrayList<EntityVideo>();
		
		public AdapterGridView(Context context, List<EntityVideo> items){
			this.mContext = context;
			this.mResolver = context.getContentResolver();
			this.mItems.addAll(items);
		}

		@Override
		public int getCount() {
			return this.mItems.size()>16?16:this.mItems.size();
		}

		@Override
		public Object getItem(int position) {
			return this.mItems.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView view = null;
			if(convertView != null){
				view = (ImageView)convertView;
			}else{
				view = new ImageView(mContext);
				view.setLayoutParams(new GridView.LayoutParams(168,168));
				view.setAdjustViewBounds(false);
				view.setScaleType(ImageView.ScaleType.CENTER_CROP);
				view.setPadding(8, 8, 8, 8);
				view.setImageResource(R.drawable.image_sample_photo);
			}
				
			CExecutor.getInstance().submit(new DisplayTask(mResolver, mItems.get(position).getMediaID(), view));
			//CubeImageLoader.display(((EntityMedia)mItems.get(position)).getUri(), view);
			
			return view;
		}
		
		private static class DisplayTask implements Runnable{
			private ContentResolver mResolver;
			private long mImageID;
			private ImageView mImageView;
			
			public DisplayTask(ContentResolver resolver, long imageID, ImageView imageView){
				this.mResolver = resolver;
				this.mImageID = imageID;
				this.mImageView = imageView;
			}

			@Override
			public void run() {
				Bitmap bitmap = MediaStore.Video.Thumbnails.getThumbnail(mResolver, mImageID, MediaStore.Video.Thumbnails.MICRO_KIND, null);
				mImageView.setImageBitmap(bitmap);
			}
			
		}
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.text_more){
			Toast.makeText(this.getContext(), "click more: "+v.getId(), Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(this.getContext(), "click other: "+v.getId(), Toast.LENGTH_SHORT).show();
		}
	}
}
