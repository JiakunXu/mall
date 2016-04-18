package com.kintiger.mall.api.company.bo;

import com.kintiger.mall.framework.bo.SearchInfo;

/**
 * 公司.
 * 
 * @author
 * 
 */
public class Company extends SearchInfo {

	private static final long serialVersionUID = 2993440158692632484L;

	/**
	 * 公司ID.
	 */
	private String companyId;

	/**
	 * 公司名称.
	 */
	private String companyName;

	/**
	 * 省.
	 */
	private String province;

	/**
	 * 市.
	 */
	private String city;

	/**
	 * 地区.
	 */
	private String area;

	/**
	 * 联系地址.
	 */
	private String address;

	/**
	 * 联系人.
	 */
	private String contactName;

	/**
	 * 联系人手机号.
	 */
	private String mobile;

	/**
	 * 公司类型.
	 */
	private String type;

	/**
	 * 状态 D:删除 U:正常 F:禁用.
	 */
	private String state;

	/**
	 * 操作人ID.
	 */
	private String modifyUser;

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
