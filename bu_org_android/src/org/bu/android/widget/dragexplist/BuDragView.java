package org.bu.android.widget.dragexplist;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public abstract class BuDragView extends LinearLayout {

	public BuDragView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BuDragView(Context context) {
		this(context, null);
	}

	public boolean isCanDrag() {
		return false;
	}

}
