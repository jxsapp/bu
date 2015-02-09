package org.bu.android.misc;

import org.bu.android.boot.BuApplication;

import android.content.Context;
import android.os.PowerManager;

/**
 * @Des 电源锁工具类
 * @Author jiangxs
 * @Des 2012-9-7 上午10:43:44
 */
public class BuWakeLockUtils {

	private static BuWakeLockUtils instance;
	private PowerManager.WakeLock wakeLock;
	private PowerManager powerManager;

	@SuppressWarnings("deprecation")
	private BuWakeLockUtils() {
		powerManager = (PowerManager) BuApplication.getApplication().getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "dh.weilh.com.sptas");
	}

	public static BuWakeLockUtils getInstance() {
		if (null == instance) {
			instance = new BuWakeLockUtils();
		}
		return instance;
	}

	/**
	 * @Des 获取唤醒锁
	 */
	public void acquireWakeLock() {
		if (wakeLock == null) {
			wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, this.getClass().getCanonicalName());
			wakeLock.acquire();
		}
	}

	/**
	 * @Des 在程序退出时，必须释放WakeLock：
	 */
	public void releaseWakeLock() {
		if (wakeLock != null && wakeLock.isHeld()) {
			wakeLock.release();
			wakeLock = null;
		}
	}

}
