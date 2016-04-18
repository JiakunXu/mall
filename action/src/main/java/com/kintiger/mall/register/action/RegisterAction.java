package com.kintiger.mall.register.action;

import com.kintiger.mall.api.register.IRegisterService;
import com.kintiger.mall.api.register.bo.RegisterResult;
import com.kintiger.mall.framework.action.BaseAction;

/**
 * 用户注册.
 * 
 * @author xujiakun
 * 
 */
public class RegisterAction extends BaseAction {

	private static final long serialVersionUID = -8519084408115355013L;

	private IRegisterService registerService;

	private String mobile;

	private String password;

	private String userName;

	/**
	 * 注册首页.
	 * 
	 * @return
	 */
	public String index() {
		return SUCCESS;
	}

	/**
	 * 注册.
	 * 
	 * @return
	 */
	public String register() {
		// 手机号注册
		RegisterResult result = registerService.registerUser(mobile, password, userName);

		// 注册成功
		if (IRegisterService.RESULT_SUCCESS.equals(result.getResultCode())) {
			this.setSuccessMessage(result.getMessage());
		} else {
			this.setFailMessage(result.getMessage());
		}

		return RESULT_MESSAGE;
	}

	public IRegisterService getRegisterService() {
		return registerService;
	}

	public void setRegisterService(IRegisterService registerService) {
		this.registerService = registerService;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
