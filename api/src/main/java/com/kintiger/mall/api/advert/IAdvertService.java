package com.kintiger.mall.api.advert;

import java.util.List;

import com.kintiger.mall.api.advert.bo.Advert;
import com.kintiger.mall.framework.bo.BooleanResult;

/**
 * 广告.
 * 
 * @author xujiakun
 * 
 */
public interface IAdvertService {

	// >>>>>>>>>>以下是卖家<<<<<<<<<<

	/**
	 * 获取广告信息定义.
	 * 
	 * @param shopId
	 * @param advert
	 * @return
	 */
	int getAdvertCount(String shopId, Advert advert);

	/**
	 * 获取广告信息定义.
	 * 
	 * @param shopId
	 * @param advert
	 * @return
	 */
	List<Advert> getAdvertList(String shopId, Advert advert);

	/**
	 * 广告统计.
	 * 
	 * @param shopId
	 * @param advertId
	 * @param gmtStart
	 * @param gmtEnd
	 * @return
	 */
	List<Advert> getAdvertStats(String shopId, String advertId, String gmtStart, String gmtEnd);

	/**
	 * 广告统计.
	 * 
	 * @param shopId
	 * @param gmtStart
	 * @param gmtEnd
	 * @return
	 */
	List<Advert> getAdvertStats(String shopId, String gmtStart, String gmtEnd);

	// >>>>>>>>>>以下是买家<<<<<<<<<<

	/**
	 * 
	 * @param userId
	 * @param ip
	 * @param shopId
	 * @param advertId
	 * @return
	 */
	BooleanResult clickAdvert(String userId, String ip, String shopId, String advertId);

	// >>>>>>>>>>以下是后台<<<<<<<<<<

	/**
	 * 
	 * @param advertId
	 * @return
	 */
	Advert getAdvert(String advertId);

}
