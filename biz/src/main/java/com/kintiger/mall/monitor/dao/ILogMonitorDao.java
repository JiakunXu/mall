package com.kintiger.mall.monitor.dao;

import java.util.List;

import com.kintiger.mall.api.monitor.bo.LogMonitor;

/**
 * 
 * @author xujiakun
 * 
 */
public interface ILogMonitorDao {

	/**
	 * 
	 * @param logMonitor
	 * @return
	 */
	int getLogMonitorCount(LogMonitor logMonitor);

	/**
	 * 
	 * @param logMonitor
	 * @return
	 */
	List<LogMonitor> getLogMonitorList(LogMonitor logMonitor);

	/**
	 * 
	 * @param logMonitors
	 * @return
	 */
	int createLogMonitor(List<LogMonitor> logMonitors);

	/**
	 * 
	 * @return
	 */
	List<LogMonitor> getLogMonitorList4SendEmail();

}
