package org.bu.android.wxapi;

import org.bu.android.share.BuShareAppDefine;
import org.bu.android.share.PlatformData;
import org.bu.android.share.PlatformDataHolder;

public class BuWXApiConfig {

	private BuWXApiConfig() {
		super();
	}

	private static BuWXApiConfig instance = null;

	public static BuWXApiConfig getInstance() {

		if (null == instance) {
			instance = new BuWXApiConfig();
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
