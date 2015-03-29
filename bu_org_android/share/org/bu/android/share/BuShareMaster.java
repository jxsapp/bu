package org.bu.android.share;

import java.io.File;
import java.util.HashMap;

import org.bu.android.R;
import org.bu.android.acty.BuActivity;
import org.bu.android.app.BuUILogic;
import org.bu.android.misc.BuFileHolder;
import org.bu.android.share.misc.ShareImageUtils;
import org.bu.android.share.misc.ShareOptionHolder;
import org.bu.android.share.misc.ShareSmsUtils;
import org.bu.android.share.misc.ShareUtils;
import org.bu.android.widget.BuMenu;
import org.bu.android.widget.BuMenuMaster.BuMenuListener;
import org.bu.android.wxapi.WeiXinMaster;
import org.bu.android.yxapi.YiXinMaster;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

public interface BuShareMaster {

	class BuShareViewHoler {
		BuMenu weiMiMenu;
	}

	public abstract class BuShareListener implements PlatformActionListener, BuShareWidget.OnTargetSelectedListener {
		public abstract View getRootView();

		private Context mContext;
		private BuMenu menu;

		public BuShareListener(Context mContext) {
			super();
			this.mContext = mContext;
		}

		@SuppressLint("InflateParams")
		public View getMenus(BuMenu menu) {
			this.menu = menu;
			View view = LayoutInflater.from(mContext).inflate(R.layout.wm_share_widget, null);
			TextView notes = (TextView) view.findViewById(R.id.notes);
			BuShareWidget bu_share_widget = (BuShareWidget) view.findViewById(R.id.bu_share_widget);
			onLayoutInflater(view, notes, bu_share_widget);
			bu_share_widget.setOnTargetSelectedListener(this);
			return view;
		}

		public void onLayoutInflater(View view, TextView notes, BuShareWidget bu_share_widget) {
			notes.setText("分享到以下社交平台");
		}

		@Override
		public void onTargetSelected(BuShareAppInfo targetAppInfo) {
			this.menu.dismiss();
		}

		@Override
		public void onCancel(Platform arg0, int arg1) {

		}

		@Override
		public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {

		}

		@Override
		public void onError(Platform arg0, int arg1, Throwable arg2) {

		}

	}

	public class BuShareLogic extends BuUILogic<BuActivity, BuShareViewHoler> implements WeiXinMaster, YiXinMaster {

		private WeixinLogic weixinLogic;
		private YiXinLogic yixinLogic;
		private BuShareListener shareListener;

		private BuMenuListener buMenuListener;

		public BuShareLogic(BuActivity t, BuShareListener _listener) {
			super(t, new BuShareViewHoler());

			mViewHolder.weiMiMenu = new BuMenu(mActivity);

			this.shareListener = _listener;
			weixinLogic = new WeixinLogic(mActivity);
			yixinLogic = new YiXinLogic(mActivity);

			buMenuListener = new BuMenuListener() {

				@Override
				public View getMenus(BuMenu menu) {
					return shareListener.getMenus(menu);
				}
			};
		}

		private void shareSDK_share(BuShareAppInfo appInfo, String title, String content, String comment, String imgPath, String url) {

			if (appInfo.getTargetAppDefine().id == BuShareAppDefine.NULL.id) {
				return;
			}
			ShareOptionHolder mOptionHolder = new ShareOptionHolder(mActivity, shareListener);
			mOptionHolder.disableSSOWhenAuthorize();
			// 分享时Notification的图标和文字
			mOptionHolder.setNotification(R.drawable.ic_launcher, mActivity.getString(R.string.app_name));
			// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
			mOptionHolder.setTitle(title);
			// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
			mOptionHolder.setTitleUrl(url);
			// text是分享文本，所有平台都需要这个字段
			mOptionHolder.setText(content);
			// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
			mOptionHolder.setImagePath(imgPath);
			// url仅在微信（包括好友和朋友圈）中使用
			mOptionHolder.setUrl(url);
			// comment是我对这条分享的评论，仅在人人网和QQ空间使用
			mOptionHolder.setComment(comment);
			// site是分享此内容的网站名称，仅在QQ空间使用
			mOptionHolder.setSite(mActivity.getString(R.string.app_name));
			// siteUrl是分享此内容的网站地址，仅在QQ空间使用
			mOptionHolder.setSiteUrl(url);
			mOptionHolder.share(appInfo);
		}

		public void showMenu(BuShareInfo locationInfo) {
			mActivity.dismissLoading();
			mViewHolder.weiMiMenu.show(shareListener.getRootView(), buMenuListener);// 显示分享到
		}

		protected Bitmap thumbZip(String imagePath) {
			Bitmap thumb = ShareUtils.decodeFile(new File(imagePath));
			thumb = ShareImageUtils.compressBySize(thumb, 80, 80);
			thumb = ShareImageUtils.compressByQuality(thumb, 32);
			thumb = ShareImageUtils.compressBySize(thumb, 32);
			return thumb;
		}

		void toShare4Location(BuShareAppInfo targetAppInfo, BuShareInfo info) {

			Bitmap thumb = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.translate1x1);
			thumb = BuFileHolder.compressImage(thumb);
			File file = new File(BuFileHolder.RandomFileName.getPicFileName());
			BuFileHolder.savePic(thumb, file);

			String title = "";

			if (targetAppInfo.getTargetAppDefine().id == BuShareAppDefine.SMS.id) {
				ShareSmsUtils.sendtoMessage(mActivity, info.getNumber(), title + " \n" + info.getShareUrl());
			} else if (targetAppInfo.getTargetAppDefine().id == BuShareAppDefine.WECHAT.id) {
				weixinLogic.sendWebPage(info.getShareUrl(), title, "", file.getAbsolutePath(), false);
			} else if (targetAppInfo.getTargetAppDefine().id == BuShareAppDefine.WECHAT_CMTS.id) {
				weixinLogic.sendWebPage(info.getShareUrl(), title, "", file.getAbsolutePath(), true);
			} else if (targetAppInfo.getTargetAppDefine().id == BuShareAppDefine.YIXIN.id) {
				yixinLogic.sendWebPage(info.getShareUrl(), title, "", file.getAbsolutePath(), false);
			} else if (targetAppInfo.getTargetAppDefine().id == BuShareAppDefine.YIXIN_CMTS.id) {
				yixinLogic.sendWebPage(info.getShareUrl(), title, "", file.getAbsolutePath(), true);
			} else {
				shareSDK_share(targetAppInfo, title, "", "", file.getPath(), info.getShareUrl());
			}
		}

	}

}
