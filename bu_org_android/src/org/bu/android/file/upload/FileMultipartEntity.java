package org.bu.android.file.upload;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;

@SuppressWarnings("deprecation")
public class FileMultipartEntity extends MultipartEntity {

	private FileUploadListener listener = null;
	private BuFileInfo uploadInfo = null;

	public FileMultipartEntity(final FileUploadListener listener, BuFileInfo uploadInfo) {
		super();
		this.listener = listener;
		this.uploadInfo = uploadInfo;
	}

	public FileMultipartEntity(final HttpMultipartMode mode, final FileUploadListener listener, BuFileInfo uploadInfo) {
		super(mode);
		this.listener = listener;
		this.uploadInfo = uploadInfo;
	}

	public FileMultipartEntity(HttpMultipartMode mode, final String boundary, final Charset charset, final FileUploadListener listener, BuFileInfo uploadInfo) {
		super(mode, boundary, charset);
		this.listener = listener;
		this.uploadInfo = uploadInfo;
	}

	@Override
	public void writeTo(final OutputStream outstream) throws IOException {
		super.writeTo(new CountingOutputStream(outstream, this.listener, uploadInfo));
	}

	public static class CountingOutputStream extends FilterOutputStream {
		private final FileUploadListener listener;
		private BuFileInfo uploadInfo = null;
		private int transferred;

		public CountingOutputStream(final OutputStream out, final FileUploadListener listener, BuFileInfo uploadInfo) {
			super(out);
			this.listener = listener;
			this.transferred = 0;
			this.uploadInfo = uploadInfo;
		}

		public void write(byte[] b, int off, int len) throws IOException {
			out.write(b, off, len);
			this.transferred += len;
			this.listener.onProgress(this.transferred, uploadInfo);
		}

		public void write(int b) throws IOException {
			out.write(b);
			this.transferred++;
			this.listener.onProgress(this.transferred, uploadInfo);
		}

	}

}