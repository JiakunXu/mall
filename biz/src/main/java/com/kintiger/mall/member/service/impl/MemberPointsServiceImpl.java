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
import com.kintiger.mall.api.member.bo.MemberPoints;
import com.kintiger.mall.framework.bo.BooleanResult;
import com.kintiger.mall.framework.exception.ServiceException;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;
import com.kintiger.mall.framework.util.LogUtil;
import com.kintiger.mall.member.dao.IMemberPointsDao;

/**
 * 
 * @author xujiakun
 * 
 */
public class MemberPointsServiceImpl implements IMemberPointsService {

	private Logger4jExtend logger = Logger4jCollection.getLogger(MemberPointsServiceImpl.class);

	private TransactionTemplate transactionTemplate;

	private IMemcachedCacheService memcachedCacheService;

	private IMemberService memberService;

	private IMemberLevelService memberLevelService;

	private IMemberPointsDao memberPointsDao;

	@Override
	public BooleanResult recordMemberPoints(String userId, String shopId, BigDecimal points, String pointsSource) {
		return recordMemberPoints(userId, shopId, points, pointsSource, null);
	}

	@Override
	public BooleanResult recordMemberPoints(final String userId, String shopId, final BigDecimal points,
		final String pointsSource, final String tradeId) {
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

		if (points == null) {
			result.setCode("积分信息不能为空！");
			return result;
		}

		// 0. 获取会员信息
		final Member member = memberService.getMember(shopId.trim(), userId.trim());
		if (member == null) {
			result.setCode("用户会员信息不存在！");
			return result;
		}

		// 会员积分
		BigDecimal p = member.getPoints();
		// 消费积分
		BigDecimal s = member.getSurplusPoints();

		// if 非 消费兑换积分 then
		if (points.compareTo(BigDecimal.ZERO) == 1) {
			p = p.add(points);
			s = s.add(points);
		} else {
			s = s.add(points);
		}

		boolean update = false;
		BigDecimal mlp = BigDecimal.ZERO;

		// if 非 消费兑换积分 then 不需要判断 会员等级升级
		if (points.compareTo(BigDecimal.ZERO) == 1) {
			// 1. 获取增加 points 后，对应会员等级 用于后面 判断是否需要会员升级
			MemberLevel memberLevel = memberLevelService.getMemberLevel(shopId, p);
			if (memberLevel == null) {
				result.setCode("会员等级不存在！");
				return result;
			}

			// 2. 判断是否完成会员升级 需要升级
			if (member.getRank() < memberLevel.getRank()) {
				update = true;

				int rank = 0;
				BigDecimal pp = p;
				do {
					rank = memberLevel.getRank();
					pp = pp.add(memberLevel.getPoints());

					memberLevel = memberLevelService.getMemberLevel(shopId, pp);
					if (memberLevel == null) {
						result.setCode("会员等级不存在！");
						return result;
					}
				} while (rank < memberLevel.getRank());

				member.setLevelId(memberLevel.getLevelId());
				mlp = getPoints(shopId, member.getRank(), memberLevel.getRank());
			}
		}

		final boolean upgrade = update;
		final BigDecimal memberLevelPoints = mlp;

		// 会员升级 增加积分
		if (upgrade) {
			p = p.add(memberLevelPoints);
			s = s.add(memberLevelPoints);
		}

		// 更新会员积分
		member.setPoints(p);
		member.setSurplusPoints(s);

		BooleanResult res = (BooleanResult) transactionTemplate.execute(new TransactionCallback() {
			public Object doInTransaction(TransactionStatus ts) {
				// 3. 新增会员积分日志记录 createMemberPoints
				BooleanResult result = createMemberPoints(member.getMemId(), points, pointsSource, tradeId);
				if (!result.getResult()) {
					ts.setRollbackOnly();

					return result;
				}

				String memPointsId = result.getCode();

				// 4. 会员升级 赠送积分
				if (upgrade) {
					result =
						createMemberPoints(member.getMemId(), memberLevelPoints,
							IMemberPointsService.POINTS_SOURCE_UPGRADE);
					if (!result.getResult()) {
						ts.setRollbackOnly();

						return result;
					}
				}

				// 5. 更新会员信息(合计积分 等级)
				result = memberService.updateMember(member, userId);
				if (!result.getResult()) {
					ts.setRollbackOnly();

					return result;
				}

				// 成功后 返回积分id
				result.setCode(memPointsId);
				result.setResult(true);
				return result;
			}
		});

		// 更新成功 remove cache
		if (res.getResult()) {
			remove(shopId, userId);
		}

		return res;
	}

