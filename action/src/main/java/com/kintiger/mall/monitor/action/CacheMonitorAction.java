package com.kintiger.mall.monitor.action;

import java.net.InetSocketAddress;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.kintiger.mall.api.cache.IMemcachedCacheService;
import com.kintiger.mall.api.cache.bo.CacheStats;
import com.kintiger.mall.api.monitor.ICacheMonitorService;
import com.kintiger.mall.framework.action.BaseAction;
import com.kintiger.mall.framework.annotation.ActionMonitor;
import com.kintiger.mall.framework.exception.ServiceException;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;
import com.kintiger.mall.framework.util.JsonUtil;
import com.kintiger.mall.framework.webwork.annotations.JsonResult;

/**
 * 
 * @author xujiakun
 * 
 */
public class CacheMonitorAction extends BaseAction {

	private static final long serialVersionUID = -8143747833864798568L;

	private Logger4jExtend logger = Logger4jCollection.getLogger(CacheMonitorAction.class);

	private IMemcachedCacheService memcachedCacheService;

	private ICacheMonitorService cacheMonitorService;

	private int total;

	private List<CacheStats> cacheStatsList;

	private String operate;

	private String key;

	private String hostAddress;

	private String port;

	/**
	 * 
	 * @return
	 */
	@ActionMonitor(actionName = "缓存实时状态查询")
	public String searchCacheStats() {
		return "searchCacheStats";
	}

	/**
	 * 
	 * @return
	 */
	@JsonResult(field = "cacheStatsList", total = "total")
	public String getCacheStatsJsonList() {
		try {
			cacheStatsList = memcachedCacheService.getStats();
		} catch (ServiceException e) {
			logger.error(e);
		}

		total = cacheStatsList == null ? 0 : cacheStatsList.size();

		return JSON_RESULT;
	}

	/**
	 * 
	 * @return
	 */
	public String configCachePrepare() {
		return UPDATE_PREPARE;
	}

	@ActionMonitor(actionName = "缓存配置")
	public String configCache() {
		try {
			if ("get".equals(operate)) {
				Object object = memcachedCacheService.get(key);
				this.setSuccessMessage(JsonUtil.bean2Json(object.getClass(), object));
			} else if ("remove".equals(operate)) {
				Object object = memcachedCacheService.remove(key);
				this.setSuccessMessage(JsonUtil.bean2Json(object.getClass(), object));
			} else if ("flushAll".equals(operate)) {
				String[] keys = key.split(":");
				InetSocketAddress address = new InetSocketAddress(keys[0], Integer.parseInt(keys[1]));
				memcachedCacheService.flushAll(address);
				this.setSuccessMessage(SUCCESS);
			}
		} catch (Exception e) {
			logger.error("operate:" + operate + "key:" + key, e);
			this.setSuccessMessage("操作失败！");
		}

		return RESULT_MESSAGE;
	}

	/**
	 * 
	 * @return
	 */
	@ActionMonitor(actionName = "缓存历史状态查询")
	public String searchCacheMonitor() {
		return "searchCacheMonitor";
	}

	@JsonResult(field = "cacheStatsList", total = "total")
	public String getCacheMonitorJsonList() {
		CacheStats c = new CacheStats();
		c = getSearchInfo(c);

		if (StringUtils.isNotEmpty(hostAddress)) {
			c.setHostAddress(hostAddress.trim());
		}

		if (StringUtils.isNotEmpty(port)) {
			c.setPort(port.trim());
		}

		total = cacheMonitorService.getCacheMonitorCount(c);
		if (total > 0) {
			cacheStatsList = cacheMonitorService.getCacheMonitorList(c);
		}

		return JSON_RESULT;
	}

	public IMemcachedCacheService getMemcachedCacheService() {
		return memcachedCacheService;
	}

	public void setMemcachedCacheService(IMemcachedCacheService memcachedCacheService) {
		this.memcachedCacheService = memcachedCacheService;
	}

	public ICacheMonitorService getCacheMonitorService() {
		return cacheMonitorService;
	}

	public void setCacheMonitorService(ICacheMonitorService cacheMonitorService) {
		this.cacheMonitorService = cacheMonitorService;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<CacheStats> getCacheStatsList() {
		return cacheStatsList;
	}

	public void setCacheStatsList(List<CacheStats> cacheStatsList) {
		this.cacheStatsList = cacheStatsList;
	}

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getHostAddress() {
		return hostAddress;
	}

	public void setHostAddress(String hostAddress) {
		this.hostAddress = hostAddress;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

}
