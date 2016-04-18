package com.kintiger.mall.item.service.impl;

import org.apache.commons.lang.StringUtils;

import com.kintiger.mall.api.item.IItemTagService;
import com.kintiger.mall.api.item.bo.ItemTag;
import com.kintiger.mall.framework.bo.BooleanResult;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;
import com.kintiger.mall.framework.util.LogUtil;
import com.kintiger.mall.item.dao.IItemTagDao;

/**
 * 商品 + 标签.
 * 
 * @author xujiakun
 * 
 */
public class ItemTagServiceImpl implements IItemTagService {

	private Logger4jExtend logger = Logger4jCollection.getLogger(ItemTagServiceImpl.class);

	private IItemTagDao itemTagDao;

	@Override
	public BooleanResult deleteItemTag(String itemId, String modifyUser) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		ItemTag itemTag = new ItemTag();

		if (StringUtils.isBlank(itemId)) {
			result.setCode("商品信息不能为空！");
			return result;
		}

		itemTag.setItemId(itemId.trim());

		if (StringUtils.isBlank(modifyUser)) {
			result.setCode("操作人不能为空！");
			return result;
		}

		itemTag.setModifyUser(modifyUser.trim());

		try {
			itemTagDao.deleteItemTag(itemTag);
			result.setResult(true);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(itemTag), e);
		}

		return result;
	}

	public IItemTagDao getItemTagDao() {
		return itemTagDao;
	}

	public void setItemTagDao(IItemTagDao itemTagDao) {
		this.itemTagDao = itemTagDao;
	}

}
