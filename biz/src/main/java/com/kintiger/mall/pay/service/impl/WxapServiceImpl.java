package com.kintiger.mall.pay.service.impl;

import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.kintiger.mall.api.cache.IMemcachedCacheService;
import com.kintiger.mall.api.pay.IPayService;
import com.kintiger.mall.api.pay.IWxapService;
import com.kintiger.mall.api.pay.bo.WxAccessToken;
import com.kintiger.mall.api.pay.bo.WxDeliverNotify;
import com.kintiger.mall.api.pay.bo.WxError;
import com.kintiger.mall.api.pay.bo.WxNotify;
import com.kintiger.mall.api.pay.bo.Wxap;
import com.kintiger.mall.framework.bo.BooleanResult;
import com.kintiger.mall.framework.exception.ServiceException;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;
import com.kintiger.mall.framework.util.HttpUtil;
import com.kintiger.mall.framework.util.LogUtil;
import com.wxap.RequestHandler;
import com.wxap.ResponseHandler;
import com.wxap.config.WxapConfig;
import com.wxap.util.Sha1Util;

/**
 * 微信支付.
 * 
 * @author xujiakun
 * 
 */
public class WxapServiceImpl implements IWxapService {

	private Logger4jExtend logger = Logger4jCollection.getLogger(WxapServiceImpl.class);

	private IMemcachedCacheService memcachedCacheService;

	private IPayService payService;

	private String tokenUrl;

	private String notifyUrl;

	private String deliverNotifyUrl;

	/**
	 * 获取access_token.
	 * 
	 * @param appId
	 * @param appSecret
	 * @return
	 * @throws ServiceException
	 */
	private String getAccessToken(String appId, String appSecret) throws ServiceException {
		if (StringUtils.isBlank(appId) || StringUtils.isBlank(appSecret)) {
			throw new ServiceException("公众平台账号信息不能为空！");
		}

		String token = null;

		try {
			token = (String) memcachedCacheService.get(IMemcachedCacheService.CACHE_KEY_WX_APP_ID + appId.trim());
		} catch (ServiceException e) {
			logger.error(IMemcachedCacheService.CACHE_KEY_WX_APP_ID + appId, e);
		}

		if (StringUtils.isNotBlank(token)) {
			return token;
		}

		String response = null;

		try {
			response =
				HttpUtil.get(tokenUrl + "?grant_type=client_credential&appid=" + appId.trim() + "&secret="
					+ appSecret.trim());
		} catch (Exception e) {
			logger.error(tokenUrl + "?grant_type=client_credential&appid=" + appId + "&secret=" + appSecret, e);
		}

		if (StringUtils.isBlank(response)) {
			throw new ServiceException("系统正忙，请稍后再试！");
		}

		WxAccessToken accessToken = JSON.parseObject(response, WxAccessToken.class);
		token = accessToken.getAccessToken();

		if (StringUtils.isBlank(token)) {
			throw new ServiceException(accessToken.getErrMsg());
		}

		try {
			// 7200 - 60 超时
			memcachedCacheService.set(IMemcachedCacheService.CACHE_KEY_WX_APP_ID + appId.trim(), token,
				IMemcachedCacheService.CACHE_KEY_WX_APP_ID_DEFAULT_EXP);
		} catch (ServiceException e) {
			logger.error(IMemcachedCacheService.CACHE_KEY_WX_APP_ID + appId, e);
		}

		return token;
	}

