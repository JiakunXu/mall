package com.kintiger.mall.trade.dao.impl;

import java.util.List;

import com.kintiger.mall.api.trade.bo.Order;
import com.kintiger.mall.framework.dao.impl.BaseDaoImpl;
import com.kintiger.mall.trade.dao.IOrderDao;

/**
 * 订单明细.
 * 
 * @author xujiakun
 * 
 */
public class OrderDaoImpl extends BaseDaoImpl implements IOrderDao {

	@Override
	public int createOrder(Order order) {
		return getSqlMapClientTemplate().update("order.createOrder", order);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Order> getOrderList(Order order) {
		return (List<Order>) getSqlMapClientTemplate().queryForList("order.getOrderList", order);
	}

}
