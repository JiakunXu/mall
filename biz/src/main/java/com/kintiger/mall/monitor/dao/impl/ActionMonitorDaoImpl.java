package com.kintiger.mall.monitor.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.orm.ibatis.SqlMapClientCallback;

import com.ibatis.sqlmap.client.SqlMapExecutor;
import com.kintiger.mall.api.monitor.bo.ActionMonitor;
import com.kintiger.mall.framework.dao.impl.BaseDaoImpl;
import com.kintiger.mall.monitor.dao.IActionMonitorDao;

/**
 * action 监控 dao.
 * 
 * @author xujiakun
 * 
 */
public class ActionMonitorDaoImpl extends BaseDaoImpl implements IActionMonitorDao {

	@Override
	public int createActionMonitor(final List<ActionMonitor> actionMonitors) {
		return (Integer) getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
			public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
				int count = 0;
				executor.startBatch();
				for (ActionMonitor monitor : actionMonitors) {
					executor.insert("monitor.action.createActionMonitor", monitor);
					count++;
				}
				executor.executeBatch();

				return count;
			}
		});
	}

	@Override
	public int getActionMonitorCount(ActionMonitor actionMonitor) {
		return (Integer) getSqlMapClientTemplate()
			.queryForObject("monitor.action.getActionMonitorCount", actionMonitor);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ActionMonitor> getActionMonitorList(ActionMonitor actionMonitor) {
		return (List<ActionMonitor>) getSqlMapClientTemplate().queryForList("monitor.action.getActionMonitorList",
			actionMonitor);
	}

}
