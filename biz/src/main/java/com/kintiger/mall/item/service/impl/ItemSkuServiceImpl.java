package com.kintiger.mall.item.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.kintiger.mall.api.item.IItemSkuService;
import com.kintiger.mall.api.item.bo.ItemSku;
import com.kintiger.mall.framework.bo.BooleanResult;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;
import com.kintiger.mall.framework.util.LogUtil;
import com.kintiger.mall.framework.util.UUIDUtil;
import com.kintiger.mall.item.dao.IItemSkuDao;

/**
 * 
 * @author xujiakun
 * 
 */
public class ItemSkuServiceImpl implements IItemSkuService {

	private Logger4jExtend logger = Logger4jCollection.getLogger(ItemSkuServiceImpl.class);

	private IItemSkuDao itemSkuDao;

	@Override
	public BooleanResult createItemSku(String itemId, List<ItemSku> skuList, String modifyUser) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		if (StringUtils.isBlank(itemId)) {
			result.setCode("商品信息不能为空！");
			return result;
		}

		if (skuList == null || skuList.size() == 0) {
			result.setCode("商品规格信息不能为空！");
			return result;
		}

		if (StringUtils.isBlank(modifyUser)) {
			result.setCode("操作人信息不能为空！");
			return result;
		}

		try {
			itemSkuDao.createItemSku(itemId, skuList, UUIDUtil.generate(), modifyUser);
			result.setResult(true);
		} catch (Exception e) {
			logger.error("itemId:" + itemId + LogUtil.parserBean(skuList) + "modifyUser:" + modifyUser, e);

			result.setCode("创建SKU信息失败！");
			return result;
		}

		return result;
	}

	@Override
	public ItemSku getItemSku(String shopId, String itemId, String skuId) {
		if (StringUtils.isBlank(shopId) || StringUtils.isBlank(itemId) || StringUtils.isBlank(skuId)) {
			return null;
		}

		ItemSku sku = new ItemSku();
		sku.setShopId(shopId.trim());
		sku.setItemId(itemId.trim());
		sku.setSkuId(skuId.trim());

		try {
			return itemSkuDao.getItemSku(sku);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(sku), e);
		}

		return null;
	}

	@Override
	public List<ItemSku> getItemSkuList(String shopId, String itemId) {
		if (StringUtils.isBlank(shopId) || StringUtils.isBlank(itemId)) {
			return null;
		}

		ItemSku sku = new ItemSku();
		sku.setShopId(shopId.trim());
		sku.setItemId(itemId.trim());

		try {
			return itemSkuDao.getItemSkuList(sku);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(sku), e);
		}

		return null;
	}

	@Override
	public BooleanResult deleteItemSku(String itemId, String modifyUser) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		ItemSku sku = new ItemSku();

		if (StringUtils.isBlank(itemId)) {
			result.setCode("商品信息不能为空！");
			return result;
		}

		sku.setItemId(itemId.trim());

		if (StringUtils.isBlank(modifyUser)) {
			result.setCode("操作人信息不能为空！");
			return result;
		}

		sku.setModifyUser(modifyUser);

		try {
			itemSkuDao.deleteItemSku(sku);
			result.setResult(true);
		} catch (Exception e) {
			logger.error("itemId:" + itemId + "modifyUser:" + modifyUser, e);

			result.setCode("更新SKU信息失败！");
		}

		return result;
	}

	@Override
	public BooleanResult updateItemSkuStock(List<ItemSku> skuList, String modifyUser) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		if (skuList == null || skuList.size() == 0) {
			result.setCode("商品规格信息不能为空！");
			return result;
		}

		if (StringUtils.isBlank(modifyUser)) {
			result.setCode("操作人信息不能为空！");
			return result;
		}

		try {
			itemSkuDao.updateItemSku(skuList, modifyUser);
			result.setResult(true);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(skuList) + "modifyUser:" + modifyUser, e);

			result.setCode("更新SKU库存信息失败！");
			return result;
		}

		return result;
	}

	public IItemSkuDao getItemSkuDao() {
		return itemSkuDao;
	}

	public void setItemSkuDao(IItemSkuDao itemSkuDao) {
		this.itemSkuDao = itemSkuDao;
	}

}
