package com.kintiger.mall.advert.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.kintiger.mall.advert.dao.IAdvertPointsDao;
import com.kintiger.mall.api.advert.IAdvertPointsService;
import com.kintiger.mall.api.advert.IAdvertService;
import com.kintiger.mall.api.advert.bo.Advert;
import com.kintiger.mall.api.advert.bo.AdvertPoints;
import com.kintiger.mall.api.advert.bo.AdvertStats;
import com.kintiger.mall.api.member.IMemberPointsService;
import com.kintiger.mall.framework.bo.BooleanResult;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;
import com.kintiger.mall.framework.util.DateUtil;
import com.kintiger.mall.framework.util.LogUtil;

/**
 * 广告点击赠送积分.
 * 
 * @author xujiakun
 * 
 */
public class AdvertPointsServiceImpl implements IAdvertPointsService {

	private Logger4jExtend logger = Logger4jCollection.getLogger(AdvertPointsServiceImpl.class);

	private TransactionTemplate transactionTemplate;

	private IAdvertService advertService;

	private IMemberPointsService memberPointsService;

	private IAdvertPointsDao advertPointsDao;

	@Override
	public BooleanResult createAdvertPoints(final List<AdvertStats> advertStatsList, final String modifyUser) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		if (advertStatsList == null || advertStatsList.size() == 0) {
			result.setCode("广告点击信息不能为空！");
			return result;
		}

		BooleanResult res = (BooleanResult) transactionTemplate.execute(new TransactionCallback() {
			public Object doInTransaction(TransactionStatus ts) {
				BooleanResult result = new BooleanResult();
				result.setResult(false);

				// key: advertId | value: advert
				Map<String, Advert> map1 = new HashMap<String, Advert>();

				// key: userId ＋ '&' ＋ advertId | value: pointsDate
				Map<String, Date> map2 = new HashMap<String, Date>();

				// key: userId ＋ '&' ＋ advertId | value: advertPoints.id 用于统一标记 isLatest
				Map<String, String> map3 = new HashMap<String, String>();

				// 遍历 advertStatsList
				for (AdvertStats advertStats : advertStatsList) {
					String userId = advertStats.getUserId();
					String advertId = advertStats.getAdvertId();

					if (!map1.containsKey(advertId)) {
						Advert advert = advertService.getAdvert(advertId);
						if (advert == null) {
							continue;
						}

						map1.put(advertId, advert);
					}

					// 积分结算周期
					int cycle = map1.get(advertId).getCycle();
					// 点击广告获赠积分
					BigDecimal points = map1.get(advertId).getPoints();

					if (!map2.containsKey(userId + "&" + advertId)) {
						String date = getPointsDate(userId, advertId);

						Date d = DateUtil.getDateDetailTime(date);
						if (d == null) {
							ts.setRollbackOnly();

							result.setCode("积分日期信息不能为空！");
							return result;
						}

						map2.put(userId + "&" + advertId, d);
					}

					Date pointsDate = map2.get(userId + "&" + advertId);

					// 判断是否符合赠送积分规则
					Date createDate = DateUtil.getDateDetailTime(advertStats.getCreateDate());
					if (createDate == null) {
						ts.setRollbackOnly();

						result.setCode("积分日期信息不能为空！");
						return result;
					}

					// 符合规则 送积分
					if (DateUtil.getQuot(pointsDate, createDate) > cycle) {
						result =
							memberPointsService.recordMemberPoints(userId, map1.get(advertId).getShopId(), points,
								IMemberPointsService.POINTS_SOURCE_ADVERT);
						if (!result.getResult()) {
							ts.setRollbackOnly();

							return result;
						}

						result.setResult(false);

						// 广告点击历史记录
						AdvertPoints advertPoints = new AdvertPoints();
						advertPoints.setAdvertId(advertId);
						advertPoints.setUserId(userId);
						advertPoints.setIsLatest("Y");
						advertPoints.setMemPointsId(result.getCode());
						advertPoints.setPointsDate(advertStats.getCreateDate());
						advertPoints.setModifyUser(modifyUser);

						try {
							String id = advertPointsDao.createAdvertPoints(advertPoints);
							map3.put(userId + "&" + advertId, id);
						} catch (Exception e) {
							logger.error(LogUtil.parserBean(advertPoints), e);
							ts.setRollbackOnly();

							result.setCode("广告点击记录保存失败！");
							return result;
						}

						// 更新 统计广告点击积分 周期起始时间
						map2.put(userId + "&" + advertId, createDate);
					}
				}

				// 遍历 map3 除了 map3 中的 advertPoints.id 其余 isLatest 设置为 N
				for (Map.Entry<String, String> m : map3.entrySet()) {
					AdvertPoints advertPoints = new AdvertPoints();
					String[] key = m.getKey().split("&");
					advertPoints.setUserId(key[0]);
					advertPoints.setAdvertId(key[1]);
					advertPoints.setCode(m.getValue());
					advertPoints.setModifyUser(modifyUser);

					try {
						advertPointsDao.updateAdvertPoints(advertPoints);
					} catch (Exception e) {
						logger.error(LogUtil.parserBean(advertPoints), e);
						ts.setRollbackOnly();

						result.setCode("更新点击广告赠送积分信息失败！");
						return result;
					}
				}

				result.setCode(null);
				result.setResult(true);
				return result;
			}
		});

