package com.kintiger.mall.monitor.action;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.kintiger.mall.api.cache.IMemcachedCacheService;
import com.kintiger.mall.api.monitor.IMethodMonitorService;
import com.kintiger.mall.api.monitor.bo.MethodMonitor;
import com.kintiger.mall.framework.action.BaseAction;
import com.kintiger.mall.framework.annotation.ActionMonitor;
import com.kintiger.mall.framework.annotation.Decode;
import com.kintiger.mall.framework.exception.ServiceException;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;
import com.kintiger.mall.framework.util.DateUtil;
import com.kintiger.mall.framework.webwork.annotations.JsonResult;

/**
 * MethodMonitorAction.
 * 
 * @author xujiakun
 * 
 */
public class MethodMonitorAction extends BaseAction {

	private static final long serialVersionUID = 3419997830827128937L;

	private Logger4jExtend logger = Logger4jCollection.getLogger(MethodMonitorAction.class);

	private IMemcachedCacheService memcachedCacheService;

	private IMethodMonitorService methodMonitorService;

	private int total;

	private List<MethodMonitor> methodMonitorList;

	@Decode
	private String className;

	@Decode
	private String methodName;

	private String key;

	/**
	 * 初始化查询时间条件.
	 */
	private void initSearchDate() {
		gmtStart = DateUtil.getDateTime(DateUtil.addDays(new Date(), -1), DateUtil.DEFAULT_DATE_FORMAT);
		gmtEnd = DateUtil.getNowDateStr();
	}

	/**
	 * 
	 * @return
	 */
	@ActionMonitor(actionName = "接口监控查询")
	public String searchMethodMonitor() {
		initSearchDate();
		return "searchMethodMonitor";
	}

	/**
	 * 
	 * @return
	 */
	@JsonResult(field = "methodMonitorList", include = { "methodMonitorId", "className", "methodName", "cost",
		"createDate" }, total = "total")
	public String getMethodMonitorJsonList() {
		MethodMonitor s = new MethodMonitor();
		s = getSearchInfo(s);

		if (StringUtils.isNotEmpty(gmtStart) && StringUtils.isNotEmpty(gmtStart.trim())) {
			s.setGmtStart(gmtStart.trim());
		}

		if (StringUtils.isNotEmpty(gmtEnd) && StringUtils.isNotEmpty(gmtEnd.trim())) {
			s.setGmtEnd(gmtEnd.trim());
		}

		if (StringUtils.isNotEmpty(className) && StringUtils.isNotEmpty(className.trim())) {
			s.setClassName(className.trim());
		}

		if (StringUtils.isNotEmpty(methodName) && StringUtils.isNotEmpty(methodName.trim())) {
			s.setMethodName(methodName.trim());
		}

		total = methodMonitorService.getMethodMonitorCount(s);
		if (total > 0) {
			methodMonitorList = methodMonitorService.getMethodMonitorList(s);
		}

		return JSON_RESULT;
	}

	@JsonResult(field = "total")
	public String get() {
		if (StringUtils.isBlank(key)) {
			return JSON_RESULT;
		}

		try {
			total = Integer.parseInt((String) memcachedCacheService.get(key.trim()));
		} catch (ServiceException e) {
			logger.error("key:" + key, e);
		}

		return JSON_RESULT;
	}

	public IMemcachedCacheService getMemcachedCacheService() {
		return memcachedCacheService;
	}

	public void setMemcachedCacheService(IMemcachedCacheService memcachedCacheService) {
		this.memcachedCacheService = memcachedCacheService;
	}

	public IMethodMonitorService getMethodMonitorService() {
		return methodMonitorService;
	}

	public void setMethodMonitorService(IMethodMonitorService methodMonitorService) {
		this.methodMonitorService = methodMonitorService;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<MethodMonitor> getMethodMonitorList() {
		return methodMonitorList;
	}

	public void setMethodMonitorList(List<MethodMonitor> methodMonitorList) {
		this.methodMonitorList = methodMonitorList;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
