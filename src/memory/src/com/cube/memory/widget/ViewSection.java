package com.cube.memory.widget;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.res.Configuration;
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

import com.cube.memory.entity.EntityPhoto;
import com.cube.memory.phone.R;
import com.cube.memory.util.CubeImageLoader;
import com.cube.memory.util.CubeInflater;

public class ViewSection extends RelativeLayout implements OnClickListener{
	private TextView mTextViewDate;
	private TextView mTextViewTotal;
	private TextView mTextViewMore;
	private GridView mGridViewImages;


	public ViewSection(Context context) {
		super(context);
		init(context);
	}

	public ViewSection(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public ViewSection(Context context, AttributeSet attrs, int defStyle) {
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

	public void setData(Date date, int total, List<String> images){
		mTextViewDate.setText(this.getContext().getString(R.string.item_name_date, date.getYear(), date.getMonth(), date.getDay()));
		mTextViewTotal.setText(this.getContext().getString(R.string.item_name_total, total));
		mGridViewImages.setAdapter(new AdapterGridView(this.getContext(), images));
	}
	
	private static class AdapterGridView extends BaseAdapter{
		private Context mContext;
		
		private List<String> mImages = new ArrayList<String>();
		
		public AdapterGridView(Context context, List<String> images){
			this.mContext = context;
			this.mImages.addAll(images);
		}

		@Override
		public int getCount() {
			return this.mImages.size();
		}

		@Override
		public Object getItem(int position) {
			return this.mImages.get(position);
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
			
			CubeImageLoader.display(mImages.get(position), view);
			
			return view;
		}
		
	}
	
	public static class EntityImageBlock{
		private String textLeft;
		private String textCenter;
		private String textRight;
		private List<EntityPhoto> listImages;
		
		
		public String getTextLeft() {
			return textLeft;
		}
		public void setTextLeft(String textLeft) {
			this.textLeft = textLeft;
		}
		public String getTextCenter() {
			return textCenter;
		}
		public void setTextCenter(String textCenter) {
			this.textCenter = textCenter;
		}
		public String getTextRight() {
			return textRight;
		}
		public void setTextRight(String textRight) {
			this.textRight = textRight;
		}
		public List<EntityPhoto> getListImages() {
			return listImages;
		}
		public void setListImages(List<EntityPhoto> listImages) {
			this.listImages = listImages;
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
