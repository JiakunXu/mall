package com.kintiger.mall.user.dao;

import com.kintiger.mall.api.user.bo.UserAddress;

/**
 * 收货地址.
 * 
 * @author xujiakun
 * 
 */
public interface IUserAddressDao {

	/**
	 * 
	 * @param userAddress
	 * @return
	 */
	String createUserAddress(UserAddress userAddress);

	/**
	 * 
	 * @param userAddress
	 * @return
	 */
	UserAddress getUserAddress(UserAddress userAddress);

	/**
	 * 
	 * @param userAddress
	 * @return
	 */
	int removeDefaultUserAddress(UserAddress userAddress);

}
