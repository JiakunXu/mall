package com.kintiger.mall.settlement.dao;

import java.math.BigDecimal;
import java.util.List;

import com.kintiger.mall.api.settlement.bo.Settlement;

/**
 * 
 * @author xujiakun
 * 
 */
public interface ISettlementDao {

	/**
	 * 
	 * @param statementName
	 * @param settlement
	 * @return
	 */
	BigDecimal getSettlement(String statementName, Settlement settlement);

	/**
	 * 
	 * @param statementName
	 * @param settlement
	 * @return
	 */
	int getSettlementCount(String statementName, Settlement settlement);

	/**
	 * 
	 * @param statementName
	 * @param settlement
	 * @return
	 */
	List<Settlement> getSettlementList(String statementName, Settlement settlement);

}
