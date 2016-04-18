package com.kintiger.mall.ip.dao;

import com.kintiger.mall.api.ip.bo.IP;

/**
 * 
 * @author xujiakun
 * 
 */
public interface IIPDao {

	/**
	 * 
	 * @param ip
	 * @return
	 */
	int getIPCount(IP ip);

	/**
	 * 
	 * @param ip
	 * @return
	 */
	String createIP(IP ip);

}
