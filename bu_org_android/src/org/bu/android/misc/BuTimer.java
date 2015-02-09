package org.bu.android.misc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.SparseArray;

@SuppressLint("SimpleDateFormat")
public class BuTimer {
	public static String getTime(Context context, long all) {
		String results = "";
		long hours = all / 3600;
		all = all % 3600;
		long minuties = all / 60;
		all = all % 60;
		long seconds = all;

		if (0 != hours && minuties != 0) {
			results = hours + "时" + minuties + "分" + seconds + "秒";
		} else if (0 == hours && minuties != 0) {
			results = minuties + "分" + seconds + "秒";
		} else if (0 == hours && minuties == 0) {
			results = seconds + "秒";
		}
		return results;
	}

	/**
	 * 当天的0点
	 * 
	 * @return
	 */
	public static long getTimesmorning() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTimeInMillis();
	}

	public static long paser(SimpleDateFormat sdf, String date) {
		try {
			return sdf.parse(date).getTime();
		} catch (ParseException e) {
			return -1;
		}
	}

	public static SimpleDateFormat getSDFyyyyMMdd() {
		return new SimpleDateFormat("yyyyMMdd");

	}

	public static SimpleDateFormat getSDFyyMMdd() {
		return new SimpleDateFormat("yyMMdd");

	}

	public static SimpleDateFormat getSDFyy__MM__dd() {
		return new SimpleDateFormat("yy/MM/dd");

	}

	public static SimpleDateFormat getSDFyyyy_MM_dd() {
		return new SimpleDateFormat("yyyy-MM-dd");

	}

	public static SimpleDateFormat getSDFyy_MM_dd() {
		return new SimpleDateFormat("yy-MM-dd");

	}

	public static SimpleDateFormat getSDFyy_MM_ddHHmmss() {
		return new SimpleDateFormat("yy-MM-dd HH:mm:ss.S");
	}

	public static SimpleDateFormat getSDFyyyy_MM_dd_CN() {
		return new SimpleDateFormat("yyyy年MM月dd日");
	}

	public static SimpleDateFormat getSDFHH_mm_CN() {
		return new SimpleDateFormat("HH:mm");
	}

	public static SimpleDateFormat getSDFMM_ddHHmm() {
		return new SimpleDateFormat("MM-dd HH:mm");
	}

	public static SimpleDateFormat getSDFyyyyMMddHHmmssS() {
		return new SimpleDateFormat("yyyyMMddHHmmssS");
	}

	public static SimpleDateFormat getSDFyyyyMMddHHmmss() {
		return new SimpleDateFormat("yyyyMMddHHmmss");
	}

	// 2014-11-04T09:54:17
	public static SimpleDateFormat getSDFyyyy_MM_ddTHHmmss() {
		return new SimpleDateFormat("yyyy-MM-ddTHH:mm:ss");
	}

	public static SimpleDateFormat getSDFyyyy_MM_ddHHmmss() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}

	public static SimpleDateFormat getSDFyyMMddHHmmss() {
		return new SimpleDateFormat("yyMMddHHmmss");
	}

	public static SimpleDateFormat getSDFMMddHHmmss() {
		return new SimpleDateFormat("MMddHHmmss");
	}

	public static SimpleDateFormat getSDFHHmm() {
		return new SimpleDateFormat("HH:mm");
	}

	public static SimpleDateFormat getSDF_YUE_RI() {
		return new SimpleDateFormat("MM月dd日");
	}

	public static SimpleDateFormat getSDF_YUE() {
		return new SimpleDateFormat("MM");
	}

	public static SimpleDateFormat getSDF_RI() {
		return new SimpleDateFormat("dd");
	}

	public static SimpleDateFormat getSDFHHmm4EventMain() {
		return new SimpleDateFormat("HH mm");
	}

	public static SimpleDateFormat getSDFyy_MM_ddHHmm() {
		return new SimpleDateFormat("yy-MM-dd HH:mm");
	}

	public static SimpleDateFormat getSDFyyyy_MM_ddHHmm() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm");
	}

	public static String getWeekDayString(long date) {

		String weekString = "";
		final String dayNames[] = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

		weekString = dayNames[dayOfWeek - 1];

		return weekString;

	}

	static SparseArray<String> daysFormats = new SparseArray<String>();
	static long A_DAY = 24 * 60 * 60 * 1000;
	static {
		daysFormats.put(-2, "前天");
		daysFormats.put(-1, "昨天");
		daysFormats.put(0, "今天");
		daysFormats.put(1, "明天");
		daysFormats.put(2, "后天");
	}

	public static String getMatterTag(long startTime) {

		String time = BuTimer.getSDFyyyy_MM_dd().format(startTime) + " " + BuTimer.getWeekDayString(startTime);
		Calendar today = Calendar.getInstance();
		Calendar old = Calendar.getInstance();

		old.setTimeInMillis(startTime);
		// 此处好像是去除0
		today.set(Calendar.HOUR, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		old.set(Calendar.HOUR, 0);
		old.set(Calendar.MINUTE, 0);
		old.set(Calendar.SECOND, 0);
		// 老的时间减去今天的时间
		long intervalMilli = old.getTimeInMillis() - today.getTimeInMillis();
		int xcts = (int) (intervalMilli / A_DAY);
		if (xcts >= -2 && xcts <= 2) {
			time = daysFormats.get(xcts);
		}
		return time;
	}

	public static final long SECOND_IN_MILLIS = 1000;
	public static final long MINUTE_IN_MILLIS = SECOND_IN_MILLIS * 60;
	public static final long HOUR_IN_MILLIS = MINUTE_IN_MILLIS * 60;
	public static final long DAY_IN_MILLIS = HOUR_IN_MILLIS * 24;
	public static final long WEEK_IN_MILLIS = DAY_IN_MILLIS * 7;

	public static String getTimeStamp2(long time) {
		return getSDFyyyyMMddHHmmss().format(time);
	}

	public static String getTimeStamp(long time) {
		return getSDFyyyyMMddHHmmssS().format(time);
	}

	public static String getTimestamp(String str) {
		if (str.length() == 14) {
			return getSDFyyyyMMddHHmmss().format(getTimeStamp(str));
		} else {
			return getSDFyyyyMMddHHmmssS().format(getTimeStamp(str));
		}
	}

	public static long getTimeStamp(String str) {
		long time = System.currentTimeMillis();
		try {
			// 2014 01 09 10 24 30
			if (str.length() == 14) {
				time = getSDFyyyyMMddHHmmss().parse(str).getTime();
			} else if (str.length() > 14) {
				time = getSDFyyyyMMddHHmmssS().parse(str).getTime();
			}
		} catch (Exception e) {
			time = System.currentTimeMillis();
		}
		return time;
	}

	public static long dateParse(String str) {
		long time = System.currentTimeMillis();
		try {
			str = str.replace("T", " ");
			time = getSDFyyyy_MM_ddHHmmss().parse(str).getTime();
		} catch (Exception e) {
			time = System.currentTimeMillis();
		}
		return time;
	}

}