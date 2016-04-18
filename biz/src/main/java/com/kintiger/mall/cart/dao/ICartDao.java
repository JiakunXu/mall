package com.kintiger.mall.cart.dao;

import java.util.List;

import com.kintiger.mall.api.cart.bo.Cart;

/**
 * 购物车.
 * 
 * @author xujiakun
 * 
 */
public interface ICartDao {

	/**
	 * 
	 * @param cart
	 * @return
	 */
	List<Cart> getCartList(Cart cart);

	/**
	 * 
	 * @param cart
	 * @return
	 */
	String createCart(Cart cart);

	/**
	 * 
	 * @param cart
	 * @return
	 */
	int updateCart(Cart cart);

	/**
	 * 
	 * @param cartId
	 * @return
	 */
	Cart getCartStats(Cart cart);

}
