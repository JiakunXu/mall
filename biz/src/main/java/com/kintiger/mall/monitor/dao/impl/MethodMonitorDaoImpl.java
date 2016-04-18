package com.kintiger.mall.monitor.dao.impl;

import java.util.List;

import com.kintiger.mall.api.monitor.bo.MethodMonitor;
import com.kintiger.mall.framework.dao.impl.BaseDaoImpl;
import com.kintiger.mall.monitor.dao.IMethodMonitorDao;

/**
 * 
 * @author xujiakun
 * 
 */
public class MethodMonitorDaoImpl extends BaseDaoImpl implements IMethodMonitorDao {

	@Override
	public int getMethodMonitorCount(MethodMonitor methodMonitor) {
		return (Integer) getSqlMapClientTemplate()
			.queryForObject("monitor.method.getMethodMonitorCount", methodMonitor);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MethodMonitor> getMethodMonitorList(MethodMonitor methodMonitor) {
		return (List<MethodMonitor>) getSqlMapClientTemplate().queryForList("monitor.method.getMethodMonitorList",
			methodMonitor);
	}

	@Override
	public String createMethodMonitor(MethodMonitor methodMonitor) {
		return (String) getSqlMapClientTemplate().insert("monitor.method.createMethodMonitor", methodMonitor);
	}

}
