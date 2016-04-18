package com.kintiger.mall.favorite.dao;

import java.util.List;

import com.kintiger.mall.api.favorite.bo.Favorite;

/**
 * 
 * @author xujiakun
 * 
 */
public interface IFavoriteDao {

	/**
	 * 
	 * @param favorite
	 * @return
	 */
	int getFavoriteCount(Favorite favorite);

	/**
	 * 
	 * @param favorite
	 * @return
	 */
	List<Favorite> getFavoriteList(Favorite favorite);

	/**
	 * 
	 * @param favorite
	 * @return
	 */
	String createFavorite(Favorite favorite);

	/**
	 * 
	 * @param favorite
	 * @return
	 */
	String addFavorite(Favorite favorite);

	/**
	 * 
	 * @param favorite
	 * @return
	 */
	int updateFavorite(Favorite favorite);

	/**
	 * 
	 * @param favorite
	 * @return
	 */
	int deleteFavorite(Favorite favorite);

	/**
	 * 
	 * @param favorite
	 * @return
	 */
	Favorite getFavorite(Favorite favorite);

}
