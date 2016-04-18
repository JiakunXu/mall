package com.kintiger.mall.api.portal;

import com.kintiger.mall.api.portal.bo.ValidateResult;

/**
 * 用户登录权限接口.
 * 
 * @author xujiakun
 * 
 */
public interface ICAService {

	String RESULT_SUCCESS = "0";

	String RESULT_FAILED = "1";

	String RESULT_ERROR = "2";

	String INCORRECT_NULL = "用户名或密码不能为空！";

	String INCORRECT_LOGINID = "该用户在系统中不存在！";

	String INCORRECT_LOGIN = "用户名或密码输入不正确！";

	String INCORRECT_TOKEN = "token验证失败！";

	/**
	 * validateUser.
	 * 
	 * @param passport
	 * @param password
	 * @return
	 */
	ValidateResult validateUser(String passport, String password);

	/**
	 * validateToken.
	 * 
	 * @param token
	 * @return
	 */
	ValidateResult validateToken(String token);

	/**
	 * generateToken.
	 * 
	 * @param object
	 * @return
	 */
	String generateToken(Object object);

	/**
	 * 验证Request有效性.
	 * 
	 * @param userId
	 * @param url
	 * @return
	 */
	boolean validateRequest(String userId, String url);

}
