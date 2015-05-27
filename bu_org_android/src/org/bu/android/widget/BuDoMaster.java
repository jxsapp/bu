package org.bu.android.widget;

import org.bu.android.R;
import org.bu.android.app.BuUILogic;
import org.bu.android.app.IBuUI;
import org.bu.android.misc.BuStringUtils;

import android.os.Bundle;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public interface BuDoMaster {

	public abstract class WeiMiDoListener {

		/**
		 * 目录项
		 * 
		 * @param menu
		 * @return
		 */
		public abstract View getMenus(BuDo menu);

		/**
		 * 标题
		 * 
		 * @return
		 */
		public abstract String getTitle();

		public WeiMiDoListener() {
			super();
		}

		public void onShow(BuDo menu) {

		}

		public void onDismiss(BuDo menu) {
		}

	}

	class BuDoViewHolder {
		LinearLayout menu_menus;
		TextView menu_title;
		View do_root;
	}

	class BuDoLogic extends BuUILogic<View, BuDoViewHolder> implements IBuUI {

		private BuDo weiMiMenu;

		public BuDoLogic(View t, BuDo weiMiMenu) {
			super(t, new BuDoViewHolder());
			this.weiMiMenu = weiMiMenu;
		}

		@Override
		public void onClick(View v) {
		}

		public void initUI(Bundle savedInstanceState, Object... params) {
			mViewHolder.do_root = mActivity.findViewById(R.id.do_root);
			mViewHolder.menu_menus = (LinearLayout) mActivity.findViewById(R.id.menu_menus);
			mViewHolder.menu_title = (TextView) mActivity.findViewById(R.id.menu_title);
			mViewHolder.do_root.setOnTouchListener(this);
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			weiMiMenu.dismiss();
			return super.onTouch(v, event);
		}

		public void show(WeiMiDoListener weiMiMenuListener) {
			mViewHolder.menu_menus.removeAllViews();
			mViewHolder.menu_menus.addView(weiMiMenuListener.getMenus(weiMiMenu));
			if (BuStringUtils.isEmpety(weiMiMenuListener.getTitle())) {
				mViewHolder.menu_title.setText("");
			} else {
				mViewHolder.menu_title.setText(Html.fromHtml(weiMiMenuListener.getTitle()));
			}

		}

	}
}
