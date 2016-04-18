package com.kintiger.mall.api.pay;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kintiger.mall.api.pay.bo.WxNotify;
import com.kintiger.mall.framework.bo.BooleanResult;
import com.wxap.ResponseHandler;

/**
 * 微信支付.
 * 
 * @author xujiakun
 * 
 */
public interface IWxapService {

	String ERROR_MESSAGE = "微信支付接口调用失败！";

	/**
	 * 
	 * @param tradeNo
	 * @param subject
	 * @param totalFee
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	String getBrandWCPayRequest(String tradeNo, String subject, String totalFee, HttpServletRequest request,
		HttpServletResponse response) throws Exception;

	/**
	 * 记录支付通知.
	 * 
	 * @param resHandler
	 * @return
	 */
	boolean recordNotify(ResponseHandler resHandler);

	/**
	 * 获取微信支付信息.
	 * 
	 * @param outTradeNo
	 * @return
	 */
	WxNotify getNotify(String outTradeNo);

	/**
	 * 微信发货通知.
	 * 
	 * @param appId
	 *            公众平台账户的 AppId.
	 * @param openId
	 *            购买用户的 OpenId.
	 * @param transId
	 *            交易单号.
	 * @param outTradeNo
	 *            第三方订单号.
	 * @param deliverStatus
	 *            发货状态,1 表明成功,0 表明失败,失败时需要在 deliver_msg 填上失败原因.
	 * @param deliverMsg
	 *            发货状态信息,失败时可以填上 UTF8 编码的错误提示信息,比如 “该商品已退款 ”.
	 * @return
	 */
	BooleanResult deliverNotify(String appId, String openId, String transId, String outTradeNo, String deliverStatus,
		String deliverMsg);

}
