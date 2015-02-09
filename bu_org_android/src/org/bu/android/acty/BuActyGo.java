package org.bu.android.acty;

import org.bu.android.app.UIGo;

import android.content.Intent;
import android.os.Bundle;

public abstract class BuActyGo extends UIGo<BuActivity> {

	public BuActyGo(BuActivity t, Class<?> actyClass) {
		super(t, actyClass);
	}

	public void go() {
		go(new Bundle());
	}

	public void go(Bundle bundle) {
		if (null == bundle) {
			bundle = new Bundle();
		}
		Intent intent = new Intent(mActivity, actyClass);
		intent.putExtras(bundle);
		mActivity.startActivity(intent);
	}

	public void go(Bundle bundle, int reg) {
		if (null == bundle) {
			bundle = new Bundle();
		}
		Intent intent = new Intent(mActivity, actyClass);
		intent.putExtras(bundle);
		mActivity.startActivityForResult(intent, reg);
	}
}
