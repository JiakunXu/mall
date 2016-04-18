package com.kintiger.mall.api.pay;

import com.kintiger.mall.api.pay.bo.AliCallback;
import com.kintiger.mall.api.pay.bo.AliNotify;
import com.kintiger.mall.api.pay.bo.WxNotify;
import com.kintiger.mall.framework.bo.BooleanResult;

/**
 * 支付接口.
 * 
 * @author xujiakun
 * 
 */
public interface IPayService {

	/**
	 * 创建支付回调信息.
	 * 
	 * @param callback
	 * @param modifyUser
	 * @return
	 */
	BooleanResult createAliCallback(AliCallback callback, String modifyUser);

	/**
	 * 创建支付通知信息.
	 * 
	 * @param notify
	 * @param modifyUser
	 * @return
	 */
	BooleanResult createAliNotify(AliNotify notify, String modifyUser);

	/**
	 * 创建支付通知信息.
	 * 
	 * @param notify
	 * @param modifyUser
	 * @return
	 */
	BooleanResult createWxNotify(WxNotify notify, String modifyUser);

	/**
	 * 获取支付通知信息.
	 * 
	 * @param notify
	 * @return
	 */
	WxNotify getWxNotify(WxNotify notify);

}
