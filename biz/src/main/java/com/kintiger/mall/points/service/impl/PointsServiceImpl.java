package com.kintiger.mall.points.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.kintiger.mall.api.cache.IMemcachedCacheService;
import com.kintiger.mall.api.cart.ICartService;
import com.kintiger.mall.api.item.IItemFileService;
import com.kintiger.mall.api.item.bo.ItemFile;
import com.kintiger.mall.api.member.IMemberPointsService;
import com.kintiger.mall.api.points.IPointsCardService;
import com.kintiger.mall.api.points.IPointsService;
import com.kintiger.mall.api.points.bo.Points;
import com.kintiger.mall.api.points.bo.PointsCard;
import com.kintiger.mall.framework.bo.BooleanResult;
import com.kintiger.mall.framework.exception.ServiceException;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;
import com.kintiger.mall.framework.util.LogUtil;
import com.kintiger.mall.points.dao.IPointsDao;

/**
 * 
 * @author xujiakun
 * 
 */
public class PointsServiceImpl implements IPointsService {

	private Logger4jExtend logger = Logger4jCollection.getLogger(PointsServiceImpl.class);

	private TransactionTemplate transactionTemplate;

	private IMemcachedCacheService memcachedCacheService;

	private IItemFileService itemFileService;

	private ICartService cartService;

	private IPointsCardService pointsCardService;

	private IMemberPointsService memberPointsService;

	private IPointsDao pointsDao;

	@Override
	public int getPointsCount(String shopId, Points points) {
		if (StringUtils.isBlank(shopId) || points == null) {
			return 0;
		}

		points.setShopId(shopId.trim());

		try {
			return pointsDao.getPointsCount(points);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(points), e);
		}

