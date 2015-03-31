package org.bu.android.share;

import java.util.HashMap;

public class PlatformData {
	private String ShareKey = "";
	private String AppId = "";
	private String AppKey = "";// 568898243"
	private String AppSecret = "";// 38a4f8204cc784f81f9f0daaf31e02e3"
	private String RedirectUrl = "";// http://www.sharesdk.cn"

	private String Id = "";// 1"

	public HashMap<String, Object> getReqData() {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("AppId", AppId);
		data.put("AppKey", AppKey);
		data.put("AppSecret", AppSecret);
		data.put("Enable", "true");
		data.put("Id", Id);
		data.put("RedirectUrl", RedirectUrl);
		data.put("ShareByAppClient", "true");
		data.put("SortId", Id);
		return data;
	}

	public String getAppKey() {
		return AppKey;
	}

	public void setAppKey(String appKey) {
		AppKey = appKey;
	}

	public String getAppSecret() {
		return AppSecret;
	}

	public void setAppSecret(String appSecret) {
		AppSecret = appSecret;
	}

	public String getRedirectUrl() {
		return RedirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		RedirectUrl = redirectUrl;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getShareKey() {
		return ShareKey;
	}

	public void setShareKey(String shareKey) {
		ShareKey = shareKey;
	}

	public String getAppId() {
		return AppId;
	}

	public void setAppId(String appId) {
		AppId = appId;
	}

}
