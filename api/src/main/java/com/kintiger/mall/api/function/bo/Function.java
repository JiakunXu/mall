package com.kintiger.mall.api.function.bo;

import com.kintiger.mall.framework.bo.SearchInfo;

/**
 * 功能点.
 * 
 * @author xujiakun
 * 
 */
public class Function extends SearchInfo {

	private static final long serialVersionUID = -3196546372377811139L;

	private String funId;

	private String funName;

	private String remark;

	private String url;

	private String actionId;

	private String actionName;

	/**
	 * 别名.
	 */
	private String alias;

	/**
	 * 是否默认拥有　(Y是/N否).
	 */
	private String defaults;

	private String state;

	private String modifyUser;

	/**
	 * role_action.
	 */
	private String roleId;

	private String id;

	public String getFunId() {
		return funId;
	}

	public void setFunId(String funId) {
		this.funId = funId;
	}

	public String getFunName() {
		return funName;
	}

	public void setFunName(String funName) {
		this.funName = funName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getActionId() {
		return actionId;
	}

	public void setActionId(String actionId) {
		this.actionId = actionId;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getDefaults() {
		return defaults;
	}

	public void setDefaults(String defaults) {
		this.defaults = defaults;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
