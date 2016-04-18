package com.kintiger.mall.api.pay.bo;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 微信返回.
 * 
 * @author xujiakun
 * 
 */
public class WxError implements Serializable {

	private static final long serialVersionUID = 1738250062035061536L;

	/**
	 * 返回码.
	 */
	@JSONField(name = "errcode")
	private int errCode;

	/**
	 * 说明.
	 */
	@JSONField(name = "errmsg")
	private String errMsg;

	public int getErrCode() {
		return errCode;
	}

	public void setErrCode(int errCode) {
		this.errCode = errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

}
