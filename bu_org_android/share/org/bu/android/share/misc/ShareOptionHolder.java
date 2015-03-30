package org.bu.android.share.misc;

import static cn.sharesdk.framework.utils.BitmapHelper.captureView;
import static cn.sharesdk.framework.utils.R.getStringRes;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bu.android.acty.BuActivity;
import org.bu.android.share.BuShareAppInfo;
import org.bu.android.share.BuShareCallback;
import org.bu.android.share.BuShareCore;
import org.bu.android.share.PlatformData;
import org.bu.android.share.PlatformDataHolder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import cn.sharesdk.framework.FakeActivity;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.utils.UIHandler;

public class ShareOptionHolder extends FakeActivity implements Handler.Callback {
	private static final int MSG_TOAST = 1;
	private static final int MSG_ACTION_CCALLBACK = 2;
	private static final int MSG_CANCEL_NOTIFY = 3;
	private HashMap<String, Object> reqMap;
	private int notifyIcon;
	private String notifyTitle;
	private PlatformActionListener callback;
	private BuShareCallback customizeCallback;
	private boolean dialogMode;
	private boolean disableSSO;
	private HashMap<String, String> hiddenPlatforms;
	private BuActivity mActivity;

	public Platform initShareSDK(BuShareAppInfo targetAppInfo) {
		ShareSDK.initSDK(mActivity, PlatformDataHolder.getDataHolder().getPlatformData("ShareSDK").getAppKey(), true);
		PlatformData platformData = PlatformDataHolder.getDataHolder().getPlatformData(targetAppInfo.getTargetAppDefine().shareSDK);
		Platform platform = ShareSDK.getPlatform(platformData.getShareKey());
		ShareSDK.setPlatformDevInfo(platformData.getShareKey(), platformData.getReqData());
		return platform;
	}

	public ShareOptionHolder(BuActivity activity, PlatformActionListener callback) {
		this.mActivity = new WeakReference<BuActivity>(activity).get();
		reqMap = new HashMap<String, Object>();
		this.callback = callback;
		hiddenPlatforms = new HashMap<String, String>();
	}

	public Context getContext() {
		return mActivity;
	}

	/** 分享时Notification的图标和文字 */
	public void setNotification(int icon, String title) {
		notifyIcon = icon;
		notifyTitle = title;
	}

	/** address是接收人地址，仅在信息和邮件使用，否则可以不提供 */
	public void setAddress(String address) {
		reqMap.put("address", address);
	}

	/**
	 * title标题，在印象笔记、邮箱、信息、微信（包括好友、朋友圈和收藏）、 易信（包括好友、朋友圈）、人人网和QQ空间使用，否则可以不提供
	 */
	public void setTitle(String title) {
		reqMap.put("title", title);
	}

	/** titleUrl是标题的网络链接，仅在人人网和QQ空间使用，否则可以不提供 */
	public void setTitleUrl(String titleUrl) {
		reqMap.put("titleUrl", titleUrl);
	}

	/** text是分享文本，所有平台都需要这个字段 */
	public void setText(String text) {
		reqMap.put("text", text);
	}

	/** 获取text字段的值 */
	public String getText() {
		return reqMap.containsKey("text") ? String.valueOf(reqMap.get("text")) : null;
	}

	/** imagePath是本地的图片路径，除Linked-In外的所有平台都支持这个字段 */
	public void setImagePath(String imagePath) {
		if (!TextUtils.isEmpty(imagePath))
			reqMap.put("imagePath", imagePath);
	}

	/** imageUrl是图片的网络路径，新浪微博、人人网、QQ空间和Linked-In支持此字段 */
	public void setImageUrl(String imageUrl) {
		if (!TextUtils.isEmpty(imageUrl))
			reqMap.put("imageUrl", imageUrl);
	}

	/** url在微信（包括好友、朋友圈收藏）和易信（包括好友和朋友圈）中使用，否则可以不提供 */
	public void setUrl(String url) {
		reqMap.put("url", url);
	}

