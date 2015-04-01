package org.bu.android.share;

import java.util.ArrayList;
import java.util.List;

public class BuShareAppInfo {
	/**
	 * APP ID
	 */
	private BuShareAppDefine targetAppDefine = BuShareAppDefine.NULL;

	public BuShareAppInfo() {
		super();
	}

	public BuShareAppInfo(BuShareAppDefine targetAppDefine) {
		super();
		this.targetAppDefine = targetAppDefine;
	}

	public static List<BuShareAppInfo> getShareList() {
		List<BuShareAppInfo> targetAppInfos = new ArrayList<BuShareAppInfo>();
		targetAppInfos.add(new BuShareAppInfo(BuShareAppDefine.WECHAT));
		targetAppInfos.add(new BuShareAppInfo(BuShareAppDefine.WECHAT_CMTS));
		targetAppInfos.add(new BuShareAppInfo(BuShareAppDefine.WEIBO));
		targetAppInfos.add(new BuShareAppInfo(BuShareAppDefine.QQ));
		targetAppInfos.add(new BuShareAppInfo(BuShareAppDefine.QZONE));
//		targetAppInfos.add(new BuShareAppInfo(BuShareAppDefine.TENCENT_WEIBO));
		targetAppInfos.add(new BuShareAppInfo(BuShareAppDefine.YIXIN));
		targetAppInfos.add(new BuShareAppInfo(BuShareAppDefine.YIXIN_CMTS));
		return targetAppInfos;
	}

	public BuShareAppDefine getTargetAppDefine() {
		return targetAppDefine;
	}

	public void setTargetAppDefine(BuShareAppDefine targetAppDefine) {
		this.targetAppDefine = targetAppDefine;
	}

}
