package org.bu.android.widget.time;

import java.text.SimpleDateFormat;

import org.bu.android.acty.BuActivity;
import org.bu.android.app.BuUILogic;
import org.bu.android.widget.time.BuTimeMenuMaster.BuTimeMenuListener;

import android.view.View;

public interface BuTimeSelectorMaster {

	class BuTimeSelectorViewHolder {

	}

	abstract class BuTimeSelectorListener extends BuTimeMenuListener {

	}

	class BuTimeSelectorLogic extends BuUILogic<BuActivity, BuTimeSelectorViewHolder> {

		public BuTimeSelectorLogic(BuActivity t) {
			super(t, new BuTimeSelectorViewHolder());
		}

		public void builder(View parent, String time, final BuTimeSelectorListener dateSelectorListener) {
			new BuTimeMenu(mActivity).show(parent, time, new BuTimeMenuListener() {

				@Override
				public void onTime(long time, String format) {
					dateSelectorListener.onTime(time, format);
				}

				@Override
				public SimpleDateFormat getDateFormat() {
					return dateSelectorListener.getDateFormat();
				}
			});
		}

	}

}
