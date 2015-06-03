package org.bu.android.app;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

public abstract class BuAdapter<T, D> extends BaseAdapter implements Filterable {

	protected List<D> datas = new ArrayList<D>();
	protected BuItemViewHolder mViewHolder;

	protected T mActivity;

	public BuAdapter(T t, List<D> datas) {
		this.mActivity = new WeakReference<T>(t).get();
		this.datas = datas;
	}

	private Object token = "";

	public Object getToken() {
		return token;
	}

	public void setToken(Object token) {
		this.token = token;
	}

	public List<D> getDatas() {
		return datas;
	}

	public void refresh(List<D> datas) {
		this.datas = datas;
		notifyDataSetChanged();
	}

	public void refresh(List<D> datas, boolean isFilter) {
		refresh(datas);
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

	public abstract class BuItemViewHolder {
		protected View convertView;

		protected BuItemViewHolder(View convertView) {
			super();
			this.convertView = convertView;
		}

		protected void init(D d) {

		}

		public void init(int position, D d) {

		}

		public void init(int position, D d, ViewGroup parent) {

		}

	}

	@Override
	public Filter getFilter() {
		return null;
	}

}