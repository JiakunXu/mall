package com.kintiger.mall.api.tag.bo;

import com.kintiger.mall.framework.bo.SearchInfo;

/**
 * 商品标签.
 * 
 * @author
 * 
 */
public class Tag extends SearchInfo {

	private static final long serialVersionUID = -6765248502350035572L;

	/**
	 * 商品标签ID.
	 */
	private String tagId;

	/**
	 * 商品标签名称.
	 */
	private String tagName;

	/**
	 * 店铺ID.
	 */
	private String shopId;

	/**
	 * 状态 D:删除 U:正常.
	 */
	private String state;

	/**
	 * 操作人ID.
	 */
	private String modifyUser;

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
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
