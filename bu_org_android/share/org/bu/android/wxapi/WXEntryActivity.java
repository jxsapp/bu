package org.bu.android.wxapi;

import org.bu.android.R;
import org.bu.android.acty.BuActivity;
import org.bu.android.widget.BuToast;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.ShowMessageFromWX;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 微信回调方法
 * 
 * @author Jiangxs
 * @Date 2014-2-22 下午4:21:50
 */
public class WXEntryActivity extends BuActivity implements IWXAPIEventHandler {
	// IWXAPI 是第三方app和微信通信的openapi接口
	private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 通过WXAPIFactory工厂，获取IWXAPI的实例
		api = WXAPIFactory.createWXAPI(this, WXApiConfig.APP_ID, false);
		if (api.isWXAppInstalled()) {
			api.registerApp(WXApiConfig.APP_ID);
		}
		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	// 微信发送请求到第三方应用时，会回调到该方法
	@Override
	public void onReq(BaseReq req) {
		switch (req.getType()) {
		case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
			goToGetMsg();
			break;
		case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
			goToShowMsg((ShowMessageFromWX.Req) req);
			break;
		default:
			break;
		}
	}

	// 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
	@Override
	public void onResp(BaseResp resp) {
		int result = 0;

		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			result = R.string.errcode_success;
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			result = R.string.errcode_cancel;
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = R.string.errcode_deny;
			break;
		default:
			result = R.string.errcode_unknown;
			break;
		}
		BuToast.show(result);
		this.finish();
	}

	protected void goToGetMsg() {
		// startActivity(new Intent(this, Welcome.class));
		// this.finish();
	}

	private void goToShowMsg(ShowMessageFromWX.Req showReq) {
		Toast.makeText(this, "goToShowMsg", Toast.LENGTH_LONG).show();
		this.finish();
	}
}