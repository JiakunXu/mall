package com.kintiger.mall.shop.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.kintiger.mall.api.cache.IMemcachedCacheService;
import com.kintiger.mall.api.file.IFileService;
import com.kintiger.mall.api.file.bo.FileInfo;
import com.kintiger.mall.api.shop.IShopService;
import com.kintiger.mall.api.shop.bo.Shop;
import com.kintiger.mall.framework.exception.ServiceException;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;
import com.kintiger.mall.framework.util.LogUtil;
import com.kintiger.mall.shop.dao.IShopDao;

/**
 * 店铺接口实现.
 * 
 * @author xujiakun
 * 
 */
public class ShopServiceImpl implements IShopService {

	private Logger4jExtend logger = Logger4jCollection.getLogger(ShopServiceImpl.class);

	private IMemcachedCacheService memcachedCacheService;

	private IFileService fileService;

	private IShopDao shopDao;

	@Override
	public List<Shop> getShopList(String[] companyId) {
		if (companyId == null || companyId.length == 0) {
			return null;
		}

		Shop shop = new Shop();
		shop.setCodes(companyId);

		try {
			return shopDao.getShopList(shop);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(shop), e);
		}

		return null;
	}

	@Override
	public Shop getShop(String shopId) {
		if (StringUtils.isBlank(shopId)) {
			return null;
		}

		Shop shop = null;

		try {
			shop = (Shop) memcachedCacheService.get(IMemcachedCacheService.CACHE_KEY_SHOP_ID + shopId.trim());
		} catch (ServiceException e) {
			logger.error(IMemcachedCacheService.CACHE_KEY_SHOP_ID + shopId, e);
		}

		if (shop != null) {
			return shop;
		}

		shop = new Shop();
		shop.setShopId(shopId.trim());

		Shop s = getShop(shop);

		if (s != null) {
			try {
				memcachedCacheService.set(IMemcachedCacheService.CACHE_KEY_SHOP_ID + shopId.trim(), s,
					IMemcachedCacheService.CACHE_KEY_SHOP_ID_DEFAULT_EXP);
			} catch (ServiceException e) {
				logger.error(IMemcachedCacheService.CACHE_KEY_SHOP_ID + shopId, e);
			}
		}

		return s;
	}

	@Override
	public Shop getShopByUUID(String uuid) {
		if (StringUtils.isBlank(uuid)) {
			return null;
		}

		Shop shop = null;

		try {
			shop = (Shop) memcachedCacheService.get(IMemcachedCacheService.CACHE_KEY_SHOP_UUID + uuid.trim());
		} catch (ServiceException e) {
			logger.error(IMemcachedCacheService.CACHE_KEY_SHOP_UUID + uuid, e);
		}

		if (shop != null) {
			return shop;
		}

		shop = new Shop();
		shop.setUuid(uuid.trim());

		Shop s = getShop(shop);

		if (s != null) {
			try {
				memcachedCacheService.set(IMemcachedCacheService.CACHE_KEY_SHOP_UUID + uuid.trim(), s,
					IMemcachedCacheService.CACHE_KEY_SHOP_UUID_DEFAULT_EXP);
			} catch (ServiceException e) {
				logger.error(IMemcachedCacheService.CACHE_KEY_SHOP_UUID + uuid, e);
			}
		}

		return s;
	}

	private Shop getShop(Shop shop) {
		try {
			shop = shopDao.getShop(shop);
			if (shop == null) {
				return null;
			}

			FileInfo fileInfo = fileService.getFileInfo(shop.getShopId(), shop.getFileId());
			if (fileInfo != null) {
				shop.setFilePath(fileInfo.getFilePath());
			}

			return shop;
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(shop), e);
		}

		return null;
	}

	public IMemcachedCacheService getMemcachedCacheService() {
		return memcachedCacheService;
	}

	public void setMemcachedCacheService(IMemcachedCacheService memcachedCacheService) {
		this.memcachedCacheService = memcachedCacheService;
	}

	public IFileService getFileService() {
		return fileService;
	}

	public void setFileService(IFileService fileService) {
		this.fileService = fileService;
	}

	public IShopDao getShopDao() {
		return shopDao;
	}

	public void setShopDao(IShopDao shopDao) {
		this.shopDao = shopDao;
	}

}
