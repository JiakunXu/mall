package com.kintiger.mall.points.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.kintiger.mall.api.points.IPointsCardService;
import com.kintiger.mall.api.points.bo.PointsCard;
import com.kintiger.mall.framework.bo.BooleanResult;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;
import com.kintiger.mall.framework.util.LogUtil;
import com.kintiger.mall.points.dao.IPointsCardDao;

/**
 * 积分卡.
 * 
 * @author xujiakun
 * 
 */
public class PointsCardServiceImpl implements IPointsCardService {

	private Logger4jExtend logger = Logger4jCollection.getLogger(PointsCardServiceImpl.class);

	private IPointsCardDao pointsCardDao;

	// >>>>>>>>>>以下是卖家<<<<<<<<<<

	@Override
	public int getPointsCardCount(String shopId, PointsCard pointsCard) {
		if (StringUtils.isBlank(shopId) || pointsCard == null) {
			return 0;
		}

		pointsCard.setShopId(shopId.trim());

		try {
			return pointsCardDao.getPointsCardCount(pointsCard);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(pointsCard), e);
		}

		return 0;
	}

	@Override
	public List<PointsCard> getPointsCardList(String shopId, PointsCard pointsCard) {
		if (StringUtils.isBlank(shopId) || pointsCard == null) {
			return null;
		}

		pointsCard.setShopId(shopId.trim());

		try {
			return pointsCardDao.getPointsCardList(pointsCard);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(pointsCard), e);
		}

		return null;
	}

	@Override
	public List<PointsCard> getPointsCardStats(String shopId, String gmtStart, String gmtEnd) {
		if (StringUtils.isBlank(shopId) || StringUtils.isBlank(gmtStart) || StringUtils.isBlank(gmtEnd)) {
			return null;
		}

		PointsCard pointsCard = new PointsCard();
		pointsCard.setGmtStart(gmtStart.trim());
		pointsCard.setGmtEnd(gmtEnd.trim());

		pointsCard.setIsUsed("Y");
		pointsCard.setStart(0);
		pointsCard.setLimit(5);
		pointsCard.setSort("modifyDate");
		pointsCard.setDir("desc");

		return getPointsCardList(shopId, pointsCard);
	}

	private BooleanResult validate(PointsCard pointsCard) {
		BooleanResult result = new BooleanResult();
		result.setResult(true);

		if (pointsCard == null) {
			return result;
		}

		if (pointsCard.getPoints().compareTo(BigDecimal.ZERO) != 1) {
			result.setResult(false);
			result.setCode("积分卡积分不能小于0！");
			return result;
		}

		if (StringUtils.isBlank(pointsCard.getExpireDate())) {
			result.setResult(false);
			result.setCode("积分卡过期日期不能为空！");
			return result;
		}

		return result;
	}

	@Override
	public BooleanResult createPointsCard(String shopId, PointsCard pointsCard, String quantity, String modifyUser) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		if (pointsCard == null) {
			result.setCode("积分卡信息不能为空！");
			return result;
		}

		if (StringUtils.isBlank(shopId)) {
			result.setCode("店铺信息不能为空！");
			return result;
		}

		pointsCard.setShopId(shopId.trim());

		if (StringUtils.isBlank(quantity)) {
			result.setCode("积分卡数量不能为空！");
			return result;
		}

		int q = 0;
		try {
			q = Integer.parseInt(quantity);
		} catch (NumberFormatException e) {
			logger.error(quantity, e);

			result.setCode("积分卡数量输入不正确！");
			return result;
		}

		if (StringUtils.isBlank(modifyUser)) {
			result.setCode("操作人信息不能为空！");
			return result;
		}

		pointsCard.setModifyUser(modifyUser);

		// 验证参数
		result = validate(pointsCard);
		if (!result.getResult()) {
			return result;
		}

		// 重置 false
		result.setResult(false);

		// 批量创建积分卡
		try {
			int c = pointsCardDao.createPointsCard(pointsCard, q, modifyUser);
			result.setCode(String.valueOf(c));
			result.setResult(true);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(pointsCard) + "q:" + q + "modifyUser:" + modifyUser, e);

			result.setCode("写入积分卡数据失败！");
		}

		return result;
	}

	// >>>>>>>>>>以下是买家<<<<<<<<<<

	@Override
	public PointsCard getPointsCard(String shopId, String cardNo, String password) {
		if (StringUtils.isBlank(shopId) || StringUtils.isBlank(cardNo) || StringUtils.isBlank(password)) {
			return null;
		}

		PointsCard pointsCard = new PointsCard();
		pointsCard.setShopId(shopId.trim());
		pointsCard.setCardNo(cardNo.trim());
		pointsCard.setPassword(password.trim());

		try {
			return pointsCardDao.getPointsCard(pointsCard);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(pointsCard), e);
		}

		return null;
	}

	@Override
	public BooleanResult usePointsCard(String shopId, String cardNo, String password, String userId, String modifyUser) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		PointsCard pointsCard = new PointsCard();

		if (StringUtils.isBlank(shopId)) {
			result.setCode("店铺信息不能为空！");
			return result;
		}

		pointsCard.setShopId(shopId.trim());

		if (StringUtils.isBlank(cardNo)) {
			result.setCode("积分卡号不能为空！");
			return result;
		}

		pointsCard.setCardNo(cardNo.trim());

		if (StringUtils.isBlank(password)) {
			result.setCode("积分卡密码不能为空！");
			return result;
		}

		pointsCard.setPassword(password.trim());

		if (StringUtils.isBlank(userId)) {
			result.setCode("充值用户信息不能为空！");
			return result;
		}

		pointsCard.setUserId(userId.trim());

		if (StringUtils.isBlank(modifyUser)) {
			result.setCode("操作人信息不能为空！");
			return result;
		}

		pointsCard.setModifyUser(modifyUser);

		// 1. 修改商品信息
		try {
			int c = pointsCardDao.updatePointsCard("points.card.usePointsCard", pointsCard);
			if (c == 1) {
				result.setResult(true);
			}
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(pointsCard), e);

			result.setCode("积分卡充值失败！");
		}

		return result;
	}

	public IPointsCardDao getPointsCardDao() {
		return pointsCardDao;
	}

	public void setPointsCardDao(IPointsCardDao pointsCardDao) {
		this.pointsCardDao = pointsCardDao;
	}

}
