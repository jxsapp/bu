package org.bu.android.file.download;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import org.bu.android.misc.BuJSON;
import org.bu.android.pact.ErrorCode;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class BuDownloadHandler {
	private static final int MAX_RESOURCE_LENGTH = 256000000;

	private static final int PROGRESS = 1;
	private static final int SUCCESS = 2;
	private static final int FAILED = 3;

	protected IBuDownloadView _view;

	protected Handler handler = null;

	public BuDownloadHandler(IBuDownloadView view, Looper looper) {
		this._view = view;
		handler = new Handler(looper, new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what) {
				case PROGRESS:
					if (_view != null)
						_view.getResourceProgress(msg.arg1, msg.arg2);
					break;
				case SUCCESS:
					if (_view != null)
						_view.getResourceSuccess((OutputStream) msg.obj, msg.arg1);
					break;
				case FAILED:
					if (_view != null)
						_view.getResourceFailed(msg.arg1, (OutputStream) msg.obj);
					break;
				}

				return true;
			}
		});
	}

	public BuDownloadHandler(IBuDownloadView view) {
		this._view = view;
	}

	/**
	 * 
	 * @param url
	 */
	public void download(final String uri, final BuJSON json, final boolean attenchProcess, final OutputStream os) {
		Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					URL url = new URL(uri);

					HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
					httpConnection.setRequestMethod("GET");
					httpConnection.setReadTimeout(60 * 1000);
					Iterator<String> keys = json.keys();
					while (keys.hasNext()) {
						String key = keys.next();
						if ("accessToken".equals(key)) {
							httpConnection.addRequestProperty(key, json.getString(key));
						}
					}

					InputStream is = httpConnection.getInputStream();

					int status = httpConnection.getResponseCode();
					if (status != HttpURLConnection.HTTP_OK//
							&& status != HttpURLConnection.HTTP_CREATED//
							&& status != HttpURLConnection.HTTP_ACCEPTED //
							&& status != HttpURLConnection.HTTP_NO_CONTENT//
					) {

						if (handler != null) {
							Message message = new Message();
							message.what = FAILED;
							message.arg1 = status;
							message.obj = os;

							handler.sendMessage(message);
						} else
							_view.getResourceFailed(status, os);
					} else {
						int contentLength = httpConnection.getContentLength();
						if (attenchProcess) {
							if (contentLength > 0 && contentLength < MAX_RESOURCE_LENGTH) {
								byte[] buffer = new byte[4096];
								int len = -1;
								int downloadedSize = 0;
								while ((len = is.read(buffer)) != -1) {
									os.write(buffer, 0, len);
									downloadedSize += len;
									if ((contentLength != -1) && (contentLength > 0)) {
										if (handler != null) {
											Message message = new Message();
											message.what = PROGRESS;
											message.arg1 = downloadedSize;
											message.arg2 = contentLength;
											handler.sendMessage(message);
										} else
											_view.getResourceProgress(downloadedSize, contentLength);
									}
								}
							} else {
								String errorcode = httpConnection.getHeaderField(ErrorCode.HEADER);
								int error = ErrorCode.RESOURCE_LENGTH_ERROR;
								if (errorcode != null)
									error = Integer.parseInt(errorcode);
								if (handler != null) {
									Message message = new Message();
									message.what = FAILED;
									message.arg1 = error;
									message.obj = os;

									handler.sendMessage(message);
								} else
									_view.getResourceFailed(error, os);
							}
						} else {
							byte[] buffer = new byte[4096];
							int len = -1;
							while ((len = is.read(buffer)) != -1) {
								os.write(buffer, 0, len);
							}
						}

						if (handler != null) {
							Message message = new Message();
							message.what = SUCCESS;
							message.obj = os;
							message.arg1 = contentLength;

							handler.sendMessage(message);
						} else
							_view.getResourceSuccess(os, contentLength);
					}
				} catch (MalformedURLException e) {
					if (handler != null) {
						Message message = new Message();
						message.what = FAILED;
						message.arg1 = ErrorCode.MALFORMED_URL_ERROR;
						message.obj = os;

						handler.sendMessage(message);
					} else
						_view.getResourceFailed(ErrorCode.MALFORMED_URL_ERROR, os);
				} catch (IOException e) {
					if (handler != null) {
						Message message = new Message();
						message.what = FAILED;
						message.arg1 = ErrorCode.IO_ERROR;
						message.obj = os;

						handler.sendMessage(message);
					} else
						_view.getResourceFailed(ErrorCode.IO_ERROR, os);
				}
			}
		};

		thread.start();
	}
}
