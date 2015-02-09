package org.bu.android.image.cropper;

import org.bu.android.R;
import org.bu.android.acty.BuActivity;
import org.bu.android.acty.BuActivityHolder.OnOptionListener;
import org.bu.android.widget.BuActionBar;

import android.app.Activity;
import android.os.Bundle;

public class BuCropper extends BuActivity implements BuCropperMaster {
	private Bundle bundle = new Bundle();
	private BuCropperLogic weiMiCropperLogic;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (null != getIntent() && null != getIntent().getExtras()) {
			bundle = getIntent().getExtras();
		}
		initBuBar(R.layout.bu_cropper);
		weiMiCropperLogic = new BuCropperLogic(this);
		weiMiCropperLogic.initUI(savedInstanceState, bundle);
	}

	@Override
	public void initBuBar(BuActionBar actionBar) {
		super.initBuBar(actionBar);
		actionBar.setTitle("剪裁图片");
		setActionBarOptionAction(actionBar, new OnOptionListener() {

			@Override
			public void callback(Activity mActivity) {
				weiMiCropperLogic.sureOk();
			}

		});
	}

}
