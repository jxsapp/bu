package org.bu.android.db;

import java.io.Serializable;

public class SystemParamCountry implements Serializable {
	private static final long serialVersionUID = 6711176160888817562L;
	private String id;
	private String name;
	private String code;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
