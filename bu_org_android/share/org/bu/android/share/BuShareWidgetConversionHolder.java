package org.bu.android.share;

import java.util.ArrayList;
import java.util.List;

public class BuShareWidgetConversionHolder {

	/** 每一页表情的个数 */
	private int pageSize = 9;

	private static BuShareWidgetConversionHolder Instance;

	/** 保存于内存中的表情集合 */
	private List<BuShareAppInfo> targetAppInfos = new ArrayList<BuShareAppInfo>();

	/** 表情分页的结果集合 */
	public List<List<BuShareAppInfo>> targetAppInfoLists = new ArrayList<List<BuShareAppInfo>>();

	private BuShareWidgetConversionHolder() {

	}

	public static BuShareWidgetConversionHolder getInstace() {
		if (Instance == null) {
			Instance = new BuShareWidgetConversionHolder();
		}
		return Instance;
	}

	public void initEntry(boolean hasTimeLine) {
		parseData(hasTimeLine);
	}

	/**
	 * 解析字符
	 * @param data
	 */
	private void parseData(boolean hasTimeLine) {
		targetAppInfoLists.clear();
		targetAppInfos.clear();
		targetAppInfos = BuShareAppInfo.getShareList(hasTimeLine);
		int pageCount = (int) Math.ceil(targetAppInfos.size() / pageSize + 0.1);

		for (int i = 0; i < pageCount; i++) {
			targetAppInfoLists.add(getData(i));
		}
	}

	/**
	 * 获取分页数据
	 * 
	 * @param page
	 * @return
	 */
	private List<BuShareAppInfo> getData(int page) {
		List<BuShareAppInfo> list = new ArrayList<BuShareAppInfo>();
		try {
			int startIndex = page * pageSize;
			int endIndex = startIndex + pageSize;

			if (endIndex > targetAppInfos.size()) {
				endIndex = targetAppInfos.size();
			}
			// 不这么写，会在viewpager加载中报集合操作异常，我也不知道为什么
			list.addAll(targetAppInfos.subList(startIndex, endIndex));
			if (list.size() < pageSize) {
				for (int i = list.size(); i < pageSize; i++) {
					BuShareAppInfo object = new BuShareAppInfo();
					list.add(object);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}