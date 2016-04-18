package com.kintiger.mall.advert.dao;

import java.util.List;

import com.kintiger.mall.api.advert.bo.Advert;

/**
 * 广告.
 * 
 * @author xujiakun
 * 
 */
public interface IAdvertDao {

	/**
	 * 
	 * @param advert
	 * @return
	 */
	int getAdvertCount(Advert advert);

	/**
	 * 
	 * @param advert
	 * @return
	 */
	List<Advert> getAdvertList(Advert advert);

	/**
	 * 
	 * @param advert
	 * @return
	 */
	Advert getAdvert(Advert advert);

	/**
	 * 
	 * @param advert
	 * @return
	 */
	List<Advert> getAdvertStats(Advert advert);

}
