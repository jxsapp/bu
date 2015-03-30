package org.bu.android.yxapi;

import im.yixin.sdk.api.BaseResp;
import im.yixin.sdk.api.BaseYXEntryActivity;
import im.yixin.sdk.api.IYXAPI;
import im.yixin.sdk.api.SendAuthToYX;
import im.yixin.sdk.api.SendMessageToYX;
import im.yixin.sdk.api.YXAPIFactory;
import im.yixin.sdk.util.YixinConstants;
import android.widget.Toast;

/**
 * 易信回调方法
 * 
 * @author Jiangxs
 * @Date 2014-2-22 下午4:21:50
 */
public class BuYXEntryActivity extends BaseYXEntryActivity {

	@Override
	public void onReq(im.yixin.sdk.api.BaseReq req) {
		goToGetMsg();
	}

	@Override
	public void onResp(im.yixin.sdk.api.BaseResp resp) {
		switch (resp.getType()) {
		case YixinConstants.RESP_SEND_MESSAGE_TYPE:
			SendMessageToYX.Resp resp1 = (SendMessageToYX.Resp) resp;
			switch (resp1.errCode) {
			case BaseResp.ErrCode.ERR_OK:
				Toast.makeText(BuYXEntryActivity.this, "分享成功", Toast.LENGTH_LONG).show();
				break;
			case BaseResp.ErrCode.ERR_COMM:
				Toast.makeText(BuYXEntryActivity.this, "分享失败", Toast.LENGTH_LONG).show();
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				Toast.makeText(BuYXEntryActivity.this, "用户取消", Toast.LENGTH_LONG).show();
				break;
			case BaseResp.ErrCode.ERR_SENT_FAILED:
				Toast.makeText(BuYXEntryActivity.this, "发送失败", Toast.LENGTH_LONG).show();
				break;
			}
			break;
		case YixinConstants.RESP_SEND_AUTH_TYPE:
			SendAuthToYX.Resp resp2 = (SendAuthToYX.Resp) resp;
			switch (resp2.errCode) {
			case BaseResp.ErrCode.ERR_OK:
				Toast.makeText(BuYXEntryActivity.this, "获取Code成功，code=" + resp2.code, Toast.LENGTH_LONG).show();
				break;
			case BaseResp.ErrCode.ERR_COMM:
				Toast.makeText(BuYXEntryActivity.this, "失败", Toast.LENGTH_LONG).show();
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				Toast.makeText(BuYXEntryActivity.this, "用户拒绝", Toast.LENGTH_LONG).show();
				break;
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
				Toast.makeText(BuYXEntryActivity.this, "用户拒绝", Toast.LENGTH_LONG).show();
				break;
			}
		}
	}

	@Override
	protected IYXAPI getIYXAPI() {
		return YXAPIFactory.createYXAPI(this, BuYXApiConfig.APP_ID);
	}

	protected void goToGetMsg() {
		// startActivity(new Intent(this, Welcome.class));
		// this.finish();
	}

}