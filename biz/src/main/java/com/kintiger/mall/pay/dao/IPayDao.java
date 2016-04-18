package com.kintiger.mall.pay.dao;

import com.kintiger.mall.api.pay.bo.AliCallback;
import com.kintiger.mall.api.pay.bo.AliNotify;
import com.kintiger.mall.api.pay.bo.WxNotify;

/**
 * 
 * @author xujiakun
 * 
 */
public interface IPayDao {

	/**
	 * 
	 * @param callback
	 * @return
	 */
	String createAliCallback(AliCallback callback);

	/**
	 * 
	 * @param notify
	 * @return
	 */
	String createAliNotify(AliNotify notify);

	/**
	 * 
	 * @param notify
	 * @return
	 */
	String createWxNotify(WxNotify notify);

	/**
	 * 
	 * @param notify
	 * @return
	 */
	WxNotify getWxNotify(WxNotify notify);

}
