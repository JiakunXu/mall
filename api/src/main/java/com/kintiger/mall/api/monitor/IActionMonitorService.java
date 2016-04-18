package com.kintiger.mall.api.monitor;

import java.util.List;

import com.kintiger.mall.api.monitor.bo.ActionMonitor;

/**
 * action 监控.
 * 
 * @author xujiakun
 * 
 */
public interface IActionMonitorService {

	/**
	 * 
	 * @param actionMonitors
	 * @return
	 */
	boolean createActionMonitor(List<ActionMonitor> actionMonitors);

	/**
	 * 
	 * @param actionMonitor
	 * @return
	 */
	int getActionMonitorCount(ActionMonitor actionMonitor);

	/**
	 * 
	 * @param actionMonitor
	 * @return
	 */
	List<ActionMonitor> getActionMonitorList(ActionMonitor actionMonitor);

}