	/** filePath是待分享应用程序的本地路劲，仅在微信（易信）好友和Dropbox中使用，否则可以不提供 */
	public void setFilePath(String filePath) {
		reqMap.put("filePath", filePath);
	}

	/** comment是我对这条分享的评论，仅在人人网和QQ空间使用，否则可以不提供 */
	public void setComment(String comment) {
		reqMap.put("comment", comment);
	}

	/** site是分享此内容的网站名称，仅在QQ空间使用，否则可以不提供 */
	public void setSite(String site) {
		reqMap.put("site", site);
	}

	/** siteUrl是分享此内容的网站地址，仅在QQ空间使用，否则可以不提供 */
	public void setSiteUrl(String siteUrl) {
		reqMap.put("siteUrl", siteUrl);
	}

	/** foursquare分享时的地方名 */
	public void setVenueName(String venueName) {
		reqMap.put("venueName", venueName);
	}

	/** foursquare分享时的地方描述 */
	public void setVenueDescription(String venueDescription) {
		reqMap.put("venueDescription", venueDescription);
	}

	/** 分享地纬度，新浪微博、腾讯微博和foursquare支持此字段 */
	public void setLatitude(float latitude) {
		reqMap.put("latitude", latitude);
	}

	/** 分享地经度，新浪微博、腾讯微博和foursquare支持此字段 */
	public void setLongitude(float longitude) {
		reqMap.put("longitude", longitude);
	}

	/** 设置编辑页的初始化选中平台 */
	public void setPlatform(String platform) {
		reqMap.put("platform", platform);
	}

	/** 设置自定义的外部回调 */
	public void setCallback(PlatformActionListener callback) {
		this.callback = callback;
	}

	/** 设置用于分享过程中，根据不同平台自定义分享内容的回调 */
	public void setShareContentCustomizeCallback(BuShareCallback callback) {
		customizeCallback = callback;
	}

	/** 设置一个总开关，用于在分享前若需要授权，则禁用sso功能 */
	public void disableSSOWhenAuthorize() {
		disableSSO = true;
	}

	/** 设置编辑页面的显示模式为Dialog模式 */
	public void setDialogMode() {
		dialogMode = true;
		reqMap.put("dialogMode", dialogMode);
	}

	/** 添加一个隐藏的platform */
	public void addHiddenPlatform(String platform) {
		hiddenPlatforms.put(platform, platform);
	}

