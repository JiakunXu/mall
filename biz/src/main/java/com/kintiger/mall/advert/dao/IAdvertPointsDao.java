package com.kintiger.mall.advert.dao;

import java.util.List;

import com.kintiger.mall.api.advert.bo.AdvertPoints;

/**
 * 
 * @author xujiakun
 * 
 */
public interface IAdvertPointsDao {

	/**
	 * 
	 * @param advertPoints
	 * @return
	 */
	AdvertPoints getAdvertPoints(AdvertPoints advertPoints);

	/**
	 * 
	 * @param advertPoints
	 * @return
	 */
	String createAdvertPoints(AdvertPoints advertPoints);

	/**
	 * 
	 * @param advertPoints
	 * @return
	 */
	int updateAdvertPoints(AdvertPoints advertPoints);

	/**
	 * 
	 * @param advertPoints
	 * @return
	 */
	List<AdvertPoints> getAdvertStats(AdvertPoints advertPoints);

	/**
	 * 
	 * @param advertPoints
	 * @return
	 */
	List<AdvertPoints> getAdvertPointsList(AdvertPoints advertPoints);

}
