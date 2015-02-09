package org.bu.android.misc;

import android.text.Editable;
import android.widget.EditText;

public class CalEditHolder {

	public static void calEditor(EditText editText, int start, String number) {
		Editable editable = editText.getText();
		String value = editText.getText().toString();
		if (BuStringUtils.isEmpety(value) && ("0".equals(number) || ".".equals(number))) {
			editable.insert(start, "0.");
		} else {
			if (".".equals(number) && !value.contains(".")) {
				editable.insert(start, number);
			} else {
				editable.insert(start, number);
				value = editText.getText().toString();
				if (value.startsWith(".")) {
					editable.insert(0, "0");
				} else {
					if (value.lastIndexOf(".") == value.length()) {
						// 判断只能输入一个点
					} else if (!BuRegExpValidator.IsDecimal(value)) {// 判断只能输入
						editable.delete(value.length() - 1, value.length());
						if (value.contains(".") && !".".equals(number)) {
							value = editText.getText().toString();
							editable.delete(value.length() - 1, value.length());
							editable.insert(value.length() - 1, number);
						}
					}
				}
			}
		}

	}

}