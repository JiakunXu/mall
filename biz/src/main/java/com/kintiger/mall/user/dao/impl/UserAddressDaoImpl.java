package com.kintiger.mall.user.dao.impl;

import com.kintiger.mall.api.user.bo.UserAddress;
import com.kintiger.mall.framework.dao.impl.BaseDaoImpl;
import com.kintiger.mall.user.dao.IUserAddressDao;

/**
 * 
 * @author xujiakun
 * 
 */
public class UserAddressDaoImpl extends BaseDaoImpl implements IUserAddressDao {

	@Override
	public String createUserAddress(UserAddress userAddress) {
		return (String) getSqlMapClientTemplate().insert("user.address.createUserAddress", userAddress);
	}

	@Override
	public UserAddress getUserAddress(UserAddress userAddress) {
		return (UserAddress) getSqlMapClientTemplate().queryForObject("user.address.getUserAddress", userAddress);
	}

	@Override
	public int removeDefaultUserAddress(UserAddress userAddress) {
		return getSqlMapClientTemplate().update("user.address.removeDefaultUserAddress", userAddress);
	}

}
