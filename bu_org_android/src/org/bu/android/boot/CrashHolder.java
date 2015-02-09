package org.bu.android.boot;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.bu.android.misc.BuFileHolder;
import org.bu.android.misc.BuTimer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

/**
 * 描述 ：全局的处理未捕获的异常的工具类 <br>
 * 
 * @author jxs
 */
@SuppressLint("SimpleDateFormat")
public class CrashHolder implements UncaughtExceptionHandler {

	public static final String TAG = "CrashHolder";

	private static CrashHolder instance = new CrashHolder();
	private Context mContext;
	/** 系统默认的 UncaughtException 处理类 */
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	// 用来存储设备信息和异常信息
	private Map<String, String> infos = new HashMap<String, String>();

	private CrashHolder() {
	}

	public static CrashHolder getInstance() {
		return instance;
	}

	/**
	 * 
	 * 描述：初始化 <br>
	 * 
	 * @param context
	 * <br>
	 * 
	 */
	public void init(Context context) {
		mContext = context;
		// 获取系统默认的 UncaughtException 处理器
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		// 设置该 CrashHandler 为程序的默认处理器
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (null != ex) {
			Log.e("WeiMi.uncaughtException", "" + ex.getMessage(), ex);
		}
		if (!handleException(ex) && mDefaultHandler != null) {
			// 如果用户没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				Log.e(TAG, "error : ", e);
			}
			// 退出程序,注释下面的重启启动程序代码
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(1);
			// 重新启动程序，注释上面的退出程序
			// Intent intent = new Intent();
			// intent.setClass(mContext,MainActivity.class);
			// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// mContext.startActivity(intent);
			// android.os.Process.killProcess(android.os.Process.myPid());
		}
	}

	/**
	 * 
	 * 描述：自定义错误处理，收集错误信息，发送错误报告等操作均在此完成 <br>
	 * 
	 * @param ex
	 *            The superclass of all classes which can be thrown by the VM <br>
	 * @return 如果处理了该异常信息；否则返回 false<br>
	 * 
	 */
	public boolean handleException(Throwable ex) {
		if (ex == null) {
			return false;
		}
		// // 使用 Toast 来显示异常信息
		// new Thread() {
		// @Override
		// public void run() {
		// Looper.prepare();
		// Toast.makeText(mContext, "很抱歉，程序出现异常，即将退出。",
		// Toast.LENGTH_LONG).show();
		// Looper.loop();
		// }
		// }.start();

		// 收集设备参数信息
		collectDeviceInfo(mContext);
		// 保存日志文件
		saveCrashInfo2File(ex);
		return true;
	}

	/**
	 * 收集设备参数信息
	 * 
	 * @param ctx
	 */
	public void collectDeviceInfo(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);

			if (pi != null) {
				String versionName = pi.versionName == null ? "null" : pi.versionName;
				String versionCode = pi.versionCode + "";
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
			Log.e(TAG, "an error occured when collect package info", e);
		}

		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());
				Log.d(TAG, field.getName() + " : " + field.get(null));
			} catch (Exception e) {
				Log.e(TAG, "an error occured when collect crash info", e);
			}
		}
	}

	/**
	 * 
	 * 描述:保存错误信息到文件中 <br>
	 * 
	 * @param ex
	 * <br>
	 * @return 返回文件名称,便于将文件传送到服务器<br>
	 * 
	 */
	private String saveCrashInfo2File(Throwable ex) {
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : infos.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key + "=" + value + "\n");
		}

		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();

		String result = writer.toString();
		sb.append(result);
		try {
			String time = BuTimer.getSDFyyyyMMddHHmmssS().format(System.currentTimeMillis());
			String fileName = "crash-" + time + ".txt";

			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				File file = new File(BuFileHolder.DIR_DEBUG + fileName);
				FileWriter filerWriter = new FileWriter(file, true);// 后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
				BufferedWriter bufWriter = new BufferedWriter(filerWriter);
				bufWriter.write(sb.toString());
				bufWriter.newLine();
				bufWriter.close();
				filerWriter.close();
			}
			return fileName;
		} catch (Exception e) {
			Log.e(TAG, "an error occured while writing file...", e);
		}

		return null;
	}

}
