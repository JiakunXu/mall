package com.kintiger.mall.spec.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.kintiger.mall.api.cache.IMemcachedCacheService;
import com.kintiger.mall.api.spec.ISpecService;
import com.kintiger.mall.api.spec.bo.SpecCat;
import com.kintiger.mall.api.spec.bo.SpecItem;
import com.kintiger.mall.framework.bo.BooleanResult;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;
import com.kintiger.mall.framework.util.LogUtil;
import com.kintiger.mall.spec.dao.ISpecDao;

/**
 * 接口实现.
 * 
 * @author xujiakun
 * 
 */
public class SpecServiceImpl implements ISpecService {

	private Logger4jExtend logger = Logger4jCollection.getLogger(SpecServiceImpl.class);

	private IMemcachedCacheService memcachedCacheService;

	private ISpecDao specDao;

	@Override
	public List<SpecCat> getSpecCatList(String shopId) {
		if (StringUtils.isBlank(shopId)) {
			return null;
		}

		SpecCat specCat = new SpecCat();
		specCat.setShopId(shopId.trim());

		return getSpecCatList(specCat);
	}

	@Override
	public List<SpecCat> getSpecCatList(String shopId, String[] specCId) {
		if (StringUtils.isBlank(shopId)) {
			return null;
		}

		SpecCat specCat = new SpecCat();
		specCat.setShopId(shopId.trim());
		specCat.setCodes(specCId);

		return getSpecCatList(specCat);
	}

	private List<SpecCat> getSpecCatList(SpecCat specCat) {
		try {
			return specDao.getSpecCatList(specCat);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(specCat), e);
		}

		return null;
	}

	@Override
	public List<SpecItem> getSpecItemList(String shopId, String specCId) {
		if (StringUtils.isBlank(shopId) || StringUtils.isBlank(specCId)) {
			return null;
		}

		SpecItem specItem = new SpecItem();
		specItem.setShopId(shopId.trim());
		specItem.setSpecCId(specCId.trim());

		return getSpecItemList(specItem);
	}

	@Override
	public List<SpecItem> getSpecItemList(String shopId, String[] specItemId) {
		if (StringUtils.isBlank(shopId) || specItemId == null || specItemId.length == 0) {
			return null;
		}

		SpecItem specItem = new SpecItem();
		specItem.setShopId(shopId.trim());
		specItem.setCodes(specItemId);

		return getSpecItemList(specItem);
	}

	private List<SpecItem> getSpecItemList(SpecItem specItem) {
		try {
			return specDao.getSpecItemList(specItem);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(specItem), e);
		}

		return null;
	}

	@Override
	public BooleanResult createSpecItem(String shopId, String specCId, String specItemValue, String modifyUser) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);

		if (StringUtils.isBlank(shopId)) {
			result.setCode("店铺信息不能为空！");
			return result;
		}

		if (StringUtils.isBlank(specCId)) {
			result.setCode("规格类目信息不能为空！");
			return result;
		}

		// 允许空格
		if (StringUtils.isEmpty(specItemValue)) {
			result.setCode("规格明细信息不能为空！");
			return result;
		}

		if (StringUtils.isBlank(modifyUser)) {
			result.setCode("操作人信息不能为空！");
			return result;
		}

		SpecItem specItem = new SpecItem();
		specItem.setShopId(shopId.trim());
		specItem.setSpecCId(specCId.trim());
		specItem.setSpecItemValue(specItemValue);
		specItem.setModifyUser(modifyUser.trim());

		try {
			String specItemId = specDao.createSpecItem(specItem);

			result.setCode(specItemId);
			result.setResult(true);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(specItem), e);

			result.setCode("规格信息创建失败，请稍后再试！");
		}

		return result;
	}

	public IMemcachedCacheService getMemcachedCacheService() {
		return memcachedCacheService;
	}

	public void setMemcachedCacheService(IMemcachedCacheService memcachedCacheService) {
		this.memcachedCacheService = memcachedCacheService;
	}

	public ISpecDao getSpecDao() {
		return specDao;
	}

	public void setSpecDao(ISpecDao specDao) {
		this.specDao = specDao;
	}

}
