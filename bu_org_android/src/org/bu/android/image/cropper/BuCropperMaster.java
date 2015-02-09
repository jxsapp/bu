package org.bu.android.image.cropper;

import java.io.File;

import org.bu.android.R;
import org.bu.android.acty.BuActivity;
import org.bu.android.app.BuUILogic;
import org.bu.android.app.IBuUI;
import org.bu.android.image.ImageCompressUtil;
import org.bu.android.image.ImageUtils;
import org.bu.android.misc.BuStringUtils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import com.edmodo.cropper.CropImageView;

public interface BuCropperMaster {

	class BuCropperViewHolder {
		CropImageView cropImageView;
	}

	class BuCropperLogic extends BuUILogic<BuActivity, BuCropperViewHolder> implements IBuUI {

		// Static final constants
		static final int DEFAULT_ASPECT_RATIO_VALUES = 10;
		static final int ROTATE_NINETY_DEGREES = 90;
		static final String ASPECT_RATIO_X = "ASPECT_RATIO_X";
		static final String ASPECT_RATIO_Y = "ASPECT_RATIO_Y";

		// Instance variables
		private int mAspectRatioX = DEFAULT_ASPECT_RATIO_VALUES;
		private int mAspectRatioY = DEFAULT_ASPECT_RATIO_VALUES;
		private String desPath = "";

		BuCropperLogic(BuActivity t) {
			super(t, new BuCropperViewHolder());
		}

		@Override
		public void onClick(View v) {

		}

		@Override
		public void initUI(Bundle savedInstanceState, Object... params) {

			Bundle bundle = new Bundle();

			try {
				bundle = (Bundle) params[0];
			} catch (Exception e) {

			}

			mViewHolder.cropImageView = (CropImageView) mActivity.findViewById(R.id.CropImageView);
			mAspectRatioX = bundle.getInt("outputX", DEFAULT_ASPECT_RATIO_VALUES);
			mAspectRatioY = bundle.getInt("outputY", DEFAULT_ASPECT_RATIO_VALUES);
			Uri uri = bundle.getParcelable(MediaStore.EXTRA_OUTPUT);
			if (null != uri) {
				desPath = uri.getPath();
				Bitmap bitmap = ImageCompressUtil.compressBySize(desPath, 480, 800);
				Bitmap resizeBimap = ImageUtils.decodeSampledBitmapFromFile(desPath, 480, 800);
				// 处理拍照后的图片旋转的问题
				int degree = ImageUtils.readPictureDegree(desPath);
				bitmap = ImageUtils.rotaingImageView(degree, resizeBimap);
				mViewHolder.cropImageView.setImageBitmap(bitmap);
			}
			// cropImageView.rotateImage(ROTATE_NINETY_DEGREES);
			mViewHolder.cropImageView.setAspectRatio(mAspectRatioX, mAspectRatioY);
			mViewHolder.cropImageView.setFixedAspectRatio(true);
		}

		@Override
		protected void onSaveInstanceState(Bundle bundle) {
			super.onSaveInstanceState(bundle);
			bundle.putInt(ASPECT_RATIO_X, mAspectRatioX);
			bundle.putInt(ASPECT_RATIO_Y, mAspectRatioY);
		}

		// Restores the state upon rotating the screen/restarting the activity
		@Override
		protected void onRestoreInstanceState(Bundle bundle) {
			super.onRestoreInstanceState(bundle);
			mAspectRatioX = bundle.getInt(ASPECT_RATIO_X);
			mAspectRatioY = bundle.getInt(ASPECT_RATIO_Y);
		}

		void sureOk() {
			Intent data = mActivity.getIntent();
			Bitmap croppedImage = mViewHolder.cropImageView.getCroppedImage();
			if (null != croppedImage && !BuStringUtils.isEmpety(desPath)) {
				File targetFile = ImageUtils.saveFileFromBytes(ImageCompressUtil.Bitmap2Bytes(croppedImage, 80), desPath);
				data.putExtra(MediaStore.EXTRA_OUTPUT, targetFile.getAbsolutePath());
			}
			mActivity.setResult(BuActivity.RESULT_OK, data);
			mActivity.finish();
		}
	}

}
