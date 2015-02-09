package org.bu.android.file.upload;

import org.bu.android.db.ModleInfo;

import com.google.gson.annotations.Expose;

public class BuFileInfo extends ModleInfo {

	public static final int TARGET_TP_CHAT = 0;// 聊天
	public static final int TARGET_TP_MATTER_ANNEX = 1;// 事情附件
	public static final int TARGET_TP_HUAXU_CMPT = 2;// 事情附件
	// {"FTP":"f0","FNAME":"张三.doc","FID":"this_is_fid.doc","SIZE":"15.4KB"}

	public static final int STATE_SUCCESS = 1;// 已上传
	public static final int STATE_DOWNLOAD = 2;// 已下载
	public static final int STATE_FAILURE = 0;// 还未上传

	private static final long serialVersionUID = -8994084925820979712L;

	/**
	 * 文件名
	 */
	@Expose
	private String FNAME = "";

	/**
	 * 文件ID
	 */
	@Expose
	private String FID = "";

	/**
	 * 文件大小
	 */
	@Expose
	private long SIZE = 1;

	/**
	 * 本地完整路径
	 */
	private String LOC = "";

	/**
	 * @Des updated = 1;
	 * @Des No updated or update failed is 0;
	 */
	private int STATE = STATE_FAILURE;

	public boolean isDownloaded() {
		return STATE == STATE_DOWNLOAD;
	}

	public boolean isSendSuccess() {
		return STATE == STATE_SUCCESS;
	}

	public boolean isSendDefault() {
		return STATE == STATE_FAILURE;
	}

	private int progress = 0;

	public int getProgress() {
		if (STATE == STATE_SUCCESS || STATE == STATE_DOWNLOAD) {
			progress = 100;
		}
		return progress;
	}

	public String getFNAME() {
		return FNAME;
	}

	public void setFNAME(String fNAME) {
		FNAME = fNAME;
	}

	public String getFID() {
		return FID;
	}

	public void setFID(String fID) {
		FID = fID;
	}

	public long getSIZE() {
		return SIZE;
	}

	public void setSIZE(long sIZE) {
		SIZE = sIZE;
	}

	public String getLOC() {
		return LOC;
	}

	public void setLOC(String lOC) {
		LOC = lOC;
	}

	public int getSTATE() {
		return STATE;
	}

	public void setSTATE(int sTATE) {
		STATE = sTATE;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

}
