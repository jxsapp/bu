package org.bu.android.image;

import java.io.File;

import org.bu.android.acty.BuActivity;
import org.bu.android.app.BuUILogic;
import org.bu.android.app.IBuUI;
import org.bu.android.log.BuLog;
import org.bu.android.misc.BuFileHolder;
import org.bu.android.misc.HandlerHolder;
import org.bu.android.photo.SelectDirRoot;
import org.bu.android.photo.SelectDirRootMaster;
import org.bu.android.widget.BuMenu;
import org.bu.android.widget.BuMenuItemPop;
import org.bu.android.widget.BuMenuMaster.BuMenuListener;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

public interface ImageSelectorMaster {

	public interface ImageSelectorListener {
		public void onResult(Uri path, ImageExtInfo extObj);

		public boolean showSysCstBtn();
	}

	class ImageSelectorViewHolder {
		BuMenu weiMiMenu;
	}

	public class ImageSelectorLogic extends BuUILogic<BuActivity, ImageSelectorViewHolder> implements IBuUI {
		private Handler handler;
		private ImageSelectorListener _listener;

		public ImageSelectorLogic(BuActivity t, ImageSelectorListener listener) {
			super(t, new ImageSelectorViewHolder());
			this._listener = listener;
			handler = new Handler(mActivity.getMainLooper(), new Callback() {

				@Override
				public boolean handleMessage(Message msg) {
					return false;
				}
			});
		}

		@Override
		public void onClick(View v) {

		}

		@Override
		public void initUI(Bundle savedInstanceState, Object... params) {
			mViewHolder.weiMiMenu = new BuMenu(mActivity);
		}

		private void loading(final boolean show) {
			handler.post(new Runnable() {

				@Override
				public void run() {
					if (show) {
						mActivity.showLoading();
					} else {
						mActivity.dismissLoading();
					}
				}
			});
		}

		public void onActivityResult(int requestCode, int resultCode, Intent data) {
			if (resultCode == BuActivity.RESULT_OK) {
				createPicInfo(data, requestCode);
			}
		}

		private void createPicInfo(Intent data, int requestCode) {
			loading(true);
			String fileName = "";
			String cuFileName = System.currentTimeMillis() + ".jpg";
			if (requestCode == HandlerHolder.IntentRequest.SELECT_CAMERA) {// 去裁剪
				fileName = BuFileHolder.DIR_PIC + cuFileName;
				Bitmap photo = data.getParcelableExtra("data");
				if (null != photo && !photo.isRecycled()) {
					BuFileHolder.savePic(photo, fileName);
					copyFileToWeiMiSdCard(fileName);
				}
			} else if (requestCode == HandlerHolder.IntentRequest.SELECT_PIC) {// 从相册选择好了照片，然后去裁剪
				fileName = data.getExtras().getString(MediaStore.EXTRA_OUTPUT);
				copyFileToWeiMiSdCard(fileName);
			}
			loading(false);
		}

		private void copyFileToWeiMiSdCard(String desPath) {

			final String fid = BuFileHolder.RandomFileName.getPicFileName();
			final File fd = new File(fid);
			Bitmap bitmap = ImageCompressUtil.compressBySize(desPath, 480, 800);
			Bitmap resizeBimap = ImageUtils.decodeSampledBitmapFromFile(desPath, 480, 800);
			// 处理拍照后的图片旋转的问题
			int degree = ImageUtils.readPictureDegree(desPath);
			bitmap = ImageUtils.rotaingImageView(degree, resizeBimap);
			if (bitmap != null) {
				File targetFile = ImageUtils.saveFileFromBytes(ImageCompressUtil.Bitmap2Bytes(bitmap, 80), fd.getAbsolutePath());
				bitmap.recycle();
				bitmap = null;
				if (null != _listener) {
					ImageExtInfo extObj = new ImageExtInfo() {

						@Override
						public String getFid() {
							return fd.getName();
						}

						@Override
						public String getDes() {
							return "";
						}
					};
					_listener.onResult(Uri.fromFile(targetFile), extObj);
				}
			}

		}

		public void selectorImage(View root) {

			mViewHolder.weiMiMenu.show(root, new BuMenuListener() {
				@Override
				public void onDismiss(BuMenu menu) {
					super.onDismiss(menu);
				}

				@Override
				public View getMenus(final BuMenu menu) {

					BuMenuItemPop contentView = new BuMenuItemPop(mActivity);
					Button take = contentView.builderItem("拍照");
					Button pick = contentView.builderItem("相册选择");

					take.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							menu.dismiss();
							doTakePhoto();
						}
					});
					pick.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							menu.dismiss();
							doPickPhoto();
						}
					});

					return contentView;
				}
			});
		}

		/**
		 * @Des 拍照获取图片
		 */
		public void doTakePhoto() {
			try {
				File file = new File(BuFileHolder.RandomFileName.getPicFileName());
				if (null != file) {
					if (!file.getParentFile().exists()) {
						file.getParentFile().mkdirs();
					}
				}
				mActivity.startActivityForResult(getTakePhotoIntent(), HandlerHolder.IntentRequest.SELECT_CAMERA);
			} catch (ActivityNotFoundException e) {

			}
		}

		private static Intent getTakePhotoIntent() {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra("return-data", true);
			return Intent.createChooser(intent, "拍照");
		}

		private static Intent getPhotoPickIntent() {
			Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			intent.setType("image/*"); // 过滤档案格式
			Intent destIntent = Intent.createChooser(intent, "请选择图片");
			return destIntent;
		}

		public void doPickPhoto4sys() {
			mActivity.startActivityForResult(getPhotoPickIntent(), HandlerHolder.IntentRequest.SELECT_PIC);
		}

		/**
		 * @Des 请求Gallery程序
		 */
		public void doPickPhoto() {
			try {
				new SelectDirRootMaster.SelectDirRootCreator(mActivity).toSelectDirRoot(true, HandlerHolder.IntentRequest.SELECT_PIC);
			} catch (ActivityNotFoundException e) {
				BuLog.e(TAG, "you must include in your mainifest.xml -->" + SelectDirRoot.class.getName());
			}
		}
	}

}