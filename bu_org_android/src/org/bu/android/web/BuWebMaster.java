package org.bu.android.web;

import org.bu.android.R;
import org.bu.android.acty.BuActivity;
import org.bu.android.app.BuUILogic;
import org.bu.android.app.IBuUI;
import org.bu.android.misc.BuStringUtils;
import org.bu.android.widget.BuMenu;
import org.bu.android.widget.BuMenuItemPop;
import org.bu.android.widget.BuMenuMaster.BuMenuListener;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;

public interface BuWebMaster {
	class BuWebViewHolder {
		WebView webView;
		BuMenu weiMiMenu;
	}

	class BuWebLogic extends BuUILogic<BuActivity, BuWebViewHolder> implements IBuUI {

		private String url = "";

		BuWebLogic(BuActivity t, String url) {
			super(t, new BuWebViewHolder());
			this.url = url;
		}

		@Override
		public void onClick(View v) {
		}

		@Override
		public void initUI(Bundle savedInstanceState, Object... params) {
			mViewHolder.weiMiMenu = new BuMenu(mActivity);
			mViewHolder.webView = new WebView(mActivity.getApplicationContext());
			((LinearLayout) mActivity.findViewById(R.id.webview_extends_ll)).addView(mViewHolder.webView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT));
			loadContent(url);
		}

		private void toOtherBrowser(String url) {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mActivity.startActivity(intent);
		}

		private void loadContent(String loadUrl) {
			mActivity.getBuActionBar().showLoading();
			new WebViewHolder().loadUrl(new WebViewHolder.WebLoadingListener(mActivity, mViewHolder.webView) {

				@Override
				public void onPageStarted(WebView webView, String url, Bitmap favicon) {
					super.onPageStarted(webView, url, favicon);
					if (!BuStringUtils.isEmpety(webView.getTitle())) {
						mActivity.getBuActionBar().setTitle(webView.getTitle());
					}
					mActivity.getBuActionBar().showLoading();
				}

				@Override
				public void onPageFinished(WebView webView, String url) {
					mActivity.getBuActionBar().setTitle(webView.getTitle());
					mActivity.getBuActionBar().dismissLoading();
				}

			}, loadUrl);
		}

		public void onBackPressed() {
			if (null != mViewHolder.webView && mViewHolder.webView.canGoBack()) {
				mViewHolder.webView.goBack();
			} else {
				mActivity.finish();
			}
		}

		public void showMoreMenu() {
			mViewHolder.weiMiMenu.show(mViewHolder.webView, new BuMenuListener() {
				@Override
				public void onDismiss(BuMenu menu) {
					super.onDismiss(menu);
				}

				@Override
				public View getMenus(final BuMenu menu) {
					BuMenuItemPop contentView = new BuMenuItemPop(mActivity);

					final Button addInvit = contentView.builderItem();
					addInvit.setText("在浏览器中打开");
					contentView.addViews(addInvit);

					View.OnClickListener clickListener = new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							menu.dismiss();
							if (v.getTag().equals(addInvit.getTag())) {
								toOtherBrowser(url);
							}
						}
					};
					addInvit.setOnClickListener(clickListener);
					return contentView;
				}
			});
		}

		@Override
		protected void onDestroy() {
			super.onDestroy();
			mViewHolder.webView.removeAllViews();
			mViewHolder.webView.destroyDrawingCache();
			mViewHolder.webView.destroy();
		}

		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		}

	}

}
