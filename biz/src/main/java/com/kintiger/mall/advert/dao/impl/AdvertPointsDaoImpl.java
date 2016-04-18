package com.kintiger.mall.advert.dao.impl;

import java.util.List;

import com.kintiger.mall.advert.dao.IAdvertPointsDao;
import com.kintiger.mall.api.advert.bo.AdvertPoints;
import com.kintiger.mall.framework.dao.impl.BaseDaoImpl;

/**
 * 
 * @author xujiakun
 * 
 */
public class AdvertPointsDaoImpl extends BaseDaoImpl implements IAdvertPointsDao {

	@Override
	public AdvertPoints getAdvertPoints(AdvertPoints advertPoints) {
		return (AdvertPoints) getSqlMapClientTemplate().queryForObject("advert.points.getAdvertPoints", advertPoints);
	}

	@Override
	public String createAdvertPoints(AdvertPoints advertPoints) {
		return (String) getSqlMapClientTemplate().insert("advert.points.createAdvertPoints", advertPoints);
	}

	@Override
	public int updateAdvertPoints(AdvertPoints advertPoints) {
		return getSqlMapClientTemplate().update("advert.points.updateAdvertPoints", advertPoints);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AdvertPoints> getAdvertStats(AdvertPoints advertPoints) {
		return (List<AdvertPoints>) getSqlMapClientTemplate()
			.queryForList("advert.points.getAdvertStats", advertPoints);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AdvertPoints> getAdvertPointsList(AdvertPoints advertPoints) {
		return (List<AdvertPoints>) getSqlMapClientTemplate().queryForList("advert.points.getAdvertPointsList",
			advertPoints);
	}

}
