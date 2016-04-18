package com.kintiger.mall.advert.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.kintiger.mall.advert.dao.IAdvertStatsDao;
import com.kintiger.mall.api.advert.IAdvertPointsService;
import com.kintiger.mall.api.advert.IAdvertStatsService;
import com.kintiger.mall.api.advert.bo.AdvertStats;
import com.kintiger.mall.api.cache.IMemcachedCacheService;
import com.kintiger.mall.framework.bo.BooleanResult;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;
import com.kintiger.mall.framework.util.DateUtil;
import com.kintiger.mall.framework.util.LogUtil;

/**
 * 广告统计.
 * 
 * @author xujiakun
 * 
 */
public class AdvertStatsServiceImpl implements IAdvertStatsService {

	private Logger4jExtend logger = Logger4jCollection.getLogger(AdvertStatsServiceImpl.class);

	private TransactionTemplate transactionTemplate;

	private IMemcachedCacheService memcachedCacheService;

	private IAdvertPointsService advertPointsService;

	private IAdvertStatsDao advertStatsDao;

	@Override
	public BooleanResult createdAdvertStats(String userId, String ip, String advertId) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		AdvertStats advertStats = new AdvertStats();

		if (StringUtils.isBlank(userId)) {
			result.setCode("用户信息不能为空！");
			return result;
		}

		advertStats.setUserId(userId.trim());

		if (StringUtils.isBlank(ip)) {
			result.setCode("用户IP信息不能为空！");
			return result;
		}

		advertStats.setIp(ip.trim());

		if (StringUtils.isBlank(advertId)) {
			result.setCode("广告信息不能为空！");
			return result;
		}

		advertStats.setAdvertId(advertId.trim());

		// 广告点击时间 yyyy-MM-dd HH:mm:ss
		advertStats.setCreateDate(DateUtil.getNowDatetimeStr());

		try {
			@SuppressWarnings("unchecked")
			List<AdvertStats> list =
				(List<AdvertStats>) memcachedCacheService.get(IMemcachedCacheService.CACHE_KEY_ADVERT_STATS);

			// 初始化cache
			if (list == null || list.size() == 0) {
				list = new ArrayList<AdvertStats>();
			}

			list.add(advertStats);

			memcachedCacheService.set(IMemcachedCacheService.CACHE_KEY_ADVERT_STATS, list,
				IMemcachedCacheService.CACHE_KEY_ADVERT_STATS_DEFAULT_EXP);
		} catch (Exception e) {
			logger.error(IMemcachedCacheService.CACHE_KEY_ADVERT_STATS, e);

			result.setCode("广告点击信息暂存失败！");
			return result;
		}

		result.setResult(true);
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean createAdvertStats() {
		List<AdvertStats> list = null;

		// 0. 从 cache 中 获取广告点击记录
		try {
			list = (List<AdvertStats>) memcachedCacheService.get(IMemcachedCacheService.CACHE_KEY_ADVERT_STATS);

		} catch (Exception e) {
			logger.error(IMemcachedCacheService.CACHE_KEY_ADVERT_STATS, e);
		}

		if (list == null || list.size() == 0) {
			return true;
		}

		final List<AdvertStats> advertStatsList = list;

		BooleanResult res = (BooleanResult) transactionTemplate.execute(new TransactionCallback() {
			public Object doInTransaction(TransactionStatus ts) {
				// 1. 逐个判断是否符合条件 赠送积分
				BooleanResult result = advertPointsService.createAdvertPoints(advertStatsList, "system");
				if (!result.getResult()) {
					ts.setRollbackOnly();

					return result;
				}

				// 2. 保存广告点击记录
				int count = createAdvertStats(advertStatsList);

				if (count == 0) {
					ts.setRollbackOnly();

					result.setCode("创建广告点击记录失败！");
					result.setResult(false);
					return result;
				}

				// 3. 清空广告点击记录缓存
				try {
					memcachedCacheService.remove(IMemcachedCacheService.CACHE_KEY_ADVERT_STATS);
				} catch (Exception e) {
					logger.error(IMemcachedCacheService.CACHE_KEY_ADVERT_STATS, e);

					ts.setRollbackOnly();

					result.setCode("清空广告点击记录缓存失败！");
					result.setResult(false);
					return result;
				}

				return result;
			}
		});

		return res.getResult();
	}

	private int createAdvertStats(List<AdvertStats> advertStatsList) {
		try {
			return advertStatsDao.createAdvertStats(advertStatsList, "system");
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(advertStatsList), e);
		}

		return 0;
	}

	@Override
	public List<AdvertStats> getAdvertStats(String advertId, String gmtStart, String gmtEnd) {
		if (StringUtils.isBlank(advertId) || StringUtils.isBlank(gmtStart) || StringUtils.isBlank(gmtEnd)) {
			return null;
		}

		AdvertStats advertStats = new AdvertStats();
		advertStats.setAdvertId(advertId.trim());
		advertStats.setGmtStart(gmtStart.trim());
		advertStats.setGmtEnd(gmtEnd.trim());

		try {
			return advertStatsDao.getAdvertStats(advertStats);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(advertStats), e);
		}

		return null;
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

	public IAdvertPointsService getAdvertPointsService() {
		return advertPointsService;
	}

	public void setAdvertPointsService(IAdvertPointsService advertPointsService) {
		this.advertPointsService = advertPointsService;
	}

	public IAdvertStatsDao getAdvertStatsDao() {
		return advertStatsDao;
	}

	public void setAdvertStatsDao(IAdvertStatsDao advertStatsDao) {
		this.advertStatsDao = advertStatsDao;
	}

}
