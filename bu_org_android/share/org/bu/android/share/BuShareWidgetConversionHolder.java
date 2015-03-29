package org.bu.android.share;

import java.util.ArrayList;
import java.util.List;

public class BuShareWidgetConversionHolder {

	/** 每一页表情的个数 */

	private static BuShareWidgetConversionHolder Instance;

	/** 表情分页的结果集合 */
	private List<List<BuShareAppInfo>> targetAppInfoLists = new ArrayList<List<BuShareAppInfo>>();

	private BuShareWidgetConversionHolder() {

	}

	private static int PAGE_SIZE = 9;

	public static BuShareWidgetConversionHolder getInstace(int pagesize) {
		if (Instance == null) {
			Instance = new BuShareWidgetConversionHolder();
		}
		PAGE_SIZE = pagesize;
		return Instance;
	}

	public List<List<BuShareAppInfo>> getTargetAppInfos() {
		return targetAppInfoLists;
	}

	public void initEntry() {
		initEntry(BuShareAppInfo.getShareList());
	}

	public void initEntry(List<BuShareAppInfo> targetAppInfos) {
		List<List<BuShareAppInfo>> targetAppInfoLists = new ArrayList<List<BuShareAppInfo>>();
		int pageCount = (int) Math.ceil(targetAppInfos.size() / PAGE_SIZE + 0.1);
		for (int i = 0; i < pageCount; i++) {
			targetAppInfoLists.add(getPageData(targetAppInfos, i));
		}
		this.targetAppInfoLists = targetAppInfoLists;
	}

	/**
	 * 获取分页数据
	 * 
	 * @param page
	 * @return
	 */
	private List<BuShareAppInfo> getPageData(List<BuShareAppInfo> targetAppInfos, int page) {
		List<BuShareAppInfo> list = new ArrayList<BuShareAppInfo>();
		try {
			int startIndex = page * PAGE_SIZE;
			int endIndex = startIndex + PAGE_SIZE;

			if (endIndex > targetAppInfos.size()) {
				endIndex = targetAppInfos.size();
			}
			// 不这么写，会在viewpager加载中报集合操作异常，我也不知道为什么
			list.addAll(targetAppInfos.subList(startIndex, endIndex));
			if (list.size() < PAGE_SIZE) {
				for (int i = list.size(); i < PAGE_SIZE; i++) {
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