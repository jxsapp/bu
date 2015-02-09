package org.bu.android.app;

import android.content.Intent;
import android.os.Bundle;

public abstract class BuActyListener {

	protected abstract void onPause();

	protected abstract void onResume();

	protected abstract void onStop();

	protected abstract void onDestroy();

	protected abstract void onSaveInstanceState(Bundle outState);

	protected abstract void onRestoreInstanceState(Bundle savedInstanceState);

	protected abstract void onActivityResult(int requestCode, int resultCode, Intent data);

	public static interface DelayDoListener {
		void delayDo(long delayMillis);
	}

}
