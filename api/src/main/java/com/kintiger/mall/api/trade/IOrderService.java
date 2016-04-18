package com.kintiger.mall.api.trade;

import java.util.List;

import com.kintiger.mall.api.trade.bo.Order;
import com.kintiger.mall.framework.bo.BooleanResult;

/**
 * 订单详细信息接口.
 * 
 * @author xujiakun
 * 
 */
public interface IOrderService {

	/**
	 * 根据购物车批量创建订单明细信息.
	 * 
	 * @param tradeId
	 *            交易号.
	 * @param shopId
	 * @param cartId
	 * @param modifyUser
	 * @return
	 */
	BooleanResult createOrder(String tradeId, String shopId, String[] cartId, String modifyUser);

	/**
	 * 买家查询(首先调用 ITradeService.getTrade).
	 * 
	 * @param tradeId
	 * @param shopId
	 * @return
	 */
	List<Order> getOrderList(String tradeId, String shopId);

}
