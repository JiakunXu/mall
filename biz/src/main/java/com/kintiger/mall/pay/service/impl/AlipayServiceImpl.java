package com.kintiger.mall.pay.service.impl;

import com.alipay.config.AlipayConfig;
import com.alipay.util.AlipayNotify;
import com.alipay.util.AlipaySubmit;
import com.alipay.util.UtilDate;
import com.kintiger.mall.api.pay.IAlipayService;
import com.kintiger.mall.api.pay.IPayService;
import com.kintiger.mall.api.pay.bo.AliCallback;
import com.kintiger.mall.api.pay.bo.AliNotify;
import com.kintiger.mall.framework.bo.BooleanResult;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;

/**
 * alipay 支付.
 * 
 * @author xujiakun
 * 
 */
public class AlipayServiceImpl implements IAlipayService {

	/**
	 * 支付宝网关地址.
	 */
	private static final String ALIPAY_GATEWAY_NEW = "http://wappaygw.alipay.com/service/rest.htm?";

	/**
	 * 返回格式(必填，不需要修改).
	 */
	private static final String format = "xml";

	/**
	 * 返回格式(必填，不需要修改).
	 */
	private static final String v = "2.0";

	private Logger4jExtend logger = Logger4jCollection.getLogger(AlipayServiceImpl.class);

	private IPayService payService;

	/**
	 * 服务器异步通知页面路径(需http://格式的完整路径，不能加?id=123这类自定义参数).
	 */
	private String notifyUrl;

	/**
	 * 页面跳转同步通知页面路径(需http://格式的完整路径，不能加?id=123这类自定义参数，不能写成http://localhost/).
	 */
	private String callbackUrl;

	/**
	 * 操作中断返回地址(用户付款中途退出返回商户的地址。需http://格式的完整路径，不允许加?id=123这类自定义参数).
	 */
	private String merchantUrl;

	@Override
	public String buildRequest(String tradeNo, String subjects, String fee) throws Exception {
		// 1.

		// 请求号(必填，须保证每次请求都是唯一)
		String reqId = UtilDate.getOrderNum();

		// req_data详细信息

		// 卖家支付宝帐户(必填)
		String sellerEmail = "lsjinshiyuan@163.com";

		// 商户订单号(商户网站订单系统中唯一订单号，必填)
		String outTradeNo = tradeNo;

		// 订单名称(必填)
		String subject = subjects;

		// 付款金额(必填)
		String totalFee = fee;

		// 请求业务参数详细(必填)
		String reqDataToken =
			"<direct_trade_create_req><notify_url>" + notifyUrl + "</notify_url><call_back_url>" + callbackUrl
				+ "</call_back_url><seller_account_name>" + sellerEmail + "</seller_account_name><out_trade_no>"
				+ outTradeNo + "</out_trade_no><subject>" + subject + "</subject><total_fee>" + totalFee
				+ "</total_fee><merchant_url>" + merchantUrl + "</merchant_url></direct_trade_create_req>";

		// 2. 调用授权接口alipay.wap.trade.create.direct获取授权码token
		String requestToken = getRequestToken(reqId, reqDataToken);

		// 3. 根据授权码token调用交易接口alipay.wap.auth.authAndExecute
		return buildRequest(requestToken);
	}

	/**
	 * 调用授权接口alipay.wap.trade.create.direct获取授权码token.
	 * 
	 * @param reqId
	 * @param reqDataToken
	 * @return
	 * @throws Exception
	 */
	private String getRequestToken(String reqId, String reqDataToken) throws Exception {
		// 把请求参数打包成数组
		Map<String, String> sParaTempToken = new HashMap<String, String>();
		sParaTempToken.put("service", "alipay.wap.trade.create.direct");
		sParaTempToken.put("partner", AlipayConfig.partner);
		sParaTempToken.put("_input_charset", AlipayConfig.input_charset);
		sParaTempToken.put("sec_id", AlipayConfig.sign_type);
		sParaTempToken.put("format", format);
		sParaTempToken.put("v", v);
		sParaTempToken.put("req_id", reqId);
		sParaTempToken.put("req_data", reqDataToken);

		// 建立请求
		String sHtmlTextToken = AlipaySubmit.buildRequest(ALIPAY_GATEWAY_NEW, "", "", sParaTempToken);
		// URLDECODE返回的信息
		sHtmlTextToken = URLDecoder.decode(sHtmlTextToken, AlipayConfig.input_charset);
		// 获取token
		String requestToken = AlipaySubmit.getRequestToken(sHtmlTextToken);

		return requestToken;
	}

	/**
	 * 根据授权码token调用交易接口alipay.wap.auth.authAndExecute.
	 * 
	 * @param requestToken
	 * @return
	 */
	private String buildRequest(String requestToken) {
		// 业务详细(必填)
		String reqData =
			"<auth_and_execute_req><request_token>" + requestToken + "</request_token></auth_and_execute_req>";

		// 把请求参数打包成数组
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", "alipay.wap.auth.authAndExecute");
		sParaTemp.put("partner", AlipayConfig.partner);
		sParaTemp.put("_input_charset", AlipayConfig.input_charset);
		sParaTemp.put("sec_id", AlipayConfig.sign_type);
		sParaTemp.put("format", format);
		sParaTemp.put("v", v);
		sParaTemp.put("req_data", reqData);

		// 建立请求
		String sHtmlText = AlipaySubmit.buildRequest(ALIPAY_GATEWAY_NEW, sParaTemp, "get", "确认");

		return sHtmlText;
	}