	@Override
	public BooleanResult validateReturnPoints(String userId, String shopId, String tradeId) {
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

		if (StringUtils.isBlank(tradeId)) {
			result.setCode("交易信息不能为空！");
			return result;
		}

		// 0. 获取交易积分记录
		List<MemberPoints> memberPointsList = getMemberPointsList(userId.trim(), shopId.trim(), tradeId.trim());

		// 1. 不存在积分 -> 直接返回 true
		if (memberPointsList == null || memberPointsList.size() == 0) {
			result.setCode("交易不存在兑换/获取积分记录！");
			result.setResult(true);
			return result;
		}

		// 统计所有积分记录汇总
		BigDecimal points = BigDecimal.ZERO;

		for (MemberPoints memberPoints : memberPointsList) {
			points = points.add(memberPoints.getPoints());
		}

		// 2. 积分 正负 合计为0
		if (points.compareTo(BigDecimal.ZERO) == 0) {
			result.setCode("退款交易，返还积分(0)！");
			result.setResult(true);
			return result;
		}

		// 因为回退积分 所以取反
		points = points.negate();

		// if points > 0 then 之前消费积分(-)，现在返还积分(+)
		// if points <= 0 then 之前消费获得了积分(+)，现在返还积分(-)
		if (points.compareTo(BigDecimal.ZERO) == 1) {
			result.setCode("退款交易，存在返还积分(" + points.toString() + ")！");
			result.setResult(true);
			return result;
		}

		// 3. 获取会员信息
		Member member = memberService.getMember(shopId.trim(), userId.trim());
		if (member == null) {
			result.setCode("用户会员信息不存在！");
			return result;
		}

		// 4. 积分小于0 可能 会员等级降级
		// 会员积分
		BigDecimal p = member.getPoints();
		p = p.add(points);

		// 5. 获取用户所有 注册 and 升级 and 降级 会员赠送积分
		String[] pointsSource =
			new String[] { IMemberPointsService.POINTS_SOURCE_REGISTER, IMemberPointsService.POINTS_SOURCE_UPGRADE,
				IMemberPointsService.POINTS_SOURCE_DOWNGRADE };

		BigDecimal points4Member = getMemberPoints(userId.trim(), shopId.trim(), pointsSource);

		// 6. member.getPoints() - points - points4Member = 除去升级赠送积分 then
		// 从低到高 重新判断会员等级
		p = p.add(points4Member.negate());

		List<MemberLevel> memberLevelList = memberLevelService.getMemberLevelList(shopId.trim());

		if (memberLevelList == null || memberLevelList.size() == 0) {
			result.setCode("会员等级不存在！");
			return result;
		}

		for (MemberLevel level : memberLevelList) {
			// p 大于或者等级 会员等级的区别范围
			if (p.compareTo(level.getStartPoints()) == 1 || p.compareTo(level.getStartPoints()) == 0) {
				p = p.add(level.getPoints());
			} else {
				break;
			}
		}

		// 7. 原会员积分 － 回退后会员积分 = 扣除积分
		points = member.getPoints().add(p.negate());

		// 消费积分
		BigDecimal s = member.getSurplusPoints();

		// 8. 交易扣除积分 小于等于 消费积分
		if (points.compareTo(s) == -1 || points.compareTo(s) == 0) {
			result.setResult(true);
		}

		result.setCode("退款交易，存在扣除积分(" + points.toString() + ")，买家剩余消费积分(" + s.toString() + ")！");

		return result;
	}

