package org.bu.android.widget.exlist;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

public class BuExpandableListViewAdapter<G, C> extends BaseExpandableListAdapter {

	protected Context mContext;
	private List<G> groupList = new ArrayList<G>();
	private LinkedHashMap<G, List<C>> childList = new LinkedHashMap<G, List<C>>();

	public BuExpandableListViewAdapter(Context context, LinkedHashMap<G, List<C>> childList) {
		this.mContext = context;
		groupList.clear();
		for (Iterator<G> it = childList.keySet().iterator(); it.hasNext();) {
			groupList.add(it.next());
		}
		this.childList = childList;
	}

	public LinkedHashMap<G, List<C>> getChildList() {
		return childList;
	}

	@Override
	public C getChild(int groupPosition, int childPosition) {
		return childList.get(getGroup(groupPosition)).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public BuChildRowView<C> getBuChildRowView() {
		return new BuChildRowView<C>(mContext);
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		return getBuChildRowView().bulider(getChild(groupPosition, childPosition), groupPosition, childPosition);
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return childList.get(getGroup(groupPosition)).size();
	}

	@Override
	public G getGroup(int groupPosition) {
		return groupList.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return childList.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public BuGroupRowView<G> getBuGroupRowView() {
		return new BuGroupRowView<G>(mContext);
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		return getBuGroupRowView().bulider(groupList.get(groupPosition), groupPosition);
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

}
