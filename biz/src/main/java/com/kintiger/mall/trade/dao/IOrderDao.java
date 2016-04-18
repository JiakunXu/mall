package com.kintiger.mall.trade.dao;

import java.util.List;

import com.kintiger.mall.api.trade.bo.Order;

/**
 * 订单具体信息.
 * 
 * @author xujiakun
 * 
 */
public interface IOrderDao {

	/**
	 * 
	 * @param order
	 * @return
	 */
	int createOrder(Order order);

	/**
	 * 
	 * @param order
	 * @return
	 */
	List<Order> getOrderList(Order order);

}
