package com.kintiger.mall.item.dao.impl;

import com.kintiger.mall.api.item.bo.ItemTag;
import com.kintiger.mall.framework.dao.impl.BaseDaoImpl;
import com.kintiger.mall.item.dao.IItemTagDao;

/**
 * 商品tag dao 现实.
 * 
 * @author limin
 * 
 */
public class ItemTagDaoImpl extends BaseDaoImpl implements IItemTagDao {

	@Override
	public int deleteItemTag(ItemTag itemTag) {
		return getSqlMapClientTemplate().update("item.tag.updateItemTag", itemTag);
	}

}
