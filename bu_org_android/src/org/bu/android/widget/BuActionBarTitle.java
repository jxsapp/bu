package org.bu.android.widget;

import org.bu.android.R;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BuActionBarTitle extends RelativeLayout {

	private TextView list_title;
	private TextView no_net_work;
	private ImageView list_switch;
	private ImageView list_has_new;

	/** 下拉PopupWindow */
	private BuPopWindow mPopupWindow;
	private ActionBarTitleListener mActionBarTitleListener;
	private LinearLayout menu_filters;

	public BuActionBarTitle(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public BuActionBarTitle(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public BuActionBarTitle(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		LayoutInflater.from(context).inflate(R.layout.bu_actionbar_list_title, this);
		this.list_title = (TextView) findViewById(R.id.list_title);
		this.no_net_work = (TextView) findViewById(R.id.no_net_work);
		this.list_switch = (ImageView) findViewById(R.id.list_switch);
		this.list_has_new = (ImageView) findViewById(R.id.list_has_new);
		switchList(false);
		setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mPopupWindow != null) {
					if (!mPopupWindow.isShowing()) {
						// 设置PopupWindow弹出,退出样式
						mPopupWindow.setFocusable(true);
						mPopupWindow.setOutsideTouchable(true);
						mPopupWindow.update();
						mPopupWindow.showAsDropDown(BuActionBarTitle.this, 0, 0);
						if (null != menu_filters) {
							menu_filters.removeAllViews();
							menu_filters.addView(mActionBarTitleListener.getMenus(BuActionBarTitle.this));
						}
					}
				}
			}
		});
	}

	public void setHasNew() {
		list_has_new.setVisibility(VISIBLE);
	}

	public void setNoHasNew() {
		list_has_new.setVisibility(INVISIBLE);
	}

	public boolean isHasNew() {
		return list_has_new.getVisibility() == VISIBLE;
	}

	public void noNetwork() {
		no_net_work.setText("(未连接)");
	}

	public void hasNetwork() {
		no_net_work.setText("");
	}

	public void setText(CharSequence text) {
		list_title.setText(text);
	}

	public void setText(int rst) {
		list_title.setText(rst);
	}

	public CharSequence getText() {
		return list_title.getText();
	}

	public void switchList(boolean hasNew) {
		if (hasNew) {
			list_switch.setVisibility(VISIBLE);
		} else {
			list_switch.setVisibility(GONE);
		}
	}

	@Override
	protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
		if (focused) {
			super.onFocusChanged(focused, direction, previouslyFocusedRect);
		}
	}

	@Override
	public void onWindowFocusChanged(boolean focused) {
		if (focused) {
			super.onWindowFocusChanged(focused);
		}
	}

	@Override
	public boolean isFocused() {
		return true;
	}

	public void dismiss() {
		if (mPopupWindow != null) {
			mPopupWindow.dismiss();
		}
	}

	/**
	 * @Description: 设置下拉布局文件,及布局文件创建监听器
	 * @param mActionBarTitleListener
	 *            布局文件创建监听器
	 */
	@SuppressWarnings("deprecation")
	public void setActionBarTitleListener(ActionBarTitleListener mActionBarTitleListener) {
		this.mActionBarTitleListener = mActionBarTitleListener;

		View view = LayoutInflater.from(getContext()).inflate(R.layout.bu_actionbar_filter, null);
		this.menu_filters = (LinearLayout) view.findViewById(R.id.menu_filters);
		mPopupWindow = new BuPopWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow.setAnimationStyle(R.style.Bu_Pop_Title_Anima);
		mPopupWindow.update();
		switchList(true);
	}

	private class BuPopWindow extends PopupWindow {

		private BuPopWindow() {
			super();
		}

		private BuPopWindow(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);

		}

		private BuPopWindow(Context context, AttributeSet attrs) {
			super(context, attrs);

		}

		private BuPopWindow(Context context) {
			super(context);

		}

		private BuPopWindow(int width, int height) {
			super(width, height);

		}

		private BuPopWindow(View contentView, int width, int height, boolean focusable) {
			super(contentView, width, height, focusable);

		}

		private BuPopWindow(View contentView, int width, int height) {
			super(contentView, width, height);

		}

		private BuPopWindow(View contentView) {
			super(contentView);
		}

		@Override
		public void showAtLocation(View parent, int gravity, int x, int y) {
			super.showAtLocation(parent, gravity, x, y);
			if (null != mActionBarTitleListener) {
				mActionBarTitleListener.onShow(BuActionBarTitle.this);
			}
		}

		@Override
		public void dismiss() {
			if (null != mActionBarTitleListener) {
				mActionBarTitleListener.onDismiss(BuActionBarTitle.this);
			}
			super.dismiss();
		}

	}

	public static abstract class ActionBarTitleListener {

		public abstract View getMenus(BuActionBarTitle menu);

		public void onShow(BuActionBarTitle menu) {

		}

		public void onDismiss(BuActionBarTitle menu) {

		}

	}

}
