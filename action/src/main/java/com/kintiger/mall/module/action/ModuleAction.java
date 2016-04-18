package com.kintiger.mall.module.action;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.kintiger.mall.api.module.IModuleService;
import com.kintiger.mall.api.module.bo.Module;
import com.kintiger.mall.framework.action.BaseAction;
import com.kintiger.mall.framework.webwork.annotations.JsonResult;

/**
 * 功能模块.
 * 
 * @author xujiakun
 * 
 */
public class ModuleAction extends BaseAction {

	private static final long serialVersionUID = 8893083682113834782L;

	private IModuleService moduleService;

	private List<Module> moduleList;

	private int total;

	private String display;

	private String modPid;

	@JsonResult(field = "moduleList", include = { "modId", "modName", "domain", "type" }, total = "total")
	public String getModuleJsonList() {
		Module m = new Module();
		m = getSearchInfo(m);

		if (StringUtils.isNotEmpty(display) && StringUtils.isNotEmpty(display.trim())) {
			m.setDisplay(display.trim());
		}

		if (StringUtils.isNotEmpty(modPid) && StringUtils.isNotEmpty(modPid.trim())) {
			m.setModPid(modPid.trim());
		}

		total = moduleService.getModuleCount(m);

		if (total > 0) {
			moduleList = moduleService.getModuleList(m);
		}

		return JSON_RESULT;
	}

	public IModuleService getModuleService() {
		return moduleService;
	}

	public void setModuleService(IModuleService moduleService) {
		this.moduleService = moduleService;
	}

	public List<Module> getModuleList() {
		return moduleList;
	}

	public void setModuleList(List<Module> moduleList) {
		this.moduleList = moduleList;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

	public String getModPid() {
		return modPid;
	}

	public void setModPid(String modPid) {
		this.modPid = modPid;
	}

}
