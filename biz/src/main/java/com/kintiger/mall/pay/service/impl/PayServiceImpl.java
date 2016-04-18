package com.kintiger.mall.pay.service.impl;

import org.apache.commons.lang.StringUtils;

import com.kintiger.mall.api.pay.IPayService;
import com.kintiger.mall.api.pay.bo.AliCallback;
import com.kintiger.mall.api.pay.bo.AliNotify;
import com.kintiger.mall.api.pay.bo.WxNotify;
import com.kintiger.mall.framework.bo.BooleanResult;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;
import com.kintiger.mall.framework.util.LogUtil;
import com.kintiger.mall.pay.dao.IPayDao;

/**
 * 
 * @author xujiakun
 * 
 */
public class PayServiceImpl implements IPayService {

	private Logger4jExtend logger = Logger4jCollection.getLogger(PayServiceImpl.class);

	private IPayDao payDao;

	@Override
	public BooleanResult createAliCallback(AliCallback callback, String modifyUser) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		if (callback == null) {
			result.setCode("回调信息不能为空！");
			return result;
		}

		if (StringUtils.isBlank(modifyUser)) {
			result.setCode("操作人不能为空！");
			return result;
		}

		callback.setModifyUser(modifyUser);

		try {
			payDao.createAliCallback(callback);
			result.setResult(true);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(callback), e);
		}

		return result;
	}

	@Override
	public BooleanResult createAliNotify(AliNotify notify, String modifyUser) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		if (notify == null) {
			result.setCode("通知信息不能为空！");
			return result;
		}

		if (StringUtils.isBlank(modifyUser)) {
			result.setCode("操作人不能为空！");
			return result;
		}

		notify.setModifyUser(modifyUser);

		try {
			payDao.createAliNotify(notify);
			result.setResult(true);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(notify), e);
		}

		return result;
	}

	@Override
	public BooleanResult createWxNotify(WxNotify notify, String modifyUser) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		if (notify == null) {
			result.setCode("通知信息不能为空！");
			return result;
		}

		if (StringUtils.isBlank(modifyUser)) {
			result.setCode("操作人不能为空！");
			return result;
		}

		notify.setModifyUser(modifyUser);

		try {
			payDao.createWxNotify(notify);
			result.setResult(true);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(notify), e);
		}

		return result;
	}

	@Override
	public WxNotify getWxNotify(WxNotify notify) {
		if (notify == null) {
			return null;
		}

		try {
			return payDao.getWxNotify(notify);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(notify), e);
		}

		return null;
	}

	public IPayDao getPayDao() {
		return payDao;
	}

	public void setPayDao(IPayDao payDao) {
		this.payDao = payDao;
	}

}
