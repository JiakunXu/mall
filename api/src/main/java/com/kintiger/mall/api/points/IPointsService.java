package com.kintiger.mall.api.points;

import java.util.List;

import com.kintiger.mall.api.points.bo.Points;
import com.kintiger.mall.framework.bo.BooleanResult;

/**
 * 积分兑换规则.
 * 
 * @author xujiakun
 * 
 */
public interface IPointsService {

	// >>>>>>>>>>以下是卖家<<<<<<<<<<

	/**
	 * 积分兑换规则.
	 * 
	 * @param shopId
	 * @param points
	 * @return
	 */
	int getPointsCount(String shopId, Points points);

	/**
	 * 积分兑换规则.
	 * 
	 * @param shopId
	 * @param points
	 * @return
	 */
	List<Points> getPointsList(String shopId, Points points);

	/**
	 * 新增积分兑换商品.
	 * 
	 * @param shopId
	 * @param itemId
	 *            商品编号.
	 * @param points
	 *            所需积分.
	 * @param expireDate
	 *            活动过期时间.
	 * @param modifyUser
	 * @return
	 */
	BooleanResult createPoints(String shopId, String itemId, String points, String expireDate, String modifyUser);

	/**
	 * 新增积分兑换商品.
	 * 
	 * @param shopId
	 * @param itemId
	 *            商品编号.
	 * @param skuId
	 *            规格编号.
	 * @param points
	 *            所需积分.
	 * @param expireDate
	 *            活动过期时间.
	 * @param modifyUser
	 * @return
	 */
	BooleanResult createPoints(String shopId, String itemId, String skuId, String points, String expireDate,
		String modifyUser);

	/**
	 * 积分商品下架.
	 * 
	 * @param shopId
	 * @param pointsId
	 * @param modifyUser
	 * @return
	 */
	BooleanResult deletePoints(String shopId, String pointsId, String modifyUser);

	// >>>>>>>>>>以下是买家<<<<<<<<<<

	/**
	 * 积分兑换 生成 订单.
	 * 
	 * @param userId
	 * @param itemId
	 * @param skuId
	 * @param pointsId
	 * @param quantity
	 * @return
	 */
	BooleanResult exchangePoints(String userId, String itemId, String skuId, String pointsId, String quantity);

	/**
	 * 积分充值.
	 * 
	 * @param userId
	 * @param shopId
	 * @param cardNo
	 *            积分卡号.
	 * @param password
	 *            密码.
	 * @return
	 */
	BooleanResult rechargePoints(String userId, String shopId, String cardNo, String password);

}
