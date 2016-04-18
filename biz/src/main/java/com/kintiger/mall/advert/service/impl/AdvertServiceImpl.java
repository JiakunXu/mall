package com.kintiger.mall.advert.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.kintiger.mall.advert.dao.IAdvertDao;
import com.kintiger.mall.api.advert.IAdvertPointsService;
import com.kintiger.mall.api.advert.IAdvertService;
import com.kintiger.mall.api.advert.IAdvertStatsService;
import com.kintiger.mall.api.advert.bo.Advert;
import com.kintiger.mall.api.advert.bo.AdvertPoints;
import com.kintiger.mall.api.advert.bo.AdvertStats;
import com.kintiger.mall.framework.bo.BooleanResult;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;
import com.kintiger.mall.framework.util.DateUtil;
import com.kintiger.mall.framework.util.LogUtil;

/**
 * 广告.
 * 
 * @author xujiakun
 * 
 */
public class AdvertServiceImpl implements IAdvertService {

	private Logger4jExtend logger = Logger4jCollection.getLogger(AdvertServiceImpl.class);

	private IAdvertStatsService advertStatsService;

	private IAdvertPointsService advertPointsService;

	private IAdvertDao advertDao;

	// >>>>>>>>>>以下是卖家<<<<<<<<<<

	@Override
	public int getAdvertCount(String shopId, Advert advert) {
		if (StringUtils.isBlank(shopId) || advert == null) {
			return 0;
		}

		advert.setShopId(shopId.trim());

		try {
			return advertDao.getAdvertCount(advert);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(advert), e);
		}

		return 0;
	}

	@Override
	public List<Advert> getAdvertList(String shopId, Advert advert) {
		if (StringUtils.isBlank(shopId) || advert == null) {
			return null;
		}

		advert.setShopId(shopId.trim());

		try {
			return advertDao.getAdvertList(advert);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(advert), e);
		}

		return null;
	}

	@Override
	public List<Advert> getAdvertStats(String shopId, String advertId, String gmtStart, String gmtEnd) {
		if (StringUtils.isBlank(shopId) || StringUtils.isBlank(gmtStart) || StringUtils.isBlank(gmtEnd)) {
			return null;
		}

		// 1. 判断广告是否属于当前店铺
		if (StringUtils.isNotBlank(advertId)) {
			Advert advert = new Advert();
			advert.setShopId(shopId.trim());
			advert.setAdvertId(advertId.trim());
			Advert ad = getAdvert(advert);

			if (ad == null) {
				return null;
			}
		}

		// 2. 组装坐标时间 list
		List<Advert> list0 = new ArrayList<Advert>();

		Date start = DateUtil.getDateTime(gmtStart);
		Date end = DateUtil.getDateTime(gmtEnd);

		// 判断 开始日期 是否小于 结束日期
		if (DateUtil.getQuot(start, end) < 0) {
			return null;
		}

		do {
			Advert a = new Advert();
			a.setDates(DateUtil.datetime(start, DateUtil.DEFAULT_DATE_FORMAT));
			list0.add(a);

			// 加 1 天
			start = DateUtil.addDays(start, 1);
		} while (DateUtil.getQuot(start, end) >= 0);

		// pv
		Map<String, Integer> map1 = new HashMap<String, Integer>();
		// uv
		Map<String, Integer> map2 = new HashMap<String, Integer>();
		// points
		Map<String, BigDecimal> map3 = new HashMap<String, BigDecimal>();

		// 3. 统计 pv uv
		List<AdvertStats> list1 = advertStatsService.getAdvertStats(advertId, gmtStart, gmtEnd);
		if (list1 != null && list1.size() > 0) {
			for (AdvertStats advertStats : list1) {
				map1.put(advertStats.getDates(), advertStats.getPv());
				map2.put(advertStats.getDates(), advertStats.getUv());
			}
		}

		// 4. 统计 赠送积分
		List<AdvertPoints> list2 = advertPointsService.getAdvertStats(advertId, gmtStart, gmtEnd);
		if (list2 != null && list2.size() > 0) {
			for (AdvertPoints advertPoints : list2) {
				map3.put(advertPoints.getDates(), advertPoints.getPoints());
			}
		}

		for (Advert a : list0) {
			String dates = a.getDates();

			Integer pv = map1.get(dates);
			a.setPv(pv == null ? 0 : pv);

			Integer uv = map2.get(dates);
			a.setUv(uv == null ? 0 : uv);

			BigDecimal points = map3.get(dates);
			a.setPoints(points == null ? BigDecimal.ZERO : points);
		}

		return list0;
	}

	@Override
	public List<Advert> getAdvertStats(String shopId, String gmtStart, String gmtEnd) {
		if (StringUtils.isBlank(shopId) || StringUtils.isBlank(gmtStart) || StringUtils.isBlank(gmtEnd)) {
			return null;
		}

		Advert advert = new Advert();
		advert.setShopId(shopId.trim());
		advert.setGmtStart(gmtStart.trim());
		advert.setGmtEnd(gmtEnd.trim());

		try {
			return advertDao.getAdvertStats(advert);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(advert), e);
		}

		return null;
	}

	// >>>>>>>>>>以下是买家<<<<<<<<<<

	@Override
	public BooleanResult clickAdvert(String userId, String ip, String shopId, String advertId) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		Advert ad = new Advert();

		if (StringUtils.isBlank(userId)) {
			result.setCode("用户信息不能为空！");
			return result;
		}

		if (StringUtils.isBlank(ip)) {
			result.setCode("用户IP信息不能为空！");
			return result;
		}

		if (StringUtils.isBlank(shopId)) {
			result.setCode("店铺信息不能为空！");
			return result;
		}

		ad.setShopId(shopId.trim());

		if (StringUtils.isBlank(advertId)) {
			result.setCode("广告信息不能为空！");
			return result;
		}

		ad.setAdvertId(advertId.trim());

		// 1. 获取广告具体信息(验证广告有效性；返回点击广告后跳转地址)
		Advert advert = getAdvert(ad);

		if (advert == null) {
			result.setCode("广告信息不存在！");
			return result;
		}

		// 2. 暂存广告点击记录
		result = advertStatsService.createdAdvertStats(userId.trim(), ip.trim(), advert.getAdvertId());
		if (result.getResult()) {
			result.setCode(advert.getRedirectUrl());
		}

		return result;
	}

	// >>>>>>>>>>以下是后台<<<<<<<<<<

	@Override
	public Advert getAdvert(String advertId) {
		if (StringUtils.isBlank(advertId)) {
			return null;
		}

		Advert advert = new Advert();
		advert.setAdvertId(advertId.trim());

		return getAdvert(advert);
	}

	// >>>>>>>>>>公共方法<<<<<<<<<<

	private Advert getAdvert(Advert advert) {
		try {
			return advertDao.getAdvert(advert);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(advert), e);
		}

		return null;
	}

	public IAdvertStatsService getAdvertStatsService() {
		return advertStatsService;
	}

	public void setAdvertStatsService(IAdvertStatsService advertStatsService) {
		this.advertStatsService = advertStatsService;
	}

	public IAdvertPointsService getAdvertPointsService() {
		return advertPointsService;
	}

	public void setAdvertPointsService(IAdvertPointsService advertPointsService) {
		this.advertPointsService = advertPointsService;
	}

	public IAdvertDao getAdvertDao() {
		return advertDao;
	}

	public void setAdvertDao(IAdvertDao advertDao) {
		this.advertDao = advertDao;
	}

}
