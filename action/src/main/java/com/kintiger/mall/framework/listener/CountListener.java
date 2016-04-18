package com.kintiger.mall.framework.listener;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * @author
 */
public class CountListener implements HttpSessionListener {

	/**
	 * (non-Javadoc).
	 * 
	 * @see javax.servlet.http.HttpSessionListener#sessionCreated(javax.servlet.http.HttpSessionEvent)
	 */
	public final void sessionCreated(final HttpSessionEvent arg0) {
		init();
		OnlineCounter.raise();
	}

	/**
	 * (non-Javadoc).
	 * 
	 * @see javax.servlet.http.HttpSessionListener#sessionDestroyed(javax.servlet.http.HttpSessionEvent)
	 */
	public final void sessionDestroyed(final HttpSessionEvent arg0) {
		if (OnlineCounter.getOnline() > 0) {
			OnlineCounter.reduce();
		}
	}

	private void init() {
	}
}