	@Override
	public BooleanResult returnMemberPoints(final String userId, String shopId, final BigDecimal points,
		final String pointsSource, final String tradeId) {
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

		if (points == null) {
			result.setCode("积分信息不能为空！");
			return result;
		}

		// if 返还积分0 then
		if (points.compareTo(BigDecimal.ZERO) == 0) {
			result.setResult(true);
			return result;
		}

		// 0. 获取会员信息
		final Member member = memberService.getMember(shopId.trim(), userId.trim());
		if (member == null) {
			result.setCode("用户会员信息不存在！");
			return result;
		}

		// 会员积分
		BigDecimal p = member.getPoints();
		// 消费积分
		BigDecimal s = member.getSurplusPoints();

		// if points > 0 then 之前消费积分(-)，现在返还积分(+)
		// if points <= 0 then 之前消费获得了积分(+)，现在返还积分(-)
		if (points.compareTo(BigDecimal.ZERO) == 1) {
			// 更新消费积分
			s = s.add(points);
		} else {
			// 更新会员积分和消费积分
			p = p.add(points);
			s = s.add(points);
		}

		boolean down = false;
		BigDecimal mlp = BigDecimal.ZERO;

		// if 消费兑换积分 then 需要判断 会员等级降级
		if (points.compareTo(BigDecimal.ZERO) == -1) {
			// 1. 获取用户所有 注册 and 升级 and 降级 会员赠送积分
			String[] source =
				new String[] { IMemberPointsService.POINTS_SOURCE_REGISTER, IMemberPointsService.POINTS_SOURCE_UPGRADE,
					IMemberPointsService.POINTS_SOURCE_DOWNGRADE };

			BigDecimal points4Member = getMemberPoints(userId.trim(), shopId.trim(), source);

			// 2. member.getPoints() - points - points4Member = 除去升级赠送积分
			// then 从低到高 重新判断会员等级
			BigDecimal pp = p.add(points4Member.negate());

			List<MemberLevel> memberLevelList = memberLevelService.getMemberLevelList(shopId.trim());

			if (memberLevelList == null || memberLevelList.size() == 0) {
				result.setCode("会员等级不存在！");
				return result;
			}

			for (MemberLevel level : memberLevelList) {
				// p 大于或者等级 会员等级的区别范围
				if (pp.compareTo(level.getStartPoints()) == 1 || pp.compareTo(level.getStartPoints()) == 0) {
					pp = pp.add(level.getPoints());
				} else {
					break;
				}
			}

			// 3. 获取退还 points 后，对应会员等级 用于后面 判断是否需要会员降级
			MemberLevel memberLevel = memberLevelService.getMemberLevel(shopId, pp);
			if (memberLevel == null) {
				result.setCode("会员等级不存在！");
				return result;
			}

			// 4. 判断是否完成会员升级 需要降级
			if (member.getRank() > memberLevel.getRank()) {
				down = true;

				member.setLevelId(memberLevel.getLevelId());
				// 可能存在降多级情况
				mlp = getPoints(shopId, member.getRank(), memberLevel.getRank());
			}

		}

		final boolean downgrade = down;
		final BigDecimal memberLevelPoints = mlp;

		// 会员降级 减少积分
		if (downgrade) {
			p = p.add(memberLevelPoints);
			s = s.add(memberLevelPoints);
		}

		// 更新会员积分
		member.setPoints(p);
		// 存在消费积分为负数的情况
		member.setSurplusPoints(s.compareTo(BigDecimal.ZERO) == -1 ? BigDecimal.ZERO : s);

		BooleanResult res = (BooleanResult) transactionTemplate.execute(new TransactionCallback() {
			public Object doInTransaction(TransactionStatus ts) {
				// 3. 新增会员积分日志记录 createMemberPoints
				BooleanResult result = createMemberPoints(member.getMemId(), points, pointsSource, tradeId);
				if (!result.getResult()) {
					ts.setRollbackOnly();

					return result;
				}

				String memPointsId = result.getCode();

				// 4. 会员降级 返还积分
				if (downgrade) {
					result =
						createMemberPoints(member.getMemId(), memberLevelPoints,
							IMemberPointsService.POINTS_SOURCE_DOWNGRADE);
					if (!result.getResult()) {
						ts.setRollbackOnly();

						return result;
					}
				}

				// 5. 更新会员信息(消费积分)
				result = memberService.updateMember(member, userId);
				if (!result.getResult()) {
					ts.setRollbackOnly();

					return result;
				}

				// 成功后 返回积分id
				result.setCode(memPointsId);
				result.setResult(true);
				return result;
			}
		});

		// 更新成功 remove cache
		if (res.getResult()) {
			remove(shopId, userId);
		}

		return res;
	}

