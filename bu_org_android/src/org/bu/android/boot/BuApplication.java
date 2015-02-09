package org.bu.android.boot;

import java.util.ArrayList;
import java.util.List;

import org.bu.android.R;
import org.bu.android.db.SystemParamCommonHolder;
import org.bu.android.image.ImageLoaderHolder;
import org.bu.android.log.BuLog;
import org.bu.android.misc.BuFileHolder;
import org.bu.android.misc.BuStringUtils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;

public abstract class BuApplication extends Application {
	static String TAG = BuApplication.class.getSimpleName();

	private static BuApplication application;

	// 添加Activity到容器中
	private List<Activity> activitys = new ArrayList<Activity>();

	public void removeActivity(Activity activity) {
		activitys.remove(activity);

		if (activitys != null && activitys.size() > 0) {
			for (Activity a : activitys) {
				BuLog.i(TAG, "The Activted Activity：" + a.getClass().getName());
			}
		}
	}

	public String getFileRoot() {
		return "bu_org";
	}

	public void addActivity(Activity activity) {
		if (activitys != null && activitys.size() > 0) {
			if (!activitys.contains(activity)) {
				activitys.add(activity);
			}
		} else {
			BuLog.i(TAG, "The Activity：" + activity.getClass().getName() + " Be Created");
			activitys.add(activity);
		}

	}

	// 遍历所有Activity并finish
	public void exit() {
		if (activitys != null && activitys.size() > 0) {
			for (Activity activity : activitys) {
				BuLog.i(TAG, "Be Killed Activity：" + activity.getClass().getName());
				activity.finish();
			}
		}
		System.exit(0);
	}

	// 遍历所有Activity并finish
	public void finsh() {
		if (activitys != null && activitys.size() > 0) {
			for (Activity activity : activitys) {
				BuLog.i(TAG, "Be Killed Activity：" + activity.getClass().getName());
				activity.finish();
			}
		}
	}

	private Handler mHandler;

	public Handler getHandler() {
		return mHandler;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		application = this;
		ImageLoaderHolder.init(application, R.drawable.default_yms_image);
		CrashHolder.getInstance().init(application);
		BuFileHolder.createDir();
		mHandler = new Handler(application.getMainLooper(), new Handler.Callback() {

			@Override
			public boolean handleMessage(Message msg) {

				return false;
			}
		});
		if (SystemParamCommonHolder.needInitData()) {
			Thread t = new Thread(new Runnable() {

				@Override
				public void run() {

					SystemParamCommonHolder.doInit();
				}
			});
			t.setDaemon(true);
			t.start();
		}

	}

	public static void setApplication(BuApplication application) {
		BuApplication.application = application;
	}

	public static BuApplication getApplication() {
		return application;
	}

	public String getDeviceId() {
		String device_id = "";
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		if (BuStringUtils.isEmpety(device_id)) {
			device_id = tm.getDeviceId();
		}
		return device_id;
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

}
