package com.kintiger.mall.api.trade;

import java.util.List;

import com.kintiger.mall.api.trade.bo.TradeExpress;
import com.kintiger.mall.framework.bo.BooleanResult;

/**
 * 交易物流.
 * 
 * @author xujiakun
 * 
 */
public interface ITradeExpressService {

	/**
	 * 创建交易物流信息.
	 * 
	 * @param tradeId
	 *            交易编号.
	 * @param epsId
	 *            物流公司编号.
	 * @param epsNo
	 *            物流单号.
	 * @param modifyUser
	 * @return
	 */
	BooleanResult createTradeExpress(String tradeId, String epsId, String epsNo, String modifyUser);

	/**
	 * 查询交易物流信息.
	 * 
	 * @param tradeId
	 * @return
	 */
	List<TradeExpress> getTradeExpressList(String tradeId);

}
