package org.bu.android.acty;

import java.util.Timer;
import java.util.TimerTask;

import org.bu.android.R;
import org.bu.android.acty.BuActivityHolder.OnOptionListener;
import org.bu.android.acty.LoadingMaster.LoadingListener;
import org.bu.android.boot.BuApplication;
import org.bu.android.misc.BuOsStatusHelper;
import org.bu.android.widget.BuActionBar;
import org.bu.android.widget.BuToast;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

/**
 * Manager The Activity Can Be Finished when Destroyed
 * 
 * @author jxs
 * @time 2014-9-15 下午8:36:17
 */
public abstract class BuActivity extends FragmentActivity implements LoadingMaster, LoadingListener {

	protected Handler mHandler;
	private final int NET_WORK_ERROR = 0x991;
	private final int _EXIT = 0x990;

	protected void softInputAdjustResize() {
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	private LoadingLogic loadingLogic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inOnCreate();
		mHandler = new Handler(getMainLooper(), new Handler.Callback() {

			@Override
			public boolean handleMessage(Message msg) {
				if (msg.what == _EXIT) {
					isExit = false;
				} else if (msg.what == NET_WORK_ERROR) {
					if (BuOsStatusHelper.isNetworkAvailable()) {
						wmActionBar.hasNetwork();
					} else {
						wmActionBar.noNetwork();
					}
					if (null != wmActionBar) {
						checkStatusNext(wmActionBar.isFilterHasNew());
					}
				}
				return mHandleMessage(msg);
			}
		});
		loadingLogic = new LoadingLogic(this);
		loadingLogic.initUI(savedInstanceState);
	}

	@Override
	public void showLoading(String msg) {
		loadingLogic.showError(msg);
	}

	@Override
	public void showLoading(int msgId) {
		loadingLogic.showError(msgId);
	}

	@Override
	public void showError(String msg) {
		loadingLogic.showError(msg);
	}

	@Override
	public void showError(int msgId) {
		loadingLogic.showError(msgId);
	}

	@Override
	public void showLoading() {
		loadingLogic.showLoading();
	}

	@Override
	public void dismissLoading() {
		loadingLogic.dismissLoading();
	}

	@Override
	public boolean isLoading() {
		return loadingLogic.isLoading();
	}

	protected boolean mHandleMessage(Message msg) {
		return false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		inOnDestroy();
	}

	protected BuActionBar wmActionBar = null;
	private Timer netWorkTimer = new Timer();

	public BuActionBar getBuActionBar() {
		if (null != wmActionBar) {
			return wmActionBar;
		}
		return new BuActionBar(this);
	}

	public void initBuBar(int layout_id) {
		initBuBar(layout_id, true);
	}

	public void initBuBar(int layout_id, boolean listener) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(layout_id);
		doInitBuBar(listener);
	}

	protected void doInitBuBar(boolean listener) {
		wmActionBar = (BuActionBar) findViewById(R.id.actionbar);
		if (null != wmActionBar) {
			if (listener) {
				initNetWorkView(wmActionBar);
			}
			this.initBuBar(wmActionBar);
		}
	}

	private void initNetWorkView(BuActionBar actionBar) {
		netWorkTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				mHandler.sendEmptyMessage(NET_WORK_ERROR);
			}
		}, 2000, 7000);
	}

	public void checkStatusNext(boolean currentHasNew) {

	}

	protected void hindErrorBar() {
		if (null != wmActionBar) {
			wmActionBar.hindErrorBar();
		}
	}

	protected void showErrorBar() {
		if (null != wmActionBar) {
			wmActionBar.showErrorBar();
		}
	}

	public void initBuBar(BuActionBar actionBar) {
		setActionBarHomeAction(actionBar, new OnOptionListener() {

			@Override
			public void callback(Activity mActivity) {
				super.callback(mActivity);
			}

			@Override
			public int getResId() {
				return R.drawable.v2_cancel_d;
			}

		});
	}

	public void setActionBarHomeAction(BuActionBar actionBar, OnOptionListener backListener) {
		actionBar.setHomeAction(BuActivityHolder.getOptionAction(this, backListener));
	}

	public void setActionBarOptionAction(BuActionBar actionBar, OnOptionListener optionListener) {
		actionBar.addAction(BuActivityHolder.getOptionAction(this, optionListener));
	}

	public void sendMessage(int what, Handler handler) {
		Message msg = new Message();
		msg.what = what;
		handler.sendMessage(msg);
	}

	public void sendMessage(int what, Handler handler, Bundle data) {
		Message msg = new Message();
		msg.what = what;
		msg.setData(data);
		handler.sendMessage(msg);
	}

	protected boolean isExit = false;

	public boolean onKeyDownListener(int keyCode, KeyEvent event) {
		return onKeyDownListener(keyCode, event, new OnKeyDownListener() {

			@Override
			public void onback() {
				exit();
			}
		});
	}

	public static interface OnKeyDownListener {
		void onback();
	}

	public boolean onKeyDownListener(int keyCode, KeyEvent event, OnKeyDownListener keyDownListener) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			keyDownListener.onback();
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	protected void exit() {
		if (!isExit) {
			isExit = true;
			BuToast.show("再按一次退出程序");
			mHandler.sendEmptyMessageDelayed(_EXIT, 2000);
		} else {
			// LogoutHolder.logout4BackPresss();
		}
	}

	protected void inOnCreate() {
		BuApplication.getApplication().addActivity(this);
	}

	protected void inOnDestroy() {
		BuApplication.getApplication().removeActivity(this);
	}

}
