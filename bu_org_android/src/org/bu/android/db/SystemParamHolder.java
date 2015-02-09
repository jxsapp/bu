package org.bu.android.db;

import org.bu.android.boot.BuApplication;
import org.bu.android.misc.BuStringUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SystemParamHolder {

	static public interface Key {
		public static final String SHARED_PREFERENCE_NAME = "system_params";
		public static final String FIRST_RUN_OVER = "sys_first_run_over";
		public static final String SESSION_ID = "sessionId";
		public static final String SERVER_IP = "server_ip";
		public static final String SERVER_PORT = "server_port";
		public static final String LOGOUT_CURRENT_USER = "logut_outed";
		public static final String LAST_USER_LH_ID = "last_user_lh_id";

		public static final String HEART_BEAT_TIME = "heart_beat_time";
		public static final String SOFT_UPDATE_URL = "soft_update_url";
		public static final String SOFT_UPDATE_CONTENT = "soft_update_content";
		public static final String SOFT_NEW_VERSION_FORCE = "soft_update_forced";
		public static final String SOFT_NEW_VESION = "soft_new_vesion";

		public static final String CURRENT_TIME_LINE_NUMBER = "current_time_line_phone";
		public static final String CURRENT_PAGE_IS_HISTORY = "current_page_is_history";
		public static final String CURRENT_PAGE_IS_TIME_LINE = "current_page_is_time_line";
		/*
		 * 软件升级时 ， 客户选择取消的版本记录
		 */
		public static final String CANCELED_VERSION = "sys_update_cacel_version";

	}

	static public interface Value {
		public static final String FALSE = "false";
		public static final String TRUE = "true";
	}

	public static void saveCurrentFrid(String phone) {
		saveOrUpdate(Key.CURRENT_TIME_LINE_NUMBER, phone);
		saveOrUpdate(Key.CURRENT_PAGE_IS_TIME_LINE, Value.TRUE);
	}

	public static long getHeartBeatTime() {
		long time = 0;
		try {
			time = Long.valueOf(getParamValue(Key.HEART_BEAT_TIME));
		} catch (Exception e) {
			time = 0;
		}
		return time;
	}

	public static void clearCurrentPhone() {
		saveOrUpdate(Key.CURRENT_TIME_LINE_NUMBER, "");
		saveOrUpdate(Key.CURRENT_PAGE_IS_TIME_LINE, Value.FALSE);
	}

	public static String getCurrentPhone() {
		return getParamValue(Key.CURRENT_TIME_LINE_NUMBER);
	}

	public static boolean isTimeLineCurrentFrid(String phone) {
		String va = getParamValue(Key.CURRENT_TIME_LINE_NUMBER);
		if (BuStringUtils.isEmpety(va) || !va.equals(phone))
			return false;
		return true;
	}

	public static boolean isTimeLinePage() {
		String va = getParamValue(Key.CURRENT_PAGE_IS_TIME_LINE);
		if (BuStringUtils.isEmpety(va) || SystemParamHolder.Value.FALSE.equals(va))
			return false;
		return true;
	}

	public static void setHistoryPage(boolean value) {
		saveOrUpdate(Key.CURRENT_PAGE_IS_HISTORY, value + "");
	}

	public static boolean isHistoryPage() {
		String va = getParamValue(Key.CURRENT_PAGE_IS_HISTORY);
		if (BuStringUtils.isEmpety(va) || SystemParamHolder.Value.FALSE.equals(va))
			return false;
		return true;
	}

	public static boolean isLogout() {
		String va = getParamValue(Key.LOGOUT_CURRENT_USER);
		if (BuStringUtils.isEmpety(va) || SystemParamHolder.Value.FALSE.equals(va))
			return false;
		return true;
	}

	public static String getLastLHID() {
		return getParamValue(Key.LAST_USER_LH_ID);
	}

	public static boolean isFistrRunOver() {
		String va = getParamValue(Key.FIRST_RUN_OVER);
		if (BuStringUtils.isEmpety(va) || SystemParamHolder.Value.FALSE.equals(va))
			return false;
		return true;
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

	public static String getCanceledVersion() {
		return getParamValue(Key.CANCELED_VERSION);
	}

	public static void saveCanceledVersion(String version) {
		saveOrUpdate(Key.CANCELED_VERSION, version);
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
