package org.bu.android.image;

import org.bu.android.R;
import org.bu.android.acty.BuActivity;
import org.bu.android.widget.BuActionBar;

import android.content.res.Configuration;
import android.os.Bundle;

public class BuImageBrowser extends BuActivity implements BuImageBrowserMaster {
	private BuImageBrowserLogic weiMiImageBrowserLogic;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initBuBar(R.layout.bu_image_browser_pager);
		weiMiImageBrowserLogic = new BuImageBrowserLogic(this);
		weiMiImageBrowserLogic.initUI(savedInstanceState, getIntent().getExtras());
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		weiMiImageBrowserLogic.onSaveInstanceState(outState);
	}

	@Override
	public void initBuBar(BuActionBar actionBar) {
		super.initBuBar(actionBar);
		actionBar.setTitle(R.string.prompt_view_image);
	}

	@Override
	public void onResume() {
		super.onResume();
		weiMiImageBrowserLogic.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		weiMiImageBrowserLogic.onPause();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		weiMiImageBrowserLogic.onConfigurationChanged(newConfig);
	}

}