package org.bu.android.misc;


import java.lang.ref.WeakReference;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("SetJavaScriptEnabled")
@SuppressWarnings("deprecation")
public class BuWebViewHolder {

	static public class WebLoadingListener {
		public void onProgressChanged(WebView view, int newProgress) {
		}

		public void onPageStarted(WebView view, String url, Bitmap favicon) {
		}

		public void onPageFinished(WebView view, String url) {
		}

		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
		}

		public boolean isOpenNewURLInOtherBrowser() {
			return false;
		}
	}

	public void loadUrl(WebLoadingListener webLoadingListener, WebView webview, String url) {
		loadUrl(webLoadingListener, webview, url, false, false);
	}

	public void loadUrl(WebLoadingListener webLoadingListener, WebView webview, String url, boolean builtInZoomControls, boolean supportZoom) {

		Activity activity = (Activity) webview.getContext();
		DisplayMetrics _dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(_dm);
		// 1280_800
		WebSettings ws = webview.getSettings();

		ws.setDefaultTextEncodingName("utf-8"); // 设置文本编码
		ws.setAppCacheEnabled(false);
		ws.setJavaScriptEnabled(true);
		ws.setBuiltInZoomControls(builtInZoomControls);
		ws.setSupportZoom(supportZoom);
		ws.setDefaultZoom(WebSettings.ZoomDensity.FAR);
		ws.setCacheMode(WebSettings.LOAD_NO_CACHE);// 设置缓存模式
		ws.setAllowFileAccess(true);
		ws.setDomStorageEnabled(true);
		ws.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		webview.setWebViewClient(new BuWebViewClient(webLoadingListener));
		webview.setWebChromeClient(new BuWebChromeClient(webLoadingListener));
		webview.loadUrl(url);
	}

	public void loadContent(WebLoadingListener webLoadingListener, WebView webview, String content) {

		// 1280_800
		WebSettings ws = webview.getSettings();

		ws.setDefaultTextEncodingName("utf-8"); // 设置文本编码
		ws.setAppCacheEnabled(false);
		ws.setJavaScriptEnabled(true);
		ws.setBuiltInZoomControls(false);
		ws.setSupportZoom(false);
		ws.setDefaultZoom(WebSettings.ZoomDensity.FAR);
		ws.setCacheMode(WebSettings.LOAD_NO_CACHE);// 设置缓存模式
		ws.setAllowFileAccess(true);
		ws.setDomStorageEnabled(false);
		ws.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		webview.setWebViewClient(getWebViewClient(webLoadingListener));
		webview.setWebChromeClient(getWebChromeClient(webLoadingListener));
		String customHtml = "<html><body style='background:#F5F5F5'>" + content.replaceAll("\n", "<br/>") + "</body></html>";
		webview.loadDataWithBaseURL(null, customHtml, "text/html", "UTF-8", null);
	}

	class BuWebChromeClient extends WebChromeClient {

		private WebLoadingListener webLoadingListener;

		private BuWebChromeClient(WebLoadingListener webLoadingListener) {
			super();
			this.webLoadingListener = new WeakReference<WebLoadingListener>(webLoadingListener).get();
		}

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			super.onProgressChanged(view, newProgress);
			webLoadingListener.onProgressChanged(view, newProgress);
		}
	}

	public BuWebViewClient getWebViewClient(WebLoadingListener webLoadingListener) {
		return new BuWebViewClient(webLoadingListener);
	}

	public BuWebChromeClient getWebChromeClient(WebLoadingListener webLoadingListener) {
		return new BuWebChromeClient(webLoadingListener);
	}

	class BuWebViewClient extends WebViewClient {

		private WebLoadingListener webLoadingListener;

		private BuWebViewClient(WebLoadingListener webLoadingListener) {
			super();
			this.webLoadingListener = new WeakReference<WebLoadingListener>(webLoadingListener).get();
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {

			if (webLoadingListener.isOpenNewURLInOtherBrowser() || (!TextUtils.isEmpty(url) && url.endsWith("apk"))) {
				view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
				return true;
			} else {
				return false;
			}
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			webLoadingListener.onPageStarted(view, url, favicon);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			webLoadingListener.onPageFinished(view, url);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
			webLoadingListener.onReceivedError(view, errorCode, description, failingUrl);
		}
	}
}