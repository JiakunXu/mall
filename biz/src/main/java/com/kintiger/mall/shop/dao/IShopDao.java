package com.kintiger.mall.shop.dao;

import java.util.List;

import com.kintiger.mall.api.shop.bo.Shop;

/**
 * 店铺 dao 接口.
 * 
 * @author xujiakun
 * 
 */
public interface IShopDao {

	/**
	 * 
	 * @param shop
	 * @return
	 */
	List<Shop> getShopList(Shop shop);

	/**
	 * 
	 * @param shop
	 * @return
	 */
	Shop getShop(Shop shop);

}
