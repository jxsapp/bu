package org.bu.android.misc;

import android.content.IntentFilter;

public class BuHandlerHolder {

	public static enum Choose {
		CAMERA, PICTURE;
	}

	public static interface SelectContactGrid_What {
		static final int TIME_LINE = 0xbc901;
		static final int MUTIL_TIME_LINE = 0xbc902;
		static final int EVENT_EDIT = 0xbc903;
		static final int MATTER_EDIT = 0xbc904;
	}

	public static enum SelectContactGrid_Type {
		TIME_LINE, MUTIL_TIME_LINE, EVENT_EDIT, MATTER_EDIT;
	}

	public static IntentFilter getOptionIntentFilter(int priority) {
		IntentFilter intentFilter = new IntentFilter(BuHandlerHolder.Action.LISTERN_PHONE_STATE);
		intentFilter.setPriority(priority);
		return intentFilter;
	}

	static public interface LaucherDirection {
		public static final String SYNTHESIS = "synthesis";
		public static final String CONTACT = "contact";
		public static final String CHAT = "chat";
	}

	static public interface Run {
		/**
		 * 发送搜集到的参数
		 */
		public static int SEND_PARAMS = 0x0527;
		/**
		 * 运行程序是清理非当前程序的内容
		 */
		public static int CLEAR_OTHER_APP_MEMOREY = 0x0529;
		/**
		 * 上传通信录
		 */
		public static int UP_LOAD_CONTACTS_NUMBER = 0x0531;
	}

	static public interface NetWorkResult {
		public static int RESULT_OK = 0x1314;
		public static int RESULT_FALI = 0x1315;
		public static int RESULT_ERROR = 0x1316;
	}

	static public interface What {
		public static int NET_WORK_ERROR = 0x101;
		public static int SMS_CHANGE = 0x118;
		public static int CONTACT_CHANGE = 0x112;
		public static int CHANGE_NUMBER_OVER = 0x120;
		public static int LOAD_BALANCING_FAILED = 0x4008, // 负载失败
				LAUHCER_CHECK_UPDATE = 0x4009, // 检查更新
				LOGIN_FAIL = 0x4019, // 登录失败
				LOGIN_FAIL_NUMBER_INVALIDE = 0x4018, // 登录失败(号码被注册)
				LOGIN_OVER = 0x4029, // 登录成功
				LOGIN_OVER_NUMBER_INVALIDE = 0x4028, // 登录成功(号码被注册)
				USER_NULL = 0x4039, // 为设置用户
				FORCE_LOGOUT = 0xab4019, // 被迫下线
				NULL = 0x40000001;//
		public static int REGISTER_LOGIN_RESULT = 0x444;// 登录数据返回结果
		public static int MESSAGE = 0x500;
		public static int HEART_BEAT = 0x600;
		public static int HEART_BEAT_ING = 0x601;
		public static int CONTINUE_NOTICE_JSON = 0x602;
		public static int RE_RECIVER_CONTINUE_NOTICE = 0x603;
		public static int LAUCHER_USER_LOGIN_STATE = 0x604;
		public static int LAUCHER_THREAD_LOGIN = 0x605;
		public static int APPLICATION_SAVE_OR_UPDATE_USER_STATE_TRUE = 0x606;
		public static int APPLICATION_SAVE_OR_UPDATE_USER_STATE_FALSE = 0x607;
		public static int TIME_LINE_RST_4_HISTORY = 0xac607;

		public static int MATTER_SEND_COMMENTS_TYPE_reply = 0xef00;
		public static int MATTER_SEND_COMMENTS_TYPE_Genera = 0xef01;
		public static int MATTER_SEND_COMMENTS_TYPE_Praise = 0xef02;
		public static int MATTER_SEND_COMMENTS_TYPE_Close = 0xef03;
		public static int MATTER_SEND_TITBITS_TYPE_delete = 0xef04;// 删除花絮
		public static int MATTER_SEND_COMMENTS_TYPE_delete = 0xef05;// 删除评论

		public static int MATTER_OPTION_favourMatter = 0xef10;
		public static int MATTER_OPTION_cancelFavourMatter = 0xef11;
		public static int MATTER_OPTION_dontRecordMatter = 0xef12;
		public static int MATTER_OPTION_recordAgainMatter = 0xef13;

		public static int ACQ_ACT_CMT_TYPE_Genera = 0xef21;
		public static int ACQ_ACT_CMT_TYPE_Praise = 0xef22;
		public static int ACQ_ACT_CMT_TYPE_Close = 0xef23;
		public static int ACQ_ACT_CMT_TYPE_delete = 0xef25;// 删除评论
		public static int ACQ_ACT_TYPE_delete = 0xef24;// 删除花絮
	}

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
		// 选择图片
		public static int BROWSER_PIC = 0x1010;
		public static int SELECT_PIC_WEB = 0x1011;
		public static int SELECT_CAMERA_WEB = 0x1021;

		public static int CHAT_DETAIL = 0x201;
		public static int TO_TIME_LINE = 0x202;

		public static int CONTACT_DETAIL = 0x301;
		public static int CONTACT_SELECT = 0x302;
		public static int MUILT_REQ_UIDS = 0x303;
		public static int PROCESS_ADD_REQ = 0x304;

