package com.kintiger.mall.module.service.impl;

import java.util.List;

import com.kintiger.mall.api.module.IModuleService;
import com.kintiger.mall.api.module.bo.Module;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;
import com.kintiger.mall.framework.util.LogUtil;
import com.kintiger.mall.module.dao.IModuleDao;

/**
 * 
 * @author xujiakun
 * 
 */
public class ModuleServiceImpl implements IModuleService {

	private Logger4jExtend logger = Logger4jCollection.getLogger(ModuleServiceImpl.class);

	private IModuleDao moduleDao;

	public int getModuleCount(Module module) {
		try {
			return moduleDao.getModuleCount(module);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(module), e);
		}

		return 0;
	}

	public List<Module> getModuleList(Module module) {
		try {
			return moduleDao.getModuleList(module);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(module), e);
		}

		return null;
	}

	public IModuleDao getModuleDao() {
		return moduleDao;
	}

	public void setModuleDao(IModuleDao moduleDao) {
		this.moduleDao = moduleDao;
	}

}
