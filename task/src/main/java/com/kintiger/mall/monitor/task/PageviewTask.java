package com.kintiger.mall.monitor.task;

import java.util.List;

import com.kintiger.mall.api.cache.IMemcachedCacheService;
import com.kintiger.mall.api.pageview.IPageviewService;
import com.kintiger.mall.api.pageview.bo.Pageview;
import com.kintiger.mall.framework.annotation.Profiler;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;

/**
 * 
 * @author xujiakun
 * 
 */
public class PageviewTask {

	private Logger4jExtend logger = Logger4jCollection.getLogger(PageviewTask.class);

	private IMemcachedCacheService memcachedCacheService;

	private IPageviewService pageviewService;

	@SuppressWarnings("unchecked")
	@Profiler
	public void pageview() {
		List<Pageview> list = null;
		try {
			list = (List<Pageview>) memcachedCacheService.get(IMemcachedCacheService.CACHE_KEY_PAGEVIEW);
		} catch (Exception e) {
			logger.error(e);
		}

		if (list != null && list.size() != 0 && pageviewService.createPageview(list)) {
			try {
				memcachedCacheService.remove(IMemcachedCacheService.CACHE_KEY_PAGEVIEW);
			} catch (Exception e) {
				logger.error(e);
			}
		}
	}

	public IMemcachedCacheService getMemcachedCacheService() {
		return memcachedCacheService;
	}

	public void setMemcachedCacheService(IMemcachedCacheService memcachedCacheService) {
		this.memcachedCacheService = memcachedCacheService;
	}

	public IPageviewService getPageviewService() {
		return pageviewService;
	}

	public void setPageviewService(IPageviewService pageviewService) {
		this.pageviewService = pageviewService;
	}

}
