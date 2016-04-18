package com.kintiger.mall.member.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.kintiger.mall.api.cache.IMemcachedCacheService;
import com.kintiger.mall.api.member.IMemberLevelService;
import com.kintiger.mall.api.member.IMemberPointsService;
import com.kintiger.mall.api.member.IMemberService;
import com.kintiger.mall.api.member.bo.Member;
import com.kintiger.mall.api.member.bo.MemberLevel;
import com.kintiger.mall.framework.bo.BooleanResult;
import com.kintiger.mall.framework.exception.ServiceException;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;
import com.kintiger.mall.framework.util.LogUtil;
import com.kintiger.mall.member.dao.IMemberDao;

/**
 * 会员接口实现.
 * 
 * @author xujiakun
 * 
 */
public class MemberServiceImpl implements IMemberService {

	private Logger4jExtend logger = Logger4jCollection.getLogger(MemberServiceImpl.class);

	private TransactionTemplate transactionTemplate;

	private IMemcachedCacheService memcachedCacheService;

	private IMemberLevelService memberLevelService;

	private IMemberPointsService memberPointsService;

	private IMemberDao memberDao;

	// >>>>>>>>>>以下是卖家<<<<<<<<<<

	@Override
	public int getMemberCount(String shopId, Member member) {
		if (StringUtils.isBlank(shopId) || member == null) {
			return 0;
		}

		member.setShopId(shopId.trim());

		try {
			return memberDao.getMemberCount(member);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(member), e);
		}

		return 0;
	}

	@Override
	public List<Member> getMemberList(String shopId, Member member) {
		if (StringUtils.isBlank(shopId) || member == null) {
			return null;
		}

		member.setShopId(shopId.trim());

		try {
			return memberDao.getMemberList(member);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(member), e);
		}

		return null;
	}

	@Override
	public List<Member> getMemberStats(String shopId, String gmtStart, String gmtEnd) {
		if (StringUtils.isBlank(shopId) || StringUtils.isBlank(gmtStart) || StringUtils.isBlank(gmtEnd)) {
			return null;
		}

		Member member = new Member();
		member.setShopId(shopId.trim());
		member.setGmtStart(gmtStart.trim());
		member.setGmtEnd(gmtEnd.trim());

		try {
			return memberDao.getMemberStats(member);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(member), e);
		}

		return null;
	}

	@Override
	public int getMemberCountStats(String shopId, String gmtStart, String gmtEnd) {
		if (StringUtils.isBlank(shopId)) {
			return 0;
		}

		Member member = new Member();
		member.setShopId(shopId.trim());

		if (StringUtils.isNotBlank(gmtStart)) {
			member.setGmtStart(gmtStart.trim());
		}

		if (StringUtils.isNotBlank(gmtEnd)) {
			member.setGmtEnd(gmtEnd.trim());
		}

		try {
			return memberDao.getMemberCountStats(member);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(member), e);
		}

		return 0;
	}

	// >>>>>>>>>>以下是买家<<<<<<<<<<

	@Override
	public BooleanResult validateMember(String shopId, String userId) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		if (StringUtils.isBlank(shopId)) {
			result.setCode("店铺信息不能为空！");
			return result;
		}

		if (StringUtils.isBlank(userId)) {
			result.setCode("用户信息不能为空！");
			return result;
		}

		Member member = getMember(shopId, userId);

		// 已成为会员
		if (member != null) {
			result.setCode(member.getMemId());
			result.setResult(true);
			return result;
		}

		return createMember(shopId, userId);
	}

	@Override
	public BooleanResult createMember(String shopId, String userId) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		final Member member = new Member();

		if (StringUtils.isBlank(shopId)) {
			result.setCode("店铺信息不能为空！");
			return result;
		}

		member.setShopId(shopId.trim());

		if (StringUtils.isBlank(userId)) {
			result.setCode("用户信息不能为空！");
			return result;
		}

		member.setUserId(userId.trim());

		// 获取会员等级
		MemberLevel memberLevel = memberLevelService.getMemberLevel(shopId, BigDecimal.ZERO);
		if (memberLevel == null) {
			result.setCode("会员等级不存在！");
			return result;
		}

		member.setLevelId(memberLevel.getLevelId());
		member.setPoints(memberLevel.getPoints());

		BooleanResult res = (BooleanResult) transactionTemplate.execute(new TransactionCallback() {
			public Object doInTransaction(TransactionStatus ts) {
				BooleanResult result = new BooleanResult();
				result.setResult(false);

				// 1. 注册会员
				String memId = null;
				try {
					memId = memberDao.createMember(member);
				} catch (Exception e) {
					logger.error(LogUtil.parserBean(member), e);
					ts.setRollbackOnly();

					result.setCode("创建会员失败！");
					return result;
				}

				// 2. 注册会员获得积分
				result =
					memberPointsService.createMemberPoints(memId, member.getPoints(),
						IMemberPointsService.POINTS_SOURCE_REGISTER);
				if (!result.getResult()) {
					ts.setRollbackOnly();

					return result;
				}

				result.setCode(memId);
				return result;
			}
		});

		return res;
	}

	@Override
	public Member getMember(String shopId, String userId) {
		if (StringUtils.isBlank(shopId) || StringUtils.isBlank(userId)) {
			return null;
		}

		Member member = null;

		try {
			member =
				(Member) memcachedCacheService.get(IMemcachedCacheService.CACHE_KEY_MEMBER_SHOP_USER_ID + shopId.trim()
					+ "&" + userId.trim());
		} catch (ServiceException e) {
			logger.error(IMemcachedCacheService.CACHE_KEY_MEMBER_SHOP_USER_ID + shopId + "&" + userId, e);
		}

		if (member != null) {
			return member;
		}

		Member mem = new Member();
		mem.setShopId(shopId.trim());
		mem.setUserId(userId.trim());

		try {
			member = memberDao.getMember(mem);
			// if not null then set to cache
			if (member != null) {
				memcachedCacheService.set(IMemcachedCacheService.CACHE_KEY_MEMBER_SHOP_USER_ID + shopId.trim() + "&"
					+ userId.trim(), member);
			}
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(mem), e);
		}

		return member;
	}

	@Override
	public BooleanResult updateMember(Member member, String modifyUser) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		if (member == null) {
			result.setCode("会员信息不能为空！");
			return result;
		}

		if (StringUtils.isBlank(modifyUser)) {
			result.setCode("操作人信息不能为空！");
			return result;
		}

		member.setModifyUser(modifyUser);

		try {
			int c = memberDao.updateMember(member);
			if (c == 1) {
				result.setResult(true);
			} else {
				result.setCode("修改会员失败！");
			}
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(member), e);

			result.setCode("更新会员表失败！");
		}

		return result;
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

	public IMemberLevelService getMemberLevelService() {
		return memberLevelService;
	}

	public void setMemberLevelService(IMemberLevelService memberLevelService) {
		this.memberLevelService = memberLevelService;
	}

	public IMemberPointsService getMemberPointsService() {
		return memberPointsService;
	}

	public void setMemberPointsService(IMemberPointsService memberPointsService) {
		this.memberPointsService = memberPointsService;
	}

	public IMemberDao getMemberDao() {
		return memberDao;
	}

	public void setMemberDao(IMemberDao memberDao) {
		this.memberDao = memberDao;
	}

}
