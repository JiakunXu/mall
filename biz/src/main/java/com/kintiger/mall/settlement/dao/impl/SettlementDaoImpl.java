package com.kintiger.mall.settlement.dao.impl;

import java.math.BigDecimal;
import java.util.List;

import com.kintiger.mall.api.settlement.bo.Settlement;
import com.kintiger.mall.framework.dao.impl.BaseDaoImpl;
import com.kintiger.mall.settlement.dao.ISettlementDao;

/**
 * 
 * @author xujiakun
 * 
 */
public class SettlementDaoImpl extends BaseDaoImpl implements ISettlementDao {

	@Override
	public BigDecimal getSettlement(String statementName, Settlement settlement) {
		return (BigDecimal) getSqlMapClientTemplate().queryForObject(statementName, settlement);
	}

	@Override
	public int getSettlementCount(String statementName, Settlement settlement) {
		return (Integer) getSqlMapClientTemplate().queryForObject(statementName, settlement);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Settlement> getSettlementList(String statementName, Settlement settlement) {
		return (List<Settlement>) getSqlMapClientTemplate().queryForList(statementName, settlement);
	}

}
