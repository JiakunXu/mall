package com.kintiger.mall.monitor.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.orm.ibatis.SqlMapClientCallback;

import com.ibatis.sqlmap.client.SqlMapExecutor;
import com.kintiger.mall.api.monitor.bo.LogMonitor;
import com.kintiger.mall.framework.dao.impl.BaseDaoImpl;
import com.kintiger.mall.monitor.dao.ILogMonitorDao;

/**
 * 
 * @author xujiakun
 * 
 */
public class LogMonitorDaoImpl extends BaseDaoImpl implements ILogMonitorDao {

	@Override
	public int getLogMonitorCount(LogMonitor logMonitor) {
		return (Integer) getSqlMapClientTemplate().queryForObject("monitor.log.getLogMonitorCount", logMonitor);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LogMonitor> getLogMonitorList(LogMonitor logMonitor) {
		return (List<LogMonitor>) getSqlMapClientTemplate().queryForList("monitor.log.getLogMonitorList", logMonitor);
	}

	@Override
	public int createLogMonitor(final List<LogMonitor> logMonitors) {
		return (Integer) getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
			public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
				int count = 0;
				executor.startBatch();
				for (LogMonitor s : logMonitors) {
					executor.insert("monitor.log.createLogMonitor", s);
					count++;
				}
				executor.executeBatch();

				return count;
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LogMonitor> getLogMonitorList4SendEmail() {
		return (List<LogMonitor>) getSqlMapClientTemplate().queryForList("log.getLogMonitorList4SendEmail");
	}

}
