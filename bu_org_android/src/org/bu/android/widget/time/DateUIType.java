package org.bu.android.widget.time;

public enum DateUIType {

	_DATE("date", 0), //
	_TIME("time", 1), //
	_DATE_TIME("date_time", 2);

	public String ui = "";
	public int index = 0;

	private DateUIType(String ui, int index) {
		this.ui = ui;
		this.index = index;
	}

	public static DateUIType valueByUI(String ui) {
		DateUIType uiType = _DATE;
		if (_DATE.ui.equals(ui)) {
			uiType = _DATE;
		} else if (_TIME.ui.equals(ui)) {
			uiType = _TIME;
		} else if (_DATE_TIME.ui.equals(ui)) {
			uiType = _DATE_TIME;
		}
		return uiType;
	}
}