package com.kintiger.mall.sms.dao;

import com.kintiger.mall.api.sms.bo.SMS;

/**
 * 
 * @author xujiakun
 * 
 */
public interface ISMSDao {

	/**
	 * 
	 * @param sms
	 * @return
	 */
	String createSMS(SMS sms);

}
