package com.kintiger.mall.pageview.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.orm.ibatis.SqlMapClientCallback;

import com.ibatis.sqlmap.client.SqlMapExecutor;
import com.kintiger.mall.api.pageview.bo.Pageview;
import com.kintiger.mall.framework.dao.impl.BaseDaoImpl;
import com.kintiger.mall.pageview.dao.IPageviewDao;

/**
 * 
 * @author xujiakun
 * 
 */
public class PageviewDaoImpl extends BaseDaoImpl implements IPageviewDao {

	@Override
	public int createPageview(final List<Pageview> pageviews) {
		return (Integer) getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
			public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
				int count = 0;
				executor.startBatch();
				for (Pageview pv : pageviews) {
					executor.insert("pageview.createPageview", pv);
					count++;
				}
				executor.executeBatch();

				return count;
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Pageview> getPageviewStats(Pageview pageview) {
		return (List<Pageview>) getSqlMapClientTemplate().queryForList("pageview.getPageviewStats", pageview);
	}

}
