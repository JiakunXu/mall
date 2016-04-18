package com.kintiger.mall.monitor.task;

import java.util.List;

import com.kintiger.mall.api.cache.IMemcachedCacheService;
import com.kintiger.mall.api.monitor.ILogMonitorService;
import com.kintiger.mall.api.monitor.bo.LogMonitor;
import com.kintiger.mall.framework.annotation.Profiler;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;
import com.kintiger.mall.framework.mail.MailService;

/**
 * 
 * @author xujiakun
 * 
 */
public class LogMonitorTask {

	private static final String TD_BEGIN = "<td>";

	private static final String TD_END = "</td>";

	private Logger4jExtend logger = Logger4jCollection.getLogger(LogMonitorTask.class);

	private IMemcachedCacheService memcachedCacheService;

	private ILogMonitorService logMonitorService;

	private String smtpServer;

	private String from;

	private String to;

	@SuppressWarnings("unchecked")
	@Profiler
	public void logMonitor() {
		List<LogMonitor> list = null;
		try {
			list = (List<LogMonitor>) memcachedCacheService.get(IMemcachedCacheService.CACHE_KEY_LOG_MONITOR);
		} catch (Exception e) {
			logger.error(e);
		}

		if (list != null && list.size() != 0 && logMonitorService.createLogMonitor(list)) {
			try {
				memcachedCacheService.remove(IMemcachedCacheService.CACHE_KEY_LOG_MONITOR);
			} catch (Exception e) {
				logger.error(e);
			}
		}
	}

	public void logMonitor2SendEmail() {

		List<LogMonitor> list = logMonitorService.getLogMonitorList4SendEmail();

		StringBuilder content = new StringBuilder();
		content.append("<table border=1><tr>");
		content.append("<td>ID</td>");
		content.append("<td>class Name</td>");
		content.append("<td>Method Name</td>");
		content.append("<td>Line Number</td>");
		content.append("<td>Message</td>");
		content.append("<td>e</td>");
		content.append("<td>Log Date</td>");
		content.append("<td>Create Date</td>");
		content.append("</tr>");

		if (list != null && list.size() != 0) {
			for (LogMonitor monitor : list) {
				content.append("<tr>");
				content.append(TD_BEGIN).append(monitor.getId()).append(TD_END);
				content.append(TD_BEGIN).append(monitor.getClassName()).append(TD_END);
				content.append(TD_BEGIN).append(monitor.getMethodName()).append(TD_END);
				content.append(TD_BEGIN).append(monitor.getLineNumber()).append(TD_END);
				content.append(TD_BEGIN).append(monitor.getMessage()).append(TD_END);
				content.append(TD_BEGIN).append(monitor.getE()).append(TD_END);
				content.append(TD_BEGIN).append(monitor.getCreateDate()).append(TD_END);
				content.append("</tr>");
			}
		}

		content.append("</table>");

		if (list != null && list.size() != 0) {
			new MailService(smtpServer, from, "EXP", to, "xplatform log", content.toString()).send();
		}
	}

	public IMemcachedCacheService getMemcachedCacheService() {
		return memcachedCacheService;
	}

	public void setMemcachedCacheService(IMemcachedCacheService memcachedCacheService) {
		this.memcachedCacheService = memcachedCacheService;
	}

	public ILogMonitorService getLogMonitorService() {
		return logMonitorService;
	}

	public void setLogMonitorService(ILogMonitorService logMonitorService) {
		this.logMonitorService = logMonitorService;
	}

	public String getSmtpServer() {
		return smtpServer;
	}

	public void setSmtpServer(String smtpServer) {
		this.smtpServer = smtpServer;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

}
