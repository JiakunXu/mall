package com.kintiger.mall.user.dao;

import com.kintiger.mall.api.user.bo.User;

/**
 * user dao.
 * 
 * @author xujiakun
 * 
 */
public interface IUserDao {

	/**
	 * 
	 * @param user
	 * @return
	 */
	String createUser(User user);

	/**
	 * 
	 * @param passport
	 * @return
	 */
	User getUser4Validate(String passport);

	/**
	 * 
	 * @param userId
	 * @return
	 */
	User getUser(User user);

	/**
	 * 
	 * @param user
	 * @return
	 */
	int updateUser(User user);

	/**
	 * 
	 * @param user
	 * @return
	 */
	int updatePassword(User user);

}
