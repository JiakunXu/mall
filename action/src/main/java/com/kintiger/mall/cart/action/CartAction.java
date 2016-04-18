package com.kintiger.mall.cart.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.kintiger.mall.api.cart.ICartService;
import com.kintiger.mall.api.cart.bo.Cart;
import com.kintiger.mall.api.shop.bo.Shop;
import com.kintiger.mall.api.user.bo.User;
import com.kintiger.mall.framework.action.BaseAction;
import com.kintiger.mall.framework.bo.BooleanResult;
import com.kintiger.mall.framework.webwork.annotations.JsonResult;

/**
 * 购物车.
 * 
 * @author xujiakun
 * 
 */
public class CartAction extends BaseAction {

	private static final long serialVersionUID = 2476506425377535583L;

	private ICartService cartService;

	private List<Cart> cartList;

	private String itemId;

	private String skuId;

	private String quantity;

	/**
	 * ajax 返回.
	 */
	private String message;

	/**
	 * 删除购物车.
	 */
	private String[] cartId;

	/**
	 * 购物车.
	 * 
	 * @return
	 */
	public String index() {

		cartList = cartService.getCartList(getUser().getUserId(), getShop().getShopId());

		if (cartList == null) {
			cartList = new ArrayList<Cart>();
		}

		return SUCCESS;
	}

	/**
	 * 添加购物车(需要登录).
	 * 
	 * @return
	 */
	@JsonResult(field = "message")
	public String addCart() {
		this.getServletResponse().setStatus(500);

		// 验证用户是否登陆
		User user = this.getUser();
		if (user == null) {
			message = "login";
			return JSON_RESULT;
		}

		Shop shop = this.getShop();
		if (shop == null) {
			message = "店铺信息不存在！";
			return JSON_RESULT;
		}

		BooleanResult result = cartService.createCart(user.getUserId(), itemId, skuId, quantity);
		if (result.getResult()) {
			this.getServletResponse().setStatus(200);
			message = "购物车添加成功！";
		} else {
			message = result.getCode();
		}

		return JSON_RESULT;
	}

	/**
	 * 删除购物车(需要登录).
	 * 
	 * @return
	 */
	public String removeCart() {
		Shop shop = this.getShop();
		if (shop == null) {
			return NONE;
		}

		User user = this.getUser();

		BooleanResult result = cartService.removeCart(user.getUserId(), shop.getShopId(), cartId);
		if (result.getResult()) {
			this.setSuccessMessage("删除购物车成功！");
		} else {
			this.setFailMessage(result.getCode());
		}

		return RESULT_MESSAGE;
	}

	public ICartService getCartService() {
		return cartService;
	}

	public void setCartService(ICartService cartService) {
		this.cartService = cartService;
	}

	public List<Cart> getCartList() {
		return cartList;
	}

	public void setCartList(List<Cart> cartList) {
		this.cartList = cartList;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String[] getCartId() {
		return cartId != null ? Arrays.copyOf(cartId, cartId.length) : null;
	}

	public void setCartId(String[] cartId) {
		if (cartId != null) {
			this.cartId = Arrays.copyOf(cartId, cartId.length);
		}
	}

}
