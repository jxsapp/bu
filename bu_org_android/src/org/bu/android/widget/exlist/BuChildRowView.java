package org.bu.android.widget.exlist;

import org.bu.android.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class BuChildRowView<C> extends LinearLayout {

	private int groupPosition;
	private int childPosition;

	public BuChildRowView(Context context) {
		super(context);
		init();
	}

	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.bu_exlist_child_row, this);
		LinearLayout exlist_child_ll = (LinearLayout) findViewById(R.id.exlist_child_ll);
		init(exlist_child_ll);
	}

	public void init(LinearLayout layout) {

	}

	public BuChildRowView<C> bulider(C c, int groupPosition, int childPosition) {
		this.childPosition = childPosition;
		this.groupPosition = groupPosition;
		builderChildRow(c, groupPosition, childPosition);
		return this;
	}

	public void builderChildRow(C c, int groupPosition, int childPosition) {

	}

	public int getGroupPosition() {
		return groupPosition;
	}

	public int getChildPosition() {
		return childPosition;
	}

}
