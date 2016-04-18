package com.kintiger.mall.points.dao;

import java.util.List;

import com.kintiger.mall.api.points.bo.PointsCard;

/**
 * 积分卡.
 * 
 * @author xujiakun
 * 
 */
public interface IPointsCardDao {

	/**
	 * 
	 * @param pointsCard
	 * @return
	 */
	int getPointsCardCount(PointsCard pointsCard);

	/**
	 * 
	 * @param pointsCard
	 * @return
	 */
	List<PointsCard> getPointsCardList(PointsCard pointsCard);

	/**
	 * 
	 * @param pointsCard
	 * @param quantity
	 * @param modifyUser
	 * @return
	 */
	int createPointsCard(PointsCard pointsCard, int quantity, String modifyUser);

	/**
	 * 
	 * @param pointsCard
	 * @return
	 */
	PointsCard getPointsCard(PointsCard pointsCard);

	/**
	 * 
	 * @param statementName
	 * @param pointsCard
	 * @return
	 */
	int updatePointsCard(String statementName, PointsCard pointsCard);

}
