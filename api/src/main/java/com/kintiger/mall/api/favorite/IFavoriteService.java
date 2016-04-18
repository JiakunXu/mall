package com.kintiger.mall.api.favorite;

import java.util.List;

import com.kintiger.mall.api.favorite.bo.Favorite;
import com.kintiger.mall.framework.bo.BooleanResult;

/**
 * 
 * @author xujiakun
 * 
 */
public interface IFavoriteService {

	String ERROR_MESSAGE = "操作失败！";

	int getFavoriteCount(Favorite favorite);

	/**
	 * 获取收藏夹.
	 * 
	 * @param userId
	 * @param node
	 * @return
	 */
	List<Favorite> getFavoriteList(String userId, String node);

	/**
	 * 获取收藏夹.
	 * 
	 * @param favorite
	 * @return
	 */
	List<Favorite> getFavoriteList(Favorite favorite);

	/**
	 * 创建收藏夹.
	 * 
	 * @param favorite
	 * @return
	 */
	BooleanResult createFavorite(Favorite favorite);

	/**
	 * 添加菜单到收藏夹.
	 * 
	 * @param favorite
	 * @return
	 */
	BooleanResult addFavorite(Favorite favorite);

	/**
	 * 修改收藏夹.
	 * 
	 * @param favorite
	 * @return
	 */
	BooleanResult updateFavorite(Favorite favorite);

	/**
	 * 删除收藏夹.
	 * 
	 * @param favorite
	 * @return
	 */
	BooleanResult deleteFavorite(Favorite favorite);

	/**
	 * 调整收藏夹层级.
	 * 
	 * @param favoriteList
	 * @param userId
	 * @return
	 */
	BooleanResult adjustFavoriteLevel(List<Favorite> favoriteList, String userId);

}
