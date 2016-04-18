package com.kintiger.mall.user.dao.impl;

import com.kintiger.mall.api.user.bo.User;
import com.kintiger.mall.framework.dao.impl.BaseDaoImpl;
import com.kintiger.mall.user.dao.IUserDao;

/**
 * user dao.
 * 
 * @author xujiakun
 * 
 */
public class UserDaoImpl extends BaseDaoImpl implements IUserDao {

	@Override
	public String createUser(User user) {
		return (String) getSqlMapClientTemplate().insert("user.createUser", user);
	}

	@Override
	public User getUser4Validate(String passport) {
		return (User) getSqlMapClientTemplate().queryForObject("user.getUser4Validate", passport);
	}

	@Override
	public User getUser(User user) {
		return (User) getSqlMapClientTemplate().queryForObject("user.getUser", user);
	}

	@Override
	public int updateUser(User user) {
		return getSqlMapClientTemplate().update("user.updateUser", user);
	}

	@Override
	public int updatePassword(User user) {
		return getSqlMapClientTemplate().update("user.updatePassword", user);
	}

}
