package com.kintiger.mall.module.dao.impl;

import java.util.List;

import com.kintiger.mall.api.module.bo.Module;
import com.kintiger.mall.framework.dao.impl.BaseDaoImpl;
import com.kintiger.mall.module.dao.IModuleDao;

/**
 * 
 * @author xujiakun
 * 
 */
public class ModuleDaoImpl extends BaseDaoImpl implements IModuleDao {

	public int getModuleCount(Module module) {
		return (Integer) getSqlMapClientTemplate().queryForObject("module.getModuleCount", module);
	}

	@SuppressWarnings("unchecked")
	public List<Module> getModuleList(Module module) {
		return (List<Module>) getSqlMapClientTemplate().queryForList("module.getModuleList", module);
	}

}