	@Override
	public BooleanResult recordCallback(HttpServletRequest request) throws UnsupportedEncodingException {
		// 获取支付宝GET过来反馈信息
		Map<String, String> params = new HashMap<String, String>();

		@SuppressWarnings("unchecked")
		Map<String, Object> requestParams = request.getParameterMap();

		for (Map.Entry<String, Object> m : requestParams.entrySet()) {
			String[] values = (String[]) m.getValue();

			StringBuilder sb = new StringBuilder();

			for (String val : values) {
				if (sb.length() != 0) {
					sb.append(",");
				}
				sb.append(val);
			}

			String valueStr = sb.toString();

			// 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(m.getKey(), valueStr);
		}

		// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
		// 商户订单号 String out_trade_no = new
		// String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

		// 支付宝交易号 String trade_no = new
		// String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

		// 交易状态
		String result = new String(request.getParameter("result").getBytes("ISO-8859-1"), "UTF-8");

		// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//

		// 计算得出通知验证结果
		boolean verify_result = AlipayNotify.verifyReturn(params);

		BooleanResult res = new BooleanResult();

		if (verify_result) {// 验证成功
			// ////////////////////////////////////////////////////////////////////////////////////////
			// 请在这里加上商户的业务逻辑程序代码

			// ——请根据您的业务逻辑来编写程序（以下代码仅作参考）——

			if ("success".equals(result)) {
				AliCallback callback = new AliCallback();
				callback.setSign(params.get("sign"));
				callback.setTradeNo(params.get("trade_no"));
				callback.setResult(params.get("result"));
				callback.setOutTradeNo(params.get("out_trade_no"));
				callback.setRequestToken(params.get("request_token"));

				// try 3 times
				for (int i = 0; i < 3; i++) {
					BooleanResult re = payService.createAliCallback(callback, "system");
					if (re.getResult()) {
						break;
					}
				}
			}

			// 该页面可做页面美工编辑
			// out.println("验证成功<br />");
			// ——请根据您的业务逻辑来编写程序（以上代码仅作参考）——

			// ////////////////////////////////////////////////////////////////////////////////////////

			res.setCode(params.get("out_trade_no"));
			res.setResult(true);
		} else {
			// 该页面可做页面美工编辑
			// out.println("验证失败");

			res.setCode("验证失败");
			res.setResult(false);
		}

		return res;
	}

	@Override
	public boolean recordNotify(Map<String, String> params) {
		if (params == null) {
			return false;
		}

		AliNotify notify = new AliNotify();
		notify.setSign(params.get("sign"));
		notify.setV(params.get("v"));
		notify.setSecId(params.get("sec_id"));

		try {
			Document docNotifyData = DocumentHelper.parseText(params.get("notify_data"));

			notify.setPaymentType(docNotifyData.selectSingleNode("//notify/payment_type").getText());
			notify.setSubject(docNotifyData.selectSingleNode("//notify/subject").getText());
			notify.setTradeNo(docNotifyData.selectSingleNode("//notify/trade_no").getText());
			notify.setBuyerEmail(docNotifyData.selectSingleNode("//notify/buyer_email").getText());
			notify.setGmtCreate(docNotifyData.selectSingleNode("//notify/gmt_create").getText());
			notify.setNotifyType(docNotifyData.selectSingleNode("//notify/notify_type").getText());
			notify.setQuantity(docNotifyData.selectSingleNode("//notify/quantity").getText());
			notify.setOutTradeNo(docNotifyData.selectSingleNode("//notify/out_trade_no").getText());
			notify.setNotifyTime(docNotifyData.selectSingleNode("//notify/notify_time").getText());
			notify.setSellerId(docNotifyData.selectSingleNode("//notify/seller_id").getText());
			notify.setTradeStatus(docNotifyData.selectSingleNode("//notify/trade_status").getText());
			notify.setIsTotalFeeAdjust(docNotifyData.selectSingleNode("//notify/is_total_fee_adjust").getText());
			notify.setTotalFee(docNotifyData.selectSingleNode("//notify/total_fee").getText());
			notify.setGmtPayment(docNotifyData.selectSingleNode("//notify/gmt_payment").getText());
			notify.setSellerEmail(docNotifyData.selectSingleNode("//notify/seller_email").getText());
			notify.setGmtClose(docNotifyData.selectSingleNode("//notify/gmt_close").getText());
			notify.setPrice(docNotifyData.selectSingleNode("//notify/price").getText());
			notify.setBuyerId(docNotifyData.selectSingleNode("//notify/buyer_id").getText());
			notify.setNotifyId(docNotifyData.selectSingleNode("//notify/notify_id").getText());
			notify.setUseCoupon(docNotifyData.selectSingleNode("//notify/use_coupon").getText());

		} catch (Exception e) {
			logger.error(e);

			return false;
		}

		notify.setService(params.get("service"));

		return payService.createAliNotify(notify, "system").getResult();
	}

	public IPayService getPayService() {
		return payService;
	}

	public void setPayService(IPayService payService) {
		this.payService = payService;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

	public String getMerchantUrl() {
		return merchantUrl;
	}

	public void setMerchantUrl(String merchantUrl) {
		this.merchantUrl = merchantUrl;
	}

}
