package com.kintiger.mall.api.cart;

import java.util.List;

import com.kintiger.mall.api.cart.bo.Cart;
import com.kintiger.mall.framework.bo.BooleanResult;

/**
 * 购物车.
 * 
 * @author xujiakun
 * 
 */
public interface ICartService {

	String STATE_COMPLETE = "E";

	String STATE_REMOVE = "D";

	/**
	 * 获取用户购物车.
	 * 
	 * @param userId
	 * @param shopId
	 * @return
	 */
	List<Cart> getCartList(String userId, String shopId);

	/**
	 * 添加购物车.
	 * 
	 * @param userId
	 * @param itemId
	 * @param skuId
	 * @param quantity
	 * @return
	 */
	BooleanResult createCart(String userId, String itemId, String skuId, String quantity);

	/**
	 * 添加购物车.
	 * 
	 * @param userId
	 * @param itemId
	 * @param skuId
	 * @param quantity
	 * @return
	 */
	BooleanResult createCart(String userId, String itemId, String skuId, String pointsId, String quantity);

	/**
	 * 根据购物车完成订单.
	 * 
	 * @param userId
	 * @param shopId
	 * @param cartId
	 * @return
	 */
	BooleanResult completeCart(String userId, String shopId, String[] cartId);

	/**
	 * 移除购物车.
	 * 
	 * @param userId
	 * @param shopId
	 * @param cartId
	 * @return
	 */
	BooleanResult removeCart(String userId, String shopId, String[] cartId);

	/**
	 * 统计商品总金额 运费.
	 * 
	 * @param userId
	 * @param shopId
	 * @param cartId
	 * @return
	 */
	Cart getCartStats(String userId, String shopId, String[] cartId);

}
