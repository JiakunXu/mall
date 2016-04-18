package com.kintiger.mall.trade.dao.impl;

import java.util.List;

import com.kintiger.mall.api.trade.bo.TradeExpress;
import com.kintiger.mall.framework.dao.impl.BaseDaoImpl;
import com.kintiger.mall.trade.dao.ITradeExpressDao;

/**
 * 
 * @author xujiakun
 * 
 */
public class TradeExpressDaoImpl extends BaseDaoImpl implements ITradeExpressDao {

	@Override
	public String createTradeExpress(TradeExpress tradeExpress) {
		return (String) getSqlMapClientTemplate().insert("trade.express.createTradeExpress", tradeExpress);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TradeExpress> getTradeExpressList(TradeExpress tradeExpress) {
		return (List<TradeExpress>) getSqlMapClientTemplate().queryForList("trade.express.getTradeExpressList",
			tradeExpress);
	}

}
