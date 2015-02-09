package org.bu.android.misc;

public class BuThreadHolder {

	private static BuThreadHolder instance = null;

	public static BuThreadHolder getInstance() {
		if (null == instance) {
			instance = new BuThreadHolder();
		}
		return instance;
	}

	private BuThreadHolder() {
		super();
	}

	public void daemonThread(Runnable runnable) {
		Thread t = new Thread(runnable);
		t.setDaemon(true);
		t.start();
	}

}
