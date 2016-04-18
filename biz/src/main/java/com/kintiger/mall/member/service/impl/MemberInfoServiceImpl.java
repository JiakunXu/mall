package com.kintiger.mall.member.service.impl;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.kintiger.mall.api.cache.IMemcachedCacheService;
import com.kintiger.mall.api.member.IMemberInfoService;
import com.kintiger.mall.api.member.IMemberPointsService;
import com.kintiger.mall.api.member.bo.MemberInfo;
import com.kintiger.mall.api.user.IUserService;
import com.kintiger.mall.api.user.bo.User;
import com.kintiger.mall.framework.bo.BooleanResult;
import com.kintiger.mall.framework.exception.ServiceException;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;
import com.kintiger.mall.framework.util.LogUtil;
import com.kintiger.mall.member.dao.IMemberInfoDao;

/**
 * 会员信息.
 * 
 * @author xujiakun
 * 
 */
public class MemberInfoServiceImpl implements IMemberInfoService {

	private Logger4jExtend logger = Logger4jCollection.getLogger(MemberInfoServiceImpl.class);

	private TransactionTemplate transactionTemplate;

	private IMemcachedCacheService memcachedCacheService;

	private IMemberPointsService memberPointsService;

	private IUserService userService;

	private IMemberInfoDao memberInfoDao;

	@Override
	public MemberInfo getMemberInfo(String memId) {
		if (StringUtils.isBlank(memId)) {
			return null;
		}

		MemberInfo memberInfo = null;

		try {
			memberInfo =
				(MemberInfo) memcachedCacheService.get(IMemcachedCacheService.CACHE_KEY_MEMBER_INFO_MEM_ID
					+ memId.trim());
		} catch (ServiceException e) {
			logger.error(IMemcachedCacheService.CACHE_KEY_MEMBER_INFO_MEM_ID + memId, e);
		}

		if (memberInfo != null) {
			return memberInfo;
		}

		MemberInfo info = new MemberInfo();
		info.setMemId(memId.trim());

		try {
			memberInfo = memberInfoDao.getMemberInfo(info);
			if (memberInfo != null) {
				memcachedCacheService.set(IMemcachedCacheService.CACHE_KEY_MEMBER_INFO_MEM_ID + memId.trim(),
					memberInfo);
			}
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(info), e);
		}

