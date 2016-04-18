package com.kintiger.mall.api.spec.bo;

import com.kintiger.mall.framework.bo.SearchInfo;

/**
 * 规格类目.
 * 
 * @author xujiakun
 * 
 */
public class SpecCat extends SearchInfo {

	private static final long serialVersionUID = -6180808931784315891L;

	private String specCId;

	private String specCName;

	private String state;

	private String modifyUser;

	/**
	 * 店铺ID.
	 */
	private String shopId;

	public String getSpecCId() {
		return specCId;
	}

	public void setSpecCId(String specCId) {
		this.specCId = specCId;
	}

	public String getSpecCName() {
		return specCName;
	}

	public void setSpecCName(String specCName) {
		this.specCName = specCName;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

}