	/** 设置一个将被截图分享的View */
	public void setViewToShare(View viewToShare) {
		try {
			Bitmap bm = captureView(viewToShare, viewToShare.getWidth(), viewToShare.getHeight());
			reqMap.put("viewToShare", bm);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/** 循环执行分享 */
	public void share(BuShareAppInfo targetAppInfo) {
		share(targetAppInfo, reqMap);
	}

	public void share(BuShareAppInfo targetAppInfo, HashMap<String, Object> reqMap) {

		Platform platform = initShareSDK(targetAppInfo);
		HashMap<Platform, HashMap<String, Object>> shareData = new HashMap<Platform, HashMap<String, Object>>();
		shareData.put(platform, reqMap);

		boolean started = false;
		for (Entry<Platform, HashMap<String, Object>> ent : shareData.entrySet()) {
			Platform plat = ent.getKey();
			plat.SSOSetting(disableSSO);
			String name = plat.getName();
			boolean isWechat = "WechatMoments".equals(name) || "Wechat".equals(name) || "WechatFavorite".equals(name);
			if (isWechat && !plat.isValid()) {
				Message msg = new Message();
				msg.what = MSG_TOAST;
				int resId = getStringRes(getContext(), "wechat_client_inavailable");
				msg.obj = mActivity.getString(resId);
				UIHandler.sendMessage(msg, this);
				continue;
			}

			boolean isGooglePlus = "GooglePlus".equals(name);
			if (isGooglePlus && !plat.isValid()) {
				Message msg = new Message();
				msg.what = MSG_TOAST;
				int resId = getStringRes(getContext(), "google_plus_client_inavailable");
				msg.obj = mActivity.getString(resId);
				UIHandler.sendMessage(msg, this);
				continue;
			}

			boolean isPinterest = "Pinterest".equals(name);
			if (isPinterest && !plat.isValid()) {
				Message msg = new Message();
				msg.what = MSG_TOAST;
				int resId = getStringRes(getContext(), "pinterest_client_inavailable");
				msg.obj = mActivity.getString(resId);
				UIHandler.sendMessage(msg, this);
				continue;
			}

			if ("Instagram".equals(name)) {
				Intent test = new Intent(Intent.ACTION_SEND);
				test.setPackage("com.instagram.android");
				test.setType("image/*");
				ResolveInfo ri = mActivity.getPackageManager().resolveActivity(test, 0);
				if (ri == null) {
					Message msg = new Message();
					msg.what = MSG_TOAST;
					int resId = getStringRes(getContext(), "instagram_client_inavailable");
					msg.obj = mActivity.getString(resId);
					UIHandler.sendMessage(msg, this);
					continue;
				}
			}

			boolean isYixin = "YixinMoments".equals(name) || "Yixin".equals(name);
			if (isYixin && !plat.isValid()) {
				Message msg = new Message();
				msg.what = MSG_TOAST;
				int resId = getStringRes(getContext(), "yixin_client_inavailable");
				msg.obj = mActivity.getString(resId);
				UIHandler.sendMessage(msg, this);
				continue;
			}

			HashMap<String, Object> data = ent.getValue();
			int shareType = Platform.SHARE_TEXT;
			String imagePath = String.valueOf(data.get("imagePath"));
			if (imagePath != null && (new File(imagePath)).exists()) {
				shareType = Platform.SHARE_IMAGE;
				if (imagePath.endsWith(".gif")) {
					shareType = Platform.SHARE_EMOJI;
				} else if (data.containsKey("url") && !TextUtils.isEmpty(data.get("url").toString())) {
					shareType = Platform.SHARE_WEBPAGE;
				}
			} else {
				Bitmap viewToShare = (Bitmap) data.get("viewToShare");
				if (viewToShare != null && !viewToShare.isRecycled()) {
					shareType = Platform.SHARE_IMAGE;
					if (data.containsKey("url")) {
						Object url = data.get("url");
						if (url != null && !TextUtils.isEmpty(url.toString())) {
							shareType = Platform.SHARE_WEBPAGE;
						}
					}
				} else {
					Object imageUrl = data.get("imageUrl");
					if (imageUrl != null && !TextUtils.isEmpty(String.valueOf(imageUrl))) {
						shareType = Platform.SHARE_IMAGE;
						if (String.valueOf(imageUrl).endsWith(".gif")) {
							shareType = Platform.SHARE_EMOJI;
						} else if (data.containsKey("url")) {
							Object url = data.get("url");
							if (url != null && !TextUtils.isEmpty(url.toString())) {
								shareType = Platform.SHARE_WEBPAGE;
							}
						}
					}
				}
			}
			data.put("shareType", shareType);

			if (!started) {
				started = true;
				if (equals(callback)) {
					int resId = getStringRes(getContext(), "sharing");
					if (resId > 0) {
						showNotification(2000, getContext().getString(resId));
					}
				}
			}
			plat.setPlatformActionListener(callback);
			BuShareCore shareCore = new BuShareCore();
			shareCore.setShareContentCustomizeCallback(customizeCallback);
			shareCore.share(plat, data);
		}
	}

	public void onComplete(Platform platform, int action, HashMap<String, Object> res) {
		Message msg = new Message();
		msg.what = MSG_ACTION_CCALLBACK;
		msg.arg1 = 1;
		msg.arg2 = action;
		msg.obj = platform;
		UIHandler.sendMessage(msg, this);
	}

	public void onError(Platform platform, int action, Throwable t) {
		t.printStackTrace();

		Message msg = new Message();
		msg.what = MSG_ACTION_CCALLBACK;
		msg.arg1 = 2;
		msg.arg2 = action;
		msg.obj = t;
		UIHandler.sendMessage(msg, this);

		// 分享失败的统计
		ShareSDK.logDemoEvent(4, platform);
	}

	public void onCancel(Platform platform, int action) {
		Message msg = new Message();
		msg.what = MSG_ACTION_CCALLBACK;
		msg.arg1 = 3;
		msg.arg2 = action;
		msg.obj = platform;
		UIHandler.sendMessage(msg, this);
	}

	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case MSG_TOAST: {
			String text = String.valueOf(msg.obj);
			Toast.makeText(mActivity, text, Toast.LENGTH_SHORT).show();
		}
			break;
		case MSG_ACTION_CCALLBACK: {
			switch (msg.arg1) {
			case 1: {
				// 成功
				int resId = getStringRes(getContext(), "share_completed");
				if (resId > 0) {
					showNotification(2000, getContext().getString(resId));
				}
			}
				break;
			case 2: {
				// 失败
				String expName = msg.obj.getClass().getSimpleName();
				if ("WechatClientNotExistException".equals(expName) || "WechatTimelineNotSupportedException".equals(expName) || "WechatFavoriteNotSupportedException".equals(expName)) {
					int resId = getStringRes(getContext(), "wechat_client_inavailable");
					if (resId > 0) {
						showNotification(2000, getContext().getString(resId));
					}
				} else if ("GooglePlusClientNotExistException".equals(expName)) {
					int resId = getStringRes(getContext(), "google_plus_client_inavailable");
					if (resId > 0) {
						showNotification(2000, getContext().getString(resId));
					}
				} else if ("QQClientNotExistException".equals(expName)) {
					int resId = getStringRes(getContext(), "qq_client_inavailable");
					if (resId > 0) {
						showNotification(2000, getContext().getString(resId));
					}
				} else if ("YixinClientNotExistException".equals(expName) || "YixinTimelineNotSupportedException".equals(expName)) {
					int resId = getStringRes(getContext(), "yixin_client_inavailable");
					if (resId > 0) {
						showNotification(2000, getContext().getString(resId));
					}
				} else {
					int resId = getStringRes(getContext(), "share_failed");
					if (resId > 0) {
						showNotification(2000, getContext().getString(resId));
					}
				}
			}
				break;
			case 3: {
				// 取消
				int resId = getStringRes(getContext(), "share_canceled");
				if (resId > 0) {
					showNotification(2000, getContext().getString(resId));
				}
			}
				break;
			}
		}
			break;
		case MSG_CANCEL_NOTIFY: {
			NotificationManager nm = (NotificationManager) msg.obj;
			if (nm != null) {
				nm.cancel(msg.arg1);
			}
		}
			break;
		}
		return false;
	}

	// 在状态栏提示分享操作
	@SuppressWarnings("deprecation")
	private void showNotification(long cancelTime, String text) {
		try {
			Context app = getContext().getApplicationContext();
			NotificationManager nm = (NotificationManager) app.getSystemService(Context.NOTIFICATION_SERVICE);
			final int id = Integer.MAX_VALUE / 13 + 1;
			nm.cancel(id);

			long when = System.currentTimeMillis();
			Notification notification = new Notification(notifyIcon, text, when);
			PendingIntent pi = PendingIntent.getActivity(app, 0, new Intent(), 0);
			notification.setLatestEventInfo(app, notifyTitle, text, pi);
			notification.flags = Notification.FLAG_AUTO_CANCEL;
			nm.notify(id, notification);

			if (cancelTime > 0) {
				Message msg = new Message();
				msg.what = MSG_CANCEL_NOTIFY;
				msg.obj = nm;
				msg.arg1 = id;
				UIHandler.sendMessageDelayed(msg, cancelTime, this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
