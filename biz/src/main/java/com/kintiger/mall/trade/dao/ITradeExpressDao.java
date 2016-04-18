package com.kintiger.mall.trade.dao;

import java.util.List;

import com.kintiger.mall.api.trade.bo.TradeExpress;

/**
 * 
 * @author xujiakun
 * 
 */
public interface ITradeExpressDao {

	/**
	 * 
	 * @param tradeExpress
	 * @return
	 */
	String createTradeExpress(TradeExpress tradeExpress);

	/**
	 * 
	 * @param tradeExpress
	 * @return
	 */
	List<TradeExpress> getTradeExpressList(TradeExpress tradeExpress);

}
