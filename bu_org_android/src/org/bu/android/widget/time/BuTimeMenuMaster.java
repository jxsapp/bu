package org.bu.android.widget.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.bu.android.R;
import org.bu.android.app.BuUILogic;
import org.bu.android.app.IBuUI;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public interface BuTimeMenuMaster {

	static public abstract class BuTimeMenuListener {

		public DateUIType getDateUIType() {
			return DateUIType._DATE;
		}

		public abstract void onTime(long time, String format);

		public void onCancel() {

		}

		public abstract SimpleDateFormat getDateFormat();

	}

	class BuTimeMenuViewHolder {
		LinearLayout menu_menus;
		Button menu_cancel;
	}

	class BuTimeMenuLogic extends BuUILogic<View, BuTimeMenuViewHolder> implements IBuUI {

		private BuTimeMenu weiMiMenu;

		public BuTimeMenuLogic(View t, BuTimeMenu weiMiMenu) {
			super(t, new BuTimeMenuViewHolder());
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

		public void show(final BuTimeMenu buTimeMenu, final BuTimeMenuListener weiMiMenuListener, String time) {
			final View timepickerview = LayoutInflater.from(mActivity.getContext()).inflate(R.layout.bu_wheel_timepicker, null);
			ScreenInfo screenInfo = new ScreenInfo((Activity) mActivity.getContext());
			final WheelMain wheelMain = new WheelMain(timepickerview, weiMiMenuListener.getDateUIType());
			wheelMain.screenheight = screenInfo.getHeight();
			Calendar calendar = Calendar.getInstance();
			if (JudgeDate.isDate(time, weiMiMenuListener.getDateFormat())) {
				try {
					calendar.setTime(weiMiMenuListener.getDateFormat().parse(time));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH);
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			int minus = calendar.get(Calendar.MINUTE);

			wheelMain.initDateTimePicker(year, month, day, hour, minus);

			mViewHolder.menu_menus.removeAllViews();
			mViewHolder.menu_menus.addView(timepickerview);

			final Button confirm = ((Button) mActivity.findViewById(R.id.menu_confirm));
			final Button cancel = ((Button) mActivity.findViewById(R.id.menu_cancel));
			OnClickListener clickListener = new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (null != weiMiMenuListener) {
						if (v.getId() == confirm.getId()) {
							buTimeMenu.dismiss();
							weiMiMenuListener.onTime(wheelMain.getTimeL(), weiMiMenuListener.getDateFormat().format(wheelMain.getTimeL()));
						} else if (v.getId() == cancel.getId()) {
							buTimeMenu.dismiss();
							weiMiMenuListener.onCancel();
						}
					}
				}
			};
			confirm.setOnClickListener(clickListener);
			cancel.setOnClickListener(clickListener);
		}

	}
}
