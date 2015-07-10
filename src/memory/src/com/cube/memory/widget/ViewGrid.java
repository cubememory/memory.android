package com.cube.memory.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class ViewGrid extends GridView {

	public ViewGrid(Context context) {
		super(context);
	}

	public ViewGrid(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ViewGrid(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//this will expand the grid items in other view, like: list view
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
