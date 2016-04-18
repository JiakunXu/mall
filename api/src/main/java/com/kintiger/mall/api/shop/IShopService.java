package com.kintiger.mall.api.shop;

import java.util.List;

import com.kintiger.mall.api.shop.bo.Shop;

/**
 * 店铺接口.
 * 
 * @author xujiakun
 * 
 */
public interface IShopService {

	/**
	 * 获取店铺信息 一家公司允许拥有多个店铺.
	 * 
	 * @param companyId
	 * @return
	 */
	List<Shop> getShopList(String[] companyId);

	/**
	 * 通过 shopId 获取信息.
	 * 
	 * @param shopId
	 * @return
	 */
	Shop getShop(String shopId);

	/**
	 * 通过别名查询店铺信息.
	 * 
	 * @param uuid
	 * @return
	 */
	Shop getShopByUUID(String uuid);

}
