package org.bu.android.share;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.bu.android.db.ModleInfo;

import android.os.Bundle;

public class BuShareInfo extends ModleInfo {

	private static final long serialVersionUID = -7668157280854487330L;

	private String shareUrl = "";
	private String number = "";
	private String title;
	private Map<String, Serializable> extras = new HashMap<String, Serializable>();

	public Bundle getExtras() {
		Bundle bundle = new Bundle();
		for (String key : extras.keySet()) {
			bundle.putSerializable(key, extras.get(key));
		}
		return bundle;
	}

	public void putExtras(Bundle extras) {
		for (String key : extras.keySet()) {
			this.extras.put(key, extras.getSerializable(key));
		}
	}

	public BuShareInfo() {
		super();
	}

	public String getShareUrl() {
		return shareUrl;
	}

	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setExtras(Map<String, Serializable> extras) {
		this.extras = extras;
	}

}
