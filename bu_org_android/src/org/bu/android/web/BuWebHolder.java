package org.bu.android.web;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class BuWebHolder {
	private static BuWebHolder instance;

	public static BuWebHolder getInstance() {
		if (null == instance) {
			instance = new BuWebHolder();
		}
		return instance;
	}

	private BuWebHolder() {
		super();
	}

	public void toBrowser(Context t, int textId, String url) {
		toBrowser(t, t.getString(textId), url);
	}

	public void toBrowser(Context t, String title, String url) {
		Context mActivity = new WeakReference<Context>(t).get();
		if (url.indexOf(".apk") != -1) {
			Intent intent = new Intent();
			intent.setAction("android.intent.action.VIEW");
			Uri content_url = Uri.parse(url);
			intent.setData(content_url);
			mActivity.startActivity(intent);
		} else {
			Bundle bundle = new Bundle();
			Intent intent = new Intent(mActivity, BuWeb.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			bundle.putString("url", url);
			bundle.putString("title", title);
			intent.putExtras(bundle);
			mActivity.startActivity(intent);
		}
	}

}
