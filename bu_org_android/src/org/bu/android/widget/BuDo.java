package org.bu.android.widget;

import org.bu.android.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

@SuppressLint("InflateParams")
public class BuDo implements BuDoMaster {

	private WeiMiDoPopWindow popupWindow;

	private BuDoLogic popMenuLogic;
	private WeiMiDoListener weiMiMenuListener;
	private Context context;

	public BuDo(Context context) {
		this.context = context;
		View view = LayoutInflater.from(this.context).inflate(R.layout.wm_do, null);
		popMenuLogic = new BuDoLogic(view, this);
		popMenuLogic.initUI(null);
		popupWindow = new WeiMiDoPopWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		popupWindow.setBackgroundDrawable(new ColorDrawable(0x50000000));
		popupWindow.update();

	}

	public void show(View parent, WeiMiDoListener weiMiMenuListener) {
		this.weiMiMenuListener = weiMiMenuListener;
		popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);// 距离底部的位置
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.update();
		popMenuLogic.show(weiMiMenuListener);
	}

	public void dismiss() {
		popupWindow.dismiss();
	}

	public boolean isShowing() {
		return popupWindow.isShowing();
	}

	public void dismiss(WeiMiDoListener weiMiMenuListener) {
		this.weiMiMenuListener = weiMiMenuListener;
		popupWindow.dismiss();
	}

	private class WeiMiDoPopWindow extends PopupWindow {

		public WeiMiDoPopWindow(View contentView, int width, int height, boolean focusable) {
			super(contentView, width, height, focusable);
		}

		@Override
		public void showAtLocation(View parent, int gravity, int x, int y) {
			super.showAtLocation(parent, gravity, x, y);
			if (null != weiMiMenuListener) {
				weiMiMenuListener.onShow(BuDo.this);
			}
		}

		@Override
		public void dismiss() {
			if (null != weiMiMenuListener) {
				weiMiMenuListener.onDismiss(BuDo.this);
			}
			super.dismiss();
		}

	}

}
