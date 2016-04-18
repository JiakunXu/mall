package com.kintiger.mall.item.dao;

import com.kintiger.mall.api.item.bo.ItemTag;

/**
 * 商品 tag dao 接口.
 * 
 * @author limin
 * 
 */
public interface IItemTagDao {

	/**
	 * 
	 * @param itemTag
	 * @return
	 */
	int deleteItemTag(ItemTag itemTag);

}
