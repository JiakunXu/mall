package com.kintiger.mall.api.spec.bo;

import com.kintiger.mall.framework.bo.SearchInfo;

/**
 * 规格类目明细.
 * 
 * @author xujiakun
 * 
 */
public class SpecItem extends SearchInfo {

	private static final long serialVersionUID = 8328358568543476567L;

	private String specItemId;

	/**
	 * 店铺ID.
	 */
	private String shopId;

	private String specCId;

	private String specItemValue;

	private String state;

	private String modifyUser;

	public String getSpecItemId() {
		return specItemId;
	}

	public void setSpecItemId(String specItemId) {
		this.specItemId = specItemId;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getSpecCId() {
		return specCId;
	}

	public void setSpecCId(String specCId) {
		this.specCId = specCId;
	}

	public String getSpecItemValue() {
		return specItemValue;
	}

	public void setSpecItemValue(String specItemValue) {
		this.specItemValue = specItemValue;
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

}
