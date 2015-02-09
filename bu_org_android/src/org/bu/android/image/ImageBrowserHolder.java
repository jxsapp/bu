package org.bu.android.image;

import java.util.List;

import org.bu.android.misc.BuStartHelper;
import org.bu.android.misc.HandlerHolder.IntentRequest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ImageBrowserHolder {

	public static Bundle getBrowser4WebUrl(boolean isLocal, String url) {
		Bundle bundle = new Bundle();
		bundle.putBoolean("isLocal", isLocal);
		bundle.putString("url", url);
		return bundle;
	}

	public static void toBrowser(Activity mActivity, String url) {
		String[] currentUrls = new String[] { url };
		toBrowser(mActivity, 0, currentUrls);
	}

	public static void toBrowser(Activity mActivity, int currentPage, String... currentUrls) {
		if (null == currentUrls) {
			currentUrls = new String[] { "" };
		}
		if (currentPage < 0) {
			currentPage = 0;
		}
		Intent intent = new Intent(mActivity, BuImageBrowser.class);
		Bundle bundle = new Bundle();
		bundle.putInt("image_index", currentPage);
		bundle.putStringArray("image_urls", currentUrls);
		intent.putExtras(bundle);
		BuStartHelper.start4ResultActivity(mActivity, intent, IntentRequest.BROWSER_PIC);
	}

	public static void toBrowser(Activity mActivity, int currentPage, List<String> currentUrls) {
		String[] array = (String[]) currentUrls.toArray();
		toBrowser(mActivity, currentPage, array);
	}

}