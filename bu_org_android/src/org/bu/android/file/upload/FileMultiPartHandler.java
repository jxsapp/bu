package org.bu.android.file.upload;

import java.util.Collections;
import java.util.Map;
import java.util.Random;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.bu.android.boot.BuApplication;
import org.bu.android.misc.HandlerHolder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ProgressBar;

public class FileMultiPartHandler {

	private Map<ProgressBar, String> progressBars = Collections.synchronizedMap(new WeakHashMap<ProgressBar, String>());

	private ExecutorService executorService;
	private String session = "";

	public FileMultiPartHandler(String session) {
		this(1, session);
	}

	public FileMultiPartHandler(int threadSize, String session) {
		executorService = Executors.newFixedThreadPool(threadSize);
		this.session = session;
	}

	public void upload(BuFileInfo fileUploadInfo, ProgressBar progressBar) {
		if (TextUtils.isEmpty(fileUploadInfo.getLOC())) {
			return;
		}
		progressBars.put(progressBar, fileUploadInfo.getLOC());
		if (fileUploadInfo.getSTATE() == 0) {
			addQueuePutImage(fileUploadInfo, progressBar);
		}
	}

	private void addQueuePutImage(BuFileInfo fileUploadInfo, ProgressBar progressBar) {
		executorService.submit(new PutAndSetWorker(new MapInfo(fileUploadInfo, progressBar)));
	}

	// Task for the queue
	private class MapInfo {
		public BuFileInfo fileUploadInfo;
		public ProgressBar progressBar;

		public MapInfo(BuFileInfo fileUploadInfo, ProgressBar progressBar) {
			this.fileUploadInfo = fileUploadInfo;
			this.progressBar = progressBar;
		}
	}

	private class PutAndSetWorker implements Runnable {

		private MapInfo mapInfo = null;

		public PutAndSetWorker(final MapInfo mapInfo) {
			this.mapInfo = mapInfo;
		}

		public void run() {
			if (progressBarReused(mapInfo))
				return;
			new FileMultilWoker(new FileUploadListener() {

				@Override
				public void onResult(final BuFileInfo fileInfo, String response) {
					// 更新数据库
					Activity activity = (Activity) mapInfo.progressBar.getContext();
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							fileInfo.setSTATE(BuFileInfo.STATE_SUCCESS);
						}
					});
					activity.runOnUiThread(new ViewRunable(mapInfo, 100, response));
				}

				@Override
				public void onProgress(int progerss, BuFileInfo fileInfo) {
					if (progressBarReused(mapInfo))
						return;
					if (progerss > 100) {
						progerss = progerss - new Random().nextInt(3);
					}
					Activity activity = (Activity) mapInfo.progressBar.getContext();
					activity.runOnUiThread(new ViewRunable(mapInfo, progerss, ""));
				}

				@Override
				public void onFailed(int errorcode, BuFileInfo fileInfo) {
					if (progressBarReused(mapInfo))
						return;
					Activity activity = (Activity) mapInfo.progressBar.getContext();
					activity.runOnUiThread(new ViewRunable(mapInfo, 0, ""));
				}
			}).upload(session, mapInfo.fileUploadInfo);
		}

	}

	private class ViewRunable implements Runnable {
		private MapInfo mapInfo;
		private int progress;
		private String response;

		public ViewRunable(MapInfo p, int progress, String response) {
			mapInfo = p;
			this.progress = progress;
		}

		public void run() {
			if (progressBarReused(mapInfo))
				return;
			setProgress(mapInfo, progress, response);
		}
	}

	public interface FileMultiPartListener {
		public void onProgressResult(MapInfo p, int progress, String response);
	}

	private void setProgress(MapInfo p, int progress, String response) {
		if (progressBarReused(p))
			return;

		p.progressBar.setProgress(progress);

		int status = UP_LOADING;
		if (progress >= 100) {// 成功
			status = UP_SUCS;
		} else if (progress > 0 && progress < 100) {// download
			status = UP_LOADING;
		} else {// error
			status = UP_ERROR;
		}

		Intent intent = new Intent(HandlerHolder.Action.FILE_UPLOAD_ACTION);
		Bundle bundle = new Bundle();
		bundle.putSerializable("fileInfo", p.fileUploadInfo);
		bundle.putInt("status", status);
		bundle.putInt("progress", progress);
		intent.putExtras(bundle);
		BuApplication.getApplication().sendStickyBroadcast(intent);
	}

	public static final int UP_SUCS = 0x01;
	public static final int UP_LOADING = 0x02;
	public static final int UP_ERROR = 0x03;

	private boolean progressBarReused(MapInfo mapInfo) {
		String tag = progressBars.get(mapInfo.progressBar);
		if (tag == null || !tag.equals(mapInfo.fileUploadInfo.getLOC()))
			return true;
		return false;
	}

}