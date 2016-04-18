package com.kintiger.mall.api.register.bo;

import java.io.Serializable;

import com.kintiger.mall.api.user.bo.User;

/**
 * 注册返回.
 * 
 * @author xujiakun
 * 
 */
public class RegisterResult implements Serializable {

	private static final long serialVersionUID = 5065726792307920599L;

	/**
	 * 返回结果.
	 */
	private String resultCode;

	/**
	 * 信息.
	 */
	private String message;

	/**
	 * 登陆用户信息.
	 */
	private User user;

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