	/**
	 * 查询 Rank from to 的积分赠送情况.
	 * 
	 * @param shopId
	 * @param from
	 * @param to
	 * @return
	 */
	private BigDecimal getPoints(String shopId, int from, int to) {
		if (from == to) {
			return BigDecimal.ZERO;
		}

		List<MemberLevel> memberLevelList = memberLevelService.getMemberLevelList(shopId);
		// 不存在
		if (memberLevelList == null || memberLevelList.size() == 0) {
			return BigDecimal.ZERO;
		}

		BigDecimal points = BigDecimal.ZERO;
		int f = from;
		int t = to;
		boolean flag;

		// 从小到大 升级 赠送积分(+)
		if (from < to) {
			flag = true;
		} else {
			// 返还积分(-)
			flag = false;

			f = to;
			t = from;
		}

		for (MemberLevel level : memberLevelList) {
			if (level.getRank() <= f) {
				continue;
			}

			if (level.getRank() > f) {
				points = points.add(level.getPoints());
			}

			if (level.getRank() == t) {
				break;
			}
		}

		// 判断返回赠送还是返还
		if (flag) {
			return points;
		} else {
			return points.negate();
		}
	}

	@Override
	public BooleanResult createMemberPoints(String memId, BigDecimal points, String pointsSource) {
		return createMemberPoints(memId, points, pointsSource, null);
	}

	private BooleanResult createMemberPoints(String memId, BigDecimal points, String pointsSource, String tradeId) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		MemberPoints memberPoints = new MemberPoints();

		if (StringUtils.isBlank(memId)) {
			result.setCode("会员信息不能为空！");
			return result;
		}

		memberPoints.setMemId(memId.trim());

		if (points == null) {
			result.setCode("积分信息不能为空！");
			return result;
		}

		memberPoints.setPoints(points);

		if (StringUtils.isBlank(pointsSource)) {
			result.setCode("积分来源信息不能为空！");
			return result;
		}

		memberPoints.setPointsSource(pointsSource.trim());

		memberPoints.setTradeId(tradeId);

		try {
			String memPointsId = memberPointsDao.createMemberPoints(memberPoints);
			result.setCode(memPointsId);
			result.setResult(true);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(memberPoints), e);

			result.setCode("创建积分日志失败！");
		}

		return result;
	}

	@Override
	public List<MemberPoints> getMemberPointsList(String userId, String shopId, MemberPoints memberPoints) {
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(shopId) || memberPoints == null) {
			return null;
		}

		memberPoints.setUserId(userId.trim());
		memberPoints.setShopId(shopId.trim());

		return getMemberPointsList(memberPoints);
	}

	@Override
	public List<MemberPoints> getMemberPointsList(String userId, String shopId, String tradeId) {
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(shopId) || StringUtils.isBlank(tradeId)) {
			return null;
		}

		MemberPoints memberPoints = new MemberPoints();
		memberPoints.setUserId(userId.trim());
		memberPoints.setShopId(shopId.trim());
		memberPoints.setTradeId(tradeId.trim());

		return getMemberPointsList(memberPoints);
	}

	private List<MemberPoints> getMemberPointsList(MemberPoints memberPoints) {
		try {
			return memberPointsDao.getMemberPointsList(memberPoints);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(memberPoints), e);
		}

		return null;
	}

	@Override
	public BigDecimal getMemberPoints(String userId, String shopId, String[] pointsSource) {
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(shopId)) {
			return BigDecimal.ZERO;
		}

		if (pointsSource == null || pointsSource.length == 0) {
			return BigDecimal.ZERO;
		}

		MemberPoints memberPoints = new MemberPoints();
		memberPoints.setUserId(userId.trim());
		memberPoints.setShopId(shopId.trim());
		memberPoints.setCodes(pointsSource);

		try {
			return memberPointsDao.getMemberPoints(memberPoints);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(memberPoints), e);
		}

		return BigDecimal.ZERO;
	}

	private void remove(String shopId, String userId) {
		try {
			memcachedCacheService.remove(IMemcachedCacheService.CACHE_KEY_MEMBER_SHOP_USER_ID + shopId.trim() + "&"
				+ userId.trim());
		} catch (ServiceException e) {
			logger.error(IMemcachedCacheService.CACHE_KEY_MEMBER_SHOP_USER_ID + shopId + "&" + userId, e);
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

	public IMemberService getMemberService() {
		return memberService;
	}

	public void setMemberService(IMemberService memberService) {
		this.memberService = memberService;
	}

	public IMemberLevelService getMemberLevelService() {
		return memberLevelService;
	}

	public void setMemberLevelService(IMemberLevelService memberLevelService) {
		this.memberLevelService = memberLevelService;
	}

	public IMemberPointsDao getMemberPointsDao() {
		return memberPointsDao;
	}

	public void setMemberPointsDao(IMemberPointsDao memberPointsDao) {
		this.memberPointsDao = memberPointsDao;
	}

}
