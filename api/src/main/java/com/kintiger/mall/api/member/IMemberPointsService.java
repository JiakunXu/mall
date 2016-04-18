package com.kintiger.mall.api.member;

import java.math.BigDecimal;
import java.util.List;

import com.kintiger.mall.api.member.bo.MemberPoints;
import com.kintiger.mall.framework.bo.BooleanResult;

/**
 * 积分记录接口(记录每次购物新增的积分 换购消费的积分，积分合计写member).
 * 
 * @author xujiakun
 * 
 */
public interface IMemberPointsService {

	String POINTS_SOURCE_REGISTER = "注册会员获取积分";

	String POINTS_SOURCE_TRADE = "消费获取积分";

	String POINTS_SOURCE_EXCHANGE = "消费兑换积分";

	String POINTS_SOURCE_UPGRADE = "会员升级获取积分";

	String POINTS_SOURCE_DOWNGRADE = "会员降级返还积分";

	String POINTS_SOURCE_MEMBER_INFO = "会员信息完善获取积分";

	String POINTS_SOURCE_POINTS_CARD = "积分卡充值获取积分";

	String POINTS_SOURCE_RECOMMEND = "推荐新会员获取积分";

	String POINTS_SOURCE_ADVERT = "点击广告获取积分";

	/**
	 * 记录会员积分 增加 或 减少(会员合计积分 积分记录 是否完成会员升级).
	 * 
	 * @param userId
	 * @param shopId
	 * @param points
	 * @param pointsSource
	 * @return
	 */
	BooleanResult recordMemberPoints(String userId, String shopId, BigDecimal points, String pointsSource);

	/**
	 * 记录会员积分 增加 或 减少(会员合计积分 积分记录 是否完成会员升级).
	 * 
	 * @param userId
	 * @param shopId
	 * @param points
	 * @param pointsSource
	 * @param tradeId
	 * @return
	 */
	BooleanResult recordMemberPoints(String userId, String shopId, BigDecimal points, String pointsSource,
		String tradeId);

	/**
	 * 验证返还积分 是否有效.
	 * 
	 * @param userId
	 * @param shopId
	 * @param tradeId
	 * @return
	 */
	BooleanResult validateReturnPoints(String userId, String shopId, String tradeId);

	/**
	 * 返还积分(消费合计积分 积分记录).
	 * 
	 * @param userId
	 * @param shopId
	 * @param points
	 * @param pointsSource
	 * @param tradeId
	 * @return
	 */
	BooleanResult returnMemberPoints(String userId, String shopId, BigDecimal points, String pointsSource,
		String tradeId);

	/**
	 * 创建积分记录.
	 * 
	 * @param memId
	 * @param points
	 * @param pointsSource
	 * @return
	 */
	BooleanResult createMemberPoints(String memId, BigDecimal points, String pointsSource);

	/**
	 * 查询个人积分记录.
	 * 
	 * @param userId
	 * @param shopId
	 * @param memberPoints
	 * @return
	 */
	List<MemberPoints> getMemberPointsList(String userId, String shopId, MemberPoints memberPoints);

	/**
	 * 查询个人某次交易积分记录.
	 * 
	 * @param userId
	 * @param shopId
	 * @param tradeId
	 * @return
	 */
	List<MemberPoints> getMemberPointsList(String userId, String shopId, String tradeId);

	/**
	 * 查询个人pointsSource的汇总积分.
	 * 
	 * @param userId
	 * @param shopId
	 * @param pointsSource
	 * @return
	 */
	BigDecimal getMemberPoints(String userId, String shopId, String[] pointsSource);

}
