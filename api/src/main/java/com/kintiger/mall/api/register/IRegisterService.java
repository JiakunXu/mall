package com.kintiger.mall.api.register;

import com.kintiger.mall.api.register.bo.RegisterResult;

/**
 * 用户注册接口.
 * 
 * @author xujiakun
 * 
 */
public interface IRegisterService {

	String RESULT_SUCCESS = "0";

	String RESULT_FAILED = "1";

	String RESULT_ERROR = "2";

	/**
	 * 用户注册.
	 * 
	 * @param mobile
	 *            手机号码.
	 * @param password
	 *            密码.
	 * @param userName
	 *            昵称.
	 * @return
	 */
	RegisterResult registerUser(String mobile, String password, String userName);

	/**
	 * 用户注册 并 成为该店铺会员.
	 * 
	 * @param mobile
	 *            手机号码.
	 * @param password
	 *            密码.
	 * @param userName
	 *            昵称.
	 * @param shopId
	 *            店铺id.
	 * @param checkCode
	 *            验证码.
	 * @param recommend
	 *            邀请.
	 * @return
	 */
	RegisterResult registerUser(String mobile, String password, String userName, String shopId, String checkCode,
		String recommend);

}
