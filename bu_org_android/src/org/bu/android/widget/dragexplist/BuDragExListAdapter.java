package org.bu.android.widget.dragexplist;

import java.util.ArrayList;
import java.util.List;

import org.bu.android.R;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseExpandableListAdapter;

public abstract class BuDragExListAdapter<G, C> extends BaseExpandableListAdapter {

	protected Context mContext;
	protected List<G> groups = new ArrayList<G>();
	protected List<List<C>> childs = new ArrayList<List<C>>();
	private BuDragExListView expand;
	private boolean groupboolean = false, childboolean = false, childclickboolean = false;

	public BuDragExListAdapter(Context c, List<G> groups, List<List<C>> childs, BuDragExListView expand) {
		this.mContext = c;
		this.childs = childs;
		this.groups = groups;
		this.expand = expand;
	}

	public void refresh(List<G> groups, List<List<C>> childs) {
		this.groups = groups;
		this.childs = childs;
		notifyDataSetChanged();
	}

	public static interface OnDragListViewAdapterListener {
		void notifyDataSetChanged(int groups);
	}

	private OnDragListViewAdapterListener onDragListViewAdapterListener;

	public void setOnDragListViewAdapterListener(OnDragListViewAdapterListener onDragListViewAdapterListener) {
		this.onDragListViewAdapterListener = onDragListViewAdapterListener;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		if (null != onDragListViewAdapterListener) {
			onDragListViewAdapterListener.notifyDataSetChanged(getGroupCount());
		}
	}

	public void setGroupboolean(boolean b) {
		this.groupboolean = b;
	}

	public void insert(int groupPosition, int position, C items) {
		childs.get(groupPosition).remove(items);
		childs.get(groupPosition).add(position, items);
	}

	public void insertTgroup(int groupPosition, int whichgroup, C items) {
		childs.get(groupPosition).remove(items);
		childs.get(whichgroup).add(items);
	}

	public void removeChild(int whichgroup, C items) {
		childs.get(whichgroup).remove(items);
	}

	@Override
	public int getGroupCount() {
		return groups.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return childs.get(groupPosition).size();
	}

	@Override
	public G getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	@Override
	public C getChild(int groupPosition, int childPosition) {
		return childs.get(groupPosition).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	public abstract View groupView(final int groupPosition, final boolean isExpanded);

	@Override
	public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {
		if (null == convertView) {
			convertView = groupView(groupPosition, isExpanded);
		}

		expand.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					break;
				case MotionEvent.ACTION_MOVE:
					groupboolean = true;
					childboolean = true;
					childclickboolean = false;
					break;
				case MotionEvent.ACTION_UP:
					break;
				default:
					break;
				}
				return false;
			}
		});
		if (!groupboolean) {
			loadAnimation(convertView, 100l + groupPosition * 100l);
		}
		convertView.requestFocus();
		return convertView;
	}

	private void loadAnimation(View view, long offset) {
		if (isCanAnimation()) {
			Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.drag_expand_top_to_bottom);
			anim.setStartOffset(offset);
			view.setAnimation(anim);
		}
	}

	public boolean isCanAnimation() {
		return true;
	}

	public abstract View childView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent);

	@Override
	public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		convertView = childView(groupPosition, childPosition, isLastChild, convertView, parent);
		if (null != convertView) {
			if (!groupboolean || !childboolean) {
				convertView.clearAnimation();
				convertView.clearFocus();
				loadAnimation(convertView, 500l + groupPosition * 50l + childPosition * 100l);
			} else if (childclickboolean) {
				convertView.clearAnimation();
				convertView.clearFocus();
				convertView.requestFocus();
				loadAnimation(convertView, 50l + childPosition * 500l);
			}
		}
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		// childclickboolean = false;
		if (null != onDragListViewAdapterListener) {
			onDragListViewAdapterListener.notifyDataSetChanged(getGroupCount());
		}
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		childclickboolean = true;
		if (null != onDragListViewAdapterListener) {
			onDragListViewAdapterListener.notifyDataSetChanged(getGroupCount());
		}
		groupCollapsed(groupPosition);
	}

	public void groupCollapsedAll() {
		for (int i = 0; i < getGroupCount(); i++) {
			expand.collapseGroup(i);
		}
	}

	public void groupCollapsed(int groupPosition) {
		for (int i = 0; i < getGroupCount(); i++) {
			if (groupPosition != i) {
				expand.collapseGroup(i);
			}
		}
	}

	public void groupExpanded(int groupPosition) {
		for (int i = 0; i < getGroupCount(); i++) {
			if (groupPosition == i) {
				expand.expandGroup(i);
			} else {
				expand.collapseGroup(i);
			}
		}
	}

	public void groupExpandedAll() {
		for (int i = 0; i < getGroupCount(); i++) {
			expand.expandGroup(i);
		}
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

}
