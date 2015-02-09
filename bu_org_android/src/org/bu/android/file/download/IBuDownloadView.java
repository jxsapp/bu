package org.bu.android.file.download;

import java.io.OutputStream;

public interface IBuDownloadView {
	public void getResourceSuccess(OutputStream os, long contentLength);

	public void getResourceFailed(int error, OutputStream os);

	public void getResourceProgress(long downloadedSize, long contentLength);
}
