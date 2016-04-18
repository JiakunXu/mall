package com.kintiger.mall.api.employee.bo;

import com.kintiger.mall.framework.bo.SearchInfo;

/**
 * 雇员.
 * 
 * @author
 * 
 */
public class Employee extends SearchInfo {

	private static final long serialVersionUID = 5385461186037323057L;

	/**
	 * 雇员ID.
	 */
	private String empId;

	/**
	 * 用户ID.
	 */
	private String userId;

	/**
	 * 公司ID.
	 */
	private String companyId;

	/**
	 * 状态 D:删除 U:正常.
	 */
	private String state;

	/**
	 * 操作人ID.
	 */
	private String modifyUser;

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
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

}
