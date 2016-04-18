package com.kintiger.mall.module.dao;

import java.util.List;

import com.kintiger.mall.api.module.bo.Module;

/**
 * 
 * @author xujiakun
 * 
 */
public interface IModuleDao {

	/**
	 * 
	 * @param module
	 * @return
	 */
	int getModuleCount(Module module);

	/**
	 * 
	 * @param module
	 * @return
	 */
	List<Module> getModuleList(Module module);

}
