package org.bu.android.db;

import org.bu.android.boot.BuApplication;
import org.bu.android.misc.BuGenerallyHolder;
import org.bu.android.misc.BuStringUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SystemParamHolder {

	static public interface Key {
		public static final String SHARED_PREFERENCE_NAME = "system_params";
		public static final String FIRST_RUN_OVER = "sys_first_run_over";
		public static final String SYS_DEVICE_ID = "sys_device_id";
		public static final String DEBT_RATE_LIMT = "sys_max_of_rate";

	}

	static public interface Value {
		public static final String FALSE = "false";
		public static final String TRUE = "true";
	}

	public static boolean isFristRunOvered() {
		String va = getParamValue(Key.FIRST_RUN_OVER);
		if (BuStringUtils.isEmpety(va) || SystemParamHolder.Value.FALSE.equals(va))
			return false;
		return true;
	}

	public static double getMaxRate() {
		double rst = -1;
		String va = getParamValue(Key.DEBT_RATE_LIMT);
		try {
			rst = Double.valueOf(va);
		} catch (Exception e) {
			rst = -1;
		}
		return rst;
	}

	public static void saveMaxRate(double limit) {
		saveOrUpdate(Key.DEBT_RATE_LIMT, limit + "");
	}

	public static String getDriverId() {
		String device_id = getParamValue(Key.SYS_DEVICE_ID);
		if (BuStringUtils.isEmpety(device_id)) {
			device_id = BuGenerallyHolder.nextSerialNumber();
			SystemParamHolder.saveOrUpdate(Key.SYS_DEVICE_ID, device_id);
		}
		return device_id;
	}

	public static void saveDriverId(String device_id) {
		SystemParamHolder.saveOrUpdate(Key.SYS_DEVICE_ID, device_id);
	}

	public static void saveFristRunOvered() {
		saveOrUpdate(Key.FIRST_RUN_OVER, Value.TRUE);
	}

	public static void saveOrUpdate(String key, String value) {
		SharedPreferences sharedPrefs = BuApplication.getApplication().getSharedPreferences(Key.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPrefs.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static String getParamValue(String key) {
		SharedPreferences sharedPrefs = BuApplication.getApplication().getSharedPreferences(Key.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		return sharedPrefs.getString(key, "");
	}

	public static long getVersionContentLenggth(String version) {
		long rst = 0;
		try {
			rst = Long.valueOf(getParamValue(version));
		} catch (Exception e) {
			rst = 0;
		}
		return rst;
	}

	public static void saveVersion(String version, long contentLength) {
		saveOrUpdate(version, contentLength + "");
	}

}
