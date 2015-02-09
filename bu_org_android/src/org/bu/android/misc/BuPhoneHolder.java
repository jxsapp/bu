package org.bu.android.misc;

import java.util.ArrayList;

import android.content.Context;
import android.telephony.TelephonyManager;

public class BuPhoneHolder {
	public static String getPhoneNumber(Context context) {
		TelephonyManager mTelephonyMgr;
		mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return mTelephonyMgr.getLine1Number();
	}

	public static String getPhone(String number) {
		number = BuStringUtils.isEmpety(number) ? "" : number;
		String new_number = BuStringUtils.getStringDigits(number.replaceAll("%20", ""));
		String phoneNumber = new_number;
		if (new_number.trim().length() > 11) {// +8613669371347
			String tphoneNumber = new_number.substring(new_number.length() - 11, new_number.length());
			if (BuRegExpValidator.isHandlerPhone(tphoneNumber))
				phoneNumber = tphoneNumber;
		}
		// else
		// phoneNumber = new_number;
		// } else {
		// phoneNumber = new_number;
		// }
		if (!BuRegExpValidator.isExtraPhone(phoneNumber)) {
			phoneNumber = new_number;
		}
		return phoneNumber;
	}

	public static String getPhone4InsertContacts(String number) {

		// 1.先判断是不是11位？ --->ch

		number = BuStringUtils.isEmpety(number) ? "" : number;
		String new_number = BuStringUtils.getStringDigits(number.replaceAll("%20", ""));
		String phoneNumber = new_number;
		if (new_number.length() > 11) {// +8613669371347
			phoneNumber = new_number.substring(new_number.length() - 11);
		}
		if (!BuRegExpValidator.isHandlerPhone(phoneNumber))
			phoneNumber = new_number;
		return phoneNumber;
	}

	public static String getSplitPhone(String phone) {
		StringBuilder sb = new StringBuilder();
		String tem = BuPhoneHolder.getPhone(phone);
		if (BuRegExpValidator.isHandlerPhone(tem)) {
			sb.append(tem.substring(0, 3));
			sb.append(" ");
			sb.append(tem.substring(3, 7));
			sb.append(" ");
			sb.append(tem.substring(7));
		} else if (BuRegExpValidator.isFixedPhone(tem)) {
			if (tem.startsWith("01") || tem.startsWith("02")) {
				sb.append(tem.substring(0, 3));
				sb.append(" ");
				sb.append(tem.substring(3, 7));
				sb.append(" ");
				sb.append(tem.substring(7));
			} else {
				sb.append(tem.substring(0, 4));
				sb.append(" ");
				sb.append(tem.substring(4, 7));
				sb.append(" ");
				sb.append(tem.substring(7));
			}
		}
		return sb.toString();
	}

	public static ArrayList<String> getPhones(String message) {
		ArrayList<String> psList = new ArrayList<String>();
		String ps[] = message.split("\\D+");
		for (String p : ps) {
			if (!BuStringUtils.isEmpety(p)) {
				if (BuRegExpValidator.isExtraPhone(getPhone(p))) {
					psList.add(getPhone(p));
				}
			}
		}
		return psList;
	}

	@Deprecated
	public static String getAppendTxt(String number) {

		int split_limt_3 = 0;
		String text = "";
		if (BuRegExpValidator.isHandlerPhone(number)) {
			split_limt_3 = Integer.valueOf(number.substring(0, 3));
			switch (split_limt_3) {
			case 135:
			case 136:
			case 137:
			case 138:
			case 139:
			case 147:
			case 150:
			case 151:
			case 152:
			case 157:
			case 158:
			case 159:
			case 182:
			case 187:
			case 188:
				text = "移动";
				break;
			case 130:
			case 131:
			case 132:
			case 155:
			case 156:
			case 185:
			case 186:
				text = "联通";
				break;
			case 133:
			case 153:
			case 180:
			case 189:
				text = "电信";
				break;
			}
		}
		return text;

	}
}