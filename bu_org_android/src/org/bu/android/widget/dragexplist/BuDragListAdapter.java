package org.bu.android.widget.dragexplist;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class BuDragListAdapter<C> extends BaseAdapter {

	protected Context mContext;
	protected List<C> childs = new ArrayList<C>();
	protected LinkedHashMap<C, List<C>> datas = new LinkedHashMap<C, List<C>>();
	private OnDragListAdapterListener<C> _listener;

	public BuDragListAdapter(Context c, LinkedHashMap<C, List<C>> datas, OnDragListAdapterListener<C> adapterListener) {
		this.mContext = c;
		this._listener = adapterListener;
		resetData(datas);
		notifyGroup();
	}

	private void notifyGroup() {
		if (null != _listener) {
			_listener.notifyDataSetChanged(getGroupSize());
		}
	}

	protected void resetData(LinkedHashMap<C, List<C>> datas) {
		this.datas = datas;
		this.childs.clear();
		for (Iterator<C> it = datas.keySet().iterator(); it.hasNext();) {
			C key = it.next();
			childs.add(key);
			childs.addAll(datas.get(key));
		}
	}

	public LinkedHashMap<C, List<C>> getDatas() {
		return datas;
	}

	public Set<C> getGroups() {
		return datas.keySet();
	}

	public void refresh(LinkedHashMap<C, List<C>> datas) {
		resetData(datas);
		notifyDataSetChanged();
	}

	public void drop(int from, int to) {
		C textInfo = getItem(from);
		childs.remove(from);
		childs.add(to, textInfo);
		drop(childs);
	}

	protected void drop(List<C> childs) {

	}

	public void remove(int childIndex) {
		childs.remove(childIndex);
		drop(childs);
	}

	public List<C> getUpdate(int childIndex, C c) {
		childs.remove(childIndex);
		childs.add(childIndex, c);
		return childs;
	}

	public void update(List<C> chils) {
		this.childs = chils;
		drop(childs);
	}

	public abstract View getView(int position, OnDragListAdapterListener<C> _listener);

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getView(position, _listener);
	}

	public void setDatas(LinkedHashMap<C, List<C>> datas) {
		resetData(datas);
	}

	public List<C> getChilds(C key) {
		return datas.get(key);
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		notifyGroup();
	}

	public int getGroupSize() {
		return datas.size();
	}

	@Override
	public int getCount() {
		return childs.size();
	}

	@Override
	public C getItem(int position) {
		return childs.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	public boolean isCanAnimation() {
		return true;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	public static interface OnDragListAdapterListener<C> {
		void notifyDataSetChanged(int groups);

		void onChildRowClick(C c, int postion, boolean isBrowser);

		void onChildRowLongClick(C c, boolean isBrowser);

		View.OnClickListener getOnClearListener(C c, int position);
	}

}
