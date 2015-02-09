package org.bu.android.image;

import org.bu.android.acty.BuActivity;
import org.bu.android.app.BuUILogic;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

/**
 * 必要要在Activity 的 onActivityResult 使用
 * ImageProcessHandler.this.onActivityResult();
 * 
 * @author jxs
 * @time 2014-2-13 下午3:04:21
 */

public interface ImageProcessMaster {



	/**
	 * 回调方法
	 * 
	 * @author jxs
	 * @time 2014-2-13 下午3:13:41
	 */
	static public abstract class ImageProcessListener {
		public abstract void onImageSelected(Uri uri, ImageExtInfo extObj);

		public boolean showSysCstBtn() {
			return false;
		}
	}

	public class ImageProcessLogic extends BuUILogic<BuActivity, ImageProcessListener> implements ImageSelectorMaster {

		private ImageSelectorLogic imageSelectorLogic;

		public ImageProcessLogic(BuActivity t, ImageProcessListener v) {
			super(t, v);
			imageSelectorLogic = new ImageSelectorLogic(mActivity, new ImageSelectorListener() {

				@Override
				public void onResult(Uri uri, ImageExtInfo extObj) {
					mViewHolder.onImageSelected(uri, extObj);
				}

				@Override
				public boolean showSysCstBtn() {
					return mViewHolder.showSysCstBtn();
				}

			});
			imageSelectorLogic.initUI(null);
		}

		/**
		 * @Des 拍照获取图片
		 */
		public void doTakePhoto() {
			imageSelectorLogic.doTakePhoto();
		}

		/**
		 * @Des 请求Gallery程序
		 */
		public void doPickPhoto() {
			imageSelectorLogic.doPickPhoto();
		}

		public void selector(View root) {
			imageSelectorLogic.selectorImage(root);
		}

		public void onActivityResult(int requestCode, int resultCode, Intent data) {
			imageSelectorLogic.onActivityResult(requestCode, resultCode, data);
		}

	}

}
