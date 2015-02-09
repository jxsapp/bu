package org.bu.android.desktop;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.bu.android.R;
import org.bu.android.acty.BuActivity;
import org.bu.android.app.BuUILogic;
import org.bu.android.app.IBuUI;
import org.bu.android.desktop.MenuDrawer.OnDrawerStateChangeListener;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

public interface DesktopMaster {

	class DesktopItem implements Serializable {
		private static final long serialVersionUID = 8520848025430295020L;
		public DesktopItemID id;// 本条目ID
		public int icon;// 图标
		public String title;// 标题
		public String des;// 描述
		public int count;// 统计

		public DesktopItem(DesktopItemID id, int icon, String title) {
			super();
			this.id = id;
			this.icon = icon;
			this.title = title;
		}

	}

	public class DesktopItemID implements Serializable {
		private static final long serialVersionUID = -2854126472165366125L;
		public static final DesktopItemID NULL = new DesktopItemID(-1); //
		public static final DesktopItemID HEADER = new DesktopItemID(-0x11); //
		public static final DesktopItemID FOOTER = new DesktopItemID(-0x12); //
		// 操作目录

		public int id = -1;

		public DesktopItemID(int id) {
			super();
			this.id = id;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

	}

	public abstract class DesktopAdapter extends BaseAdapter {

		public static class ViewHolder {
		}

		private List<DesktopItem> leftMenuInfos = new ArrayList<DesktopItem>();

		private LayoutInflater inflater = null;
		private ViewHolder viewHolder;
		private DesktopItemID currentId = DesktopItemID.NULL;
		private DesktopLogic leftMenuLogic;

		public DesktopAdapter(DesktopLogic leftMenuLogic, List<DesktopItem> leftMenuInfos) {
			this.inflater = leftMenuLogic.getInflater();
			this.leftMenuLogic = leftMenuLogic;
			this.leftMenuInfos = leftMenuInfos;
		}

		public int getCount() {
			return leftMenuInfos.size();
		}

		public DesktopItem getItem(int position) {
			return leftMenuInfos.get(position);
		}

		public int getPosition(DesktopItem item) {
			return leftMenuInfos.indexOf(item);
		}

		public long getItemId(int position) {
			return position;
		}

		public abstract ViewHolder getViewHolder();

		public abstract View initConvertView(ViewHolder holder);

		public abstract void valueConvertView(ViewHolder holder, DesktopItem desktopItem);

		public View getView(final int position, View convertView, ViewGroup parent) {
			final DesktopItem leftMenuInfo = leftMenuInfos.get(position);
			if (null == convertView) {
				viewHolder = getViewHolder();

				convertView = inflater.inflate(R.layout.bu_desktop_list_item, null);
				LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.menu_items);
				layout.addView(initConvertView(viewHolder));
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			valueConvertView(viewHolder, leftMenuInfo);
			leftMenuLogic.onItemClick(currentId, leftMenuInfo.id, convertView);
			return convertView;
		}

		public void refreshLeftMenu(DesktopItemID currentId, List<DesktopItem> leftMenuInfos) {
			this.currentId = currentId;
			this.leftMenuInfos = leftMenuInfos;
			notifyDataSetChanged();
		}

		public void refreshLeftMenu(List<DesktopItem> leftMenuInfos) {
			this.leftMenuInfos = leftMenuInfos;
			notifyDataSetChanged();
		}

	}

	class DesktopViewHolder {

		MenuDrawer menuDrawer;
		View wm_desktop_menu;
		ListView listView;
		View list_topView;
		View list_bottomView;
	}

	/**
	 * 左侧菜单适配器
	 * 
	 * @author jxs
	 * @time 2014-5-22 下午3:09:16
	 * 
	 */

	interface DesktopListener {
		public void onDesktopItemClick(DesktopItemID currentId, DesktopItemID desktopItemID);

		public void onDesktopHeaderClick(View view);

		public void onDesktopFooterClick(View view);

		public List<DesktopItem> getDesktopItem();
	}

	/**
	 * 左侧菜单
	 * 
	 * @author jxs
	 * @time 2014-5-22 下午3:09:05
	 * 
	 */
	abstract class DesktopLogic extends BuUILogic<BuActivity, DesktopViewHolder> implements IBuUI {

		private DesktopAdapter desktopAdapter;
		private DesktopListener desktopListener;

		private LayoutInflater inflater;

