package org.bu.android.widget;

import java.util.ArrayList;
import java.util.List;

import org.bu.android.R;
import org.bu.android.widget.BuMenuAdapter.OnBuMenuItemListener;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;

public class BuMenu implements BuMenuMaster {

	private BuMenuPopWindow popupWindow;

	private BuMenuLogic popMenuLogic;
	private BuMenuListener buMenuListener;
	private Context context;

	@SuppressWarnings("deprecation")
	public BuMenu(Context context) {
		this.context = context;
		View view = LayoutInflater.from(this.context).inflate(R.layout.bu_menu, null);
		popMenuLogic = new BuMenuLogic(view, this);
		popMenuLogic.initUI(null);
		popupWindow = new BuMenuPopWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setAnimationStyle(R.style.Bu_Pop_Menu_Anima);
		popupWindow.update();
	}

	public Context getContext() {
		return context;
	}

	public void show(View parent, BuMenuListener weiMiMenuListener) {
		this.buMenuListener = weiMiMenuListener;
		popupWindow.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);// 距离底部的位置
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.update();
		popMenuLogic.show(weiMiMenuListener);
	}

	@SuppressWarnings("rawtypes")
	public void show(final View parent, final List list, final OnBuMenuItemListener onWeiMiMenuItemListener) {
		this.buMenuListener = new BuMenuListener() {

			@SuppressWarnings("unchecked")
			@Override
			public View getMenus(BuMenu menu) {

				ListView listView = (ListView) LayoutInflater.from(context).inflate(R.layout.bu_menu_list, null);
				listView.setAdapter(new BuMenuAdapter(BuMenu.this, list, onWeiMiMenuItemListener));
				return listView;
			}
		};
		popupWindow.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);// 距离底部的位置
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.update();
		popMenuLogic.show(buMenuListener);
	}

	public static void main(String[] args) {
		List<Object> list = new ArrayList<Object>();
		new BuMenu(null).show(null, list, new OnBuMenuItemListener<Object>() {

			@Override
			public void onItemClick(int position, Object t) {

			}
		});
	}

	public void dismiss() {
		popupWindow.dismiss();
	}

	public boolean isShowing() {
		return popupWindow.isShowing();
	}

	public void dismiss(BuMenuListener weiMiMenuListener) {
		this.buMenuListener = weiMiMenuListener;
		popupWindow.dismiss();
	}

	private class BuMenuPopWindow extends PopupWindow {

		private BuMenuPopWindow() {
			super();
		}

		private BuMenuPopWindow(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);

		}

		private BuMenuPopWindow(Context context, AttributeSet attrs) {
			super(context, attrs);

		}

		private BuMenuPopWindow(Context context) {
			super(context);

		}

		private BuMenuPopWindow(int width, int height) {
			super(width, height);

		}

		private BuMenuPopWindow(View contentView, int width, int height, boolean focusable) {
			super(contentView, width, height, focusable);

		}

		private BuMenuPopWindow(View contentView, int width, int height) {
			super(contentView, width, height);

		}

		private BuMenuPopWindow(View contentView) {
			super(contentView);
		}

		@Override
		public void showAtLocation(View parent, int gravity, int x, int y) {
			super.showAtLocation(parent, gravity, x, y);
			if (null != buMenuListener) {
				buMenuListener.onShow(BuMenu.this);
			}
		}

		@Override
		public void dismiss() {
			if (null != buMenuListener) {
				buMenuListener.onDismiss(BuMenu.this);
			}
			super.dismiss();
		}

	}

}
