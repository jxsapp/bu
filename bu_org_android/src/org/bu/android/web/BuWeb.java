package org.bu.android.web;

import org.bu.android.R;
import org.bu.android.acty.BuActivity;
import org.bu.android.acty.BuActivityHolder.OnOptionListener;
import org.bu.android.misc.BuStringUtils;
import org.bu.android.widget.BuActionBar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class BuWeb extends BuActivity implements BuWebMaster {
	private String url = "";
	private String title = "";
	private BuWebLogic weiMiWebLogic;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		if (null != getIntent() && null != getIntent().getExtras()) {
			this.url = getIntent().getStringExtra("url");
			this.title = getIntent().getStringExtra("title");
		}
		super.onCreate(savedInstanceState);
		initBuBar(R.layout.bu_topbar_webview);
		weiMiWebLogic = new BuWebLogic(this, url);
		weiMiWebLogic.initUI(savedInstanceState);
	}

	@Override
	public void initBuBar(BuActionBar actionBar) {
		super.initBuBar(actionBar);
		if (!BuStringUtils.isEmpety(title)) {
			actionBar.setTitle(title);
		} else {
			actionBar.setTitle(R.string.app_name);
		}
		setActionBarOptionAction(actionBar, new OnOptionListener() {

			@Override
			public void callback(Activity mActivity) {
				weiMiWebLogic.showMoreMenu();
			}

			@Override
			public int getResId() {
				return R.drawable.v2_more;
			}

		});
	}

	@Override
	public void onBackPressed() {
		weiMiWebLogic.onBackPressed();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		weiMiWebLogic.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		weiMiWebLogic.onDestroy();
	}

}