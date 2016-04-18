package com.kintiger.mall.cart.dao.impl;

import java.util.List;

import com.kintiger.mall.api.cart.bo.Cart;
import com.kintiger.mall.cart.dao.ICartDao;
import com.kintiger.mall.framework.dao.impl.BaseDaoImpl;

/**
 * 购物车.
 * 
 * @author xujiakun
 * 
 */
public class CartDaoImpl extends BaseDaoImpl implements ICartDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Cart> getCartList(Cart cart) {
		return (List<Cart>) getSqlMapClientTemplate().queryForList("cart.getCartList", cart);
	}

	@Override
	public String createCart(Cart cart) {
		return (String) getSqlMapClientTemplate().insert("cart.createCart", cart);
	}

	@Override
	public int updateCart(Cart cart) {
		return getSqlMapClientTemplate().update("cart.updateCart", cart);
	}

	@Override
	public Cart getCartStats(Cart cart) {
		return (Cart) getSqlMapClientTemplate().queryForObject("cart.getCartStats", cart);
	}

}
