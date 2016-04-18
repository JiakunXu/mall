package com.kintiger.mall.api.trade.bo;

/***
 * 交易物流.
 * 
 * @author xujiakun
 * 
 */
public class TradeExpress {

	private String id;

	/**
	 * 交易id.
	 */
	private String tradeId;

	/**
	 * 物流公司id.
	 */
	private String epsId;

	/**
	 * 物流单号.
	 */
	private String epsNo;

	private String modifyUser;

	// >>>>>>>>>>以下是辅助属性<<<<<<<<<<

	/**
	 * 物流公司.
	 */
	private String epsName;

	private String api;

	private String mobileApi;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTradeId() {
		return tradeId;
	}

	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}

	public String getEpsId() {
		return epsId;
	}

	public void setEpsId(String epsId) {
		this.epsId = epsId;
	}

	public String getEpsNo() {
		return epsNo;
	}

	public void setEpsNo(String epsNo) {
		this.epsNo = epsNo;
	}

	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
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

}
