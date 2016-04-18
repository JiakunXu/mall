package com.kintiger.mall.notify.dao;

import java.util.List;

import com.kintiger.mall.api.notify.bo.Notify;

/**
 * 
 * @author xujiakun
 * 
 */
public interface INotifyDao {

	/**
	 * 
	 * @param notify
	 * @return
	 */
	int getNotifyCount(Notify notify);

	/**
	 * 
	 * @param notify
	 * @return
	 */
	List<Notify> getNotifyList(Notify notify);

	/**
	 * 
	 * @param notify
	 * @return
	 */
	String createNotify(Notify notify);

	/**
	 * 
	 * @param notify
	 * @return
	 */
	int updateNotify(Notify notify);

}
