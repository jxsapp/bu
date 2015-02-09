package org.bu.android.db;

public class SystemParamsGenery {

	public static enum ParamType {
		PT("PT"), OS("OS"), BRAND("BRAND");
		public String name = "";

		ParamType(String name) {
			this.name = name;
		}
	}

	private String param_type = "";
	private String param_type_desp = "";
	private String param_id = "";
	private String param_value = "";
	private String param_desp = "";

	public String getParam_type() {
		return param_type;
	}

	public void setParam_type(String param_type) {
		this.param_type = param_type;
	}

	public String getParam_type_desp() {
		return param_type_desp;
	}

	public void setParam_type_desp(String param_type_desp) {
		this.param_type_desp = param_type_desp;
	}

	public String getParam_id() {
		return param_id;
	}

	public void setParam_id(String param_id) {
		this.param_id = param_id;
	}

	public String getParam_value() {
		return param_value;
	}

	public void setParam_value(String param_value) {
		this.param_value = param_value;
	}

	public String getParam_desp() {
		return param_desp;
	}

	public void setParam_desp(String param_desp) {
		this.param_desp = param_desp;
	}

}
