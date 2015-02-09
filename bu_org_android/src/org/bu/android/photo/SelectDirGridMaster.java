package org.bu.android.photo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bu.android.R;
import org.bu.android.acty.BuActivity;
import org.bu.android.app.BuSelfLogic;
import org.bu.android.photo.BitmapCache.ImageCallback;
import org.bu.android.photo.SelectDirGridMaster.SelectDirGridAdapter.TextCallback;
import org.bu.android.widget.BuToast;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;


public interface SelectDirGridMaster {

	class SelectDirGridAdapter extends BaseAdapter {

		private TextCallback textcallback = null;
		final String TAG = getClass().getSimpleName();
		private Activity act;
		private List<ImageItem> dataList = new ArrayList<ImageItem>();
		private Map<String, String> map = new HashMap<String, String>();
		private BitmapCache cache;
		private Handler mHandler;
		private int selectTotal = 0;

		private boolean single = false;

		ImageCallback callback = new ImageCallback() {
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

		public static interface TextCallback {
			public void onListen(int count);
		}

		public void setTextCallback(TextCallback listener) {
			textcallback = listener;
		}

		public SelectDirGridAdapter(Activity act, List<ImageItem> list, Handler mHandler, boolean single) {
			this.act = act;
			dataList = list;
			cache = new BitmapCache();
			this.mHandler = mHandler;
			this.single = single;
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
		public Object getItem(int position) {
			if (dataList != null) {
				return dataList.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		class Holder {
			private ImageView iv;
			private ImageView selected;
			private TextView text;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final Holder holder;

			if (convertView == null) {
				holder = new Holder();
				convertView = View.inflate(act, R.layout.item_image_grid, null);
				holder.iv = (ImageView) convertView.findViewById(R.id.image);
				holder.selected = (ImageView) convertView.findViewById(R.id.isselected);
				holder.text = (TextView) convertView.findViewById(R.id.item_image_grid_text);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
			final ImageItem item = dataList.get(position);

			holder.iv.setTag(item.imagePath);
			cache.displayBmp(holder.iv, item.thumbnailPath, item.imagePath, callback);
			if (item.isSelected) {
				holder.selected.setImageResource(R.drawable.v2_checked_img);
				holder.text.setBackgroundResource(R.drawable.bgd_relatly_line);
			} else {
				holder.selected.setImageResource(R.drawable.translate1x1);
				holder.text.setBackgroundColor(0x00000000);
			}
			holder.iv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String path = dataList.get(position).imagePath;

					if (single) {
						Message msg = new Message();
						msg.what = 999;
						Bundle data = new Bundle();
						data.putString(MediaStore.EXTRA_OUTPUT, path);
						msg.setData(data);
						mHandler.sendMessage(msg);
					} else {

						if ((PhotoBitmapHolder.inJustdrr.size() + selectTotal) < 9) {
							item.isSelected = !item.isSelected;
							if (item.isSelected) {
								holder.selected.setImageResource(R.drawable.v2_checked_img);
								holder.text.setBackgroundResource(R.drawable.bgd_relatly_line);
								selectTotal++;
								if (textcallback != null)
									textcallback.onListen(selectTotal);
								map.put(path, path);

							} else if (!item.isSelected) {
								holder.selected.setImageResource(-1);
								holder.text.setBackgroundColor(0x00000000);
								selectTotal--;
								if (textcallback != null)
									textcallback.onListen(selectTotal);
								map.remove(path);
							}
						} else if ((PhotoBitmapHolder.inJustdrr.size() + selectTotal) >= 9) {
							if (item.isSelected == true) {
								item.isSelected = !item.isSelected;
								holder.selected.setImageResource(-1);
								selectTotal--;
								map.remove(path);

							} else {
								Message message = Message.obtain(mHandler, 0);
								message.sendToTarget();
							}
						}
					}
				}

			});

			return convertView;
		}
	}

	class SelectDirGridViewHolder {
		Button finishedBtn;
		GridView gridView;
	}

	class SelectDirGridLogic extends BuSelfLogic<SelectDirGridViewHolder> {

		private List<ImageItem> dataList = new ArrayList<ImageItem>();
		private SelectDirGridAdapter adapter;
		private AlbumHelper helper;
		private Handler mHandler;
		private boolean single = false;

		public SelectDirGridLogic(BuActivity t, boolean single) {
			super(t, new SelectDirGridViewHolder());
			this.single = single;
			mHandler = new Handler(new Handler.Callback() {

				@Override
				public boolean handleMessage(Message msg) {
					switch (msg.what) {
					case 0:
						BuToast.sendBoradCastMsg("最多选择9张图片");
						break;
					case 999:
						Intent intent = new Intent();
						intent.putExtras(msg.getData());
						mActivity.setResult(BuActivity.RESULT_OK, intent);
						mActivity.finish();
						break;
					}
					return false;
				}
			});

			helper = AlbumHelper.getHelper();
			helper.init(mActivity.getApplicationContext());
		}

		@Override
		public void onClick(View view) {
			ArrayList<String> list = new ArrayList<String>();
			Collection<String> c = adapter.map.values();
			Iterator<String> it = c.iterator();
			for (; it.hasNext();) {
				list.add(it.next());
			}
			if (PhotoBitmapHolder.act_bool) {
				PhotoBitmapHolder.act_bool = false;
			}
			for (int i = 0; i < list.size(); i++) {
				if (PhotoBitmapHolder.inJustdrr.size() < 9) {
					PhotoBitmapHolder.inJustdrr.add(list.get(i));
				}
			}
			mActivity.setResult(BuActivity.RESULT_OK);
			mActivity.finish();
		}

		@Override
		public void initUI(Bundle savedInstanceState, Object... params) {
			ImageBucket bucket = new ImageBucket();
			try {
				bucket = (ImageBucket) params[0];
			} catch (Exception e) {
				bucket = new ImageBucket();
			}
			dataList = new ArrayList<ImageItem>(bucket.imageList.values());

			mViewHolder.gridView = (GridView) mActivity.findViewById(R.id.gridview);
			mViewHolder.gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
			adapter = new SelectDirGridAdapter(mActivity, dataList, mHandler, single);
			mViewHolder.gridView.setAdapter(adapter);
			adapter.setTextCallback(new TextCallback() {
				public void onListen(int count) {
					mViewHolder.finishedBtn.setText("完成" + "(" + count + ")");
				}
			});
			mViewHolder.gridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					adapter.notifyDataSetChanged();
				}

			});
			mViewHolder.finishedBtn = (Button) mActivity.findViewById(R.id.bt);
			mViewHolder.finishedBtn.setOnClickListener(this);

			if (single) {
				mActivity.findViewById(R.id.bottom_bar).setVisibility(View.GONE);
			}

		}

	}
}
