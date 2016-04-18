package com.kintiger.mall.api.advert;

import java.util.List;

import com.kintiger.mall.api.advert.bo.AdvertPoints;
import com.kintiger.mall.api.advert.bo.AdvertStats;
import com.kintiger.mall.framework.bo.BooleanResult;

/**
 * 广告赠送积分.
 * 
 * @author xujiakun
 * 
 */
public interface IAdvertPointsService {

	/**
	 * 根据 advertStats 判断是否赠送积分.
	 * 
	 * @param advertStatsList
	 * @param modifyUser
	 * @return
	 */
	BooleanResult createAdvertPoints(List<AdvertStats> advertStatsList, String modifyUser);

	/**
	 * 广告点击赠送积分统计(调用前必须先验证 advertId 是否属于店铺).
	 * 
	 * @param advertId
	 * @param gmtStart
	 * @param gmtEnd
	 * @return
	 */
	List<AdvertPoints> getAdvertStats(String advertId, String gmtStart, String gmtEnd);

	/**
	 * 查询某店铺某时间段 广告赠送积分信息.
	 * 
	 * @param shopId
	 * @param gmtStart
	 * @param gmtEnd
	 * @return
	 */
	List<AdvertPoints> getAdvertPointsList(String shopId, String gmtStart, String gmtEnd);

}