	@Override
	public String getBrandWCPayRequest(String tradeNo, String subject, String totalFee, HttpServletRequest request,
		HttpServletResponse response) throws Exception {
		// 初始化

		RequestHandler reqHandler = new RequestHandler(request, response);

		// 初始化
		reqHandler.init(WxapConfig.APP_ID, WxapConfig.APP_SECRET, WxapConfig.APP_KEY, WxapConfig.PARTNER_KEY);

		// 订单号，此处用时间加随机数生成，商户根据自己情况调整，只要保持全局唯一就行
		String out_trade_no = tradeNo;

		// 获取提交的商品价格
		String order_price = totalFee;
		// 获取提交的商品名称
		String product_name = subject;

		// 设置package订单参数
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("bank_type", "WX"); // 支付类型
		packageParams.put("body", product_name); // 商品描述
		packageParams.put("fee_type", "1"); // 银行币种
		packageParams.put("input_charset", "GBK"); // 字符集
		packageParams.put("notify_url", notifyUrl); // 通知地址
		packageParams.put("out_trade_no", out_trade_no); // 商户订单号
		packageParams.put("partner", WxapConfig.PARTNER); // 设置商户号
		packageParams.put("total_fee", order_price); // 商品总金额,以分为单位
		packageParams.put("spbill_create_ip", request.getRemoteAddr()); // 订单生成的机器IP，指用户浏览器端IP

		// 获取package包
		String packageValue = reqHandler.genPackage(packageParams);
		String noncestr = Sha1Util.getNonceStr();
		String timestamp = Sha1Util.getTimeStamp();

		// 设置支付参数
		SortedMap<String, String> signParams = new TreeMap<String, String>();
		signParams.put("appid", WxapConfig.APP_ID);
		signParams.put("appkey", WxapConfig.APP_KEY);
		signParams.put("noncestr", noncestr);
		signParams.put("package", packageValue);
		signParams.put("timestamp", timestamp);
		// 生成支付签名，要采用URLENCODER的原始值进行SHA1算法！
		String sign = Sha1Util.createSHA1Sign(signParams);

		// 增加非参与签名的额外参数
		signParams.put("paySign", sign);
		signParams.put("signType", "sha1");

		Wxap wechatPay = new Wxap();
		wechatPay.setAppId(WxapConfig.APP_ID);
		wechatPay.setNonceStr(noncestr);
		wechatPay.setPackageValue(packageValue);
		wechatPay.setPaySign(sign);
		wechatPay.setTimeStamp(timestamp);

		return JSON.toJSONString(wechatPay);
	}

	@Override
	public boolean recordNotify(ResponseHandler resHandler) {
		if (resHandler == null) {
			return false;
		}

		if (StringUtils.isBlank(resHandler.getParameter("bank_type"))) {
			return false;
		}

		WxNotify notify = new WxNotify();

		notify.setSignType(resHandler.getParameter("sign_type"));
		notify.setInputCharset(resHandler.getParameter("input_charset"));
		notify.setSign(resHandler.getParameter("sign"));
		notify.setTradeMode(Integer.parseInt(resHandler.getParameter("trade_mode")));
		notify.setTradeState(Integer.parseInt(resHandler.getParameter("trade_state")));
		notify.setPartner(resHandler.getParameter("partner"));
		notify.setBankType(resHandler.getParameter("bank_type"));
		notify.setBankBillno(resHandler.getParameter("bank_billno"));
		notify.setTotalFee(Integer.parseInt(resHandler.getParameter("total_fee")));
		notify.setFeeType(Integer.parseInt(resHandler.getParameter("fee_type")));
		notify.setNotifyId(resHandler.getParameter("notify_id"));
		notify.setTransactionId(resHandler.getParameter("transaction_id"));
		notify.setOutTradeNo(resHandler.getParameter("out_trade_no"));
		notify.setAttach(resHandler.getParameter("attach"));
		notify.setTimeEnd(resHandler.getParameter("time_end"));
		notify.setTransportFee(Integer.parseInt(resHandler.getParameter("transport_fee")));
		notify.setProductFee(Integer.parseInt(resHandler.getParameter("product_fee")));
		notify.setProductFee(Integer.parseInt(resHandler.getParameter("product_fee")));
		notify.setDiscount(Integer.parseInt(resHandler.getParameter("discount")));

		SortedMap<String, String> postData = resHandler.getPostData();
		notify.setAppId(postData.get("appid"));
		notify.setTimeStamp(postData.get("timestamp"));
		notify.setNonceStr(postData.get("noncestr"));
		notify.setOpenId(postData.get("openid"));
		notify.setAppSignature(postData.get("appsignature"));
		notify.setIsSubscribe(postData.get("issubscribe"));

		return payService.createWxNotify(notify, "system").getResult();
	}

