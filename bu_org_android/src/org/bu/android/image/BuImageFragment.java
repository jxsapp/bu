package org.bu.android.image;

import org.bu.android.R;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class BuImageFragment extends Fragment {
	private String mImageUrl;
	private boolean onTouchFinish = false;
	private ImageView mImageView;
	private ProgressBar progressBar;
	private TextView error_tv;

	public static BuImageFragment newInstance(String imageUrl) {
		return newInstance(imageUrl, true);
	}

	public static BuImageFragment newInstance(String imageUrl, boolean onTouchFinish) {
		final BuImageFragment f = new BuImageFragment();

		final Bundle args = new Bundle();
		args.putString("url", imageUrl);
		args.putBoolean("onTouchFinish", onTouchFinish);
		f.setArguments(args);

		return f;
	}

	public ImageView getImageView() {
		return mImageView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (null != getArguments()) {
			mImageUrl = getArguments().getString("url");
			onTouchFinish = getArguments().getBoolean("onTouchFinish");
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.bu_image_browser_fragment, container, false);
		mImageView = (ImageView) v.findViewById(R.id.image);
		if (onTouchFinish) {
			mImageView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					getActivity().finish();
				}
			});
		}
		progressBar = (ProgressBar) v.findViewById(R.id.loading);
		error_tv = (TextView) v.findViewById(R.id.error_tv);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		ImageLoaderHolder.displayImage(mImageUrl, mImageView, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				progressBar.setVisibility(View.VISIBLE);
				error_tv.setVisibility(View.GONE);
			}

			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				String message = null;
				switch (failReason.getType()) {
				case IO_ERROR:
					message = "图片不存在";
					break;
				case DECODING_ERROR:
					message = "图片无法显示";
					break;
				case NETWORK_DENIED:
					message = "网络有问题，无法下载";
					break;
				case OUT_OF_MEMORY:
					message = "图片太大无法显示";
					break;
				case UNKNOWN:
					message = "未知的错误";
					break;
				}
				error_tv.setText(message);
				error_tv.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.GONE);
			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				progressBar.setVisibility(View.GONE);
			}
		});

	}

}
