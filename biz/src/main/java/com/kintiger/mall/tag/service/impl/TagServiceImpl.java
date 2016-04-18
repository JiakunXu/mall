package com.kintiger.mall.tag.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.kintiger.mall.api.cache.IMemcachedCacheService;
import com.kintiger.mall.api.tag.ITagService;
import com.kintiger.mall.api.tag.bo.Tag;
import com.kintiger.mall.framework.exception.ServiceException;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;
import com.kintiger.mall.framework.util.LogUtil;
import com.kintiger.mall.tag.dao.ITagDao;

/**
 * 
 * @author xujiakun
 * 
 */
public class TagServiceImpl implements ITagService {

	private Logger4jExtend logger = Logger4jCollection.getLogger(TagServiceImpl.class);

	private IMemcachedCacheService memcachedCacheService;

	private ITagDao tagDao;

	@SuppressWarnings("unchecked")
	@Override
	public List<Tag> getTagList(String shopId) {
		if (StringUtils.isBlank(shopId)) {
			return null;
		}

		List<Tag> tagList = null;

		try {
			tagList =
				(List<Tag>) memcachedCacheService.get(IMemcachedCacheService.CACHE_KEY_TAG_SHOP_ID + shopId.trim());
		} catch (ServiceException e) {
			logger.error(IMemcachedCacheService.CACHE_KEY_TAG_SHOP_ID + shopId, e);
		}

		if (tagList != null && tagList.size() > 0) {
			return tagList;
		}

		Tag tag = new Tag();
		tag.setShopId(shopId.trim());

		try {
			tagList = tagDao.getTagList(tag);
			if (tagList != null && tagList.size() > 0) {
				memcachedCacheService.set(IMemcachedCacheService.CACHE_KEY_TAG_SHOP_ID + shopId.trim(), tagList);
			}
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(tag), e);
		}

		return tagList;
	}

	public IMemcachedCacheService getMemcachedCacheService() {
		return memcachedCacheService;
	}

	public void setMemcachedCacheService(IMemcachedCacheService memcachedCacheService) {
		this.memcachedCacheService = memcachedCacheService;
	}

	public ITagDao getTagDao() {
		return tagDao;
	}

	public void setTagDao(ITagDao tagDao) {
		this.tagDao = tagDao;
	}

}
