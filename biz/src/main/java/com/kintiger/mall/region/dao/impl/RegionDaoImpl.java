package com.kintiger.mall.region.dao.impl;

import java.util.List;

import com.kintiger.mall.api.region.bo.Region;
import com.kintiger.mall.framework.dao.impl.BaseDaoImpl;
import com.kintiger.mall.region.dao.IRegionDao;

/**
 * 区域 dao 实现.
 * 
 * @author xujiakun
 * 
 */
public class RegionDaoImpl extends BaseDaoImpl implements IRegionDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Region> getRegionList(Region region) {
		return (List<Region>) getSqlMapClientTemplate().queryForList("region.getRegionList", region);
	}

}
