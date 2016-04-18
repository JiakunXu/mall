package com.kintiger.mall.member.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.kintiger.mall.api.cache.IMemcachedCacheService;
import com.kintiger.mall.api.member.IMemberLevelService;
import com.kintiger.mall.api.member.bo.MemberLevel;
import com.kintiger.mall.framework.exception.ServiceException;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;
import com.kintiger.mall.framework.util.LogUtil;
import com.kintiger.mall.member.dao.IMemberLevelDao;

/**
 * 
 * @author xujiakun
 * 
 */
public class MemberLevelServiceImpl implements IMemberLevelService {

	private Logger4jExtend logger = Logger4jCollection.getLogger(MemberLevelServiceImpl.class);

	private IMemcachedCacheService memcachedCacheService;

	private IMemberLevelDao memberLevelDao;

	@SuppressWarnings("unchecked")
	@Override
	public List<MemberLevel> getMemberLevelList(String shopId) {
		if (StringUtils.isBlank(shopId)) {
			return null;
		}

		List<MemberLevel> memberLevelList = null;

		try {
			memberLevelList =
				(List<MemberLevel>) memcachedCacheService.get(IMemcachedCacheService.CACHE_KEY_MEMBER_LEVEL_SHOP_ID
					+ shopId.trim());
		} catch (ServiceException e) {
			logger.error(IMemcachedCacheService.CACHE_KEY_MEMBER_LEVEL_SHOP_ID + shopId, e);
		}

		if (memberLevelList != null && memberLevelList.size() > 0) {
			return memberLevelList;
		}

		MemberLevel memberLevel = new MemberLevel();
		memberLevel.setShopId(shopId.trim());

		try {
			memberLevelList = memberLevelDao.getMemberLevelList(memberLevel);
			if (memberLevelList != null && memberLevelList.size() > 0) {
				memcachedCacheService.set(IMemcachedCacheService.CACHE_KEY_MEMBER_LEVEL_SHOP_ID + shopId.trim(),
					memberLevelList);
			}
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(memberLevel), e);
		}

		return memberLevelList;
	}

	@Override
	public MemberLevel getMemberLevel(String shopId, BigDecimal points) {
		if (StringUtils.isBlank(shopId) || points == null) {
			return null;
		}

		MemberLevel memlevel = new MemberLevel();
		memlevel.setShopId(shopId.trim());
		memlevel.setPoints(points);

		try {
			return memberLevelDao.getMemberLevel(memlevel);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(memlevel), e);
		}

		return null;
	}

	public IMemcachedCacheService getMemcachedCacheService() {
		return memcachedCacheService;
	}

	public void setMemcachedCacheService(IMemcachedCacheService memcachedCacheService) {
		this.memcachedCacheService = memcachedCacheService;
	}

	public IMemberLevelDao getMemberLevelDao() {
		return memberLevelDao;
	}

	public void setMemberLevelDao(IMemberLevelDao memberLevelDao) {
		this.memberLevelDao = memberLevelDao;
	}

}
