package org.bu.android.app;

import org.bu.android.R;
import org.bu.android.acty.BuActivity;
import org.bu.android.acty.BuActivityHolder;
import org.bu.android.acty.BuActivityHolder.OnOptionListener;
import org.bu.android.widget.BuActionBar;

import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class BuFragment extends Fragment implements View.OnTouchListener {

	protected void softInputAdjustResize() {
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	protected void softInputHindden() {
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return true;// 防止Fragment点击穿透
	}

	protected BuActionBar wmActionBar;

	public void initBuBar(Bundle savedInstanceState, View rootView) {
		initBuBar(savedInstanceState, rootView, true);
	}

	public void initBuBar(Bundle savedInstanceState, View rootView, boolean listener) {
		wmActionBar = (BuActionBar) rootView.findViewById(R.id.actionbar);
		if (null != wmActionBar) {
			initBuBar(wmActionBar);
		}
	}

	public BuActivity getBuActivity() {
		return (BuActivity) super.getActivity();
	}

	public Looper getMainLooper() {
		return getBuActivity().getMainLooper();
	}

	public void initBuBar(BuActionBar actionBar) {

	}

	public void setActionBarHomeAction(BuActionBar actionBar, OnOptionListener backListener) {
		actionBar.setHomeAction(BuActivityHolder.getOptionAction(getActivity(), backListener));
	}

	public void setActionBarOptionAction(BuActionBar actionBar, OnOptionListener optionListener) {
		actionBar.addAction(BuActivityHolder.getOptionAction(getActivity(), optionListener));
	}

}