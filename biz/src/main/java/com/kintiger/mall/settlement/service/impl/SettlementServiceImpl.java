package com.kintiger.mall.settlement.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.kintiger.mall.api.settlement.ISettlementService;
import com.kintiger.mall.api.settlement.bo.Settlement;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;
import com.kintiger.mall.framework.util.LogUtil;
import com.kintiger.mall.settlement.dao.ISettlementDao;

/**
 * 结算.
 * 
 * @author xujiakun
 * 
 */
public class SettlementServiceImpl implements ISettlementService {

	private Logger4jExtend logger = Logger4jCollection.getLogger(SettlementServiceImpl.class);

	private ISettlementDao settlementDao;

	@Override
	public BigDecimal getSettlement(String shopId, String type, Settlement settlement) {
		if (StringUtils.isBlank(shopId) || StringUtils.isBlank(type) || settlement == null) {
			return BigDecimal.ZERO;
		}

		settlement.setShopId(shopId.trim());

		if (ISettlementService.TYPE_ALIPAY.equals(type)) {
			return getSettlement("settlement.getAlipaySettlement", settlement);
		} else if (ISettlementService.TYPE_WX.equals(type)) {
			BigDecimal totalFee = getSettlement("settlement.getWxSettlement", settlement);
			if (totalFee != null) {
				return totalFee.divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP);
			}
		}

		return BigDecimal.ZERO;
	}

	@Override
	public int getSettlementCount(String shopId, String type, Settlement settlement) {
		if (StringUtils.isBlank(shopId) || settlement == null) {
			return 0;
		}

		settlement.setShopId(shopId.trim());

		if (ISettlementService.TYPE_ALIPAY.equals(type)) {
			return getSettlementCount("settlement.getAlipaySettlementCount", settlement);
		} else if (ISettlementService.TYPE_WX.equals(type)) {
			return getSettlementCount("settlement.getWxSettlementCount", settlement);
		} else {
			return getSettlementCount("settlement.getSettlementCount", settlement);
		}
	}

	@Override
	public List<Settlement> getSettlementList(String shopId, String type, Settlement settlement) {
		if (StringUtils.isBlank(shopId) || settlement == null) {
			return null;
		}

		settlement.setShopId(shopId.trim());

		if (ISettlementService.TYPE_ALIPAY.equals(type)) {
			return getSettlementList("settlement.getAlipaySettlementList", settlement);
		} else if (ISettlementService.TYPE_WX.equals(type)) {
			return getSettlementList("settlement.getWxSettlementList", settlement);
		} else {
			return getSettlementList("settlement.getSettlementList", settlement);
		}
	}

	private BigDecimal getSettlement(String statementName, Settlement settlement) {
		try {
			return settlementDao.getSettlement(statementName, settlement);
		} catch (Exception e) {
			logger.error("statementName:" + statementName + LogUtil.parserBean(settlement), e);
		}

		return BigDecimal.ZERO;
	}

	private int getSettlementCount(String statementName, Settlement settlement) {
		try {
			return settlementDao.getSettlementCount(statementName, settlement);
		} catch (Exception e) {
			logger.error("statementName:" + statementName + LogUtil.parserBean(settlement), e);
		}

		return 0;
	}

	private List<Settlement> getSettlementList(String statementName, Settlement settlement) {
		try {
			return settlementDao.getSettlementList(statementName, settlement);
		} catch (Exception e) {
			logger.error("statementName:" + statementName + LogUtil.parserBean(settlement), e);
		}

		return null;
	}

	public ISettlementDao getSettlementDao() {
		return settlementDao;
	}

	public void setSettlementDao(ISettlementDao settlementDao) {
		this.settlementDao = settlementDao;
	}

}
