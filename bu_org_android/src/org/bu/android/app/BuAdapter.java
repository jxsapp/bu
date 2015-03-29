package org.bu.android.app;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.BaseAdapter;

public abstract class BuAdapter<T, D> extends BaseAdapter {

	protected T mActivity;

	protected List<D> datas = new ArrayList<D>();

	public BuAdapter(T t, List<D> datas) {
		super();
		this.mActivity = new WeakReference<T>(t).get();
		this.datas = datas;
	}

	public void refresh(List<D> datas) {
		this.datas = datas;
		notifyDataSetChanged();
	}

	public void appends(List<D> datas) {
		this.datas.addAll(datas);
		notifyDataSetChanged();
	}

	public void append(D data) {
		this.datas.add(data);
		notifyDataSetChanged();
	}

	public void remove(int position) {
		this.datas.remove(position);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public D getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public abstract class ItemViewHolder {
		protected View convertView;

		protected ItemViewHolder(View convertView) {
			super();
			this.convertView = convertView;
		}

		protected abstract void init(D d);

	}

}