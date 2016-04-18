package com.kintiger.mall.framework.listener;

/**
 * 
 * @author
 * 
 */
public final class OnlineCounter {

	private static long online;

	public static long getOnline() {
		return online;
	}

	public static void raise() {
		online++;
	}

	public static void reduce() {
		online--;
	}

}
