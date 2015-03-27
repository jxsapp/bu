package org.bu.android.share;

import android.graphics.Bitmap;

public class ShareContentInfo {

	/**
	 * 标题
	 */
	private String title;
	/**
	 * 描述内容
	 */
	private String desc;
	/**
	 * 网址
	 */
	private String url;
	/**
	 * 图标
	 */
	private Bitmap bitmap;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

}
