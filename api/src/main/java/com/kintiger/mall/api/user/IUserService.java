package com.kintiger.mall.api.user;

import com.kintiger.mall.api.user.bo.User;
import com.kintiger.mall.framework.bo.BooleanResult;

/**
 * 用户接口.
 * 
 * @author xujiakun
 * 
 */
public interface IUserService {

	String ERROR_MESSAGE = "操作失败！";

	String ERROR_INPUT_MESSAGE = "操作失败，输入有误！";

	String ERROR_NULL_MESSAGE = "操作失败，不存在！";

	String ERROR_EXIST_MESSAGE = "操作失败，已存在！";

	String NAMESPACE = "http://user.api.mall.kintiger.com/";

	/**
	 * 创建用户.
	 * 
	 * @param user
	 * @return
	 */
	BooleanResult createUser(User user);

	/**
	 * 根据账号获取用户信息.
	 * 
	 * @param passport
	 * @return
	 */
	User getUser4Validate(String passport);

	/**
	 * 获取用户信息.
	 * 
	 * @param userId
	 * @return
	 */
	User getUser(String userId);

	/**
	 * 修改用户基本信息.
	 * 
	 * @param userId
	 * @param user
	 * @return
	 */
	BooleanResult updateUser(String userId, User user);

	/**
	 * 修改密码.
	 * 
	 * @param userId
	 * @param password
	 * @param oldPassword
	 * @return
	 */
	BooleanResult resetPassword(String userId, String password, String oldPassword);

	/**
	 * 设置密码.
	 * 
	 * @param userId
	 * @param password
	 * @param modifyUser
	 * @return
	 */
	BooleanResult setPassword(String userId, String password, String modifyUser);

}
