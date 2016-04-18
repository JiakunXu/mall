package com.kintiger.mall.trade.dao;

import java.util.List;

import com.kintiger.mall.api.trade.bo.Trade;

/**
 * trade 接口.
 * 
 * @author xujiakun
 * 
 */
public interface ITradeDao {

	/**
	 * 统计.
	 * 
	 * @param trade
	 * @return
	 */
	List<Trade> getTradeStats(Trade trade);

	/**
	 * 统计.
	 * 
	 * @param trade
	 * @return
	 */
	int getTradeRevenue(Trade trade);

	/**
	 * 查询某店铺交易.
	 * 
	 * @param trade
	 * @return
	 */
	int getTradeCount(Trade trade);

	/**
	 * 查询某店铺交易.
	 * 
	 * @param trade
	 * @return
	 */
	List<Trade> getTradeList(Trade trade);

	/**
	 * 
	 * @param trade
	 * @return
	 */
	int updateTrade(Trade trade);

	/**
	 * 
	 * @param trade
	 * @return
	 */
	String createTrade(Trade trade);

	/**
	 * 
	 * @param trade
	 * @return
	 */
	Trade getTrade(Trade trade);

}
