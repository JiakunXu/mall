package com.kintiger.mall.api.module.bo;

import com.kintiger.mall.framework.bo.SearchInfo;

/**
 * 模块.
 * 
 * @author xujiakun
 * 
 */
public class Module extends SearchInfo {

	private static final long serialVersionUID = 8713401229058920807L;

	/**
	 * 主键.
	 */
	private String modId;

	private String modName;

	private String modPid;

	private String domain;

	/**
	 * 显示(Y)/不显示(N).
	 */
	private String display;

	/**
	 * 类型: E 平台; S 子系统; C CORDYS...
	 */
	private String type;

	public String getModId() {
		return modId;
	}

	public void setModId(String modId) {
		this.modId = modId;
	}

	public String getModName() {
		return modName;
	}

	public void setModName(String modName) {
		this.modName = modName;
	}

	public String getModPid() {
		return modPid;
	}

	public void setModPid(String modPid) {
		this.modPid = modPid;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
