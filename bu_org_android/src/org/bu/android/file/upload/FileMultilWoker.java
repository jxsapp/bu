package org.bu.android.file.upload;

import java.io.File;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.bu.android.pact.ErrorCode;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * @author jxs
 * @time 2014-5-22 上午12:35:05
 */
public class FileMultilWoker implements ErrorCode {

	protected FileUploadListener _view;

	protected Handler handler = null;

	/**
	 * 
	 * @param view
	 * @param looper
	 */
	public FileMultilWoker(FileUploadListener view, Handler handler) {
		this._view = view;
		this.handler = handler;

	}

	/**
	 * 
	 * @param view
	 */
	public FileMultilWoker(FileUploadListener view) {
		this._view = view;
	}

	private void onResultMessage(BuFileInfo fileInfo, String response) {
		FileUploadCache.getCache().remove(fileInfo);
		fileInfo.setSTATE(BuFileInfo.STATE_SUCCESS);
		Bundle bundle = new Bundle();
		bundle.putSerializable("fileInfo", fileInfo);
		bundle.putSerializable("response", response);
		if (handler != null) {
			Message msg = new Message();
			msg.what = _SUCCESS_WHAT;
			msg.setData(bundle);
			handler.sendMessage(msg);
		} else
			_view.onResult(fileInfo, response);
	}

	private void onProgressMessage(int progerss, BuFileInfo fileInfo) {
		Bundle bundle = new Bundle();
		bundle.putSerializable("fileInfo", fileInfo);
		if (handler != null) {
			Message msg = new Message();
			msg.what = _PROGRESS_WHAT;
			msg.setData(bundle);
			msg.arg1 = progerss;
			handler.sendMessage(msg);
		} else
			_view.onProgress(progerss, fileInfo);
	}

	private void onErrorMessage(int error, BuFileInfo fileInfo) {
		FileUploadCache.getCache().remove(fileInfo);
		Bundle bundle = new Bundle();
		bundle.putSerializable("fileInfo", fileInfo);
		if (handler != null) {
			Message msg = new Message();
			msg.what = _FAILED_WHAT;
			msg.setData(bundle);
			msg.arg1 = error;
			handler.sendMessage(msg);
		} else
			_view.onFailed(error, fileInfo);
	}

	private long totalSize;

	/**
	 * @param session
	 * @param fileInfo
	 */
	@SuppressWarnings("deprecation")
	public void upload(String url, final BuFileInfo fileInfo) {

		if (!FileUploadCache.getCache().hasIn(fileInfo)) {// 判断如果已经在上传队列中
			FileUploadCache.getCache().put(fileInfo);// 加入缓存
		} else {
			return;
		}
		File file = new File(fileInfo.getLOC());
		if (null == file || !file.exists()) {// 如果文件为空或者文件已经被删除了
			onErrorMessage(FILE_NOT_FOUND, fileInfo);
			return;
		}
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext httpContext = new BasicHttpContext();
		HttpPost httpPost = new HttpPost(url);
		try {
			FileMultipartEntity multipartContent = new FileMultipartEntity(new FileUploadListener() {

				@Override
				public void onResult(final BuFileInfo fileInfo, String response) {
					onResultMessage(fileInfo, response);
				}

				@Override
				public void onProgress(int progerss, BuFileInfo fileInfo) {
					int proc = (int) ((progerss / (float) totalSize) * 100);
					onProgressMessage(proc, fileInfo);
				}

				@Override
				public void onFailed(int errorcode, BuFileInfo fileInfo) {
					onErrorMessage(errorcode, fileInfo);
				}
			}, fileInfo);

			// We use FileBody to transfer an image
			multipartContent.addPart("field_name", new StringBody("field_image", ContentType.MULTIPART_FORM_DATA));
			multipartContent.addPart("file", new FileBody(new File(fileInfo.getLOC())));
			totalSize = multipartContent.getContentLength();
			// Send it
			httpPost.setEntity(multipartContent);
			HttpResponse response = httpClient.execute(httpPost, httpContext);
			String serverResponse = EntityUtils.toString(response.getEntity());
			onResultMessage(fileInfo, serverResponse);
		} catch (Exception e) {
			onErrorMessage(IO_ERROR, fileInfo);
			return;
		}

	}
}
