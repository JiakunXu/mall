package com.kintiger.mall.api.pay.bo;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 微信获取令牌返回.
 * 
 * @author xujiakun
 * 
 */
public class WxAccessToken extends WxError {

	private static final long serialVersionUID = 2243866440940887362L;

	/**
	 * 获取到的凭证.
	 */
	@JSONField(name = "access_token")
	private String accessToken;

	/**
	 * 凭证有效时间，单位：秒.
	 */
	@JSONField(name = "expires_in")
	private int expiresIn;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public int getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}

}
