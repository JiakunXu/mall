package com.kintiger.mall.api.express.bo;

import com.kintiger.mall.framework.bo.SearchInfo;

/**
 * 物流.
 * 
 * @author xujiakun
 * 
 */
public class Express extends SearchInfo {

	private static final long serialVersionUID = -773516904004549317L;

	private String epsId;

	/**
	 * 物流公司名称.
	 */
	private String epsName;

	/**
	 * 查询物流接口.
	 */
	private String api;

	/**
	 * 联系方式.
	 */
	private String mobileApi;

	private String state;

	public String getEpsId() {
		return epsId;
	}

	public void setEpsId(String epsId) {
		this.epsId = epsId;
	}

	public String getEpsName() {
		return epsName;
	}

	public void setEpsName(String epsName) {
		this.epsName = epsName;
	}

	public String getApi() {
		return api;
	}

	public void setApi(String api) {
		this.api = api;
	}

	public String getMobileApi() {
		return mobileApi;
	}

	public void setMobileApi(String mobileApi) {
		this.mobileApi = mobileApi;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
