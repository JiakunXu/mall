package com.kintiger.mall.cart.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.kintiger.mall.api.cart.ICartService;
import com.kintiger.mall.api.cart.bo.Cart;
import com.kintiger.mall.api.item.IItemFileService;
import com.kintiger.mall.api.item.bo.ItemFile;
import com.kintiger.mall.cart.dao.ICartDao;
import com.kintiger.mall.framework.bo.BooleanResult;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;
import com.kintiger.mall.framework.util.LogUtil;

/**
 * 购物车.
 * 
 * @author xujiakun
 * 
 */
public class CartServiceImpl implements ICartService {

	private Logger4jExtend logger = Logger4jCollection.getLogger(CartServiceImpl.class);

	private IItemFileService itemFileService;

	private ICartDao cartDao;

	@Override
	public List<Cart> getCartList(String userId, String shopId) {
		// userId 必填
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(shopId)) {
			return null;
		}

		Cart cart = new Cart();
		cart.setUserId(userId.trim());
		cart.setShopId(shopId.trim());

		List<Cart> cartList = null;

		try {
			cartList = cartDao.getCartList(cart);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(cart), e);
		}

		if (cartList == null || cartList.size() == 0) {
			return null;
		}

		String[] itemId = new String[cartList.size()];
		int i = 0;
		for (Cart ca : cartList) {
			itemId[i++] = ca.getItemId();
		}

		// 2. 获取商品文件信息
		Map<String, List<ItemFile>> map = itemFileService.getItemFileList(shopId, itemId);

		// 不存在商品文件 直接返回
		if (map == null || map.isEmpty()) {
			return cartList;
		}

		for (Cart ca : cartList) {
			ca.setItemFileList(map.get(ca.getItemId()));
		}

		return cartList;
	}

	@Override
	public BooleanResult createCart(String userId, String itemId, String skuId, String quantity) {
		return createCart(userId, itemId, skuId, "0", quantity);
	}

	@Override
	public BooleanResult createCart(String userId, String itemId, String skuId, String pointsId, String quantity) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		Cart cart = new Cart();

		if (StringUtils.isBlank(userId)) {
			result.setCode("用户信息不能为空！");
			return result;
		}

		cart.setUserId(userId.trim());

		if (StringUtils.isBlank(itemId)) {
			result.setCode("商品信息不能为空！");
			return result;
		}

		cart.setItemId(itemId.trim());
		cart.setSkuId(skuId);
		cart.setPointsId(pointsId);

		if (StringUtils.isBlank(quantity)) {
			result.setCode("购买商品数量不能为空！");
			return result;
		}

		int q;

		try {
			q = Integer.parseInt(quantity);
		} catch (Exception e) {
			logger.error("quantity:" + quantity, e);
			result.setCode("购买商品数量非数字类型！");
			return result;
		}

		if (q == 0 || q < 1) {
			result.setCode("数量不能为0或负！");
			return result;
		}

		cart.setQuantity(q);

		try {
			cartDao.createCart(cart);
			result.setResult(true);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(cart), e);

			result.setCode("添加购物车失败，请稍后再试！");
		}

		return result;
	}

	@Override
	public BooleanResult completeCart(String userId, String shopId, String[] cartId) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		Cart cart = new Cart();

		if (StringUtils.isBlank(userId)) {
			result.setCode("用户信息不能为空！");
			return result;
		}

		cart.setUserId(userId.trim());

		if (StringUtils.isBlank(shopId)) {
			result.setCode("店铺信息不能为空！");
			return result;
		}

		cart.setShopId(shopId.trim());

		if (cartId == null || cartId.length == 0) {
			result.setCode("购物车商品信息不能为空！");
			return result;
		}

		cart.setCodes(cartId);

		cart.setState(ICartService.STATE_COMPLETE);
		int n = updateCart(cart);
		if (n == -1) {
			result.setCode("购物车更新失败！");
			return result;
		}

		result.setResult(true);
		return result;
	}

	@Override
	public BooleanResult removeCart(String userId, String shopId, String[] cartId) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		Cart cart = new Cart();

		if (StringUtils.isBlank(userId)) {
			result.setCode("用户信息不能为空！");
			return result;
		}

		cart.setUserId(userId.trim());

		if (StringUtils.isBlank(shopId)) {
			result.setCode("店铺信息不能为空！");
			return result;
		}

		cart.setShopId(shopId.trim());

		if (cartId == null || cartId.length == 0) {
			result.setCode("购物车商品信息不能为空！");
			return result;
		}

		cart.setCodes(cartId);

		cart.setState(ICartService.STATE_REMOVE);
		int n = updateCart(cart);
		if (n == -1) {
			result.setCode("购物车更新失败！");
			return result;
		}

		result.setResult(true);
		return result;
	}

	private int updateCart(Cart cart) {
		try {
			return cartDao.updateCart(cart);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(cart), e);
		}

		return -1;
	}

	@Override
	public Cart getCartStats(String userId, String shopId, String[] cartId) {
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(shopId) || cartId == null || cartId.length == 0) {
			return new Cart();
		}

		Cart cart = new Cart();
		cart.setUserId(userId.trim());
		cart.setShopId(shopId.trim());
		cart.setCodes(cartId);

		try {
			return cartDao.getCartStats(cart);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(cart), e);
		}

		return cart;
	}

	public IItemFileService getItemFileService() {
		return itemFileService;
	}

	public void setItemFileService(IItemFileService itemFileService) {
		this.itemFileService = itemFileService;
	}

	public ICartDao getCartDao() {
		return cartDao;
	}

	public void setCartDao(ICartDao cartDao) {
		this.cartDao = cartDao;
	}

}
