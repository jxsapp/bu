package org.bu.android.widget.exlist;

import org.bu.android.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class BuGroupRowView<G> extends LinearLayout {

	public BuGroupRowView(Context context) {
		super(context);
		init();
	}

	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.bu_exlist_group_row, this);
		LinearLayout exlist_group_ll = (LinearLayout) findViewById(R.id.exlist_group_ll);
		init(exlist_group_ll);
	}

	public void init(LinearLayout layout) {

	}

	public void buliderBuGroupRow(G g, int groupPosition) {

	}

	public BuGroupRowView<G> bulider(G g, int groupPosition) {
		buliderBuGroupRow(g, groupPosition);
		this.setTag(groupPosition);
		return this;
	}

}
