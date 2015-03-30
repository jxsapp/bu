package org.bu.android.share;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.bu.android.db.ModleInfo;

import android.os.Bundle;

public class BuShareInfo extends ModleInfo {

	private static final long serialVersionUID = -7668157280854487330L;

	private String url = "";
	private String number = "";
	private String title = "";
	private String content = "";
	private String imagePath = "";
	private Map<String, Serializable> extras = new HashMap<String, Serializable>();
	private boolean shareText = false;

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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public void setExtras(Map<String, Serializable> extras) {
		this.extras = extras;
	}

	public boolean isShareText() {
		return shareText;
	}

	public void setShareText(boolean shareText) {
		this.shareText = shareText;
	}

}
