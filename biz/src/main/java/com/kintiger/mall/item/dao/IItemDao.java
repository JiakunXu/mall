package com.kintiger.mall.item.dao;

import java.util.List;

import com.kintiger.mall.api.item.bo.Item;

/**
 * 商品 dao 接口.
 * 
 * @author xujiakun
 * 
 */
public interface IItemDao {

	/**
	 * 
	 * @param item
	 * @return
	 */
	int getItemCount(Item item);

	/**
	 * 
	 * @param item
	 * @return
	 */
	List<Item> getItemList(Item item);

	/**
	 * 
	 * @param statementName
	 * @param shopId
	 * @return
	 */
	List<Item> getItemStats(String statementName, String shopId);

	/**
	 * 
	 * @param item
	 * @return
	 */
	String createItem(Item item);

	/**
	 * 
	 * @param item
	 * @return
	 */
	Item getItem(Item item);

	/**
	 * 
	 * @param item
	 * @return
	 */
	int updateItem(Item item);

	/**
	 * 
	 * @param shopId
	 * @param itemList
	 * @param modifyUser
	 * @return
	 */
	int updateItem(String shopId, List<Item> itemList, String modifyUser);

	/**
	 * 
	 * @param shopId
	 * @param itemId
	 * @param modifyUser
	 * @return
	 */
	int updateItem(String shopId, String[] itemId, String modifyUser);

	/**
	 * 逻辑删除商品.
	 * 
	 * @param item
	 * @return
	 */
	int deleteItem(Item item);

}
