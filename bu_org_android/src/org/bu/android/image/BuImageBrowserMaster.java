package org.bu.android.image;

import java.io.File;
import java.util.List;

import org.bu.android.R;
import org.bu.android.acty.BuActivity;
import org.bu.android.app.BuUILogic;
import org.bu.android.app.IBuUI;
import org.bu.android.misc.BuFileHolder;
import org.bu.android.misc.BuStringUtils;
import org.bu.android.widget.BuToast;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

public interface BuImageBrowserMaster {

	public static final String STATE_POSITION = "STATE_POSITION";
	public static final String EXTRA_IMAGE_INDEX = "image_index";
	public static final String EXTRA_IMAGE_URLS = "image_urls";

	class BuImageBrowserViewHolder {
		BuImageViewPager mPager;
		TextView indicator;
		ImageButton save_pic;
	}

	class ImagePagerAdapter extends FragmentStatePagerAdapter {

		public String[] fileList;

		public ImagePagerAdapter(BuActivity t, String[] fileList) {
			super(t.getSupportFragmentManager());
			this.fileList = fileList;
		}

		@Override
		public int getCount() {
			return fileList == null ? 0 : fileList.length;
		}

		@Override
		public Fragment getItem(int position) {
			String url = fileList[position];
			return BuImageFragment.newInstance(url);
		}

	}

	class BuImageBrowserLogic extends BuUILogic<BuActivity, BuImageBrowserViewHolder> implements IBuUI {
		private int pagerPosition;
		private ImagePagerAdapter mAdapter;
		private String[] urls;

		public BuImageBrowserLogic(BuActivity t) {
			super(t, new BuImageBrowserViewHolder());
		}

		@Override
		public void onClick(View v) {
			if (v.getId() == mViewHolder.save_pic.getId()) {
				int currentPage = mViewHolder.mPager.getCurrentItem();
				if (urls != null && urls.length > currentPage && currentPage > -1) {
					String url = urls[currentPage];
					if (!BuStringUtils.isEmpety(url)) {
						String name = ImageLoaderHolder.getFileName(url);
						File file = new File(BuFileHolder.DIR_PIC, name);
						if (null != file && file.exists()) {
							File dir = BuFileHolder.getDCIMFolder();
							boolean rst = BuFileHolder.copyFile(file, new File(dir, name));
							if (rst) {
								BuToast.show("已保存至:\n" + dir.getPath() + "目录下");
							}
						}

					}
				}
			}
		}

		public void onSaveInstanceState(Bundle outState) {
			outState.putInt(STATE_POSITION, mViewHolder.mPager.getCurrentItem());
		}

		@Override
		public void initUI(Bundle savedInstanceState, Object... params) {

			Bundle bundle = new Bundle();
			try {
				bundle = (Bundle) params[0];
			} catch (Exception e) {
				bundle = new Bundle();
			}

			pagerPosition = bundle.getInt(EXTRA_IMAGE_INDEX, 0);
			urls = bundle.getStringArray(EXTRA_IMAGE_URLS);

			mViewHolder.save_pic = (ImageButton) mActivity.findViewById(R.id.save_pic);
			mViewHolder.save_pic.setOnClickListener(this);

			mViewHolder.mPager = (BuImageViewPager) mActivity.findViewById(R.id.pager);
			mAdapter = new ImagePagerAdapter(mActivity, urls);
			mViewHolder.mPager.setAdapter(mAdapter);
			mViewHolder.indicator = (TextView) mActivity.findViewById(R.id.indicator);

			CharSequence text = mActivity.getString(R.string.viewpager_indicator, 1, mViewHolder.mPager.getAdapter().getCount());
			mViewHolder.indicator.setText(text);
			// 更新下标
			mViewHolder.mPager.setOnPageChangeListener(new OnPageChangeListener() {

				@Override
				public void onPageScrollStateChanged(int arg0) {
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
				}

				@Override
				public void onPageSelected(int arg0) {
					CharSequence text = mActivity.getString(R.string.viewpager_indicator, arg0 + 1, mViewHolder.mPager.getAdapter().getCount());
					mViewHolder.indicator.setText(text);
				}

			});
			if (savedInstanceState != null) {
				pagerPosition = savedInstanceState.getInt(STATE_POSITION);
			}

			mViewHolder.mPager.setCurrentItem(pagerPosition);

		}

		/** _______________________以下代码重点在监控屏幕横竖屏__________________________ **/
		private float last_x = 0;
		private float last_y = 0;
		private boolean init = false;

		private class SensorListener implements SensorEventListener {
			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
			}

			@Override
			@SuppressWarnings("deprecation")
			public void onSensorChanged(SensorEvent event) {
				float data_y = event.values[SensorManager.DATA_Y];
				float data_x = event.values[SensorManager.DATA_Y];
				if (!init) {
					last_y = data_y;
					last_x = data_x;
					init = true;
				}
				if (last_y > 5 && last_x > 5) {// 竖屏幕
					mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				} else {// 横屏幕
					mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
				}
				last_y = data_y;
				last_x = data_x;
			}
		}

		public void onConfigurationChanged(Configuration newConfig) {
			disPlay();
		}

		private void disPlay() {
			if (mActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
				mActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
			} else {
				quitFullScreen();
			}
		}

		private void quitFullScreen() {
			final WindowManager.LayoutParams attrs = mActivity.getWindow().getAttributes();
			attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
			mActivity.getWindow().setAttributes(attrs);
			mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		}

		private SensorEventListener sensorToggerListener;
		private SensorManager sensorMgr;

		private void initSensor() {
			Sensor orientation = null;
			sensorMgr = (SensorManager) mActivity.getSystemService(Context.SENSOR_SERVICE);
			sensorToggerListener = new SensorListener();
			List<Sensor> sensorList = sensorMgr.getSensorList(Sensor.TYPE_ALL);
			int size = sensorList.size();
			for (int i = 0; i < size; i++) {
				int type = sensorList.get(i).getType();
				if (type == Sensor.TYPE_ACCELEROMETER) {
					orientation = sensorList.get(i);
					break;
				}
			}
			if (orientation != null) {// 返回true表示注册成功，false则反之
				sensorMgr.registerListener(sensorToggerListener, orientation, SensorManager.SENSOR_DELAY_NORMAL);
			}
		}

		protected void onResume() {
			initSensor();// 暂时去掉横竖屏检测
		}

		protected void onPause() {
			/*------------------暂时取消横竖屏------*/
			sensorMgr.unregisterListener(sensorToggerListener);
		}

	}

}
