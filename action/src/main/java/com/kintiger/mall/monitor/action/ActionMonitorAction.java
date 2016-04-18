package com.kintiger.mall.monitor.action;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.kintiger.mall.api.monitor.IActionMonitorService;
import com.kintiger.mall.api.monitor.bo.ActionMonitor;
import com.kintiger.mall.api.user.bo.User;
import com.kintiger.mall.framework.action.BaseAction;
import com.kintiger.mall.framework.util.DateUtil;
import com.kintiger.mall.framework.webwork.annotations.JsonResult;

/**
 * log.
 * 
 * @author xujiakun
 * 
 */
public class ActionMonitorAction extends BaseAction {

	private static final long serialVersionUID = 8136069885100794581L;

	private IActionMonitorService actionMonitorService;

	private int total;

	private List<ActionMonitor> logs;

	private String passport;

	private String type;

	public String searchActionLog() {
		initSearchDate();
		return "searchActionLog";
	}

	@JsonResult(field = "logs", include = { "id", "actionName", "passport", "userName", "ip", "createDate" }, total = "total")
	public String getActionLogJsonList() {
		ActionMonitor c = new ActionMonitor();
		c = getSearchInfo(c);

		if (StringUtils.isNotEmpty(passport) && StringUtils.isNotEmpty(passport.trim())) {
			c.setPassport(passport.trim());
		}

		if (StringUtils.isNotEmpty(gmtStart) && StringUtils.isNotEmpty(gmtStart.trim())) {
			c.setGmtStart(gmtStart.trim());
		}

		if (StringUtils.isNotEmpty(gmtEnd) && StringUtils.isNotEmpty(gmtEnd.trim())) {
			c.setGmtEnd(gmtEnd.trim());
		}

		total = actionMonitorService.getActionMonitorCount(c);
		if (total > 0) {
			logs = actionMonitorService.getActionMonitorList(c);
		}

		return JSON_RESULT;
	}

	public String searchMyActionLog() {
		type = "i";
		initSearchDate();
		return "searchActionLog";
	}

	@JsonResult(field = "logs", include = { "id", "actionName", "passport", "userName", "ip", "createDate" }, total = "total")
	public String getMyActionLogJsonList() {
		ActionMonitor c = new ActionMonitor();
		c = getSearchInfo(c);

		User user = getUser();
		c.setUserId(user.getUserId());

		if (StringUtils.isNotEmpty(gmtStart) && StringUtils.isNotEmpty(gmtStart.trim())) {
			c.setGmtStart(gmtStart.trim());
		}

		if (StringUtils.isNotEmpty(gmtEnd) && StringUtils.isNotEmpty(gmtEnd.trim())) {
			c.setGmtEnd(gmtEnd.trim());
		}

		total = actionMonitorService.getActionMonitorCount(c);
		if (total > 0) {
			logs = actionMonitorService.getActionMonitorList(c);
		}

		return JSON_RESULT;
	}

	/**
	 * 初始化查询时间条件.
	 */
	private void initSearchDate() {
		gmtStart = DateUtil.getDateTime(DateUtil.addDays(new Date(), -1), DateUtil.DEFAULT_DATE_FORMAT);
		gmtEnd = DateUtil.getNowDateStr();
	}

	public IActionMonitorService getActionMonitorService() {
		return actionMonitorService;
	}

	public void setActionMonitorService(IActionMonitorService actionMonitorService) {
		this.actionMonitorService = actionMonitorService;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<ActionMonitor> getLogs() {
		return logs;
	}

	public void setLogs(List<ActionMonitor> logs) {
		this.logs = logs;
	}

	public String getPassport() {
		return passport;
	}

	public void setPassport(String passport) {
		this.passport = passport;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
