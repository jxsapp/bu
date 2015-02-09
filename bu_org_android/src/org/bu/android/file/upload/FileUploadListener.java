package org.bu.android.file.upload;

/**
 * @author jxs
 * @time 2014-5-22 上午12:35:12
 */
public interface FileUploadListener {

	void onProgress(int progerss, BuFileInfo fileInfo);

	void onResult(BuFileInfo fileInfo, String response);

	void onFailed(int errorcode, BuFileInfo fileInfo);

}
