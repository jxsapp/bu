package org.bu.android.web;

import java.lang.ref.WeakReference;

import org.bu.android.misc.BuScreenHolder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("SetJavaScriptEnabled")
@SuppressWarnings("deprecation")
public class WebViewHolder {

	static public abstract class WebLoadingListener {

		String startUrl = "";
		private Activity mActivity;
		private WebView mWebView;

		public WebLoadingListener(Activity mActivity, WebView mWebView) {
			this.mActivity = new WeakReference<Activity>(mActivity).get();
			this.mWebView = mWebView;
		}

		public void onProgressChanged(WebView view, int newProgress) {
		}

		public void onPageStarted(WebView view, String url, Bitmap favicon) {
		}

		public void onPageFinished(WebView view, String url) {
		}

		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
		}

		private boolean inSelectionMode = false;

		public boolean isInSelectionMode() {
			return inSelectionMode;
		}

		public void toOtherBrowser(String url) {
			toWeiMiBrowser(url);
		}

		private void toWeiMiBrowser(String url) {
			BuWebHolder.getInstance().toBrowser(getActivity(), "Bu_ORG", url);
		}

		public Activity getActivity() {
			return mActivity;
		}

		public WebView getWebView() {
			return mWebView;
		}

		public void setStartUrl(String startUrl) {
			this.startUrl = startUrl;
		}

		public boolean isFilterUrl() {
			return false;
		}

	}

	public void loadUrl(WebLoadingListener webLoadingListener, String url) {
		loadUrl(webLoadingListener, url, false, false);
	}

	public void loadUrl(WebLoadingListener webLoadingListener, String url, boolean builtInZoomControls, boolean supportZoom) {

		webLoadingListener.setStartUrl(url);

		WebView webview = webLoadingListener.getWebView();
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

		DisplayMetrics _dm = BuScreenHolder.matchParent(webview.getContext());
		if (_dm.widthPixels <= 480) {
			ws.setTextSize(WebSettings.TextSize.LARGEST);
		}

		// ws.setUseWideViewPort(true);
		// ws.setLoadWithOverviewMode(true);
		// webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
		webview.setVerticalScrollBarEnabled(false);
		webview.setVerticalScrollbarOverlay(false);
		webview.setHorizontalScrollBarEnabled(false);
		webview.setHorizontalScrollbarOverlay(false);

		webview.setWebViewClient(new BuWebViewClient(webLoadingListener));
		webview.setWebChromeClient(new BuWebChromeClient(webLoadingListener));
		webview.loadUrl(url);
	}

	public void loadContent(WebLoadingListener webLoadingListener, String content) {

		WebView webview = webLoadingListener.getWebView();
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

		private BuWebViewClient(WebLoadingListener _lster) {
			super();
			this.webLoadingListener = new WeakReference<WebLoadingListener>(_lster).get();
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {

			if (webLoadingListener.isFilterUrl() || (!TextUtils.isEmpty(url) && url.endsWith("apk"))) {
				webLoadingListener.toOtherBrowser(url);
				return true;
			} else {
				return false;
			}
		}

		@Override
		public void onScaleChanged(WebView view, float oldScale, float newScale) {
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			webLoadingListener.onPageStarted(view, url, favicon);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			view.invalidate();
			webLoadingListener.onPageFinished(view, url);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
			webLoadingListener.onReceivedError(view, errorCode, description, failingUrl);
		}
	}
}