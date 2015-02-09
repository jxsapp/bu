package org.bu.android.photo;

import java.util.ArrayList;
import java.util.List;

import org.bu.android.R;
import org.bu.android.acty.BuActivity;
import org.bu.android.app.BuSelfLogic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;

public interface WatchPagerMaster {

	class WatchPagerApater extends PagerAdapter {

		private ArrayList<View> listViews;// content

		private int size;// 页数

		public WatchPagerApater(ArrayList<View> listViews) {// 构造函数
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public void setListViews(ArrayList<View> listViews) {// 自己写的一个方法用来添加数据
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public int getCount() {// 返回数量
			return size;
		}

		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		public void destroyItem(View arg0, int arg1, Object arg2) {// 销毁view对象
			((ViewPager) arg0).removeView(listViews.get(arg1 % size));
		}

		public void finishUpdate(View arg0) {
		}

		public Object instantiateItem(View arg0, int arg1) {// 返回view对象
			try {
				((ViewPager) arg0).addView(listViews.get(arg1 % size), 0);

			} catch (Exception e) {
			}
			return listViews.get(arg1 % size);
		}

		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

	}

	class WatchPagerViewHolder {
		ViewPager pager;
		Button bt_exit;
		Button bt_del;
		Button bt_enter;
	}

	class WatchPagerLogic extends BuSelfLogic<WatchPagerViewHolder> {

		private ArrayList<View> listViews = new ArrayList<View>();

		private WatchPagerApater adapter;
		private int count;

		private List<Bitmap> bmp = new ArrayList<Bitmap>();
		private List<String> drr = new ArrayList<String>();
		private List<String> del = new ArrayList<String>();
		private int max;

		public WatchPagerLogic(BuActivity t) {
			super(t, new WatchPagerViewHolder());
		}

		@Override
		public void onClick(View view) {
			if (view.getId() == mViewHolder.bt_enter.getId()) {
				PhotoBitmapHolder.selectedBitmaps = bmp;
				PhotoBitmapHolder.inJustdrr = drr;
				PhotoBitmapHolder.max = max;
				for (int i = 0; i < del.size(); i++) {
					FileUtils.delFile(del.get(i) + ".JPEG");
				}
				setResultOK();
			} else if (view.getId() == mViewHolder.bt_del.getId()) {
				if (listViews.size() == 1) {
					PhotoBitmapHolder.selectedBitmaps.clear();
					PhotoBitmapHolder.inJustdrr.clear();
					PhotoBitmapHolder.max = 0;
					FileUtils.deleteDir();
					setResultOK();
				} else {
					String newStr = drr.get(count).substring(drr.get(count).lastIndexOf("/") + 1, drr.get(count).lastIndexOf("."));
					bmp.remove(count);
					drr.remove(count);
					del.add(newStr);
					max--;
					mViewHolder.pager.removeAllViews();
					listViews.remove(count);
					adapter.setListViews(listViews);
					adapter.notifyDataSetChanged();
				}
			} else if (view.getId() == mViewHolder.bt_exit.getId()) {
				setResultCancle();
			}
		}

		private void setResultOK() {
			mActivity.setResult(BuActivity.RESULT_OK);
			mActivity.finish();
		}

		private void setResultCancle() {
			mActivity.setResult(BuActivity.RESULT_CANCELED);
			mActivity.finish();
		}

		@Override
		public void initUI(Bundle savedInstanceState, Object... params) {
			mActivity.findViewById(R.id.photo_relativeLayout).setBackgroundColor(0x70000000);
			for (int i = 0; i < PhotoBitmapHolder.selectedBitmaps.size(); i++) {
				bmp.add(PhotoBitmapHolder.selectedBitmaps.get(i));
			}
			for (int i = 0; i < PhotoBitmapHolder.inJustdrr.size(); i++) {
				drr.add(PhotoBitmapHolder.inJustdrr.get(i));
			}
			max = PhotoBitmapHolder.max;

			mViewHolder.bt_exit = (Button) mActivity.findViewById(R.id.photo_bt_exit);
			mViewHolder.bt_del = (Button) mActivity.findViewById(R.id.photo_bt_del);
			mViewHolder.bt_enter = (Button) mActivity.findViewById(R.id.photo_bt_enter);

			mViewHolder.bt_exit.setOnClickListener(this);
			mViewHolder.bt_del.setOnClickListener(this);
			mViewHolder.bt_enter.setOnClickListener(this);

			mViewHolder.pager = (ViewPager) mActivity.findViewById(R.id.viewpager);
			mViewHolder.pager.setOnPageChangeListener(new PageSelected() {
				@Override
				public void onPageScrollStateChanged(int arg0) {
					count = arg0;
				}
			});
			for (int i = 0; i < bmp.size(); i++) {
				initListViews(bmp.get(i));//
			}

			adapter = new WatchPagerApater(listViews);// 构造adapter
			mViewHolder.pager.setAdapter(adapter);// 设置适配器

			Intent intent = mActivity.getIntent();
			int id = intent.getIntExtra("ID", 0);
			mViewHolder.pager.setCurrentItem(id);
		}

		private void initListViews(Bitmap bm) {
			ImageView img = new ImageView(mActivity);// 构造textView对象
			img.setBackgroundColor(0xff000000);
			img.setImageBitmap(bm);
			img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			listViews.add(img);// 添加view
		}

		abstract class PageSelected implements OnPageChangeListener {

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageSelected(int arg0) {

			}

		}

	}

}
