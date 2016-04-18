package com.kintiger.mall.advert.dao;

import java.util.List;

import com.kintiger.mall.api.advert.bo.AdvertStats;

/**
 * 广告统计.
 * 
 * @author xujiakun
 * 
 */
public interface IAdvertStatsDao {

	/**
	 * 
	 * @param advertStatsList
	 * @param modifyUser
	 * @return
	 */
	int createAdvertStats(List<AdvertStats> advertStatsList, String modifyUser);

	/**
	 * 
	 * @param advertStats
	 * @return
	 */
	List<AdvertStats> getAdvertStats(AdvertStats advertStats);

}
