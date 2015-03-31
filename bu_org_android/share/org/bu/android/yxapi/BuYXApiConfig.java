package org.bu.android.yxapi;

import org.bu.android.share.BuShareAppDefine;
import org.bu.android.share.PlatformData;
import org.bu.android.share.PlatformDataHolder;

public class BuYXApiConfig {

	private BuYXApiConfig() {
		super();
	}

	private static BuYXApiConfig instance = null;

	public static BuYXApiConfig getInstance() {

		if (null == instance) {
			instance = new BuYXApiConfig();
		}

		return instance;
	}

	public final String getAppId() {

		PlatformData data = PlatformDataHolder.getDataHolder().getPlatformData(BuShareAppDefine.WECHAT.shareSDK);
		if (null == data) {
			data = new PlatformData();
		}
		return data.getAppId();
	}

	public final String getAppKey() {

		PlatformData data = PlatformDataHolder.getDataHolder().getPlatformData(BuShareAppDefine.WECHAT.shareSDK);
		if (null == data) {
			data = new PlatformData();
		}
		return data.getAppKey();
	}

}
