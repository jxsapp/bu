package org.bu.android.widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bu.android.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BuMenuAdapter<T> extends BaseAdapter {
	static public interface OnBuMenuItemListener<T> {
		void onItemClick(int position, T t);
	}

	private ViewHolder holder = null;

	protected List<T> mDatas = new ArrayList<T>();

	private BuMenu mBuMenu;
	private OnBuMenuItemListener<T> itemClickListener;

	public BuMenuAdapter(BuMenu mBuMenu, List<T> mDatas, OnBuMenuItemListener<T> clickListener) {
		this.mBuMenu = mBuMenu;
		this.itemClickListener = clickListener;
		this.mDatas = mDatas;
	}

	public BuMenuAdapter(BuMenu mBuMenu, T[] mDatas, OnBuMenuItemListener<T> clickListener) {
		this.mBuMenu = mBuMenu;
		this.itemClickListener = clickListener;
		this.mDatas = Arrays.asList(mDatas);
	}

	public List<T> getDatas() {
		return mDatas;
	}

	public void refresh(List<T> datas) {
		if (datas != null) {
			mDatas = datas;
		}
		notifyDataSetChanged();
	}

	public void refresh(T[] mDatas) {
		refresh(Arrays.asList(mDatas));
	}

	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public T getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	@SuppressWarnings("unchecked")
	public View getView(final int position, View convertView, ViewGroup parent) {
		final T t = mDatas.get(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(mBuMenu.getContext()).inflate(R.layout.bu_menu_item_txt, null);
			holder = new ViewHolder();
			holder.label = (TextView) convertView;
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.label.setText(t.toString());
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mBuMenu.dismiss();
				if (null != itemClickListener) {
					itemClickListener.onItemClick(position, t);
				}
			}
		});
		return convertView;
	}

	class ViewHolder {
		TextView label;
	}

}