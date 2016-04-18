package com.kintiger.mall.api.account;

import com.kintiger.mall.api.user.bo.User;
import com.kintiger.mall.framework.bo.BooleanResult;

/**
 * 账号管理.
 * 
 * @author xujiakun
 * 
 */
public interface IAccountService {

	// >>>>>>>>>>以下是卖家<<<<<<<<<<

	/**
	 * 邮件找回密码.
	 * 
	 * @param password
	 * @return
	 */
	BooleanResult generateCheckCode4Mail(String password);

	/**
	 * 修改用户基本信息.
	 * 
	 * @param userId
	 * @param user
	 * @return
	 */
	BooleanResult updateAccount(String userId, User user);

	// >>>>>>>>>>以下是买家<<<<<<<<<<

	/**
	 * 生成 6 位数字.
	 * 
	 * @param mobile
	 * @return
	 */
	BooleanResult generateCheckCode4Mobile(String mobile);

	/**
	 * 生成 6 位数字.
	 * 
	 * @param mobile
	 * @param type
	 *            是否需要验证mobile是否存在.
	 * @return
	 */
	BooleanResult generateCheckCode4Mobile(String mobile, boolean type);

	// >>>>>>>>>>以下是公共接口<<<<<<<<<<

	/**
	 * 忘记密码.
	 * 
	 * @param password
	 * @param checkCode
	 * @return
	 */
	BooleanResult setPassword(String password, String checkCode);

	/**
	 * 修改密码.
	 * 
	 * @param userId
	 * @param password
	 * @param oldPassword
	 * @return
	 */
	BooleanResult resetPassword(String userId, String password, String oldPassword);

}
