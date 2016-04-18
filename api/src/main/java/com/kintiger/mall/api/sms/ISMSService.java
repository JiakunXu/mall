package com.kintiger.mall.api.sms;

import com.kintiger.mall.framework.bo.BooleanResult;

/**
 * 短信接口.
 * 
 * @author xujiakun
 * 
 */
public interface ISMSService {

	/**
	 * 发送短信.
	 * 
	 * @param sender
	 * @param receiver
	 * @param content
	 * @param modifyUser
	 * @return
	 */
	BooleanResult send(String sender, String receiver, String content, String modifyUser);

}
