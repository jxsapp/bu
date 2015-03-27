package org.bu.android.share;

import java.util.HashMap;

public class PlatformData {
	private String shareSDK = "";
	private String AppKey = "";// 568898243"
	private String AppSecret = "";// 38a4f8204cc784f81f9f0daaf31e02e3"
	private String Enable = "";// true"
	private String Id = "";// 1"
	private String RedirectUrl = "";// http://www.sharesdk.cn"
	private String ShareByAppClient = "";// true"
	private String SortId = "";// 1"

	public String getShareSDK() {
		return shareSDK;
	}

	public HashMap<String, Object> getReqData() {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("AppKey", AppKey);
		data.put("AppSecret", AppSecret);
		data.put("Enable", Enable);
		data.put("Id", Id);
		data.put("RedirectUrl", RedirectUrl);
		data.put("ShareByAppClient", ShareByAppClient);
		data.put("SortId", SortId);
		return data;
	}

	public void setShareSDK(String tag) {
		this.shareSDK = tag;
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

	public String getEnable() {
		return Enable;
	}

	public void setEnable(String enable) {
		Enable = enable;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getRedirectUrl() {
		return RedirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		RedirectUrl = redirectUrl;
	}

	public String getShareByAppClient() {
		return ShareByAppClient;
	}

	public void setShareByAppClient(String shareByAppClient) {
		ShareByAppClient = shareByAppClient;
	}

	public String getSortId() {
		return SortId;
	}

	public void setSortId(String sortId) {
		SortId = sortId;
	}

}
