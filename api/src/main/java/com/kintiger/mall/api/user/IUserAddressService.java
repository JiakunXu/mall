package com.kintiger.mall.api.user;

import com.kintiger.mall.api.user.bo.UserAddress;
import com.kintiger.mall.framework.bo.BooleanResult;

/**
 * 收货地址.
 * 
 * @author xujiakun
 * 
 */
public interface IUserAddressService {

	/**
	 * 获取用户默认收货地址.
	 * 
	 * @param userId
	 * @return
	 */
	UserAddress getDefaultUserAddress(String userId);

	/**
	 * 新增收货地址 设置默认收货地址 并 修改交易记录.
	 * 
	 * @param userId
	 * @param userAddress
	 * @param shopId
	 * @param tradeId
	 * @return
	 */
	BooleanResult createUserAddress(String userId, UserAddress userAddress, String shopId, String tradeId);

	/**
	 * 新增收货地址.
	 * 
	 * @param userId
	 * @param userAddress
	 * @return
	 */
	BooleanResult createUserAddress(String userId, UserAddress userAddress);

	/**
	 * remove 默认收货地址.
	 * 
	 * @param userId
	 * @return
	 */
	BooleanResult removeDefaultUserAddress(String userId);

}
