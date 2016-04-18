package com.kintiger.mall.advert.dao.impl;

import java.util.List;

import com.kintiger.mall.advert.dao.IAdvertDao;
import com.kintiger.mall.api.advert.bo.Advert;
import com.kintiger.mall.framework.dao.impl.BaseDaoImpl;

/**
 * 广告.
 * 
 * @author xujiakun
 * 
 */
public class AdvertDaoImpl extends BaseDaoImpl implements IAdvertDao {

	@Override
	public int getAdvertCount(Advert advert) {
		return (Integer) getSqlMapClientTemplate().queryForObject("advert.getAdvertCount", advert);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Advert> getAdvertList(Advert advert) {
		return (List<Advert>) getSqlMapClientTemplate().queryForList("advert.getAdvertList", advert);
	}

	@Override
	public Advert getAdvert(Advert advert) {
		return (Advert) getSqlMapClientTemplate().queryForObject("advert.getAdvert", advert);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Advert> getAdvertStats(Advert advert) {
		return (List<Advert>) getSqlMapClientTemplate().queryForList("advert.getAdvertStats", advert);
	}

}