		return 0;
	}

	@Override
	public List<Points> getPointsList(String shopId, Points points) {
		if (StringUtils.isBlank(shopId) || points == null) {
			return null;
		}

		points.setShopId(shopId.trim());

		List<Points> pointsList = null;

		try {
			pointsList = pointsDao.getPointsList(points);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(points), e);
		}

		if (pointsList == null || pointsList.size() == 0) {
			return null;
		}

		String[] itemId = new String[pointsList.size()];
		int i = 0;
		for (Points po : pointsList) {
			itemId[i++] = po.getItemId();
		}

		// 2. 获取商品文件信息
		Map<String, List<ItemFile>> map = itemFileService.getItemFileList(shopId, itemId);

		// 不存在商品文件 直接返回
		if (map == null || map.isEmpty()) {
			return pointsList;
		}

		for (Points po : pointsList) {
			po.setItemFileList(map.get(po.getItemId()));
		}

		return pointsList;
	}

	@Override
	public BooleanResult createPoints(String shopId, String itemId, String points, String expireDate, String modifyUser) {
		return createPoints(shopId, itemId, "0", points, expireDate, modifyUser);
	}

	@Override
	public BooleanResult createPoints(String shopId, String itemId, String skuId, String points, String expireDate,
		String modifyUser) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		Points points4create = new Points();

		if (StringUtils.isBlank(shopId)) {
			result.setCode("店铺信息不能为空！");
			return result;
		}

		points4create.setShopId(shopId.trim());

		if (StringUtils.isBlank(itemId)) {
			result.setCode("商品信息不能为空！");
			return result;
		}

		points4create.setItemId(itemId.trim());

		if (StringUtils.isBlank(skuId)) {
			result.setCode("商品规格信息不能为空！");
			return result;
		}

		points4create.setSkuId(skuId.trim());

		if (StringUtils.isBlank(points)) {
			result.setCode("商品兑换所需积分信息不能为空！");
			return result;
		}

		try {
			BigDecimal p = new BigDecimal(points.trim());
			if (BigDecimal.ZERO.compareTo(p) != -1) {
				result.setCode("商品兑换所需积分必须大于0！");
				return result;
			}
			points4create.setPoints(p);
		} catch (Exception e) {
			logger.error(points, e);

			result.setCode("商品兑换所需积分格式不正确！");
			return result;
		}

		if (StringUtils.isBlank(expireDate)) {
			result.setCode("商品兑换活动到期时间不能为空！");
			return result;
		}

		points4create.setExpireDate(expireDate.trim());

		if (StringUtils.isEmpty(modifyUser)) {
			result.setCode("操作人信息不能为空！");
			return result;
		}

		points4create.setModifyUser(modifyUser);

		try {
			String pointsId = pointsDao.createPoints(points4create);
			result.setCode(pointsId);
			result.setResult(true);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(points4create), e);

			result.setCode("写入积分商品数据失败！");
		}

		return result;
	}

	@Override
	public BooleanResult deletePoints(String shopId, String pointsId, String modifyUser) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		Points points = new Points();

		if (StringUtils.isBlank(shopId)) {
			result.setCode("店铺信息不能为空！");
			return result;
		}

		points.setShopId(shopId.trim());

		if (StringUtils.isBlank(pointsId)) {
			result.setCode("积分商品信息不能为空！");
			return result;
		}

		points.setPointsId(pointsId.trim());

		if (StringUtils.isBlank(modifyUser)) {
			result.setCode("操作人不能为空！");
			return result;
		}

		points.setModifyUser(modifyUser);

		try {
			int i = pointsDao.deletePoints(points);
			if (i == 1) {
				result.setResult(true);
			} else {
				result.setCode("当前删除积分商品已被删除！");
			}
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(points), e);

			result.setCode("删除积分商品信息失败！");
		}

		return result;
	}

	@Override
	public BooleanResult exchangePoints(String userId, String itemId, String skuId, String pointsId, String quantity) {
		return cartService.createCart(userId, itemId, skuId, pointsId, quantity);
	}

	@Override
	public BooleanResult rechargePoints(final String userId, final String shopId, final String cardNo,
		final String password) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		if (StringUtils.isBlank(userId)) {
			result.setCode("用户信息不能为空！");
			return result;
		}

		if (StringUtils.isBlank(shopId)) {
			result.setCode("店铺信息不能为空！");
			return result;
		}

		if (StringUtils.isBlank(cardNo)) {
			result.setCode("积分卡号不能为空！");
			return result;
		}

		if (StringUtils.isBlank(password)) {
			result.setCode("积分卡密码不能为空！");
			return result;
		}

		// 1. 锁定当前积分卡
		try {
			memcachedCacheService.add(IMemcachedCacheService.CACHE_KEY_CARD_NO + cardNo.trim(), cardNo,
				IMemcachedCacheService.CACHE_KEY_CARD_NO_DEFAULT_EXP);
		} catch (ServiceException e) {
			result.setCode("当前积分卡已被锁定，请稍后再试！");
			return result;
		}

		// 2. 获取积分卡信息
		final PointsCard pointsCard = pointsCardService.getPointsCard(shopId.trim(), cardNo.trim(), password);
		if (pointsCard == null) {
			result.setCode("当前积分卡，卡号密码不正确或已被使用！");
			return result;
		}

		BooleanResult res = (BooleanResult) transactionTemplate.execute(new TransactionCallback() {
			public Object doInTransaction(TransactionStatus ts) {
				BooleanResult result = new BooleanResult();
				result.setResult(false);

				// 3. 更新当前积分卡状态
				result = pointsCardService.usePointsCard(shopId.trim(), cardNo.trim(), password, userId.trim(), userId);
				if (!result.getResult()) {
					ts.setRollbackOnly();

					return result;
				}

				// 4. 充值积分
				result =
					memberPointsService.recordMemberPoints(userId.trim(), shopId.trim(), pointsCard.getPoints(),
						IMemberPointsService.POINTS_SOURCE_POINTS_CARD);
				if (!result.getResult()) {
					ts.setRollbackOnly();

					return result;
				}

				return result;
			}
		});

		return res;
	}

	public TransactionTemplate getTransactionTemplate() {
		return transactionTemplate;
	}

	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}

	public IMemcachedCacheService getMemcachedCacheService() {
		return memcachedCacheService;
	}

	public void setMemcachedCacheService(IMemcachedCacheService memcachedCacheService) {
		this.memcachedCacheService = memcachedCacheService;
	}

	public IItemFileService getItemFileService() {
		return itemFileService;
	}

	public void setItemFileService(IItemFileService itemFileService) {
		this.itemFileService = itemFileService;
	}

	public ICartService getCartService() {
		return cartService;
	}

	public void setCartService(ICartService cartService) {
		this.cartService = cartService;
	}

	public IPointsCardService getPointsCardService() {
		return pointsCardService;
	}

	public void setPointsCardService(IPointsCardService pointsCardService) {
		this.pointsCardService = pointsCardService;
	}

	public IMemberPointsService getMemberPointsService() {
		return memberPointsService;
	}

	public void setMemberPointsService(IMemberPointsService memberPointsService) {
		this.memberPointsService = memberPointsService;
	}

	public IPointsDao getPointsDao() {
		return pointsDao;
	}

	public void setPointsDao(IPointsDao pointsDao) {
		this.pointsDao = pointsDao;
	}

}
