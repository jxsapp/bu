package org.bu.android.photo;

import org.bu.android.R;
import org.bu.android.acty.BuActivity;

import android.os.Bundle;

public class WatchPager extends BuActivity implements WatchPagerMaster {

	private WatchPagerLogic watchPagerLogic;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		watchPagerLogic = new WatchPagerLogic(this);
		initBuBar(R.layout.wm_photo_watch_pager);
		watchPagerLogic.initUI(savedInstanceState);
	}

}
