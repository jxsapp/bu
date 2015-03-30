package org.bu.android.share;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bu.android.boot.BuApplication;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 需要在Assets下放置 bu_share_config.json配置文件
 * 
 * @author jxs
 * @date 2015-3-30 下午12:00:20
 */
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
			InputStream is = BuApplication.getApplication().getAssets().open("bu_share_config.json");
			this.platformDatas = parse(is);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public PlatformData getPlatformData(String key) {
		if (null == platformDatas || platformDatas.size() == 0) {
			init();
		}
		return platformDatas.get(key);
	}

	public Map<String, PlatformData> parse(InputStream is) throws Exception {
		Map<String, PlatformData> datas = new HashMap<String, PlatformData>();
		List<PlatformData> list = getlist(inputStream2String(is));
		int index = 0;
		for (PlatformData data : list) {
			index++;
			data.setId(index + "");
			datas.put(data.getShareKey(), data);
		}
		return datas;
	}

	public static String inputStream2String(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i = -1;
		while ((i = is.read()) != -1) {
			baos.write(i);
		}
		return baos.toString();
	}

	public static List<PlatformData> getlist(String json) {
		List<PlatformData> list = new ArrayList<PlatformData>();
		java.lang.reflect.Type type = new TypeToken<List<PlatformData>>() {
		}.getType();
		Gson gson = new Gson();
		list = gson.fromJson(json, type);
		return list;

	}

}