		return memberInfo;
	}

	@Override
	public BooleanResult updateMemberInfo(String memId, final String userId, final String shopId,
		final MemberInfo memberInfo) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		if (memberInfo == null) {
			result.setCode("会员信息不能为空！");
			return result;
		}

		if (StringUtils.isBlank(memId)) {
			result.setCode("会员编号不能为空！");
			return result;
		}

		if (StringUtils.isBlank(userId)) {
			result.setCode("用户信息不能为空！");
			return result;
		}

		if (StringUtils.isBlank(shopId)) {
			result.setCode("店铺信息不能为空！");
			return result;
		}

		memberInfo.setMemId(memId.trim());

		// if 未婚 then 结婚纪念日 set null
		if (!"Y".equals(memberInfo.getMaritalStatus())) {
			memberInfo.setWeddingDay(null);
		}

		// 锁定当前会员信息修改
		try {
			memcachedCacheService.add(IMemcachedCacheService.CACHE_KEY_MEM_ID + memId.trim(), memId,
				IMemcachedCacheService.CACHE_KEY_MEM_ID_DEFAULT_EXP);
		} catch (ServiceException e) {
			result.setCode("当前会员信息已被锁定，请稍后再试！");
			return result;
		}

		// 1. 判断 是否已维护 会员信息
		MemberInfo info = getMemberInfo(memId.trim());

		if (info == null) {
			result.setCode("当前用户不存在！");
			return result;
		}

		// false 不存在
		boolean f1 = false;
		// false 不需要修改 user name 信息
		boolean f2 = false;

		// if true then 已存在 info 信息
		if (StringUtils.isNotBlank(info.getId())) {
			memberInfo.setId(info.getId());
			f1 = true;
		}

		// 判断是否需要修改 user name 信息
		if (info.getUserName() == null) {
			if (memberInfo.getUserName() != null) {
				f2 = true;
			}
		} else {
			if (!info.getUserName().equals(memberInfo.getUserName())) {
				f2 = true;
			}
		}

		// if true then 已赠送
		if (StringUtils.isNotBlank(info.getMemPointsId())) {
			memberInfo.setMemPointsId(info.getMemPointsId());
		}

		// 判断是否赠送积分 ＝ 没有赠送过积分 并且 信息完整
		final boolean state = (StringUtils.isBlank(info.getMemPointsId()) && validate(memberInfo)) ? true : false;

		// insert or update
		final boolean flag1 = f1;
		// 判断是否需要修改 user name 信息
		final boolean flag2 = f2;

		BooleanResult res = (BooleanResult) transactionTemplate.execute(new TransactionCallback() {
			public Object doInTransaction(TransactionStatus ts) {
				BooleanResult result = new BooleanResult();
				result.setResult(false);

				// 1. 完善信息后 获得积分
				if (state) {
					result =
						memberPointsService.recordMemberPoints(userId, shopId, new BigDecimal("100"),
							IMemberPointsService.POINTS_SOURCE_MEMBER_INFO);
					if (!result.getResult()) {
						ts.setRollbackOnly();

						return result;
					}

					memberInfo.setMemPointsId(result.getCode());
				}

				// 2. if flag true then update
				if (flag1) {
					// 2.0 udpate
					try {
						int c = memberInfoDao.updateMemberInfo(memberInfo);
						if (c != 1) {
							ts.setRollbackOnly();

							result.setCode("修改会员信息失败！");
							return result;
						}
					} catch (Exception e) {
						logger.error(LogUtil.parserBean(memberInfo), e);
						ts.setRollbackOnly();

						result.setCode("修改会员信息异常！");
						return result;
					}
				} else {
					// 2.1 insert
					try {
						memberInfoDao.createMemberInfo(memberInfo);
					} catch (Exception e) {
						logger.error(LogUtil.parserBean(memberInfo), e);
						ts.setRollbackOnly();

						result.setCode("修改会员信息异常！");
						return result;
					}
				}

				// 3. 判断是否需要求改 user 信息(userName)
				if (flag2) {
					User user = new User();
					user.setUserName(memberInfo.getUserName());

					result = userService.updateUser(userId, user);
					if (!result.getResult()) {
						ts.setRollbackOnly();

						return result;
					}
				}

				result.setResult(true);
				return result;
			}
		});

		// 更新成功
		if (res.getResult()) {
			remove(memId.trim());
		}

		return res;
	}

	private boolean validate(MemberInfo memberInfo) {
		if (StringUtils.isBlank(memberInfo.getUserName())) {
			return false;
		}

		if (StringUtils.isBlank(memberInfo.getSex())) {
			return false;
		}

		if (StringUtils.isBlank(memberInfo.getBirthday())) {
			return false;
		}

		if (StringUtils.isBlank(memberInfo.getAddress())) {
			return false;
		}

		if (StringUtils.isBlank(memberInfo.getPostalCode())) {
			return false;
		}

		if (StringUtils.isBlank(memberInfo.getProfession())) {
			return false;
		}

		if (StringUtils.isBlank(memberInfo.getEducation())) {
			return false;
		}

		if (StringUtils.isBlank(memberInfo.getMaritalStatus())) {
			return false;
		}

		// if 选择已婚 then weddingDay is not null
		if ("Y".equals(memberInfo.getMaritalStatus())) {
			if (StringUtils.isBlank(memberInfo.getWeddingDay())) {
				return false;
			}
		}

		return true;
	}

	/**
	 * remove cache.
	 * 
	 * @param userId
	 */
	private void remove(String memId) {
		try {
			memcachedCacheService.remove(IMemcachedCacheService.CACHE_KEY_MEMBER_INFO_MEM_ID + memId.trim());
		} catch (ServiceException e) {
			logger.error(IMemcachedCacheService.CACHE_KEY_MEMBER_INFO_MEM_ID + memId, e);
		}
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

	public IMemberPointsService getMemberPointsService() {
		return memberPointsService;
	}

	public void setMemberPointsService(IMemberPointsService memberPointsService) {
		this.memberPointsService = memberPointsService;
	}

	public IUserService getUserService() {
		return userService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public IMemberInfoDao getMemberInfoDao() {
		return memberInfoDao;
	}

	public void setMemberInfoDao(IMemberInfoDao memberInfoDao) {
		this.memberInfoDao = memberInfoDao;
	}

}
