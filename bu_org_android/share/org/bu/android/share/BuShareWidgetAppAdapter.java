package org.bu.android.share;

import java.util.ArrayList;
import java.util.List;

import org.bu.android.R;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BuShareWidgetAppAdapter extends BaseAdapter {
	private List<BuShareAppInfo> fileInfos = new ArrayList<BuShareAppInfo>();
	private LayoutInflater inflater;

	public BuShareWidgetAppAdapter(LayoutInflater inflater, List<BuShareAppInfo> infos) {
		this.fileInfos = infos;
		this.inflater = inflater;
	}

	@Override
	public int getCount() {
		return this.fileInfos.size();
	}

	@Override
	public BuShareAppInfo getItem(int position) {
		return this.fileInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	@SuppressLint("InflateParams")
	public View getView(final int position, View convertView, ViewGroup parent) {

		BuShareAppInfo app = fileInfos.get(position);
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.wm_share_grid_item, null);
			viewHolder.app_icon = (ImageView) convertView.findViewById(R.id.app_icon);
			viewHolder.app_des = (TextView) convertView.findViewById(R.id.app_des);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.app_des.setText(app.getTargetAppDefine().name);
		viewHolder.app_icon.setImageResource(app.getTargetAppDefine().icon);
		return convertView;

	}

	class ViewHolder {
		ImageView app_icon; // 头像
		TextView app_des;
	}
}