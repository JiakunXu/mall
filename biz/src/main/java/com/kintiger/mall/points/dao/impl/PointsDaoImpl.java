package com.kintiger.mall.points.dao.impl;

import java.util.List;

import com.kintiger.mall.api.points.bo.Points;
import com.kintiger.mall.framework.dao.impl.BaseDaoImpl;
import com.kintiger.mall.points.dao.IPointsDao;

/**
 * 
 * @author xujiakun
 * 
 */
public class PointsDaoImpl extends BaseDaoImpl implements IPointsDao {

	@Override
	public int getPointsCount(Points points) {
		return (Integer) getSqlMapClientTemplate().queryForObject("points.getPointsCount", points);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Points> getPointsList(Points points) {
		return (List<Points>) getSqlMapClientTemplate().queryForList("points.getPointsList", points);
	}

	@Override
	public String createPoints(Points points) {
		return (String) getSqlMapClientTemplate().insert("points.createPoints", points);
	}

	@Override
	public int deletePoints(Points points) {
		return getSqlMapClientTemplate().update("points.deletePoints", points);
	}

}
