package org.bu.android.share;

import java.util.ArrayList;
import java.util.List;

import org.bu.android.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class BuShareWidget extends RelativeLayout implements OnItemClickListener {

	private Context context;
	private LayoutInflater inflater;
	/** 表情页的监听事件 */
	private OnTargetSelectedListener mListener;
	/** 显示表情页的viewpager */
	private ViewPager vp_face;

	/** 表情页界面集合 */
	private ArrayList<View> pageViews = new ArrayList<View>();

	/** 游标显示布局 */
	private LinearLayout layout_point;

	/** 游标点集合 */
	private ArrayList<ImageView> pointViews = new ArrayList<ImageView>();

	/** 表情集合 */
	private List<List<BuShareAppInfo>> targetAppInfoLists = new ArrayList<List<BuShareAppInfo>>();

	/** 表情数据填充器 */
	private List<BuShareWidgetAppAdapter> faceAdapters = new ArrayList<BuShareWidgetAppAdapter>();

	/** 当前表情页 */
	private int current = 0;

	public BuShareWidget(Context context) {
		this(context, null);
	}

	public BuShareWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.wm_share_grid, this);
		resetUI();
	}

	public void resetUI() {
		resetUI(BuShareAppInfo.getShareList());
	}

	private int pageSize = 6;

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void resetUI(List<BuShareAppInfo> appLists) {
		BuShareWidgetConversionHolder.getInstace(pageSize).initEntry(appLists);
		targetAppInfoLists = BuShareWidgetConversionHolder.getInstace(pageSize).getTargetAppInfos();
		onCreate();
	}

	public void setOnTargetSelectedListener(OnTargetSelectedListener listener) {
		mListener = listener;
	}

	public interface OnTargetSelectedListener {
		void onTargetSelected(BuShareAppInfo targetAppInfo);
	}

	private void onCreate() {
		vp_face = (ViewPager) findViewById(R.id.vp_contains);
		layout_point = (LinearLayout) findViewById(R.id.guid_ll);

		pageViews.clear();
		faceAdapters.clear();
		pointViews.clear();
		layout_point.removeAllViews();
		vp_face.removeAllViews();

		initPageViews();
		initPoint();
		initData();
	}

	/**
	 * 初始化显示表情的viewpager
	 */
	@SuppressLint("InflateParams")
	private void initPageViews() {
		// 左侧添加空页
		View nullView1 = new View(context);
		// 设置透明背景
		nullView1.setBackgroundColor(Color.TRANSPARENT);
		pageViews.add(nullView1);

		// 中间添加表情页
		for (int i = 0; i < targetAppInfoLists.size(); i++) {
			GridView view = (GridView) inflater.inflate(R.layout.include_common_grid, null);
			view.setNumColumns(3);
			BuShareWidgetAppAdapter adapter = new BuShareWidgetAppAdapter(inflater, targetAppInfoLists.get(i));
			view.setAdapter(adapter);
			view.setOnItemClickListener(this);
			view.setBackgroundColor(Color.TRANSPARENT);
			view.setHorizontalSpacing(5);
			view.setVerticalSpacing(10);
			view.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
			view.setCacheColorHint(0);
			view.setPadding(10, 10, 10, 10);
			view.setSelector(new ColorDrawable(Color.TRANSPARENT));
			view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			view.setGravity(Gravity.CENTER);
			pageViews.add(view);
			faceAdapters.add(adapter);
		}

		// 右侧添加空页面
		View nullView2 = new View(context);
		// 设置透明背景
		nullView2.setBackgroundColor(Color.TRANSPARENT);
		pageViews.add(nullView2);
	}

	/**
	 * 初始化游标
	 */
	private void initPoint() {

		pointViews = new ArrayList<ImageView>();
		ImageView imageView;
		for (int i = 0; i < pageViews.size(); i++) {
			imageView = new ImageView(context);
			imageView.setBackgroundResource(R.drawable.page_indicator_unfocused);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			layoutParams.leftMargin = 10;
			layoutParams.rightMargin = 10;
			layoutParams.width = 8;
			layoutParams.height = 8;
			layout_point.addView(imageView, layoutParams);
			if (i == 0 || i == pageViews.size() - 1) {
				imageView.setVisibility(View.GONE);
			}
			if (i == 1) {
				imageView.setBackgroundResource(R.drawable.page_indicator_focused);
			}
			pointViews.add(imageView);

		}
	}

	/**
	 * 填充数据
	 */
	private void initData() {
		vp_face.setAdapter(new BuShareWidgetPagerAdapter(pageViews));

		vp_face.setCurrentItem(1);
		current = 0;
		vp_face.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				if (pointViews.size() > 0) {
					current = position - 1;
					// faceAdapters.get(current).notifyDataSetChanged();
					// 描绘分页点
					draw_Point(position);
					// 如果是第一屏或者是最后一屏禁止滑动，其实这里实现的是如果滑动的是第一屏则跳转至第二屏，如果是最后一屏则跳转到倒数第二屏.
					if (position == pointViews.size() - 1 || position == 0) {
						if (position == 0) {
							vp_face.setCurrentItem(position + 1);// 第二屏
																	// 会再次实现该回调方法实现跳转.
							pointViews.get(1).setBackgroundResource(R.drawable.page_indicator_focused);
						} else {

							vp_face.setCurrentItem(position - 1);// 倒数第二屏
							pointViews.get(position - 1).setBackgroundResource(R.drawable.page_indicator_focused);
						}
					}
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

	}

	/**
	 * 绘制游标背景
	 */
	private void draw_Point(int index) {
		for (int i = 1; i < pointViews.size(); i++) {
			if (index == i) {
				pointViews.get(i).setBackgroundResource(R.drawable.page_indicator_focused);
			} else {
				pointViews.get(i).setBackgroundResource(R.drawable.page_indicator_unfocused);
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		if (null != faceAdapters.get(current)) {
			BuShareAppInfo targetAppInfo = (BuShareAppInfo) faceAdapters.get(current).getItem(position);
			if (mListener != null && null != targetAppInfo)
				mListener.onTargetSelected(targetAppInfo);
		}

	}
}
