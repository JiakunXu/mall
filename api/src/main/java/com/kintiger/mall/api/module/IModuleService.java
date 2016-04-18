package com.kintiger.mall.api.module;

import java.util.List;

import com.kintiger.mall.api.module.bo.Module;

/**
 * 模块.
 * 
 * @author xujiakun
 * 
 */
public interface IModuleService {

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
