package com.kintiger.mall.advert.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.orm.ibatis.SqlMapClientCallback;

import com.ibatis.sqlmap.client.SqlMapExecutor;
import com.kintiger.mall.advert.dao.IAdvertStatsDao;
import com.kintiger.mall.api.advert.bo.AdvertStats;
import com.kintiger.mall.framework.dao.impl.BaseDaoImpl;

/**
 * 
 * @author xujiakun
 * 
 */
public class AdvertStatsDaoImpl extends BaseDaoImpl implements IAdvertStatsDao {

	@Override
	public int createAdvertStats(final List<AdvertStats> advertStatsList, final String modifyUser) {
		return (Integer) getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
			public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
				int count = 0;
				executor.startBatch();
				for (AdvertStats advertStats : advertStatsList) {
					advertStats.setModifyUser(modifyUser);
					executor.insert("advert.stats.createAdvertStats", advertStats);
					count++;
				}
				executor.executeBatch();

				return count;
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AdvertStats> getAdvertStats(AdvertStats advert) {
		return (List<AdvertStats>) getSqlMapClientTemplate().queryForList("advert.stats.getAdvertStats", advert);
	}

}
