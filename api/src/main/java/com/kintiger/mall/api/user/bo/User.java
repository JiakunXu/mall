package com.kintiger.mall.api.user.bo;

import com.kintiger.mall.framework.bo.SearchInfo;

/**
 * 用户对象.
 * 
 * @author xujiakun
 * 
 */
public class User extends SearchInfo {

	private static final long serialVersionUID = 1L;

	private String userId;

	/**
	 * 登录帐号.
	 */
	private String passport;

	/**
	 * 登录密码.
	 */
	private String password;

	/**
	 * 原密码.
	 */
	private String oldPassword;

	/**
	 * 用户名.
	 */
	private String userName;

	/**
	 * mobile.
	 */
	private String mobile;

	/**
	 * email.
	 */
	private String email;

	/**
	 * 状态(D:删除 U:正常 F：禁用).
	 */
	private String state;

	/**
	 * 修改人id.
	 */
	private String modifyUser;

	/**
	 * 所在公司id.
	 */
	private String companyId;

	/**
	 * 所在店铺id.
	 */
	private String shopId;

	/**
	 * 属于某店铺的会员.
	 */
	private String memId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassport() {
		return passport;
	}

	public void setPassport(String passport) {
		this.passport = passport;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getMemId() {
		return memId;
	}

	public void setMemId(String memId) {
		this.memId = memId;
	}

}
