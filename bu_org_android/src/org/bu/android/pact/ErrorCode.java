package org.bu.android.pact;

public interface ErrorCode {
	public static final int _1000 = 1000;
	public static final int IOException = 1011;
	public static final int UnknownHostException = 1012;
	public static final int SocketException = 1013;
	public static final int SocketTimeoutException = 1014;

	public static final String HEADER = "errorcode";

	public static final int SUCCESS = 0;
	public static final int FAILED = 1;

	public static final int UNKNOWN_ERROR = 1000;
	public static final int MALFORMED_URL_ERROR = 1001;
	public static final int IO_ERROR = 1002;
	public static final int PROTOCOL_ERROR = 1003;
	public static final int URL_CONNECTION_ERROR = 1004;
	public static final int FILE_NOT_FOUND = 1005;
	
	
	public static int RESOURCE_LENGTH_ERROR = 2046;

	static final int _SUCCESS_WHAT = 0x13;
	static final int _FAILED_WHAT = 0x14;
	static final int _PROGRESS_WHAT = 0x15;
}
