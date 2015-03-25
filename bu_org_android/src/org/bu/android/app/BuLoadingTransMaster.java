package org.bu.android.app;

import org.bu.android.app.BuMaster.BuLogic;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

public interface BuLoadingTransMaster {

	interface BuLoadingTransListener {
		void onBackground();

		void onResult();
	}

	class BuLoadingTransLogic extends BuLogic<BuLoadingTrans, BuLoadingTransListener> implements IBuUI {

		public BuLoadingTransLogic(BuLoadingTrans t, BuLoadingTransListener listener) {
			super(t, listener);
		}

		@Override
		public void onClick(View v) {

		}

		@Override
		public void initUI(Bundle savedInstanceState, Object... params) {
			new LongAsyncTask().execute();
		}

		private class LongAsyncTask extends AsyncTask<Void, Void, Void> {

			@Override
			protected Void doInBackground(Void... params) {
				mViewHolder.onBackground();
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				mActivity.dismiss();
			}

		}

	}
}
