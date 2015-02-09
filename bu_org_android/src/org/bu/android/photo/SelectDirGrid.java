package org.bu.android.photo;

import org.bu.android.R;
import org.bu.android.acty.BuActivity;
import org.bu.android.widget.BuActionBar;

import android.os.Bundle;

public class SelectDirGrid extends BuActivity implements SelectDirGridMaster {

	private SelectDirGridLogic selectDirGridLogic;

	private ImageBucket imageBucket = new ImageBucket();
	private boolean single = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (null != getIntent() && null != getIntent().getExtras()) {
			Bundle bundle = getIntent().getExtras();
			imageBucket = (ImageBucket) bundle.getSerializable("imageBucket");
			single = bundle.getBoolean("single", false);
		}
		selectDirGridLogic = new SelectDirGridLogic(this, single);
		initBuBar(R.layout.wm_select_dir_grid);
		selectDirGridLogic.initUI(savedInstanceState, imageBucket);
	}

	@Override
	public void initBuBar(BuActionBar actionBar) {
		super.initBuBar(actionBar);
		actionBar.setTitle(imageBucket.bucketName);
	}

}
