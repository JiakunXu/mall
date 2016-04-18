package com.kintiger.mall.api.settlement;

import java.math.BigDecimal;
import java.util.List;

import com.kintiger.mall.api.settlement.bo.Settlement;

/**
 * 结算.
 * 
 * @author xujiakun
 * 
 */
public interface ISettlementService {

	String TYPE_ALIPAY = "alipay";

	String TYPE_WX = "wx";

	/**
	 * 结算金额合计.
	 * 
	 * @param shopId
	 * @param type
	 * @param settlement
	 * @return
	 */
	BigDecimal getSettlement(String shopId, String type, Settlement settlement);

	/**
	 * 查询结算明细.
	 * 
	 * @param shopId
	 * @param type
	 *            结算方式(alipay weixin).
	 * @param settlement
	 * @return
	 */
	int getSettlementCount(String shopId, String type, Settlement settlement);

	/**
	 * 查询结算明细.
	 * 
	 * @param shopId
	 * @param type
	 *            结算方式(alipay weixin).
	 * @param settlement
	 * @return
	 */
	List<Settlement> getSettlementList(String shopId, String type, Settlement settlement);

}