	@Override
	public WxNotify getNotify(String outTradeNo) {
		if (StringUtils.isBlank(outTradeNo)) {
			return null;
		}

		WxNotify notify = new WxNotify();
		notify.setOutTradeNo(outTradeNo.trim());

		return payService.getWxNotify(notify);
	}

	@Override
	public BooleanResult deliverNotify(String appId, String openId, String transId, String outTradeNo,
		String deliverStatus, String deliverMsg) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		if (StringUtils.isBlank(appId)) {
			result.setCode("公众平台账户的appId不能为空！");
			return result;
		}

		if (StringUtils.isBlank(openId)) {
			result.setCode("购买用户的openId不能为空！");
			return result;
		}

		if (StringUtils.isBlank(transId)) {
			result.setCode("微信支付交易单号不能为空！");
			return result;
		}

		if (StringUtils.isBlank(outTradeNo)) {
			result.setCode("交易单号不能为空！");
			return result;
		}

		if (!"0".equals(deliverStatus) && !"1".equals(deliverStatus)) {
			result.setCode("发货状态不能为空或不正确！");
			return result;
		}

		// 1. 排序
		SortedMap<String, String> signParams = new TreeMap<String, String>();
		signParams.put("appid", appId.trim());
		signParams.put("appkey", WxapConfig.APP_KEY);
		signParams.put("openid", openId.trim());
		signParams.put("transid", transId.trim());
		signParams.put("out_trade_no", outTradeNo.trim());
		signParams.put("deliver_timestamp", Sha1Util.getTimeStamp());
		signParams.put("deliver_status", deliverStatus.trim());
		signParams.put("deliver_msg", deliverMsg);

		// 2. 签名
		String sign = null;
		try {
			sign = Sha1Util.createSHA1Sign(signParams);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(signParams), e);

			result.setCode("签名失败！");
			return result;
		}

		// 3. 拼装 postData
		WxDeliverNotify wxDeliverNotify = new WxDeliverNotify();
		wxDeliverNotify.setAppId(signParams.get("appid"));
		wxDeliverNotify.setOpenId(signParams.get("openid"));
		wxDeliverNotify.setTransId(signParams.get("transid"));
		wxDeliverNotify.setOutTradeNo(signParams.get("out_trade_no"));
		wxDeliverNotify.setDeliverTimestamp(signParams.get("deliver_timestamp"));
		wxDeliverNotify.setDeliverStatus(signParams.get("deliver_status"));
		wxDeliverNotify.setDeliverMsg(signParams.get("deliver_msg"));
		wxDeliverNotify.setAppSignature(sign);
		wxDeliverNotify.setSignMethod("sha1");

		String accessToken = null;

		try {
			accessToken = getAccessToken(appId.trim(), WxapConfig.APP_SECRET);
		} catch (ServiceException e) {
			result.setCode(e.getMessage());
			return result;
		}

		String response = null;

		try {
			response =
				HttpUtil.post(deliverNotifyUrl + "?access_token=" + accessToken, JSON.toJSONString(wxDeliverNotify));
		} catch (Exception e) {
			logger.error(deliverNotifyUrl + "?access_token=" + accessToken, e);
		}

		if (StringUtils.isBlank(response)) {
			throw new ServiceException("系统正忙，请稍后再试！");
		}

		WxError error = JSON.parseObject(response, WxError.class);
		result.setCode(error.getErrMsg());
		if (error.getErrCode() == 0.) {
			result.setResult(true);
		}

		return result;
	}

	public IMemcachedCacheService getMemcachedCacheService() {
		return memcachedCacheService;
	}

	public void setMemcachedCacheService(IMemcachedCacheService memcachedCacheService) {
		this.memcachedCacheService = memcachedCacheService;
	}

	public IPayService getPayService() {
		return payService;
	}

	public void setPayService(IPayService payService) {
		this.payService = payService;
	}

	public String getTokenUrl() {
		return tokenUrl;
	}

	public void setTokenUrl(String tokenUrl) {
		this.tokenUrl = tokenUrl;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getDeliverNotifyUrl() {
		return deliverNotifyUrl;
	}

	public void setDeliverNotifyUrl(String deliverNotifyUrl) {
		this.deliverNotifyUrl = deliverNotifyUrl;
	}

}