		return res;
	}

	private String getPointsDate(String userId, String advertId) {
		AdvertPoints advertPoints = new AdvertPoints();
		advertPoints.setUserId(userId);
		advertPoints.setAdvertId(advertId);
		advertPoints.setIsLatest("Y");

		try {
			AdvertPoints points = advertPointsDao.getAdvertPoints(advertPoints);
			if (points == null) {
				return "2000-01-01 00:00:00";
			} else {
				return points.getPointsDate();
			}
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(advertPoints), e);
		}

		return "9999-12-31 23:59:59";
	}

	@Override
	public List<AdvertPoints> getAdvertStats(String advertId, String gmtStart, String gmtEnd) {
		if (StringUtils.isBlank(advertId) || StringUtils.isBlank(gmtStart) || StringUtils.isBlank(gmtEnd)) {
			return null;
		}

		AdvertPoints advertPoints = new AdvertPoints();
		advertPoints.setAdvertId(advertId.trim());
		advertPoints.setGmtStart(gmtStart.trim());
		advertPoints.setGmtEnd(gmtEnd.trim());

		try {
			return advertPointsDao.getAdvertStats(advertPoints);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(advertPoints), e);
		}

		return null;
	}

	@Override
	public List<AdvertPoints> getAdvertPointsList(String shopId, String gmtStart, String gmtEnd) {
		if (StringUtils.isBlank(shopId) || StringUtils.isBlank(gmtStart) || StringUtils.isBlank(gmtEnd)) {
			return null;
		}

		AdvertPoints advertPoints = new AdvertPoints();
		advertPoints.setShopId(shopId.trim());
		advertPoints.setGmtStart(gmtStart.trim());
		advertPoints.setGmtEnd(gmtEnd.trim());

		try {
			return advertPointsDao.getAdvertPointsList(advertPoints);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(advertPoints), e);
		}

		return null;
	}

	public TransactionTemplate getTransactionTemplate() {
		return transactionTemplate;
	}

	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}

	public IAdvertService getAdvertService() {
		return advertService;
	}

	public void setAdvertService(IAdvertService advertService) {
		this.advertService = advertService;
	}

	public IMemberPointsService getMemberPointsService() {
		return memberPointsService;
	}

	public void setMemberPointsService(IMemberPointsService memberPointsService) {
		this.memberPointsService = memberPointsService;
	}

	public IAdvertPointsDao getAdvertPointsDao() {
		return advertPointsDao;
	}

	public void setAdvertPointsDao(IAdvertPointsDao advertPointsDao) {
		this.advertPointsDao = advertPointsDao;
	}

}
