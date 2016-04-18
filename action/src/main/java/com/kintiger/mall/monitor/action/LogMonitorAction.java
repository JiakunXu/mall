package com.kintiger.mall.monitor.action;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.kintiger.mall.api.monitor.ILogMonitorService;
import com.kintiger.mall.api.monitor.bo.LogMonitor;
import com.kintiger.mall.framework.action.BaseAction;
import com.kintiger.mall.framework.annotation.ActionMonitor;
import com.kintiger.mall.framework.util.DateUtil;
import com.kintiger.mall.framework.webwork.annotations.JsonResult;

/**
 * log4j error log.
 * 
 * @author xujiakun
 * 
 */
public class LogMonitorAction extends BaseAction {

	private static final long serialVersionUID = -898983625221087105L;

	private ILogMonitorService logMonitorService;

	private int total;

	private List<LogMonitor> logMonitorList;

	/**
	 * 初始化查询时间条件.
	 */
	private void initSearchDate() {
		gmtStart = DateUtil.getDateTime(DateUtil.addDays(new Date(), -1), DateUtil.DEFAULT_DATE_FORMAT);
		gmtEnd = DateUtil.getNowDateStr();
	}

	@ActionMonitor(actionName = "错误日志查询")
	public String searchLogMonitor() {
		initSearchDate();
		return "searchLogMonitor";
	}

	@JsonResult(field = "logMonitorList", include = { "logMonitorId", "className", "methodName", "lineNumber",
		"message", "e", "createDate" }, total = "total")
	public String getLogMonitorJsonList() {
		LogMonitor c = new LogMonitor();
		c = getSearchInfo(c);

		if (StringUtils.isNotEmpty(gmtStart) && StringUtils.isNotEmpty(gmtStart.trim())) {
			c.setGmtStart(gmtStart.trim());
		}

		if (StringUtils.isNotEmpty(gmtEnd) && StringUtils.isNotEmpty(gmtEnd.trim())) {
			c.setGmtEnd(gmtEnd.trim());
		}

		total = logMonitorService.getLogMonitorCount(c);
		if (total > 0) {
			logMonitorList = logMonitorService.getLogMonitorList(c);
		}

		return JSON_RESULT;
	}

	public ILogMonitorService getLogMonitorService() {
		return logMonitorService;
	}

	public void setLogMonitorService(ILogMonitorService logMonitorService) {
		this.logMonitorService = logMonitorService;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<LogMonitor> getLogMonitorList() {
		return logMonitorList;
	}

	public void setLogMonitorList(List<LogMonitor> logMonitorList) {
		this.logMonitorList = logMonitorList;
	}

}
