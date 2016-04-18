package com.kintiger.mall.api.ip.bo;

import java.io.Serializable;

/**
 * taobao 返回.
 * 
 * @author xujiakun
 * 
 */
public class IPInfo implements Serializable {

	private static final long serialVersionUID = -2823180780277861044L;

	private String code;

	private IP data;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public IP getData() {
		return data;
	}

	public void setData(IP data) {
		this.data = data;
	}

}
