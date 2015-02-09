package org.bu.android.widget;

import org.bu.android.R;
import org.bu.android.app.BuUILogic;
import org.bu.android.app.IBuUI;
import org.bu.android.misc.BuKeyboardUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public interface BuMenuMaster {

	class BuPopMenuViewHolder {
		LinearLayout menu_menus;
		Button menu_cancel;
	}

	public abstract class BuMenuListener {

		public abstract View getMenus(BuMenu menu);

		private boolean edit = false;// 编辑事情

		private View root;

		public View getRoot() {
			return root;
		}

		public void setRoot(View root) {
			this.root = root;
		}

		public boolean isEdit() {
			return edit;
		}

		public void setEdit(boolean edit) {
			this.edit = edit;
		}

		public void onShow(BuMenu menu) {

		}

		public void onDismiss(BuMenu menu) {

		}

		private Object object;

		public Object getObject() {
			return object;
		}

		public void setObject(Object object) {
			this.object = object;
		}

		public boolean isToDetail() {
			return true;
		}

	}

	class BuMenuLogic extends BuUILogic<View, BuPopMenuViewHolder> implements IBuUI {

		private BuMenu weiMiMenu;

		public BuMenuLogic(View t, BuMenu weiMiMenu) {
			super(t, new BuPopMenuViewHolder());
			this.weiMiMenu = weiMiMenu;
		}

		@Override
		public void onClick(View v) {
			if (mViewHolder.menu_cancel.getId() == v.getId()) {
				weiMiMenu.dismiss();
			}
		}

		public void initUI(Bundle savedInstanceState, Object... params) {
			mViewHolder.menu_cancel = (Button) mActivity.findViewById(R.id.menu_cancel);
			mViewHolder.menu_menus = (LinearLayout) mActivity.findViewById(R.id.menu_menus);
			mViewHolder.menu_cancel.setOnClickListener(this);
		}

		public void show(BuMenuListener weiMiMenuListener) {
			BuKeyboardUtil.hideSoftKeyBoard((Activity) mActivity.getContext());
			mViewHolder.menu_menus.removeAllViews();
			mViewHolder.menu_menus.addView(weiMiMenuListener.getMenus(weiMiMenu));
		}

	}
}
