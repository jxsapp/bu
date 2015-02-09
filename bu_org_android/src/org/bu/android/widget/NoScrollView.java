package org.bu.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class NoScrollView extends ScrollView {

	public interface NoScrollViewListener {
		public boolean onTouchEvent(MotionEvent ev);

		public boolean onInterceptTouchEvent(MotionEvent ev);
	}

	private NoScrollViewListener noScrollViewListener;

	public void setNoScrollViewListener(NoScrollViewListener noScrollViewListener) {
		this.noScrollViewListener = noScrollViewListener;
	}

	public NoScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NoScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public NoScrollView(Context context) {
		super(context);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (null != noScrollViewListener) {
			return noScrollViewListener.onInterceptTouchEvent(ev);
		}
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (null != noScrollViewListener) {
			return noScrollViewListener.onTouchEvent(ev);
		}
		return false;
	}

	public boolean superOnInterceptTouchEvent(MotionEvent ev) {
		return super.onInterceptTouchEvent(ev);
	}

	public boolean superOnTouchEvent(MotionEvent ev) {
		return super.onTouchEvent(ev);
	}
}