package com.kintiger.mall.api.advert;

import java.util.List;

import com.kintiger.mall.api.advert.bo.AdvertStats;
import com.kintiger.mall.framework.bo.BooleanResult;

/**
 * 广告统计(点击).
 * 
 * @author xujiakun
 * 
 */
public interface IAdvertStatsService {

	/**
	 * 暂存广告点击记录.
	 * 
	 * @param userId
	 * @param ip
	 * @param advertId
	 * @return
	 */
	BooleanResult createdAdvertStats(String userId, String ip, String advertId);

	/**
	 * 存数据库.
	 * 
	 * @return
	 */
	boolean createAdvertStats();

	/**
	 * 广告点击统计(调用前必须先验证 advertId 是否属于店铺).
	 * 
	 * @param advertId
	 * @param gmtStart
	 * @param gmtEnd
	 * @return
	 */
	List<AdvertStats> getAdvertStats(String advertId, String gmtStart, String gmtEnd);

}
