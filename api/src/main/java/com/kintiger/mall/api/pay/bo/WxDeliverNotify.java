package com.kintiger.mall.api.pay.bo;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 发货通知.
 * 
 * @author xujiakun
 * 
 */
public class WxDeliverNotify implements Serializable {

	private static final long serialVersionUID = -8740788084375111550L;

	@JSONField(name = "appid")
	private String appId;

	@JSONField(name = "openid")
	private String openId;

	@JSONField(name = "transid")
	private String transId;

	@JSONField(name = "out_trade_no")
	private String outTradeNo;

	@JSONField(name = "deliver_timestamp")
	private String deliverTimestamp;

	@JSONField(name = "deliver_status")
	private String deliverStatus;

	@JSONField(name = "deliver_msg")
	private String deliverMsg;

	@JSONField(name = "app_signature")
	private String appSignature;

	@JSONField(name = "sign_method")
	private String signMethod;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getDeliverTimestamp() {
		return deliverTimestamp;
	}

	public void setDeliverTimestamp(String deliverTimestamp) {
		this.deliverTimestamp = deliverTimestamp;
	}

	public String getDeliverStatus() {
		return deliverStatus;
	}

	public void setDeliverStatus(String deliverStatus) {
		this.deliverStatus = deliverStatus;
	}

	public String getDeliverMsg() {
		return deliverMsg;
	}

	public void setDeliverMsg(String deliverMsg) {
		this.deliverMsg = deliverMsg;
	}

	public String getAppSignature() {
		return appSignature;
	}

	public void setAppSignature(String appSignature) {
		this.appSignature = appSignature;
	}

	public String getSignMethod() {
		return signMethod;
	}

	public void setSignMethod(String signMethod) {
		this.signMethod = signMethod;
	}

}
