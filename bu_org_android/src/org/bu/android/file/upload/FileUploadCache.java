package org.bu.android.file.upload;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;


public class FileUploadCache {

	static FileUploadCache cache;

	public static FileUploadCache getCache() {
		if (null == cache) {
			cache = new FileUploadCache();
		}
		return cache;
	}

	private FileUploadCache() {
		super();
	}

	private Map<String, BuFileInfo> objs = Collections.synchronizedMap(new LinkedHashMap<String, BuFileInfo>(10, 1.5f, true));

	public boolean hasIn(BuFileInfo info) {
		return objs.containsKey(info.getLOC());
	}

	public void put(BuFileInfo info) {
		objs.put(info.getLOC(), info);
	}

	public void remove(BuFileInfo info) {
		objs.remove(info.getLOC());
	}

	public void clear() {
		objs.clear();
	}

}
