package com.kintiger.mall.trade.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.kintiger.mall.api.trade.IOrderService;
import com.kintiger.mall.api.trade.bo.Order;
import com.kintiger.mall.framework.bo.BooleanResult;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;
import com.kintiger.mall.framework.util.LogUtil;
import com.kintiger.mall.trade.dao.IOrderDao;

/**
 * 
 * @author xujiakun
 * 
 */
public class OrderServiceImpl implements IOrderService {

	private Logger4jExtend logger = Logger4jCollection.getLogger(OrderServiceImpl.class);

	private IOrderDao orderDao;

	@Override
	public BooleanResult createOrder(String tradeId, String shopId, String[] cartId, String modifyUser) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		Order order = new Order();

		if (StringUtils.isBlank(tradeId)) {
			result.setCode("交易信息不能为空！");
			return result;
		}

		order.setTradeId(tradeId.trim());

		if (StringUtils.isBlank(shopId)) {
			result.setCode("店铺信息不能为空！");
			return result;
		}

		order.setShopId(shopId.trim());

		if (cartId == null || cartId.length == 0) {
			result.setCode("购物车不能为空！");
			return result;
		}

		order.setCodes(cartId);

		if (StringUtils.isBlank(modifyUser)) {
			result.setCode("操作人信息不能为空！");
			return result;
		}

		order.setModifyUser(modifyUser.trim());

		try {
			orderDao.createOrder(order);
			result.setResult(true);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(order), e);

			result.setCode("创建订单失败！");
		}

		return result;
	}

	@Override
	public List<Order> getOrderList(String tradeId, String shopId) {
		if (StringUtils.isBlank(tradeId) || StringUtils.isBlank(shopId)) {
			return null;
		}

		Order order = new Order();
		order.setTradeId(tradeId.trim());
		order.setShopId(shopId.trim());

		try {
			return orderDao.getOrderList(order);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(order), e);
		}

		return null;
	}

	public IOrderDao getOrderDao() {
		return orderDao;
	}

	public void setOrderDao(IOrderDao orderDao) {
		this.orderDao = orderDao;
	}

}
