package org.bu.android.app;

import java.lang.ref.WeakReference;

public class UIGo<T> {
	protected String TAG = "";
	protected T mActivity;
	protected Class<?> actyClass = null;

	public UIGo(T t, Class<?> actyClass) {
		this.mActivity = new WeakReference<T>(t).get();
		TAG = mActivity.getClass().getSimpleName();
		this.actyClass = actyClass;
	}

}