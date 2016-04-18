package com.kintiger.mall.favorite.dao.impl;

import java.util.List;

import com.kintiger.mall.api.favorite.bo.Favorite;
import com.kintiger.mall.favorite.dao.IFavoriteDao;
import com.kintiger.mall.framework.dao.impl.BaseDaoImpl;

/**
 * 
 * @author xujiakun
 * 
 */
public class FavoriteDaoImpl extends BaseDaoImpl implements IFavoriteDao {

	public int getFavoriteCount(Favorite favorite) {
		return (Integer) getSqlMapClientTemplate().queryForObject("favorite.getFavoriteCount", favorite);
	}

	@SuppressWarnings("unchecked")
	public List<Favorite> getFavoriteList(Favorite favorite) {
		return (List<Favorite>) getSqlMapClientTemplate().queryForList("favorite.getFavoriteList", favorite);
	}

	public String createFavorite(Favorite favorite) {
		return (String) getSqlMapClientTemplate().insert("favorite.createFavorite", favorite);
	}

	public String addFavorite(Favorite favorite) {
		return (String) getSqlMapClientTemplate().insert("favorite.addFavorite", favorite);
	}

	public int updateFavorite(Favorite favorite) {
		return getSqlMapClientTemplate().update("favorite.updateFavorite", favorite);
	}

	public int deleteFavorite(Favorite favorite) {
		return getSqlMapClientTemplate().delete("favorite.deleteFavorite", favorite);
	}

	public Favorite getFavorite(Favorite favorite) {
		return (Favorite) getSqlMapClientTemplate().queryForObject("favorite.getFavorite", favorite);
	}

}
