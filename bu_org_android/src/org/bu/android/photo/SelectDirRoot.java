package org.bu.android.photo;

import org.bu.android.R;
import org.bu.android.acty.BuActivity;
import org.bu.android.widget.BuActionBar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

public class SelectDirRoot extends BuActivity implements SelectDirRootMaster {

	private SelectDirRootLogic selectDirRootLogic;
	private boolean single = false;
	public static Bitmap bimap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (null != getIntent() && null != getIntent().getExtras()) {
			Bundle bundle = getIntent().getExtras();
			if (bundle.containsKey("single")) {
				single = bundle.getBoolean("single", false);
			}
		}
		bimap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_unfocused);
		selectDirRootLogic = new SelectDirRootLogic(this, single);
		initBuBar(R.layout.wm_select_dir_root);
		selectDirRootLogic.initUI(savedInstanceState);
	}
	

	@Override
	public void initBuBar(BuActionBar actionBar) {
		super.initBuBar(actionBar);
		actionBar.setTitle("选择图片");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		selectDirRootLogic.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		selectDirRootLogic.onDestroy();
	}

}
