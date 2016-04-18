package com.kintiger.mall.trade.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.kintiger.mall.api.trade.ITradeExpressService;
import com.kintiger.mall.api.trade.bo.TradeExpress;
import com.kintiger.mall.framework.bo.BooleanResult;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;
import com.kintiger.mall.framework.util.LogUtil;
import com.kintiger.mall.trade.dao.ITradeExpressDao;

/**
 * 交易物流.
 * 
 * @author xujiakun
 * 
 */
public class TradeExpressServiceImpl implements ITradeExpressService {

	private Logger4jExtend logger = Logger4jCollection.getLogger(TradeExpressServiceImpl.class);

	private ITradeExpressDao tradeExpressDao;

	@Override
	public BooleanResult createTradeExpress(String tradeId, String epsId, String epsNo, String modifyUser) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		TradeExpress te = new TradeExpress();

		if (StringUtils.isBlank(tradeId)) {
			result.setCode("交易信息不能为空！");
			return result;
		}

		te.setTradeId(tradeId.trim());

		if (StringUtils.isBlank(epsId)) {
			result.setCode("物流公司信息不能为空！");
			return result;
		}

		te.setEpsId(epsId.trim());

		if (StringUtils.isBlank(epsNo)) {
			result.setCode("物流单号信息不能为空！");
			return result;
		}

		te.setEpsNo(epsNo.trim());

		if (StringUtils.isBlank(modifyUser)) {
			result.setCode("操作人信息不能为空！");
			return result;
		}

		te.setModifyUser(modifyUser.trim());

		try {
			tradeExpressDao.createTradeExpress(te);

			result.setCode("操作成功！");
			result.setResult(true);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(te), e);
		}

		return result;
	}

	@Override
	public List<TradeExpress> getTradeExpressList(String tradeId) {
		if (StringUtils.isBlank(tradeId)) {
			return null;
		}

		TradeExpress tradeExpress = new TradeExpress();
		tradeExpress.setTradeId(tradeId.trim());

		try {
			return tradeExpressDao.getTradeExpressList(tradeExpress);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(tradeExpress), e);
		}

		return null;
	}

	public ITradeExpressDao getTradeExpressDao() {
		return tradeExpressDao;
	}

	public void setTradeExpressDao(ITradeExpressDao tradeExpressDao) {
		this.tradeExpressDao = tradeExpressDao;
	}

}
