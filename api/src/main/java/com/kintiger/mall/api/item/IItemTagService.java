package com.kintiger.mall.api.item;

import com.kintiger.mall.framework.bo.BooleanResult;

/**
 * 商品 + 标签.
 * 
 * @author
 * 
 */
public interface IItemTagService {

	/**
	 * 删除某商品的标签.
	 * 
	 * @param itemId
	 * @param modifyUser
	 * @return
	 */
	BooleanResult deleteItemTag(String itemId, String modifyUser);

}
