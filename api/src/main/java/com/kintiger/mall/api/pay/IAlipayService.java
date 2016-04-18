package com.kintiger.mall.api.pay;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.kintiger.mall.framework.bo.BooleanResult;

/**
 * alipay 支付.
 * 
 * @author xujiakun
 * 
 */
public interface IAlipayService {

	String ERROR_MESSAGE = "支付宝支付接口调用失败！";

	/**
	 * 生成支付.
	 * 
	 * @param tradeNo
	 *            商户订单号(商户网站订单系统中唯一订单号，必填).
	 * @param subject
	 *            订单名称(必填).
	 * @param totalFee
	 *            付款金额(必填).
	 * @return
	 * @throws Exception
	 */
	String buildRequest(String tradeNo, String subject, String totalFee) throws Exception;

	/**
	 * 支付回调.
	 * 
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	BooleanResult recordCallback(HttpServletRequest request) throws UnsupportedEncodingException;

	/**
	 * 记录支付通知.
	 * 
	 * @param notify
	 * @return
	 */
	boolean recordNotify(Map<String, String> params);

}
