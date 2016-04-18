package com.kintiger.mall.shop.dao.impl;

import java.util.List;

import com.kintiger.mall.api.shop.bo.Shop;
import com.kintiger.mall.framework.dao.impl.BaseDaoImpl;
import com.kintiger.mall.shop.dao.IShopDao;

/**
 * 店铺 dao 实现.
 * 
 * @author xujiakun
 * 
 */
public class ShopDaoImpl extends BaseDaoImpl implements IShopDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Shop> getShopList(Shop shop) {
		return (List<Shop>) getSqlMapClientTemplate().queryForList("shop.getShopList", shop);
	}

	@Override
	public Shop getShop(Shop shop) {
		return (Shop) getSqlMapClientTemplate().queryForObject("shop.getShop", shop);
	}

}
