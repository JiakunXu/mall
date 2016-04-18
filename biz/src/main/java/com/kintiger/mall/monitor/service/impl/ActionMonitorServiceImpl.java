package com.kintiger.mall.monitor.service.impl;

import java.util.List;

import com.kintiger.mall.api.monitor.IActionMonitorService;
import com.kintiger.mall.api.monitor.bo.ActionMonitor;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;
import com.kintiger.mall.framework.util.LogUtil;
import com.kintiger.mall.monitor.dao.IActionMonitorDao;

/**
 * action 监控 service.
 * 
 * @author xujiakun
 * 
 */
public class ActionMonitorServiceImpl implements IActionMonitorService {

	private Logger4jExtend logger = Logger4jCollection.getLogger(ActionMonitorServiceImpl.class);

	private IActionMonitorDao actionMonitorDao;

	@Override
	public boolean createActionMonitor(List<ActionMonitor> actionMonitors) {
		if (actionMonitors == null || actionMonitors.size() == 0) {
			return false;
		}

		try {
			actionMonitorDao.createActionMonitor(actionMonitors);
			return true;
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(actionMonitors), e);
		}

		return false;
	}

	@Override
	public int getActionMonitorCount(ActionMonitor actionMonitor) {
		try {
			return actionMonitorDao.getActionMonitorCount(actionMonitor);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(actionMonitor), e);
		}

		return 0;
	}

	@Override
	public List<ActionMonitor> getActionMonitorList(ActionMonitor actionMonitor) {
		try {
			return actionMonitorDao.getActionMonitorList(actionMonitor);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(actionMonitor), e);
		}

		return null;
	}

	public IActionMonitorDao getActionMonitorDao() {
		return actionMonitorDao;
	}

	public void setActionMonitorDao(IActionMonitorDao actionMonitorDao) {
		this.actionMonitorDao = actionMonitorDao;
	}

}
