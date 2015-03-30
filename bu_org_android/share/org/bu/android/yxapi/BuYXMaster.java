package org.bu.android.yxapi;

import im.yixin.sdk.api.IYXAPI;
import im.yixin.sdk.api.SendMessageToYX;
import im.yixin.sdk.api.YXAPIFactory;
import im.yixin.sdk.api.YXImageMessageData;
import im.yixin.sdk.api.YXMessage;
import im.yixin.sdk.api.YXTextMessageData;
import im.yixin.sdk.api.YXWebPageMessageData;

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


/**
 * 发送消息到微信服务类
 * 
 * @author jxs
 * @time 2014-2-11 上午8:59:24
 */
public interface BuYXMaster {

	public static final int THUMB_SIZE = 150;

	class BuYXViewHolder {

	}

	public enum BuYXTransaction {

		TEXT("text"), WEB_PAGE("webpage"), IMG("img");

		private String type;

		private BuYXTransaction(String type) {
			this.type = type;
		}

		public String transaction() {
			return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
		}
	}

	public class BuYXLogic extends BuUILogic<Activity, BuYXViewHolder> {
		private IYXAPI api;

		public BuYXLogic(Activity t) {
			super(t, new BuYXViewHolder());
			// api = YXAPIFactory.createYXAPI(mActivity, ApiConfig.APP_ID);
			api = YXAPIFactory.createYXAPI(mActivity, BuYXApiConfig.APP_ID);
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
			// 初始化一个YXTextMessageData对象

			YXTextMessageData textObj = new YXTextMessageData();
			textObj.text = text;

			// 用YXTextMessageData对象初始化一个YXMessage对象
			YXMessage msg = new YXMessage();
			msg.messageData = textObj;
			// 发送文本类型的消息时，title字段不起作用
			// msg.title = "Will be ignored";
			msg.description = text;

			// 构造一个Req
			SendMessageToYX.Req req = new SendMessageToYX.Req();
			req.transaction = BuYXTransaction.TEXT.transaction(); // transaction字段用于唯一标识一个请求
			req.message = msg;
			req.scene = isTimelineCb ? SendMessageToYX.Req.YXSceneTimeline : SendMessageToYX.Req.YXSceneSession;
			// 调用api接口发送数据到微信
			BuLog.e("sendTextMsg", "..." + req.toString());
			boolean rst = api.sendRequest(req);

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
			YXWebPageMessageData webpage = new YXWebPageMessageData();
			webpage.webPageUrl = webpageUrl;

			YXMessage msg = new YXMessage();
			msg.messageData = webpage;
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

			SendMessageToYX.Req req = new SendMessageToYX.Req();
			req.transaction = BuYXTransaction.WEB_PAGE.transaction();
			req.message = msg;
			req.scene = isTimelineCb ? SendMessageToYX.Req.YXSceneTimeline : SendMessageToYX.Req.YXSceneSession;

			boolean rst = api.sendRequest(req);
			BuLog.e(TAG, rst + "...");
		}

		/**
		 * @param bmp
		 * @param imagePath
		 *            本地图片路径
		 * @param isTimelineCb
		 */
		public void sendImg(Bitmap bmp, String imagePath, String imageUrl, boolean isTimelineCb) {
			YXImageMessageData imgObj = new YXImageMessageData(bmp);
			imgObj.imagePath = imagePath;
			imgObj.imageUrl = imageUrl;

			YXMessage msg = new YXMessage();
			msg.messageData = imgObj;

			Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
			bmp.recycle();
			msg.thumbData = ShareUtils.bmpToByteArray(thumbBmp, true); // 设置缩略图

			SendMessageToYX.Req req = new SendMessageToYX.Req();
			req.transaction = BuYXTransaction.IMG.transaction();
			req.message = msg;
			req.scene = isTimelineCb ? SendMessageToYX.Req.YXSceneTimeline : SendMessageToYX.Req.YXSceneSession;
			boolean rst = api.sendRequest(req);
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
