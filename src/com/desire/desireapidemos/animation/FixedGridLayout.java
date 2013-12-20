package com.desire.desireapidemos.animation;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public class FixedGridLayout extends ViewGroup {
	int mCellWidth;
	int mCellHeight;

	public FixedGridLayout(Context context) {
		super(context);
	}

	public void setCellWidth(int cw) {
		mCellWidth = cw;
		requestLayout();
	}

	public void setCellHeight(int ch) {
		mCellHeight = ch;
		requestLayout();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int cellWidthSpec = MeasureSpec.makeMeasureSpec(mCellWidth, MeasureSpec.AT_MOST);
		int cellHeightSpec = MeasureSpec.makeMeasureSpec(mCellHeight, MeasureSpec.AT_MOST);

		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);
			child.measure(cellWidthSpec, cellHeightSpec);
		}

		int minCount = count > 3 ? count : 3;
		setMeasuredDimension(resolveSize(mCellWidth * minCount, widthMeasureSpec), resolveSize(mCellHeight * minCount, heightMeasureSpec));
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int cellWidth = mCellWidth;
		int cellHeight = mCellHeight;
		int columns = (r - l) / cellWidth;
		if (columns <= 0) {
			columns = 1;
		}

		int x = 0, y = 0, i = 0;
		int count = getChildCount();
		for (int j = 0; j < count; j++) {
			final View child = getChildAt(j);
			
			int w = child.getMeasuredWidth();
			int h = child.getMeasuredHeight();
			
			int left = x + (cellWidth - w) / 2;
			int top = y + (cellHeight - h) / 2;
			
			child.layout(left, top, left + w, top + h);
			if (i >= (columns - 1)) {
				i = 0;
				x = 0;
				y += cellHeight;
			} else {
				i++;
				x += cellWidth;
			}
		}

	}

}
