package com.kintiger.mall.api.pay.bo;

import java.io.Serializable;

/**
 * 
 * @author xujiakun
 * 
 */
public class Wxap implements Serializable {

	private static final long serialVersionUID = -8637186720873751933L;

	private String appId;

	private String timeStamp;

	private String nonceStr;

	private String packageValue;

	private String paySign;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public String getPackageValue() {
		return packageValue;
	}

	public void setPackageValue(String packageValue) {
		this.packageValue = packageValue;
	}

	public String getPaySign() {
		return paySign;
	}

	public void setPaySign(String paySign) {
		this.paySign = paySign;
	}

}