		public static int SET_NOTE_NAME = 0x400;
		public static int SET_NICK_NAME = 0x401;
		public static int SET_REAL_NAME = 0x402;
		public static int SET_SIGN_CONENT = 0x403;
		public static int SET_PRIVINE_CITY = 0x405;
		public static int SET_CITY = 0x406;
		public static int CHANGE_USER_INFO = 0x407;

		public static int MATTER_F2F_CREATE = 0xf08;// 面对面创建事情
		public static int MATTER_F2F_JOIN = 0xf09;// 面对面加入事情
		public static int MATTER_CREATE_MATTER = 0xf10;// 创建事情
		public static int MATTER_NOTEPAD = 0xf11;// 收到一个事情时候表示参与
		public static int MATTER_EDIT_MATTER = 0xf12;// 事情编辑
		public static int MATTER_DETAIL_MATTER = 0xf13;// 事情详情,去编辑或者删除事情
		public static int MATTER_TO_RADAR = 0xf14;// 去雷达界面

		public static int MATTER_CREATE_JS = 0xf14;// Web模板
		public static int MATTER_CREATE_DF = 0xf15;// 默认模板

		public static int MATTER_CREATE_TITBITS = 0xf20;// 花絮
		public static int HUAXU_CMPTS_CREATE = 0xf21;// 花絮

		public static int MATTER_SELECT_TMPL = 0xf60;// 选择模板位置
		public static int MATTER_SELECT_ACT_TMPL = 0xf70;// 选择花絮模板

		public static int ACQ_ACTS_CREATE = 0xf80;// 选择花絮模板
		public static int ACQ_ACTS_DETAIL = 0xf81;// 动态详情

		public static int RESULT_OK = 0x01b2;

	}

	static public interface Action {
		public static final String DIALPAD_LIST_ITEM_CLICK = "com.weilh.weidh.dialpad.list_item.click";
		public static final String DIALPAD_LIST_UP_ACTION = "com.weilh.weidh.dialpad.list.upaction.move";
		public static final String MY_LOCATION_ADDRESS = "com.weilh.weidh.location.address";
		public static final String LAUCHER_LIST_CONTACT_FILTER = "com.weilh.weidh.laucher.list.contact.filter";
		public static final String DIAL_PAD_SHOW = "com.weilh.weidh.dialpad.show";
		public static final String LAUCHER_BOTTOM_BAR_TOGGER = "com.weilh.weidh.lacuher.bottom.bar.togger";
		public static final String CHAT_CONTACT_SELECTED_CHANGED = "com.weilh.weidh.chat.contact.selected.changed";
		public static final String NEW_MESSAGE_IN = "com.weilh.weidh.new.message.in.process";
		public static final String NEW_MSG_OTHER_IN = "com.iwxlh.weimi.new.message.in.process";
		/** 有新的事情动态 */
		public static final String SMS_DATA_BASE_DETAIL_CHANGE = "com.weilh.weidh.chat.sms.detail.db.changed";
		public static final String SMS_SEND_ACTIOIN = "com.weilh.weidh.chat.sms.send.action";
		public static final String SMS_DELIVERED_ACTIOIN = "com.weilh.weidh.chat.sms.deliver.action";
		public static final String NET_PERMANENCE_RECONNECT_ACTIOIN = "com.weilh.weidh.net.permanence.reconnection.action";

		public static final String LOGIN_RESULT_ACTION = "com.weilh.weidh.login.udp.result_ACTION";
		public static final String LOGOUT_RESULT_ACTION = "com.weilh.weidh.logout.udp.result_ACTION";
		public static final String SEND_MESSAGE_BY_PHONE_RESULT_ACTION = "com.weilh.weidh.sendmsg.by.phone.udp.result_ACTION";

		public static final String GET_USERINFO_RESULT_ACTION = "com.weilh.weidh.get.user.info.udp.result_ACTION";
		public static final String CHANGE_NAVI_NUMBER_RESULT_ACTION = "com.weilh.weidh.change.nvai.number.result_ACTION";
		public static final String REBIND_NUMBER_RESULT_ACTION = "com.weilh.weidh.rebind.number.result_ACTION";

		public static final String HAS_NEW_VERSION_ACTION = "com.weilh.weidh.has.new.version_ACTION";
		public static final String CONTACTS_LIST_CHANGED = "com.weilh.weidh.contacts.list.chanaged_ACTION";

		public static final String RECIVER_REQUEST_ADD_FRIENDS_RESULT = "com.weilh.weidh.contacts.friend_RECIVER_REQUEST_ADD_FRIENDS_RESULT_ACTION";

		public static final String LISTERN_PHONE_STATE = "com.weilh.sptas.start.listern_PHONE_STATE";
	}

	static public interface URI {
		public static final String URI_WEI_LH = "com.weilh";
	}

	static public interface ProviderWhich {
		public static final String NOITCE_URI_ALL = "weidh/notice";
		public static final String NOITCE_URI_SIGLE = "weidh/notice/#";
	}

	static public enum ProviderOption {

		ALL(1), SINGLE(2);

		public int index;

		private ProviderOption(int index) {
			this.index = index;
		}

	}

	static public enum Which {
		HISTORY_SYNS, CONTACT;
	}

}
