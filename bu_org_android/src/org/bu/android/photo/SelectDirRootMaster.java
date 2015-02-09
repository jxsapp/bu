package org.bu.android.photo;

import java.util.ArrayList;
import java.util.List;

import org.bu.android.R;
import org.bu.android.acty.BuActivity;
import org.bu.android.acty.BuActyGo;
import org.bu.android.app.BuSelfLogic;
import org.bu.android.misc.HandlerHolder.IntentRequest;
import org.bu.android.photo.BitmapCache.ImageCallback;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;


public interface SelectDirRootMaster {

	public class SelectDirRootCreator extends BuActyGo {

		public SelectDirRootCreator(BuActivity t) {
			super(t, SelectDirRoot.class);
		}

		public void toSelectDirRoot() {
			toSelectDirRoot(IntentRequest.SELECT_FR_PIC);
		}

		public void toSelectDirRoot(int req) {
			toSelectDirRoot(false, req);
		}

		/**
		 * 是否是选择单页
		 * 
		 * @param single
		 */
		public void toSelectDirRoot(boolean single, int requestCode) {
			Intent intent = new Intent(mActivity, SelectDirRoot.class);
			Bundle bundle = new Bundle();
			bundle.putBoolean("single", single);
			intent.putExtras(bundle);
			mActivity.startActivityForResult(intent, requestCode);
		}

	}

	class ImageBucketAdapter extends BaseAdapter {
		final String TAG = getClass().getSimpleName();

		private Activity act;
		/**
		 * 图片集列表
		 */
		private List<ImageBucket> dataList = new ArrayList<ImageBucket>();
		private BitmapCache cache;
		private ImageCallback callback = new ImageCallback() {
			@Override
			public void imageLoad(ImageView imageView, Bitmap bitmap, Object... params) {
				if (imageView != null && bitmap != null) {
					String url = (String) params[0];
					if (url != null && url.equals((String) imageView.getTag())) {
						((ImageView) imageView).setImageBitmap(bitmap);
					} else {
						Log.e(TAG, "callback, bmp not match");
					}
				} else {
					Log.e(TAG, "callback, bmp null");
				}
			}
		};

		public ImageBucketAdapter(Activity act, List<ImageBucket> list) {
			this.act = act;
			dataList = list;
			cache = new BitmapCache();
		}

		@Override
		public int getCount() {
			int count = 0;
			if (dataList != null) {
				count = dataList.size();
			}
			return count;
		}

		@Override
		public Object getItem(int arg0) {
			if (dataList != null) {
				return dataList.get(arg0);
			}
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		class Holder {
			private ImageView iv;
			private ImageView selected;
			private TextView name;
			private TextView count;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			Holder holder;
			if (arg1 == null) {
				holder = new Holder();
				arg1 = View.inflate(act, R.layout.item_image_bucket, null);
				holder.iv = (ImageView) arg1.findViewById(R.id.image);
				holder.selected = (ImageView) arg1.findViewById(R.id.isselected);
				holder.name = (TextView) arg1.findViewById(R.id.name);
				holder.count = (TextView) arg1.findViewById(R.id.count);
				arg1.setTag(holder);
			} else {
				holder = (Holder) arg1.getTag();
			}
			ImageBucket item = dataList.get(arg0);
			holder.count.setText("" + item.count);
			holder.name.setText(item.bucketName);
			holder.selected.setVisibility(View.GONE);
			if (item.imageList != null && item.imageList.size() > 0) {
				List<ImageItem> imageItems = new ArrayList<ImageItem>(item.imageList.values());
				String thumbPath = imageItems.get(0).thumbnailPath;
				String sourcePath = imageItems.get(0).imagePath;
				holder.iv.setTag(sourcePath);
				cache.displayBmp(holder.iv, thumbPath, sourcePath, callback);
			} else {
				holder.iv.setImageBitmap(null);
			}
			return arg1;
		}

	}

	class SelectDirRootViewHolder {
		GridView gridView;
	}

	class SelectDirRootLogic extends BuSelfLogic<SelectDirRootViewHolder> {

		private final int REQ_DIR_ROOT = 0x1914;
		private List<ImageBucket> dataList = new ArrayList<ImageBucket>();
		private ImageBucketAdapter adapter;
		private AlbumHelper helper;
		private boolean single = false;

		public SelectDirRootLogic(BuActivity t, boolean single) {
			super(t, new SelectDirRootViewHolder());
			this.single = single;
			helper = AlbumHelper.getHelper();
			helper.init(mActivity.getApplicationContext());
			dataList = helper.getImagesBucketList();

		}

		@Override
		public void onClick(View arg0) {
		}

		@Override
		public void initUI(Bundle savedInstanceState, Object... params) {

			mViewHolder.gridView = (GridView) mActivity.findViewById(R.id.gridview);
			adapter = new ImageBucketAdapter(mActivity, dataList);
			mViewHolder.gridView.setAdapter(adapter);

			mViewHolder.gridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Intent intent = new Intent(mActivity, SelectDirGrid.class);
					ImageBucket imageBucket = dataList.get(position);
					intent.putExtra("imageBucket", imageBucket);
					intent.putExtra("single", single);
					mActivity.startActivityForResult(intent, REQ_DIR_ROOT);
				}

			});

		}

		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			if (requestCode == REQ_DIR_ROOT && resultCode == BuActivity.RESULT_OK) {
				if (single) {
					mActivity.setResult(BuActivity.RESULT_OK, data);
				} else {
					mActivity.setResult(BuActivity.RESULT_OK);
				}
				mActivity.finish();
			}
		}

		@Override
		protected void onDestroy() {
			super.onDestroy();
		}

	}
}
