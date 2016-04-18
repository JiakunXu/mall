package com.kintiger.mall.item.dao;

import java.util.List;

import com.kintiger.mall.api.item.bo.ItemSku;

/**
 * sku.
 * 
 * @author xujiakun
 * 
 */
public interface IItemSkuDao {

	/**
	 * 批量创建sku.
	 * 
	 * @param itemId
	 * @param skuList
	 * @param verId
	 * @param modifyUser
	 * @return
	 */
	int createItemSku(String itemId, List<ItemSku> skuList, String verId, String modifyUser);

	/**
	 * 
	 * @param sku
	 * @return
	 */
	ItemSku getItemSku(ItemSku sku);

	/**
	 * 
	 * @param sku
	 * @return
	 */
	List<ItemSku> getItemSkuList(ItemSku sku);

	/**
	 * 
	 * @param sku
	 * @return
	 */
	int deleteItemSku(ItemSku sku);

	/**
	 * 
	 * @param skuList
	 * @param modifyUser
	 * @return
	 */
	int updateItemSku(List<ItemSku> skuList, String modifyUser);

}
