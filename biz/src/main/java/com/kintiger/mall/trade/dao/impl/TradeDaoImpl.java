package com.kintiger.mall.trade.dao.impl;

import java.util.List;

import com.kintiger.mall.api.trade.bo.Trade;
import com.kintiger.mall.framework.dao.impl.BaseDaoImpl;
import com.kintiger.mall.trade.dao.ITradeDao;

/**
 * trade 实现.
 * 
 * @author xujiakun
 * 
 */
public class TradeDaoImpl extends BaseDaoImpl implements ITradeDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Trade> getTradeStats(Trade trade) {
		return (List<Trade>) getSqlMapClientTemplate().queryForList("trade.getTradeStats", trade);
	}

	@Override
	public int getTradeRevenue(Trade trade) {
		return (Integer) getSqlMapClientTemplate().queryForObject("trade.getTradeRevenue", trade);
	}

	@Override
	public int getTradeCount(Trade trade) {
		return (Integer) getSqlMapClientTemplate().queryForObject("trade.getTradeCount", trade);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Trade> getTradeList(Trade trade) {
		return (List<Trade>) getSqlMapClientTemplate().queryForList("trade.getTradeList", trade);
	}

	@Override
	public int updateTrade(Trade trade) {
		return getSqlMapClientTemplate().update("trade.updateTrade", trade);
	}

	@Override
	public String createTrade(Trade trade) {
		return (String) getSqlMapClientTemplate().insert("trade.createTrade", trade);
	}

	@Override
	public Trade getTrade(Trade trade) {
		return (Trade) getSqlMapClientTemplate().queryForObject("trade.getTrade", trade);
	}

}
