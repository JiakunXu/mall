package com.kintiger.mall.api.item;

import java.util.List;

import com.kintiger.mall.api.item.bo.ItemSku;
import com.kintiger.mall.framework.bo.BooleanResult;

/**
 * 
 * @author xujiakun
 * 
 */
public interface IItemSkuService {

	/**
	 * 批量创建sku.
	 * 
	 * @param itemId
	 * @param skuList
	 * @param modifyUser
	 * @return
	 */
	BooleanResult createItemSku(String itemId, List<ItemSku> skuList, String modifyUser);

	/**
	 * 
	 * @param shopId
	 * @param itemId
	 * @param skuId
	 * @return
	 */
	ItemSku getItemSku(String shopId, String itemId, String skuId);

	/**
	 * 
	 * @param shopId
	 * @param itemId
	 * @return
	 */
	List<ItemSku> getItemSkuList(String shopId, String itemId);

	/**
	 * 
	 * @param itemId
	 * @param modifyUser
	 * @return
	 */
	BooleanResult deleteItemSku(String itemId, String modifyUser);

	/**
	 * 
	 * @param skuList
	 * @param modifyUser
	 * @return
	 */
	BooleanResult updateItemSkuStock(List<ItemSku> skuList, String modifyUser);

}
