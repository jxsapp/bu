package org.bu.android.misc;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BuStatck<E extends Object> {
	private List<E> pool = new ArrayList<E>();
	private int maxSize = 20;
	private String split = ";";

	public BuStatck() {
	}

	public static interface WeiMiStatckListener<E> {
		void onPushed(int maxSize, int currentSize, E obj);

		void onPoped(int maxSize, int currentSize, E obj);
	}

	private WeiMiStatckListener<E> statckListener;

	public void setStatckListener(WeiMiStatckListener<E> statckListener) {
		this.statckListener = statckListener;
	}

	public void setSize(int size) {
		this.maxSize = size;
	}

	public BuStatck(int size, String split) {
		this.maxSize = size;
		this.split = split;
	}

	public void clear() {
		pool.clear();
	}

	public boolean empty() {
		return pool.isEmpty() || size() == 0;
	}

	/**
	 * 获取栈顶元素
	 * */
	public E top() {
		if (empty()) {
			return null;
		}
		return pool.get(pool.size() - 1);
	}

	/**
	 * 弹出栈操作
	 * */
	public E pop() {
		E e = null;
		if (empty()) {
			return e;
		}
		e = pool.remove(pool.size() - 1);
		if (null != statckListener) {
			statckListener.onPoped(maxSize, pool.size(), e);
		}
		return e;
	}

	/**
	 * 压入栈
	 * */
	public void push(E e) {
		if (pool.size() >= maxSize) {
			return;
		}
		pool.add(e);
		if (null != statckListener) {
			statckListener.onPushed(maxSize, pool.size(), e);
		}
	}

	/**
	 * 获取当前栈大小
	 * */
	public int size() {
		return pool.size();
	}

	public List<E> revert() {
		List<E> rsts = new ArrayList<E>(pool);
		Collections.reverse(rsts);
		return rsts;
	}

	@Override
	public String toString() {
		StringBuilder rst = new StringBuilder("");
		if (!empty()) {
			int i = 0;
			for (E e : pool) {
				if (i != 0) {
					rst.append(split);
				}
				rst.append(e.toString());
				i++;
			}
		}
		return rst.toString();
	}

}
