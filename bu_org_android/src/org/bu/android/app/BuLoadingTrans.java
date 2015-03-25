package org.bu.android.app;

import org.bu.android.R;
import org.bu.android.acty.BuActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

@SuppressLint("ValidFragment")
public class BuLoadingTrans extends DialogFragment implements BuLoadingTransMaster {

	public static String TAG = BuLoadingTrans.class.getSimpleName();

	private BuLoadingTransLogic sendNaviDFrgLogic;
	private BuLoadingTransListener listener;

	public BuLoadingTrans() {
		super();
	}

	public BuLoadingTrans(BuLoadingTransListener listener) {
		super();
		this.listener = listener;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Bu_Load_Frg);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		View view = inflater.inflate(R.layout.bu_loading_tras_frg, container, false);
		initView(view);
		return view;
	}

	public BuLoadingTrans show(BuActivity activity) {
		show(activity.getSupportFragmentManager(), "" + toString());
		return this;
	}

	/**
	 * 
	 * @param view
	 */
	protected void initView(View view) {
		sendNaviDFrgLogic = new BuLoadingTransLogic(this, listener);
		sendNaviDFrgLogic.initUI(getArguments());
	}

}
