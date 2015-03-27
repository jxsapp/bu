package org.bu.android.wxapi;

import java.io.File;
import java.net.URL;

import org.bu.android.app.BuUILogic;
import org.bu.android.log.BuLog;
import org.bu.android.misc.BuStringUtils;
import org.bu.android.share.misc.ShareImageUtils;
import org.bu.android.share.misc.ShareUtils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 发送消息到微信服务类
 * 
 * @author jxs
 * @time 2014-2-11 上午8:59:24
 */
public interface WeiXinMaster {

	public static final int THUMB_SIZE = 150;

	class WeixinViewHolder {

	}

	public enum WeiXinTransaction {

		TEXT("text"), WEB_PAGE("webpage"), IMG("img");

		private String type;

		private WeiXinTransaction(String type) {
			this.type = type;
		}

		public String transaction() {
			return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
		}
	}

	public class WeixinLogic extends BuUILogic<Activity, WeixinViewHolder> {
		private IWXAPI api;

		public WeixinLogic(Activity t) {
			super(t, new WeixinViewHolder());
			// api = WXAPIFactory.createWXAPI(mActivity, ApiConfig.APP_ID);
			api = WXAPIFactory.createWXAPI(mActivity, WXApiConfig.APP_ID, false);
		}

		/**
		 * @param text
		 *            文本信息
		 * @param isTimelineCb
		 *            是否发送到朋友圈
		 */
		public void sendTextMsg(String text, boolean isTimelineCb) {
			if (text == null || text.length() == 0) {
				return;
			}
			// 初始化一个WXTextObject对象
			WXTextObject textObj = new WXTextObject();
			textObj.text = text;

			// 用WXTextObject对象初始化一个WXMediaMessage对象
			WXMediaMessage msg = new WXMediaMessage();
			msg.mediaObject = textObj;
			// 发送文本类型的消息时，title字段不起作用
			// msg.title = "Will be ignored";
			msg.description = text;

			// 构造一个Req
			SendMessageToWX.Req req = new SendMessageToWX.Req();
			req.transaction = WeiXinTransaction.TEXT.transaction(); // transaction字段用于唯一标识一个请求
			req.message = msg;
			req.scene = isTimelineCb ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
			// 调用api接口发送数据到微信
			BuLog.e("sendTextMsg", "..." + req.toString());
			boolean rst = api.sendReq(req);

			BuLog.e("sendTextMsg", rst + "...");
		}

		/**
		 * 发送Web页面
		 * 
		 * @param webpageUrl
		 * @param title
		 * @param description
		 * @param thumb
		 * @param isTimelineCb
		 */
		public void sendWebPage(String webpageUrl, String title, String description, String imagePath, boolean isTimelineCb) {
			WXWebpageObject webpage = new WXWebpageObject();
			webpage.webpageUrl = webpageUrl;

			WXMediaMessage msg = new WXMediaMessage();
			msg.mediaObject = webpage;
			msg.title = title;
			msg.description = description;
			if (!BuStringUtils.isEmpety(imagePath)) {
				Bitmap thumb = ShareUtils.decodeFile(new File(imagePath));
				thumb = ShareImageUtils.compressBySize(thumb, 80, 80);
				thumb = ShareImageUtils.compressByQuality(thumb, 32);
				thumb = ShareImageUtils.compressBySize(thumb, 32);
				msg.thumbData = ShareImageUtils.Bitmap2Bytes(thumb, 60);
				BuLog.e(TAG, "TAG::" + msg.thumbData.length);
			}
			BuLog.e(TAG, "参数合法:MSG__" + checkArgs(msg));

			SendMessageToWX.Req req = new SendMessageToWX.Req();
			req.transaction = WeiXinTransaction.WEB_PAGE.transaction();
			req.message = msg;
			req.scene = isTimelineCb ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;

			BuLog.e(TAG, "参数合法:Req__" + req.checkArgs());

			boolean rst = api.sendReq(req);
			BuLog.e(TAG, rst + "...");
		}

		private boolean checkArgs(WXMediaMessage msg) {

			if ((msg.getType() == 8) && ((msg.thumbData == null) || (msg.thumbData.length == 0))) {
				BuLog.e(TAG, "checkArgs fail, thumbData should not be null when send emoji");
				return false;
			}
			if ((msg.thumbData != null) && (msg.thumbData.length > 32768)) {
				BuLog.e(TAG, "checkArgs fail, thumbData is invalid");
				return false;
			}
			if ((msg.title != null) && (msg.title.length() > 512)) {
				BuLog.e(TAG, "checkArgs fail, title is invalid");
				return false;
			}
			if ((msg.description != null) && (msg.description.length() > 1024)) {
				BuLog.e(TAG, "checkArgs fail, description is invalid");
				return false;
			}
			if (msg.mediaObject == null) {
				BuLog.e(TAG, "checkArgs fail, mediaObject is null");
				return false;
			}
			if ((msg.mediaTagName != null) && (msg.mediaTagName.length() > 64)) {
				BuLog.e(TAG, "checkArgs fail, mediaTagName is too long");
				return false;
			}
			if ((msg.messageAction != null) && (msg.messageAction.length() > 2048)) {
				BuLog.e(TAG, "checkArgs fail, messageAction is too long");
				return false;
			}
			if ((msg.messageExt != null) && (msg.messageExt.length() > 2048)) {
				BuLog.e(TAG, "checkArgs fail, messageExt is too long");
				return false;
			}
			return true;
		}

		/**
		 * @param bmp
		 * @param imagePath
		 *            本地图片路径
		 * @param isTimelineCb
		 */
		public void sendImg(Bitmap bmp, String imagePath, String imageUrl, boolean isTimelineCb) {
			WXImageObject imgObj = new WXImageObject(bmp);
			imgObj.imagePath = imagePath;
			imgObj.imageUrl = imageUrl;

			WXMediaMessage msg = new WXMediaMessage();
			msg.mediaObject = imgObj;

			Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
			bmp.recycle();
			msg.thumbData = ShareUtils.bmpToByteArray(thumbBmp, true); // 设置缩略图

			SendMessageToWX.Req req = new SendMessageToWX.Req();
			req.transaction = WeiXinTransaction.IMG.transaction();
			req.message = msg;
			req.scene = isTimelineCb ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
			boolean rst = api.sendReq(req);
			BuLog.e("Send IMG", rst + "...");
		}

		/**
		 * 发送图片
		 * 
		 * @param bmp
		 * @param isTimelineCb
		 */
		public void sendImg(Bitmap bmp, boolean isTimelineCb) {
			sendImg(bmp, null, null, isTimelineCb);
		}

		/**
		 * @param imagePath
		 *            本地图片路径
		 * @param isTimelineCb
		 */
		public void sendImg4Local(String imagePath, boolean isTimelineCb) {
			Bitmap bmp = BitmapFactory.decodeFile(imagePath);
			sendImg(bmp, imagePath, null, isTimelineCb);
		}

		public void sendImg4Web(String imageUrl, boolean isTimelineCb) {
			try {
				Bitmap bmp = BitmapFactory.decodeStream(new URL(imageUrl).openStream());
				sendImg(bmp, null, imageUrl, isTimelineCb);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
