package org.bu.android.app;

import java.lang.ref.WeakReference;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;

public abstract class BuUILogic<T, V> extends BuActyListener {
	protected String TAG = "";
	protected T mActivity;
	protected V mViewHolder;
	protected Handler mHandler;

	public BuUILogic(T t, V v) {
		mActivity = new WeakReference<T>(t).get();
		this.mViewHolder = v;
		TAG = getClass().getSimpleName();
		mHandler = new Handler(new Callback() {

			@Override
			public boolean handleMessage(Message msg) {
				return mHandleMessage(msg);
			}
		});
	}

	public void mHandlePostDelayed(final DelayDoListener delayDoListener, final long delayMillis) {
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				delayDoListener.delayDo(delayMillis);
			}
		}, delayMillis);
	}

	public boolean mHandleMessage(Message msg) {
		return false;
	}

	protected void onBackPressed() {

	}

	@Override
	protected void onPause() {

	}

	@Override
	protected void onResume() {

	}

	@Override
	protected void onStop() {

	}

	@Override
	protected void onDestroy() {

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

	}

}
