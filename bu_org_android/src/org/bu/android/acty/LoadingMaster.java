package org.bu.android.acty;

import org.bu.android.app.BuUILogic;
import org.bu.android.app.IBuUI;
import org.bu.android.widget.load.LoadingAlertDialog;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.view.View;

public interface LoadingMaster {

	class LoadingViewHolder {
		LoadingAlertDialog loadingDailog;
	}

	interface LoadingListener {

		public abstract void showLoading(String msg);

		public abstract void showLoading(int msgId);

		public abstract void showError(String msg);

		public abstract void showError(int msgId);

		public abstract void showLoading();

		public abstract void dismissLoading();

		public abstract boolean isLoading();

	}

	class LoadingLogic extends BuUILogic<Activity, LoadingViewHolder> implements IBuUI, LoadingListener {

		public LoadingLogic(Activity t) {
			super(t, new LoadingViewHolder());
		}

		@Override
		public boolean mHandleMessage(Message msg) {
			if (null != mViewHolder.loadingDailog) {
				mViewHolder.loadingDailog.cancel();
			}
			return false;
		}

		@Override
		public void onClick(View v) {

		}

		@Override
		public void initUI(Bundle savedInstanceState, Object... params) {
			mViewHolder.loadingDailog = new LoadingAlertDialog(mActivity);
			// mViewHolder.loadingDailog.setProgressDrawable(mActivity.getResources().getDrawable(R.anim.alert_loading));
		}

		@Override
		public boolean isLoading() {
			if (null == mViewHolder.loadingDailog) {
				return false;
			}
			return mViewHolder.loadingDailog.isShowing();
		}

		@Override
		public void dismissLoading() {
			if (null != mViewHolder.loadingDailog) {
				mViewHolder.loadingDailog.dismiss();
			}
			mHandler.sendEmptyMessageDelayed(-1, 200);
		}

		@Override
		public void showLoading() {
			if (!isLoading()) {
				mViewHolder.loadingDailog.show();
			}
		}

		@Override
		public void showLoading(int msgId) {// 隐藏按钮开始加载
			mViewHolder.loadingDailog.showLoading(msgId);
		}

		@Override
		public void showLoading(String msg) {// 隐藏按钮开始加载
			mViewHolder.loadingDailog.showLoading(msg);
		}

		@Override
		public void showError(String msg) {
			dismissLoading();
		}

		@Override
		public void showError(int msgId) {
			dismissLoading();
		}

	}

}
