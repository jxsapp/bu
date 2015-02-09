package org.bu.android.acty;

import java.lang.ref.WeakReference;

import org.bu.android.R;
import org.bu.android.widget.BuActionBar;
import org.bu.android.widget.BuActionBar.AbstractAction;
import org.bu.android.widget.BuActionBar.Action;

import android.app.Activity;
import android.view.View;

public class BuActivityHolder {

	public static class OnOptionListener {
		public void callback(Activity mActivity) {
			mActivity.finish();
		}

		public int getResId() {
			return R.drawable.translate1x1;
		}

		public int getTxtId() {
			return BuActionBar.Pattern.VALIDATE;
		}
	}

	public static Action getOptionAction(Activity activity, OnOptionListener optionListener) {
		return new OptionAction(activity, optionListener);
	}

	private static class OptionAction extends AbstractAction {

		private WeakReference<Activity> activity;
		private OnOptionListener optionListener;

		public OptionAction(Activity activity, OnOptionListener optionListener) {
			super(optionListener.getResId(), optionListener.getTxtId());
			this.activity = new WeakReference<Activity>(activity);
			this.optionListener = optionListener;
		}

		@Override
		public void performAction(View view) {
			optionListener.callback(activity.get());
		}
	}

}