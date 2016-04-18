package com.kintiger.mall.spec.dao.impl;

import java.util.List;

import com.kintiger.mall.api.spec.bo.SpecCat;
import com.kintiger.mall.api.spec.bo.SpecItem;
import com.kintiger.mall.framework.dao.impl.BaseDaoImpl;
import com.kintiger.mall.spec.dao.ISpecDao;

/**
 * dao 实现.
 * 
 * @author xujiakun
 * 
 */
public class SpecDaoImpl extends BaseDaoImpl implements ISpecDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<SpecCat> getSpecCatList(SpecCat specCat) {
		return (List<SpecCat>) getSqlMapClientTemplate().queryForList("spec.getSpecCatList", specCat);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SpecItem> getSpecItemList(SpecItem specItem) {
		return (List<SpecItem>) getSqlMapClientTemplate().queryForList("spec.getSpecItemList", specItem);
	}

	@Override
	public String createSpecItem(SpecItem specItem) {
		return (String) getSqlMapClientTemplate().insert("spec.createSpecItem", specItem);
	}

}
