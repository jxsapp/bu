package org.bu.android.misc;


import java.util.Timer;
import java.util.TimerTask;

/**
 * 计时器
 * 
 * @author jxs
 * @time 2014-4-9 上午9:03:38
 * 
 */
public class BuWaitor {
	public int wait = 10000;

	public boolean isStop() {
		return wait < 0;
	}

	public BuWaitor() {
		super();
	}

	public BuWaitor(int wait) {
		super();
		this.wait = wait;
	}

	private void timer(final int period) {
		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				wait -= period;
				if (isStop()) {
					this.cancel();
				}
			}
		}, 0, period);
	}

	public void timer() {
		timer(500);
	}
}