		public DesktopLogic(BuActivity t, DesktopListener desktopListener) {
			super(t, new DesktopViewHolder());
			this.desktopListener = desktopListener;
			inflater = LayoutInflater.from(mActivity);
		}

		public LayoutInflater getInflater() {
			return inflater;
		}

		public abstract DesktopAdapter getDesktopAdapter();

		public abstract int getContentView();

		public int getDeskBackResid() {
			return -1;
		}

		public View getHeaderView() {
			return null;
		}

		public View getBottomView() {
			return null;
		}

		public boolean isFixed() {
			return true;
		}

		@Override
		public void onClick(View v) {
		}

		public void onItemClick(final DesktopItemID currentId, final DesktopItemID desktopItemID, View convertView) {
			convertView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					desktopListener.onDesktopItemClick(currentId, desktopItemID);
				}
			});
		}

		@Override
		public void initUI(Bundle savedInstanceState, Object... params) {

			this.desktopAdapter = getDesktopAdapter();
			mViewHolder.list_topView = inflater.inflate(R.layout.bu_desktop_header, null);
			mViewHolder.list_bottomView = inflater.inflate(R.layout.bu_desktop_bottom, null);

			if (null != getHeaderView()) {
				((LinearLayout) mViewHolder.list_topView.findViewById(R.id.menu_headers)).addView(getHeaderView());
			}

			if (null != getBottomView()) {
				((LinearLayout) mViewHolder.list_bottomView.findViewById(R.id.menu_bottoms)).addView(getBottomView());
			}

			mViewHolder.menuDrawer = MenuDrawer.attach(mActivity, MenuDrawer.MENU_DRAG_CONTENT);
			mViewHolder.menuDrawer.setContentView(getContentView());
			mViewHolder.menuDrawer.setMenuView(R.layout.bu_desktop_list);

			mViewHolder.listView = (ListView) mActivity.findViewById(R.id.left_listView);
			mViewHolder.wm_desktop_menu = mActivity.findViewById(R.id.wm_desktop_menu);
			mViewHolder.listView.setDividerHeight(0);
			if (isFixed()) {

				if (null != getHeaderView()) {
					((LinearLayout) mActivity.findViewById(R.id.left_header)).addView(getHeaderView());
				}

				if (null != getBottomView()) {
					((LinearLayout) mActivity.findViewById(R.id.left_bottomer)).addView(getBottomView());
				}

			} else {
				mViewHolder.listView.addHeaderView(mViewHolder.list_topView);
				mViewHolder.listView.addFooterView(mViewHolder.list_bottomView);
			}
			mViewHolder.listView.setAdapter(desktopAdapter);
			mViewHolder.list_topView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					desktopListener.onDesktopHeaderClick(v);
				}
			});
			View.OnClickListener footerListener = new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					desktopListener.onDesktopFooterClick(v);
				}
			};
			if (getDeskBackResid() > -1) {
				mViewHolder.wm_desktop_menu.setBackgroundResource(getDeskBackResid());
			}
			mViewHolder.list_bottomView.setOnClickListener(footerListener);
			refreshDesktopData();
		}

		public void refreshDesktopData() {

		}

		public void setOnDrawerStateChangeListener(OnDrawerStateChangeListener listener) {
			mViewHolder.menuDrawer.setOnDrawerStateChangeListener(listener);
		}

		public void refreshLeftMenu(DesktopItemID currentId, List<DesktopItem> leftMenuInfos) {
			desktopAdapter.refreshLeftMenu(currentId, leftMenuInfos);
			viewSelected(mViewHolder.list_topView, currentId.id == DesktopItemID.HEADER.id);
			viewSelected(mViewHolder.list_bottomView, currentId.id == DesktopItemID.FOOTER.id);
		}

		public void viewSelected(View convertView, boolean selected) {
		}

		public void closeMenu() {
			mViewHolder.menuDrawer.closeMenu();
		}

		public void openMenu() {
			mViewHolder.menuDrawer.openMenu();
		}

		public boolean isOpened() {
			int drawerState = mViewHolder.menuDrawer.getDrawerState();
			return drawerState == MenuDrawer.STATE_OPEN || drawerState == MenuDrawer.STATE_OPENING;
		}

		/**
		 * @param leftMenuInfos
		 */
		public void refreshLeftMenu(List<DesktopItem> leftMenuInfos) {
			desktopAdapter.refreshLeftMenu(leftMenuInfos);
		}

	}

}
