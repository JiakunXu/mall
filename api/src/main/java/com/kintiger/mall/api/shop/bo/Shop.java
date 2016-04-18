package com.kintiger.mall.api.shop.bo;

import com.kintiger.mall.framework.bo.SearchInfo;

/**
 * 店铺.
 * 
 * @author
 * 
 */
public class Shop extends SearchInfo {

	private static final long serialVersionUID = 8574544815417058571L;

	/**
	 * 店铺ID.
	 */
	private String shopId;

	/**
	 * 店铺名称.
	 */
	private String shopName;

	/**
	 * 公司ID.
	 */
	private String companyId;

	/**
	 * 联系人.
	 */
	private String contactName;

	/**
	 * 联系人手机号.
	 */
	private String mobile;

	/**
	 * 店铺20位唯一标记.
	 */
	private String uuid;

	/**
	 * 店铺 logo.
	 */
	private String fileId;

	/**
	 * 店铺 logo 路径.
	 */
	private String filePath;

	/**
	 * 状态 D:删除 U:正常.
	 */
	private String state;

	/**
	 * 操作人ID.
	 */
	private String modifyUser;

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
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
