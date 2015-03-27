package org.bu.android.share;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.bu.android.boot.BuApplication;
import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

public class PlatformDataHolder {

	private static PlatformDataHolder dataHolder;

	private Map<String, PlatformData> platformDatas = null;

	public static PlatformDataHolder getDataHolder() {
		if (null == dataHolder) {
			dataHolder = new PlatformDataHolder();
		}
		return dataHolder;
	}

	private PlatformDataHolder() {
		super();
		init();
	}

	private void init() {
		try {
			InputStream is = BuApplication.getApplication().getAssets().open("ShareSDK.xml");
			this.platformDatas = parse(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public PlatformData getPlatformData(String key) {
		if (null == platformDatas && platformDatas.size() == 0) {
			init();
		}
		return platformDatas.get(key);
	}

	public Map<String, PlatformData> parse(InputStream is) throws Exception {
		Map<String, PlatformData> datas = null;
		PlatformData data = null;
		XmlPullParser parser = Xml.newPullParser(); // 由android.util.Xml创建一个XmlPullParser实例
		parser.setInput(is, "UTF-8"); // 设置输入流 并指明编码方式

		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				datas = new HashMap<String, PlatformData>();
				break;
			case XmlPullParser.START_TAG:
				if (!parser.getName().equals("DevInfor")) {
					data = new PlatformData();
					data.setShareSDK(parser.getName());
					data.setAppKey(parser.getAttributeValue("", "AppKey"));
					data.setAppSecret(parser.getAttributeValue("", "AppSecret"));
					data.setEnable(parser.getAttributeValue("", "Enable"));
					data.setId(parser.getAttributeValue("", "Id"));
					data.setRedirectUrl(parser.getAttributeValue("", "RedirectUrl"));
					data.setShareByAppClient(parser.getAttributeValue("", "ShareByAppClient"));
					data.setSortId(parser.getAttributeValue("", "SortId"));
					datas.put(data.getShareSDK(), data);
				}
				break;
			case XmlPullParser.END_TAG:
				break;
			}
			eventType = parser.next();
		}
		return datas;
	}

}