package org.bu.android.widget.dragexplist;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public abstract class BuDragExView extends RelativeLayout {

	public BuDragExView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BuDragExView(Context context) {
		this(context, null);
	}

	public boolean isCanDrag() {
		return false;
	}

	public int getGroupPosition() {
		return -1;
	}

	public int getChildPosition() {
		return -1;
	}

}
