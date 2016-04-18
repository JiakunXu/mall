package com.kintiger.mall.function.action;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.kintiger.mall.api.function.IFunctionService;
import com.kintiger.mall.api.function.bo.Function;
import com.kintiger.mall.api.user.bo.User;
import com.kintiger.mall.framework.action.BaseAction;
import com.kintiger.mall.framework.annotation.ActionMonitor;
import com.kintiger.mall.framework.annotation.Decode;
import com.kintiger.mall.framework.bo.BooleanResult;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;
import com.kintiger.mall.framework.webwork.annotations.JsonResult;

/**
 * 功能点.
 * 
 * @author xujiakun
 * 
 */
public class FunctionAction extends BaseAction {

	private static final long serialVersionUID = -2195638708016542804L;

	private static final String CHARSET_UTF8 = "UTF-8";

	private static final String CHARSET_ISO8859 = "ISO8859-1";

	private Logger4jExtend logger = Logger4jCollection.getLogger(FunctionAction.class);

	private IFunctionService functionService;

	private List<Function> functionList;

	private int total;

	@Decode
	private String roleId;

	private String roleName;

	/**
	 * as menuId.
	 */
	private String funId;

	/**
	 * 维护角色权限点页面.
	 * 
	 * @return
	 */
	public String searchFunAction4Role() {
		if (StringUtils.isNotBlank(roleId)) {
			try {
				roleId = new String(roleId.trim().getBytes(CHARSET_ISO8859), CHARSET_UTF8);
			} catch (UnsupportedEncodingException e) {
				logger.error(roleId, e);
			}
		}

		if (StringUtils.isNotBlank(roleName)) {
			try {
				roleName = new String(roleName.trim().getBytes(CHARSET_ISO8859), CHARSET_UTF8);
			} catch (UnsupportedEncodingException e) {
				logger.error(roleName, e);
			}
		}

		return "searchFunAction4Role";
	}

	@JsonResult(field = "functionList", include = { "id", "actionId", "actionName" }, total = "total")
	public String getFunAction4RoleJsonList() {
		Function f = new Function();
		f = getSearchInfo(f);

		if (StringUtils.isNotBlank(roleId)) {
			f.setRoleId(roleId.trim());
		} else {
			return JSON_RESULT;
		}

		functionList = functionService.getFunAction4RoleList(f);

		if (functionList != null && functionList.size() > 0) {
			total = functionList.size();
		}

		return JSON_RESULT;
	}

	/**
	 * 更新权限点.
	 * 
	 * @return
	 */
	@ActionMonitor(actionName = "权限点配置")
	public String updateFunAction4Role() {
		if (StringUtils.isBlank(roleId)) {
			this.setFailMessage(IFunctionService.ERROR_INPUT_MESSAGE);
			return RESULT_MESSAGE;
		}

		User user = this.getUser();

		if (user == null) {
			this.setFailMessage(IFunctionService.ERROR_MESSAGE);
			return RESULT_MESSAGE;
		}

		BooleanResult r = functionService.updateFunAction4Role(roleId, functionList, user.getUserId());

		if (r.getResult()) {
			this.setSuccessMessage(r.getCode());
		} else {
			this.setFailMessage(r.getCode());
		}

		return RESULT_MESSAGE;
	}

	@JsonResult(field = "functionList", include = { "funName", "actionId", "actionName", "alias", "remark", "url",
		"defaults" }, total = "total")
	public String getFunActionJsonList() {
		Function f = new Function();
		f = getSearchInfo(f);

		if (StringUtils.isNotBlank(roleId)) {
			f.setRoleId(roleId.trim());
		}

		if (StringUtils.isNotBlank(funId)) {
			f.setFunId(funId.trim());
		}

		// if roleId is null && funId is null then return json
		if (StringUtils.isBlank(roleId) && StringUtils.isBlank(funId)) {
			return JSON_RESULT;
		}

		total = functionService.getFunActionCount(f);
		if (total > 0) {
			// 不需要分页
			if (StringUtils.isNotBlank(funId)) {
				this.setLimit(total);
			}

			functionList = functionService.getFunActionList(f);
		}

		return JSON_RESULT;
	}

	/**
	 * 
	 * @return
	 */
	@ActionMonitor(actionName = "权限点属性配置")
	public String updateFunAction() {
		if (StringUtils.isBlank(funId)) {
			this.setFailMessage(IFunctionService.ERROR_INPUT_MESSAGE);
			return RESULT_MESSAGE;
		}

		User user = this.getUser();

		if (user == null) {
			this.setFailMessage(IFunctionService.ERROR_MESSAGE);
			return RESULT_MESSAGE;
		}

		BooleanResult r = functionService.updateFunAction(funId, functionList, user.getUserId());

		if (r.getResult()) {
			this.setSuccessMessage(r.getCode());
		} else {
			this.setFailMessage(r.getCode());
		}

		return RESULT_MESSAGE;
	}

	public IFunctionService getFunctionService() {
		return functionService;
	}

	public void setFunctionService(IFunctionService functionService) {
		this.functionService = functionService;
	}

	public List<Function> getFunctionList() {
		return functionList;
	}

	public void setFunctionList(List<Function> functionList) {
		this.functionList = functionList;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getFunId() {
		return funId;
	}

	public void setFunId(String funId) {
		this.funId = funId;
	}

}
