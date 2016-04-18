package com.kintiger.mall.express.service.impl;

import java.util.List;

import com.kintiger.mall.api.cache.IMemcachedCacheService;
import com.kintiger.mall.api.express.IExpressService;
import com.kintiger.mall.api.express.bo.Express;
import com.kintiger.mall.express.dao.IExpressDao;
import com.kintiger.mall.framework.exception.ServiceException;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;

/**
 * 物流.
 * 
 * @author xujiakun
 * 
 */
public class ExpressServiceImpl implements IExpressService {

	private Logger4jExtend logger = Logger4jCollection.getLogger(ExpressServiceImpl.class);

	private IMemcachedCacheService memcachedCacheService;

	private IExpressDao expressDao;

	@SuppressWarnings("unchecked")
	@Override
	public List<Express> getExpressList() {
		List<Express> expressList = null;

		try {
			expressList = (List<Express>) memcachedCacheService.get(IMemcachedCacheService.CACHE_KEY_EXPRESS);
		} catch (ServiceException e) {
			logger.error(e);
		}

		if (expressList != null && expressList.size() > 0) {
			return expressList;
		}

		try {
			expressList = expressDao.getExpressList();
			if (expressList != null && expressList.size() > 0) {
				memcachedCacheService.set(IMemcachedCacheService.CACHE_KEY_EXPRESS, expressList);
			}
		} catch (Exception e) {
			logger.error(e);
		}

		return expressList;
	}

	public IMemcachedCacheService getMemcachedCacheService() {
		return memcachedCacheService;
	}

	public void setMemcachedCacheService(IMemcachedCacheService memcachedCacheService) {
		this.memcachedCacheService = memcachedCacheService;
	}

	public IExpressDao getExpressDao() {
		return expressDao;
	}

	public void setExpressDao(IExpressDao expressDao) {
		this.expressDao = expressDao;
	}

}
