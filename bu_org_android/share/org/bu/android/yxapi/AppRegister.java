/**
 * 
 */
package org.bu.android.yxapi;

import im.yixin.sdk.api.YXAPIBaseBroadcastReceiver;
import im.yixin.sdk.channel.YXMessageProtocol;

public class AppRegister extends YXAPIBaseBroadcastReceiver {

	/*
	 * (non-Javadoc)
	 * 
	 * @see im.yixin.sdk.api.YXAPIBaseBroadcastReceiver#getAppId()
	 */
	@Override
	protected String getAppId() {
		return YXApiConfig.APP_ID;
	}

	/***********************
	 * 易信启动后，会广播消息给第三方APP，第三方APP注册之后，系统会调用此函数。 当第三方APP需要在易信启动后，完成相关工作，可以实现此函数。
	 * 此函数默认实现为空。
	 * 
	 * @param protocol
	 */
	protected void onAfterYixinStart(final YXMessageProtocol protocol) {
		/*
		 * Log.i("Yixin.SDK.AppRegister", "ClientonAfterYixinStart@" + (new
		 * Date()) + ",AppId=" + protocol.getAppId() + ",Command=" +
		 * protocol.getCommand() + ",SdkVersion=" + protocol.getSdkVersion() +
		 * ",appPackage=" + protocol.getAppPackage());
		 */
	}

}
