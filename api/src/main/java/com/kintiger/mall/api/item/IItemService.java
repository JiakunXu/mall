package com.kintiger.mall.api.item;

import java.util.List;

import com.kintiger.mall.api.item.bo.Item;
import com.kintiger.mall.framework.bo.BooleanResult;

/**
 * 商品接口.
 * 
 * @author xujiakun
 * 
 */
public interface IItemService {

	String STATS_TYPE_SOLD = "sold";

	String STATS_TYPE_SEARCH = "search";

	String STATS_TYPE_STOCK = "stock";

	String STATS_TYPE_EXCHANGE = "exchange";

	/**
	 * 卖家 or 买家 查询某店铺商品.
	 * 
	 * @param shopId
	 *            必填.
	 * @param item
	 * @return
	 */
	int getItemCount(String shopId, Item item);

	/**
	 * 卖家 or 买家 查询某店铺商品.
	 * 
	 * @param shopId
	 *            必填.
	 * @param item
	 * @return
	 */
	List<Item> getItemList(String shopId, Item item);

	/**
	 * 获取店铺概况统计.
	 * 
	 * @param shopId
	 * @param type
	 * @return
	 */
	List<Item> getItemStats(String shopId, String type);

	/**
	 * 七天搜索商品排名.
	 * 
	 * @param shopId
	 * @return
	 */
	List<Item> getItemStats(String shopId);

	/**
	 * 创建商品.
	 * 
	 * @param shopId
	 * @param item
	 *            商品概览.
	 * @param modifyUser
	 * @return
	 */
	BooleanResult createItem(String shopId, Item item, String modifyUser);

	/**
	 * 卖家 or 买家 获取商品基本信息.
	 * 
	 * @param shopId
	 * @param itemId
	 * @return
	 */
	Item getItem(String shopId, String itemId);

	/**
	 * 买家获取商品基本信息.
	 * 
	 * @param shopId
	 * @param itemId
	 * @param skuId
	 * @return
	 */
	Item getItem(String shopId, String itemId, String skuId);

	/**
	 * 编辑商品.
	 * 
	 * @param shopId
	 * @param itemId
	 * @param item
	 *            商品概览.
	 * @param modifyUser
	 * @return
	 */
	BooleanResult updateItem(String shopId, String itemId, Item item, String modifyUser);

	/**
	 * 更新商品库存.
	 * 
	 * @param shopId
	 * @param itemList
	 * @param modifyUser
	 * @return
	 */
	BooleanResult updateItemStock(String shopId, List<Item> itemList, String modifyUser);

	/**
	 * 更新商品库存.
	 * 
	 * @param shopId
	 * @param itemId
	 * @param modifyUser
	 * @return
	 */
	BooleanResult updateItemStock(String shopId, String[] itemId, String modifyUser);

	/**
	 * 卖家删除商品.
	 * 
	 * @param shopId
	 * @param itemId
	 * @param item
	 *            商品概览.
	 * @param modifyUser
	 * @return
	 */
	BooleanResult deleteItem(String shopId, String itemId, String modifyUser);

}
