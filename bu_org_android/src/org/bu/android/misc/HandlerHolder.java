package org.bu.android.misc;

public class HandlerHolder {
	public static final int WHAT_MSG = 0x001;

	static public interface IntentRequest {
		public static int NAVIGATION_CODE = 0x001;
		public static int SELECT_CONTACT = 0x002;
		public static int SELECT_PIC = 0x101;
		public static int SELECT_CROP = 0x100;
		public static int SELECT_CAMERA = 0x102;// 拍照
		public static int SELECT_GRAFFITIS = 0x103;// 涂鸦
		public static int SELECT_LOCATION = 0x104;// 位置
		public static int SELECT_NUMBER = 0x105;// 选择电话号码
		public static int SELECT_POKER_STYLE = 0x106;// 选择POKER
		public static int SELECT_FR_PIC = 0x107;// 选择POKER
		public static int SELECT_WEIMI_PIC = 0x108;// 从微秘图库选择图片
		public static int SELECT_DOCS = 0x109;// 选择文本
		public static int BROWSER_DOCS = 0x110;// 选择文本
		// 选择图片
		public static int BROWSER_PIC = 0x1010;
		public static int SELECT_PIC_WEB = 0x1011;
		public static int SELECT_CAMERA_WEB = 0x1021;

		public static int RESULT_OK = 0x01b2;

	}

	public static interface Action {
		static final String FILE_UPLOAD_ACTION = "bu.org.android.file_upload_ACTION";
	}
}
