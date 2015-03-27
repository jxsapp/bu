package org.bu.android.share;

import org.bu.android.R;

import cn.sharesdk.tencent.qzone.QZone;

public enum BuShareAppDefine {

	NULL(0, "", R.drawable.translate1x1, "Text"), //
	SMS(1, "短信", R.drawable.ic_share_sms, "ShortMessage"), //
	WECHAT(2, "微信好友", R.drawable.ic_share_wechat, "Wechat"), //
	WECHAT_CMTS(3, "微信朋友圈", R.drawable.ic_share_wechat_cmts, "WechatMoments"), //
	QQ(4, "QQ好友", R.drawable.ic_share_qq, "QQ"), //
	QZONE(5, "QQ空间", R.drawable.ic_share_qzone, QZone.NAME), //
	YIXIN(6, "易信好友", R.drawable.ic_share_yixin, "Yixin"), //
	YIXIN_CMTS(7, "易信朋友圈", R.drawable.ic_share_yixin_cmts, "YixinMoments"), //
	WEIBO(8, "新浪微博", R.drawable.ic_share_weibo, "SinaWeiBo"), //
	TENCENT_WEIBO(9, "腾讯微博", R.drawable.ic_share_tweibo, "YixinMoments"); //

	public int id = -1;
	public int icon = R.drawable.translate1x1;
	public String name = "";
	public String shareSDK = "";

	private BuShareAppDefine(int id, String name, int icon, String tagName) {
		this.id = id;
		this.name = name;
		this.icon = icon;
		this.shareSDK = tagName;
	}

}
