package org.bu.android.db;

import java.io.Serializable;

public class SystemParamProvine implements Serializable {

	private static final long serialVersionUID = 2803650089181301865L;
	private String id;
	private String name;

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

	@Override
	public String toString() {
		return name;
	}

}
