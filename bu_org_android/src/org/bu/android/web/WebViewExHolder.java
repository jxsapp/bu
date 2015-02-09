package org.bu.android.web;

import org.bu.android.misc.BuStringUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;

@SuppressLint("SetJavaScriptEnabled")
public class WebViewExHolder extends WebViewHolder {
	public static final String BLANK_URL = "file:///android_asset/html/blank.html";
	public static final String IMAGE_WEB_BROWSER_PAGE = "file:///android_asset/html/image_web_browser.html";

	@Override
	public void loadUrl(WebLoadingListener webLoadingListener, String url, boolean builtInZoomControls, boolean supportZoom) {
		if (BuStringUtils.isEmpety(url)) {
			url = BLANK_URL;
		}
		WebView webview = webLoadingListener.getWebView();
		Activity activity = webLoadingListener.getActivity();
		DisplayMetrics _dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(_dm);
		// 1280_800
		WebSettings ws = webview.getSettings();

		ws.setDefaultTextEncodingName("utf-8"); // 设置文本编码
		ws.setAppCacheEnabled(false);
		ws.setJavaScriptEnabled(true);
		ws.setBuiltInZoomControls(builtInZoomControls);
		ws.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		ws.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);// 设置缓存模式
		ws.setAllowFileAccess(true);
		ws.setDomStorageEnabled(false);
		// ws.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		ws.setUseWideViewPort(true);
		ws.setSupportZoom(supportZoom);
		ws.setLoadWithOverviewMode(true);
		webview.setWebViewClient(getWebViewClient(webLoadingListener));
		webview.setWebChromeClient(getWebChromeClient(webLoadingListener));
		webview.loadUrl(url);
	}

	@SuppressWarnings("deprecation")
	public void loadContent(WebLoadingListener webLoadingListener, String content) {

		WebView webview = webLoadingListener.getWebView();
		Activity activity = webLoadingListener.getActivity();

		DisplayMetrics _dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(_dm);
		// 1280_800
		WebSettings ws = webview.getSettings();

		ws.setDefaultTextEncodingName("utf-8"); // 设置文本编码
		ws.setAppCacheEnabled(false);
		ws.setJavaScriptEnabled(true);
		ws.setBuiltInZoomControls(false);
		ws.setSupportZoom(false);
		ws.setCacheMode(WebSettings.LOAD_NO_CACHE);// 设置缓存模式
		if (_dm.widthPixels >= 720) {
			ws.setTextSize(WebSettings.TextSize.LARGER);
		}
		ws.setAllowFileAccess(true);
		ws.setDomStorageEnabled(false);
		ws.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		webview.setWebViewClient(getWebViewClient(webLoadingListener));
		webview.setWebChromeClient(getWebChromeClient(webLoadingListener));
		webview.loadDataWithBaseURL(null, content, "text/html", "UTF-8", null);
	}

}
