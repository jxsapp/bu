package org.bu.android.file.upload;

public class FilePutHandler {

	private FileUploadListener mListener;
	private String url = "";

	public FilePutHandler(String url, FileUploadListener listener) {
		this.url = url;
		this.mListener = listener;
	}

	public void upload(BuFileInfo fileInfo) {
		new FileMultilWoker(mListener).upload(url, fileInfo);
	}

}