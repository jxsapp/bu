package org.bu.android.app;

import org.bu.android.acty.BuActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;

public abstract class BuSelfLogic<V> extends BuUILogic<BuActivity, V> implements IBuUI, View.OnTouchListener {

	public BuSelfLogic(BuActivity t, V v) {
		super(t, v);
	}

	public BuActivity getActivity() {
		return mActivity;
	}

	@Override
	public void mHandlePostDelayed(DelayDoListener delayDoListener, long delayMillis) {
		super.mHandlePostDelayed(delayDoListener, delayMillis);
	}

	@Override
	public boolean mHandleMessage(Message msg) {
		return super.mHandleMessage(msg);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {

		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return false;
	}

	@Override
	public void initUI(Bundle savedInstanceState, Object... params) {

	}

}
