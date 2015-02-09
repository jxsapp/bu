package org.bu.android.widget.time;

import org.bu.android.R;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

public class BuTimeMenu implements BuTimeMenuMaster {

	private BuMenuPopWindow popupWindow;

	private BuTimeMenuLogic popMenuLogic;
	private Context context;

	@SuppressWarnings("deprecation")
	public BuTimeMenu(Context context) {
		this.context = context;
		View view = LayoutInflater.from(this.context).inflate(R.layout.bu_time_menu, null);
		popMenuLogic = new BuTimeMenuLogic(view, this);
		popMenuLogic.initUI(null);
		popupWindow = new BuMenuPopWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setAnimationStyle(R.style.Bu_Pop_Menu_Anima);
		popupWindow.update();
	}

	public void show(View parent, String time, BuTimeMenuListener weiMiMenuListener) {
		popupWindow.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);// 距离底部的位置
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.update();
		popMenuLogic.show(this, weiMiMenuListener, time);
	}

	public void dismiss() {
		popupWindow.dismiss();
	}

	public boolean isShowing() {
		return popupWindow.isShowing();
	}

	private class BuMenuPopWindow extends PopupWindow {

		private BuMenuPopWindow(View contentView, int width, int height) {
			super(contentView, width, height);

		}

	}

}
