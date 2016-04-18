package com.kintiger.mall.api.points;

import java.util.List;

import com.kintiger.mall.api.points.bo.PointsCard;
import com.kintiger.mall.framework.bo.BooleanResult;

/**
 * 积分卡.
 * 
 * @author xujiakun
 * 
 */
public interface IPointsCardService {

	// >>>>>>>>>>以下是卖家<<<<<<<<<<

	/**
	 * 积分卡.
	 * 
	 * @param shopId
	 * @param pointsCard
	 * @return
	 */
	int getPointsCardCount(String shopId, PointsCard pointsCard);

	/**
	 * 积分卡.
	 * 
	 * @param shopId
	 * @param pointsCard
	 * @return
	 */
	List<PointsCard> getPointsCardList(String shopId, PointsCard pointsCard);

	/**
	 * 获取积分卡兑换统计.
	 * 
	 * @param shopId
	 * @param gmtStart
	 * @param gmtEnd
	 * @return
	 */
	List<PointsCard> getPointsCardStats(String shopId, String gmtStart, String gmtEnd);

	/**
	 * 创建积分卡.
	 * 
	 * @param shopId
	 * @param pointsCard
	 * @param quantity
	 * @param modifyUser
	 * @return
	 */
	BooleanResult createPointsCard(String shopId, PointsCard pointsCard, String quantity, String modifyUser);

	// >>>>>>>>>>以下是买家<<<<<<<<<<

	/**
	 * 获取积分卡信息.
	 * 
	 * @param shopId
	 * @param cardNo
	 * @param password
	 * @return
	 */
	PointsCard getPointsCard(String shopId, String cardNo, String password);

	/**
	 * 积分卡使用.
	 * 
	 * @param shopId
	 * @param cardNo
	 * @param password
	 * @param userId
	 *            充值人.
	 * @param modifyUser
	 *            操作人.
	 * @return
	 */
	BooleanResult usePointsCard(String shopId, String cardNo, String password, String userId, String modifyUser);

}
