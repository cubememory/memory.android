package com.cube.memory.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cube.memory.entity.EntityPhoto;
import com.cube.memory.phone.R;
import com.cube.memory.util.CubeImageLoader;
import com.cube.memory.util.CubeInflater;

public class ViewImageBlock extends RelativeLayout{
	private TextView mTextViewLeft;
	private TextView mTextViewCenter;
	private TextView mTextViewRight;
	private GridView mGridViewImage;
	
	public ViewImageBlock(Context context, EntityImageBlock data) {
		super(context);
		
		/*inflate the view with layout xml*/
		CubeInflater.inflate(context, R.layout.view_image_block, this);
		
		/*find all the view in the widget*/
		mTextViewLeft = (TextView) this.findViewById(R.id.text_left);
		mTextViewCenter = (TextView) this.findViewById(R.id.text_center);
		mTextViewRight = (TextView) this.findViewById(R.id.text_right);
		mGridViewImage = (GridView) this.findViewById(R.id.grid_image);
		
		/*initialize view data*/
		mTextViewLeft.setText(data.getTextLeft());
		mTextViewCenter.setText(data.getTextCenter());
		mTextViewRight.setText(data.getTextRight());
		mGridViewImage.setAdapter(new AdapterGridView(context, data.getListImages()));
	}
	
	private static class AdapterGridView extends BaseAdapter{
		private Context mContext;
		
		private List<EntityPhoto> photoItems = new ArrayList<EntityPhoto>();
		
		public AdapterGridView(Context context, List<EntityPhoto> photoItems){
			this.mContext = context;
			this.photoItems.addAll(photoItems);
		}

		@Override
		public int getCount() {
			return this.photoItems.size();
		}

		@Override
		public Object getItem(int position) {
			return this.photoItems.get(position);
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
				CubeImageLoader.display(this.photoItems.get(position).getUri(), view);
			}
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
}
