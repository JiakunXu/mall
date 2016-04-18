package com.kintiger.mall.user.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.kintiger.mall.api.trade.ITradeService;
import com.kintiger.mall.api.trade.bo.Trade;
import com.kintiger.mall.api.user.IUserAddressService;
import com.kintiger.mall.api.user.bo.UserAddress;
import com.kintiger.mall.framework.bo.BooleanResult;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;
import com.kintiger.mall.framework.util.LogUtil;
import com.kintiger.mall.user.dao.IUserAddressDao;

/**
 * 
 * @author xujiakun
 * 
 */
public class UserAddressServiceImpl implements IUserAddressService {

	private Logger4jExtend logger = Logger4jCollection.getLogger(UserAddressServiceImpl.class);

	private TransactionTemplate transactionTemplate;

	private ITradeService tradeService;

	private IUserAddressDao userAddressDao;

	@Override
	public UserAddress getDefaultUserAddress(String userId) {
		if (StringUtils.isBlank(userId)) {
			return null;
		}

		UserAddress userAddress = new UserAddress();
		userAddress.setUserId(userId.trim());
		userAddress.setDefaults("Y");

		try {
			return userAddressDao.getUserAddress(userAddress);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(userAddress), e);
		}

		return null;
	}

	@Override
	public BooleanResult createUserAddress(final String userId, final UserAddress userAddress, final String shopId,
		final String tradeId) {
		BooleanResult res = (BooleanResult) transactionTemplate.execute(new TransactionCallback() {
			public Object doInTransaction(TransactionStatus ts) {
				// remove 默认收货地址
				BooleanResult result = removeDefaultUserAddress(userId);
				if (!result.getResult()) {
					ts.setRollbackOnly();

					return result;
				}

				// 创建收货地址
				result = createUserAddress(userId, userAddress);
				if (!result.getResult()) {
					ts.setRollbackOnly();

					return result;
				}

				// 修改交易收货地址
				Trade trade = new Trade();
				trade.setReceiverName(userAddress.getContactName());
				trade.setReceiverProvince(userAddress.getProvince());
				trade.setReceiverCity(userAddress.getCity());
				trade.setReceiverArea(userAddress.getArea());
				trade.setReceiverBackCode(userAddress.getBackCode());
				trade.setReceiverAddress(userAddress.getAddress());
				trade.setReceiverZip(userAddress.getPostalCode());
				trade.setReceiverTel(userAddress.getTel());

				result = tradeService.updateReceiver(userId, shopId, tradeId, trade);
				if (!result.getResult()) {
					ts.setRollbackOnly();

					return result;
				}

				return result;
			}
		});

		return res;
	}

	@Override
	public BooleanResult createUserAddress(String userId, UserAddress userAddress) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		if (StringUtils.isBlank(userId)) {
			result.setCode("用户信息不能为空！");
			return result;
		}

		if (userAddress == null) {
			result.setCode("收货地址信息不能为空！");
			return result;
		}

		if (StringUtils.isBlank(userAddress.getContactName()) || userAddress.getContactName().length() > 10) {
			result.setCode("收货人信息不能为空或过长！");
			return result;
		}

		if (StringUtils.isBlank(userAddress.getProvince())) {
			result.setCode("省不能为空！");
			return result;
		}

		if (StringUtils.isBlank(userAddress.getCity())) {
			result.setCode("市不能为空！");
			return result;
		}

		if (StringUtils.isBlank(userAddress.getArea())) {
			result.setCode("区不能为空！");
			return result;
		}

		if (StringUtils.isBlank(userAddress.getAddress()) || userAddress.getAddress().length() > 50) {
			result.setCode("详细地址信息不能为空或过长！");
			return result;
		}

		if (StringUtils.isBlank(userAddress.getPostalCode()) || userAddress.getPostalCode().length() > 10) {
			result.setCode("邮政编码信息不能为空或过长！");
			return result;
		}

		if (StringUtils.isBlank(userAddress.getTel()) || userAddress.getTel().length() > 25) {
			result.setCode("联系电话信息不能为空或过长！");
			return result;
		}

		userAddress.setUserId(userId.trim());

		try {
			String addId = userAddressDao.createUserAddress(userAddress);
			result.setCode(addId);
			result.setResult(true);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(userAddress), e);

			result.setCode("收货地址信息创建失败，请稍后再试！");
		}

		return result;
	}

	@Override
	public BooleanResult removeDefaultUserAddress(String userId) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		UserAddress userAddress = new UserAddress();

		if (StringUtils.isBlank(userId)) {
			result.setCode("用户信息不能为空！");
			return result;
		}

		userAddress.setUserId(userId.trim());

		try {
			userAddressDao.removeDefaultUserAddress(userAddress);
			result.setResult(true);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(userAddress), e);

			result.setCode("默认收货地址信息设置失败，请稍后再试！");
		}

		return result;
	}

	public TransactionTemplate getTransactionTemplate() {
		return transactionTemplate;
	}

	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}

	public ITradeService getTradeService() {
		return tradeService;
	}

	public void setTradeService(ITradeService tradeService) {
		this.tradeService = tradeService;
	}

	public IUserAddressDao getUserAddressDao() {
		return userAddressDao;
	}

	public void setUserAddressDao(IUserAddressDao userAddressDao) {
		this.userAddressDao = userAddressDao;
	}

}
