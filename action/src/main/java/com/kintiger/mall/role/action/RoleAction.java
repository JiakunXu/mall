package com.kintiger.mall.role.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import jxl.Sheet;
import jxl.Workbook;

import com.kintiger.mall.api.dict.IDictService;
import com.kintiger.mall.api.role.IRoleService;
import com.kintiger.mall.api.role.bo.Role;
import com.kintiger.mall.api.user.IUserService;
import com.kintiger.mall.api.user.bo.User;
import com.kintiger.mall.framework.action.BaseAction;
import com.kintiger.mall.framework.annotation.ActionMonitor;
import com.kintiger.mall.framework.annotation.Decode;
import com.kintiger.mall.framework.annotation.FunAction;
import com.kintiger.mall.framework.bo.StringResult;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;
import com.kintiger.mall.framework.util.DateUtil;
import com.kintiger.mall.framework.util.ExcelUtil;
import com.kintiger.mall.framework.util.LogUtil;
import com.kintiger.mall.framework.webwork.annotations.JsonResult;

/**
 * 角色.
 * 
 * @author xujiakun
 * 
 */
public class RoleAction extends BaseAction {

	private static final long serialVersionUID = 2107317464113267380L;

	private static final String CHARSET_UTF8 = "UTF-8";

	private static final String CHARSET_ISO8859 = "ISO8859-1";

	private Logger4jExtend logger = Logger4jCollection.getLogger(RoleAction.class);

	private IRoleService roleService;

	private IUserService userService;

	private IDictService dictService;

	private List<Role> roleList;

	private int total;

	@Decode
	private String roleId;

	@Decode
	private String roleName;

	private Role role;

	/**
	 * 岗位.
	 */
	private String staId;

	private String staName;

	private String roleIds;

	private String staIds;

	/**
	 * 菜单id.
	 */
	private String menuId;

	/**
	 * 角色类型.
	 */
	private String type;

	private String passport;

	@Decode
	private String userName;

	private String orgId;

	private String jsonList;

	private String id;

	private String userIds;

	private String userId;

	/**
	 * 编制.
	 */
	private String posId;

	private String posName;

	private String expireDate;

	private String target;

	/**
	 * 查询状态类型 Y or N.
	 */
	private String staState;

	/**
	 * 查询状态类型 Y or N.
	 */
	private String userState;

	/**
	 * 角色复制 用户id.
	 */
	private String copyUserId;

	/**
	 * 是否默认拥有　(Y是/N否).
	 */
	private String defaults;

	private File upload;

	private String uploadFileName;

	/**
	 * 查询角色.
	 * 
	 * @return
	 */
	@ActionMonitor(actionName = "角色查询")
	public String searchRole() {
		return "searchRole";
	}

	@JsonResult(field = "roleList", include = { "roleId", "roleName", "remark", "type", "menuCount", "stationCount",
		"userCount", "positionCount", "dictName" }, total = "total")
	public String getRoleJsonList() {
		Role r = new Role();
		r = getSearchInfo(r);

		if (StringUtils.isNotEmpty(roleId) && StringUtils.isNotEmpty(roleId.trim())) {
			r.setRoleId(roleId.trim());
		}

		if (StringUtils.isNotEmpty(roleName) && StringUtils.isNotEmpty(roleName.trim())) {
			r.setRoleName(roleName.trim());
		}

		if (StringUtils.isNotEmpty(type) && StringUtils.isNotEmpty(type.trim())) {
			r.setType(type.trim());
		}

		if (StringUtils.isNotEmpty(staState) && StringUtils.isNotEmpty(staState.trim())) {
			r.setStaState(staState.trim());
		}

		if (StringUtils.isNotEmpty(userState) && StringUtils.isNotEmpty(userState.trim())) {
			r.setUserState(userState.trim());
		}

		if (StringUtils.isNotEmpty(defaults) && StringUtils.isNotEmpty(defaults.trim())) {
			r.setDefaults(defaults.trim());
		}

		total = roleService.getRoleCount(r);
		if (total > 0) {
			roleList = roleService.getRoleList(r);
		}
		return JSON_RESULT;
	}

	/**
	 * 获取mail参数.
	 * 
	 * @return
	 */
	private Map<String, String> initMail() {
		return dictService.getDictMap("roleExpireDays");
	}

	/**
	 * 初始化过期时间.
	 */
	private void initExpireDate() {
		gmtStart = DateUtil.getDateTime(DateUtil.addDays(new Date(), 1), DateUtil.DEFAULT_DATE_FORMAT);

		Map<String, String> map = initMail();
		int days = -1;
		try {
			days = Integer.parseInt(map.get("days"));
		} catch (NumberFormatException e) {
			logger.error(LogUtil.parserBean(map), e);
		}

		expireDate =
			DateUtil.getDateTime(DateUtil.addDays(new Date(), (days == -1) ? 30 : days), DateUtil.DEFAULT_DATE_FORMAT);
	}

	/**
	 * 查询指定userId的角色.
	 * 
	 * @return
	 */
	@ActionMonitor(actionName = "人员角色查询")
	@FunAction(alias = "searchUserRole")
	public String searchUserRole() {
		initExpireDate();

		if (StringUtils.isNotEmpty(passport) && StringUtils.isNotEmpty(passport.trim())) {
			try {
				passport = new String(passport.trim().getBytes(CHARSET_ISO8859), CHARSET_UTF8);
			} catch (UnsupportedEncodingException e) {
				logger.error(passport, e);
			}
		}

		if (StringUtils.isNotEmpty(userName) && StringUtils.isNotEmpty(userName.trim())) {
			try {
				userName = new String(userName.trim().getBytes(CHARSET_ISO8859), CHARSET_UTF8);
			} catch (UnsupportedEncodingException e) {
				logger.error(userName, e);
			}
		}

		return "searchUserRole";
	}

	/**
	 * 查询指定userId的角色清单.
	 * 
	 * @return
	 */
	@JsonResult(field = "roleList", include = { "id", "roleId", "roleName", "remark", "dictName", "expireDate", "state" }, total = "total")
	public String getUserRoleJsonList() {
		if (StringUtils.isBlank(userId)) {
			return JSON_RESULT;
		}

		Role r = new Role();
		r.setUserId(userId.trim());

		r.setStart(0);
		r.setLimit(300);

		roleList = roleService.getUserRoleList(r);

		if (roleList != null && roleList.size() > 0) {
			total = roleList.size();
		} else {
			total = 0;
		}

		return JSON_RESULT;
	}

	/**
	 * validate.
	 * 
	 * @param role
	 * @return
	 */
	private boolean validate(Role role) {
		if (role == null) {
			this.setFailMessage(IRoleService.ERROR_INPUT_MESSAGE);
			return false;
		}

		if (StringUtils.isBlank(role.getRoleId()) || StringUtils.isBlank(role.getRoleName())) {
			this.setFailMessage("角色编号和角色名称不能为空！");
			return false;
		}

		if (role.getRoleId().trim().length() > 30) {
			this.setFailMessage("角色编号长度不能超过30！");
			return false;
		}

		if (role.getRoleName().trim().length() > 80) {
			this.setFailMessage("角色编号长度不能超过80！");
			return false;
		}

		if (StringUtils.isBlank(role.getType())) {
			this.setFailMessage("角色类型不能为空！");
			return false;
		}

		return true;
	}

	public String createRolePrepare() {
		return CREATE_PREPARE;
	}

	/**
	 * 创建角色.
	 * 
	 * @return
	 */
	@ActionMonitor(actionName = "角色创建")
	public String createRole() {
		if (!validate(role)) {
			return RESULT_MESSAGE;
		}

		String d = role.getRoleId().substring(0, 1);
		if ("Z".equals(d) || "z".equals(d) || "Y".equals(d) || "y".equals(d)) {
			this.setFailMessage("Z和Y开头的角色编号为ERP中保留编号，请重新命名！");
			return RESULT_MESSAGE;
		}

		User user = this.getUser();
		if (user != null) {
			role.setModifyUser(user.getUserId());
		} else {
			this.setFailMessage(IRoleService.ERROR_MESSAGE);
			return RESULT_MESSAGE;
		}

		StringResult result = roleService.createRole(role);

		if (IRoleService.ERROR.equals(result.getCode())) {
			this.setFailMessage(result.getResult());
		} else {
			this.setSuccessMessage("角色创建成功！");
		}

		return RESULT_MESSAGE;
	}

	/**
	 * 修改/查询角色信息.
	 * 
	 * @return
	 */
	public String updateRolePrepare() {
		if (StringUtils.isNotEmpty(roleId) && StringUtils.isNotEmpty(roleId.trim())) {
			try {
				roleId = new String(roleId.trim().getBytes(CHARSET_ISO8859), CHARSET_UTF8);
				role = roleService.getRoleById(roleId);
			} catch (UnsupportedEncodingException e) {
				logger.error(roleId, e);
			}
		}

		role = role == null ? new Role() : role;

		return UPDATE_PREPARE;
	}

	@ActionMonitor(actionName = "角色修改")
	public String updateRole() {
		if (!validate(role)) {
			this.setFailMessage(IRoleService.ERROR_INPUT_MESSAGE);
			return RESULT_MESSAGE;
		}

		User user = this.getUser();
		if (user == null) {
			this.setFailMessage(IRoleService.ERROR_MESSAGE);
			return RESULT_MESSAGE;
		}

		role.setModifyUser(user.getUserId());

		StringResult result = roleService.updateRole(role);

		if (IRoleService.ERROR.equals(result.getCode())) {
			this.setFailMessage(result.getResult());
		} else {
			this.setSuccessMessage("角色修改成功！");
		}

		return RESULT_MESSAGE;
	}

	@ActionMonitor(actionName = "角色删除")
	public String deleteRole() {
		if (StringUtils.isBlank(roleId)) {
			this.setFailMessage("被删除角色不能为空！");
			return RESULT_MESSAGE;
		}

		try {
			roleId = new String(roleId.trim().getBytes(CHARSET_ISO8859), CHARSET_UTF8);
		} catch (UnsupportedEncodingException e) {
			logger.error(roleId, e);
			this.setFailMessage("系统正忙，请稍后再试！");
			return RESULT_MESSAGE;
		}

		User user = this.getUser();

		if (user == null) {
			this.setFailMessage(IRoleService.ERROR_MESSAGE);
			return RESULT_MESSAGE;
		}

		StringResult result = roleService.deleteRole(roleId, user.getUserId());

		if (IRoleService.ERROR.equals(result.getCode())) {
			this.setFailMessage(result.getResult());
		} else {
			this.setSuccessMessage(result.getResult());
		}

		return RESULT_MESSAGE;
	}

	/**
	 * 供权限点使用/供菜单使用.
	 * 
	 * @return
	 */
	public String searchRole4Config() {
		return "searchRole4Config";
	}

	/**
	 * 供权限点使用/供菜单使用.
	 * 
	 * @return
	 */
	@JsonResult(field = "roleList", include = { "roleId", "roleName", "remark" }, total = "total")
	public String getRole4ConfigJsonList() {
		Role r = new Role();
		r = getSearchInfo(r);

		if (StringUtils.isNotEmpty(roleId) && StringUtils.isNotEmpty(roleId.trim())) {
			r.setRoleId(roleId.trim());
		}

		if (StringUtils.isNotEmpty(roleName) && StringUtils.isNotEmpty(roleName.trim())) {
			r.setRoleName(roleName.trim());
		}

		if (StringUtils.isNotEmpty(menuId) && StringUtils.isNotEmpty(menuId.trim())) {
			r.setMenuId(menuId.trim());

			total = roleService.getRole4MenuCount(r);
			if (total > 0) {
				roleList = roleService.getRole4MenuList(r);
			}

		}

		return JSON_RESULT;
	}

	/**
	 * 选择角色 用于岗位.
	 * 
	 * @return
	 */
	public String searchSelectedRole() {
		if (StringUtils.isNotEmpty(staId) && StringUtils.isNotEmpty(staId.trim())) {
			try {
				staId = new String(staId.getBytes(CHARSET_ISO8859), "utf-8");
			} catch (UnsupportedEncodingException e) {
				logger.error("staId:" + staId, e);
			}
		}

		if (StringUtils.isNotEmpty(staName) && StringUtils.isNotEmpty(staName.trim())) {
			try {
				staName = new String(staName.getBytes(CHARSET_ISO8859), "utf-8");
			} catch (UnsupportedEncodingException e) {
				logger.error("staName:" + staName, e);
			}
		}

		if (StringUtils.isNotEmpty(posName) && StringUtils.isNotEmpty(posName.trim())) {
			try {
				posName = new String(posName.getBytes(CHARSET_ISO8859), "utf-8");
			} catch (UnsupportedEncodingException e) {
				logger.error("posName:" + posName, e);
			}
		}

		return "searchSelectedRole";
	}

	@JsonResult(field = "roleList", include = { "id", "roleId", "roleName", "remark", "menuCount", "type", "dictName" }, total = "total")
	public String getSelectedRoleJsonList() {
		Role r = new Role();
		r = getSearchInfo(r);

		if (StringUtils.isNotEmpty(roleId) && StringUtils.isNotEmpty(roleId.trim())) {
			r.setRoleId(roleId.trim());
		}

		if (StringUtils.isNotEmpty(roleName) && StringUtils.isNotEmpty(roleName.trim())) {
			r.setRoleName(roleName.trim());
		}

		if (StringUtils.isNotEmpty(staId) && StringUtils.isNotEmpty(staId.trim())) {
			r.setStaId(staId.trim());

			total = roleService.getRole4StationCount(r);
			if (total > 0) {
				roleList = roleService.getRole4StationList(r);
			}
		} else if (StringUtils.isNotEmpty(posId) && StringUtils.isNotEmpty(posId.trim())) {
			r.setPosId(posId.trim());

			total = roleService.getRole4PositionCount(r);
			if (total > 0) {
				roleList = roleService.getRole4PositionList(r);
			}
		}

		return JSON_RESULT;
	}

	/**
	 * 选择角色 用于岗位.
	 * 
	 * @return
	 */
	@ActionMonitor(actionName = "角色岗位/编制配置")
	public String selectRole() {
		Role r = new Role();
		StringResult result = null;

		if (StringUtils.isNotEmpty(roleIds) && StringUtils.isNotEmpty(roleIds.trim())) {

			String[] temp = roleIds.split(",");
			String[] ids = new String[temp.length];
			int i = 0;

			for (String t : temp) {
				ids[i++] = t.trim();
			}
			r.setCodes(ids);
		}

		User u = this.getUser();
		if (u == null) {
			this.setFailMessage(IRoleService.ERROR_MESSAGE);
			return RESULT_MESSAGE;
		}

		if (StringUtils.isNotEmpty(staId) && StringUtils.isNotEmpty(staId.trim())) {
			r.setStaId(staId.trim());
			r.setModifyUser(u.getUserId());
			result = roleService.selectRole4Station(r);
		} else if (StringUtils.isNotEmpty(posId) && StringUtils.isNotEmpty(posId.trim())) {
			r.setPosId(posId.trim());
			r.setModifyUser(u.getUserId());
			result = roleService.selectRole4Position(r);
		}

		if (result == null) {
			this.setFailMessage(IRoleService.ERROR_MESSAGE);
		} else if (IRoleService.ERROR.equals(result.getCode())) {
			this.setFailMessage(result.getResult());
		} else if (IRoleService.SUCCESS.equals(result.getCode())) {
			this.setSuccessMessage("角色维护成功！");
		}

		return RESULT_MESSAGE;
	}

	/**
	 * 通过复制其他岗位角色方式 维护岗位角色关系.
	 * 
	 * @return
	 */
	public String selectStationRole() {
		Role r = new Role();
		StringResult result = null;

		if (StringUtils.isBlank(staIds) || StringUtils.isBlank(staId)) {
			this.setFailMessage(IRoleService.ERROR_INPUT_MESSAGE);
			return RESULT_MESSAGE;
		}

		String[] temp = staIds.split(",");
		String[] ids = new String[temp.length];
		int i = 0;

		for (String t : temp) {
			ids[i++] = t.trim();
		}

		r.setCodes(ids);

		User u = this.getUser();
		if (u == null) {
			this.setFailMessage(IRoleService.ERROR_MESSAGE);
			return RESULT_MESSAGE;
		}

		r.setStaId(staId.trim());
		r.setModifyUser(u.getUserId());
		result = roleService.selectStationRole4Station(r);

		if (IRoleService.ERROR.equals(result.getCode())) {
			this.setFailMessage(result.getResult());
		} else if (IRoleService.SUCCESS.equals(result.getCode())) {
			if ("0".equals(result.getResult())) {
				this.setSuccessMessage("选择岗位的角色都已维护！");
			} else {
				this.setSuccessMessage("成功维护 " + result.getResult() + " 个角色！");
			}
		}

		return RESULT_MESSAGE;
	}

	/**
	 * 选择角色 用于岗位 or 编制.
	 * 
	 * @return
	 */
	@ActionMonitor(actionName = "角色岗位/编制删除")
	public String deleteSelectedRole() {
		String[] l = new String[roleList.size()];
		int i = 0;
		Role ro = new Role();
		String t = null;

		if (StringUtils.isNotEmpty(staId) && StringUtils.isNotEmpty(staId.trim())) {
			t = "station";
		} else if (StringUtils.isNotEmpty(posId) && StringUtils.isNotEmpty(posId.trim())) {
			t = "position";
		}

		for (Role r : roleList) {
			l[i++] = r.getId();
		}

		// 无有效的角色id
		if (i == 0) {
			this.setFailMessage("请选择角色！");
			return RESULT_MESSAGE;
		}

		ro.setCodes(l);

		StringResult result = null;

		if ("station".equals(t)) {
			result = roleService.deleteRole4Station(ro);
		} else if ("position".equals(t)) {
			result = roleService.deleteRole4Position(ro);
		}

		if (result == null) {
			this.setFailMessage(IRoleService.ERROR_MESSAGE);
		} else if (IRoleService.ERROR.equals(result.getCode())) {
			this.setFailMessage(result.getResult());
		} else {
			this.setSuccessMessage("已成功删除 " + result.getResult() + " 个角色！");
		}

		return RESULT_MESSAGE;
	}

	/**
	 * 配置user_role.
	 * 
	 * @return
	 */
	@ActionMonitor(actionName = "角色人员查询")
	public String searchUser4RolePrepare() {
		if (StringUtils.isNotEmpty(roleId) && StringUtils.isNotEmpty(roleId.trim())) {
			try {
				roleId = new String(roleId.trim().getBytes(CHARSET_ISO8859), CHARSET_UTF8);
			} catch (UnsupportedEncodingException e) {
				logger.error(roleId, e);
			}
		}

		if (StringUtils.isNotEmpty(roleName) && StringUtils.isNotEmpty(roleName.trim())) {
			try {
				roleName = new String(roleName.trim().getBytes(CHARSET_ISO8859), CHARSET_UTF8);
			} catch (UnsupportedEncodingException e) {
				logger.error(roleName, e);
			}
		}

		return "searchUser4RolePrepare";
	}

	@JsonResult(field = "roleList", include = { "id", "userId", "passport", "userName", "expireDate", "state" }, total = "total")
	public String getUser4RoleJsonList() {
		Role r = new Role();
		r = getSearchInfo(r);

		if (StringUtils.isNotEmpty(roleId) && StringUtils.isNotEmpty(roleId.trim())) {
			r.setRoleId(roleId.trim());
		}

		if (StringUtils.isNotEmpty(passport) && StringUtils.isNotEmpty(passport.trim())) {
			r.setPassport(passport.trim());
		}

		if (StringUtils.isNotEmpty(userName) && StringUtils.isNotEmpty(userName.trim())) {
			r.setUserName(userName.trim());
		}

		total = roleService.getUserRoleCount(r);
		if (total > 0) {
			roleList = roleService.getUserRoleList(r);
		}

		return JSON_RESULT;
	}

	public String searchUser() {
		try {
			roleId = new String(roleId.getBytes(CHARSET_ISO8859), "utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("roleId:" + roleId, e);
		}

		return "searchUser";
	}

	/**
	 * decode.
	 */
	private void decodeRoleId() {
		if (StringUtils.isNotEmpty(roleId)) {
			try {
				roleId = new String(roleId.getBytes(CHARSET_ISO8859), "utf-8");
			} catch (UnsupportedEncodingException e) {
				logger.error("roleId:" + roleId, e);
			}
		}
	}

	/**
	 * 展开人员组织树.
	 * 
	 * @return
	 */
	public String getOrgTree() {
		decodeRoleId();

		this.actionName = "roleAction!getUserSelector.htm?roleId=" + roleId + "&target=" + target;
		return "orgTreeAjaxInfo";
	}

	public String getUserSelector() {
		decodeRoleId();

		initExpireDate();

		return "getUserSelector";
	}

	/**
	 * 保存角色人员.
	 * 
	 * @return
	 */
	@ActionMonitor(actionName = "角色人员配置")
	public String createUser4Role() {
		if (StringUtils.isBlank(roleId) || StringUtils.isBlank(userIds) || StringUtils.isBlank(expireDate)) {
			this.setFailMessage("角色编号和人员编号不能为空！");
			return RESULT_MESSAGE;
		}

		String[] uid = userIds.split(",");

		User user = this.getUser();
		if (user == null) {
			this.setFailMessage(IRoleService.ERROR_MESSAGE);
			return RESULT_MESSAGE;
		}

		String modifyUser = user.getUserId();

		StringResult c = roleService.createUser4Role(roleId, uid, expireDate, modifyUser);

		if (IRoleService.SUCCESS.equals(c.getCode())) {
			this.setSuccessMessage("已成功维护 " + c.getResult() + " 个人员角色！");
		} else {
			this.setFailMessage(c.getResult());
		}

		return RESULT_MESSAGE;
	}

	/**
	 * 保存角色人员.
	 * 
	 * @return
	 */
	@ActionMonitor(actionName = "角色人员配置")
	public String createRole4User() {
		if (StringUtils.isBlank(roleIds) || StringUtils.isBlank(userId) || StringUtils.isBlank(expireDate)) {
			this.setFailMessage("角色编号和人员编号不能为空！");
			return RESULT_MESSAGE;
		}

		String[] temp = roleIds.split(",");
		String[] ids = new String[temp.length];
		int i = 0;

		for (String t : temp) {
			ids[i++] = t.trim();
		}

		User user = this.getUser();
		if (user == null) {
			this.setFailMessage(IRoleService.ERROR_MESSAGE);
			return RESULT_MESSAGE;
		}

		String modifyUser = user.getUserId();

		StringResult c = roleService.createRole4User(ids, userId, expireDate, modifyUser);

		if (IRoleService.SUCCESS.equals(c.getCode())) {
			this.setSuccessMessage("成功维护人员角色！");
		} else {
			this.setFailMessage(c.getResult());
		}

		return RESULT_MESSAGE;
	}

	/**
	 * 复制角色人员.
	 * 
	 * @return
	 */
	@ActionMonitor(actionName = "角色人员复制")
	public String copyRole4User() {
		if (StringUtils.isEmpty(copyUserId) || StringUtils.isEmpty(userId) || StringUtils.isEmpty(copyUserId.trim())
			|| StringUtils.isEmpty(userId.trim()) || StringUtils.isEmpty(expireDate)
			|| StringUtils.isEmpty(expireDate.trim())) {
			this.setFailMessage("复制人员编号和人员编号不能为空！");
			return RESULT_MESSAGE;
		}

		User user = this.getUser();
		if (user == null) {
			this.setFailMessage(IRoleService.ERROR_MESSAGE);
			return RESULT_MESSAGE;
		}

		String modifyUser = user.getUserId();

		StringResult c = roleService.copyRole4User(copyUserId, userId, expireDate, modifyUser);

		if (IRoleService.SUCCESS.equals(c.getCode())) {
			this.setSuccessMessage("已成功维护 " + c.getResult() + " 个人员角色！");
		} else {
			this.setFailMessage(c.getResult());
		}

		return RESULT_MESSAGE;
	}

	/**
	 * 删除角色人员.
	 * 
	 * @return
	 */
	@ActionMonitor(actionName = "角色人员删除")
	public String deleteUser4Role() {
		String[] s = null;
		int i = 0;

		if (roleList != null && roleList.size() > 0) {
			s = new String[roleList.size()];

			for (Role r : roleList) {
				s[i++] = r.getId();
			}
		} else if (StringUtils.isNotEmpty(id) && StringUtils.isNotEmpty(id.trim())) {
			s = new String[1];
			s[i++] = id;
		}

		// 无有效的角色id
		if (i == 0) {
			this.setFailMessage("请选择角色！");
			return RESULT_MESSAGE;
		}

		Role ro = new Role();
		ro.setCodes(s);

		StringResult r = roleService.deleteUser4Role(ro);

		if (IRoleService.SUCCESS.equals(r.getCode())) {
			this.setSuccessMessage("角色人员删除成功!");
		} else {
			this.setFailMessage("角色人员删除失败!");
		}

		return RESULT_MESSAGE;
	}

	@ActionMonitor(actionName = "角色人员过期日期修改")
	public String updateExpireDate() {
		if (StringUtils.isEmpty(id) || StringUtils.isEmpty(id.trim()) || StringUtils.isEmpty(expireDate)
			|| StringUtils.isEmpty(expireDate.trim())) {
			this.setFailMessage("角色人员编号和过期日期不能为空！");
			return RESULT_MESSAGE;
		}

		User user = this.getUser();
		if (user == null) {
			this.setFailMessage(IRoleService.ERROR_MESSAGE);
			return RESULT_MESSAGE;
		}

		Role r = new Role();
		r.setId(id.trim());
		r.setModifyUser(user.getUserId());
		r.setExpireDate(expireDate.trim());

		StringResult s = roleService.updateExpireDate(r);

		if (IRoleService.SUCCESS.equals(s.getCode())) {
			this.setSuccessMessage("角色人员过期日期修改成功!");
		} else {
			this.setFailMessage(s.getResult());
		}

		return RESULT_MESSAGE;

	}

	/**
	 * 导出role模板.
	 * 
	 * @return
	 */
	public String exportDataTemplate() {
		InputStream inputStream = null;
		OutputStream outputStream = null;

		try {
			ExcelUtil util = new ExcelUtil();
			inputStream =
				Thread.currentThread().getContextClassLoader().getResource("resource/exportRoleDataTemplate.xls")
					.openStream();
			HttpServletResponse response = this.getServletResponse();
			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition",
				"attachment; filename=\"" + new String("RoleDataTemplate".getBytes("GBK"), "ISO8859-1") + ".xls\"");
			outputStream = response.getOutputStream();
			util.createExcelWithTemplate(inputStream, outputStream, null, null);
			outputStream.flush();

			return RESULT_MESSAGE;
		} catch (Exception e) {
			logger.error(e);
			this.setFailMessage(IRoleService.ERROR_MESSAGE);
			return RESULT_MESSAGE;
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
		}
	}

	public void importData() {
		String resultMsg = null;

		User user = this.getUser();
		if (user == null) {
			resultMsg = IRoleService.ERROR_MESSAGE;
		}

		if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(userId.trim())) {
			resultMsg = IRoleService.ERROR_INPUT_MESSAGE;
		}

		if (StringUtils.isEmpty(expireDate) || StringUtils.isEmpty(expireDate.trim())) {
			resultMsg = IRoleService.ERROR_INPUT_MESSAGE;
		}

		PrintWriter out = null;

		try {
			HttpServletResponse response = this.getServletResponse();
			response.setContentType("text/html; charset=GBK");
			out = response.getWriter();

			// 处理上传文件
			if (StringUtils.isEmpty(resultMsg) || StringUtils.isEmpty(resultMsg.trim())) {
				resultMsg = processData(uploadFileName, upload, userId.trim(), expireDate.trim(), user.getUserId());
			}

			out.write(StringUtils.isEmpty(resultMsg) ? "{success:true,msg:'操作成功'}" : "{success:false,msg:'" + resultMsg
				+ "'}");
			out.flush();
		} catch (Exception e) {
			logger.error(e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
					logger.error(e);
				}
			}
		}
	}

	private String processData(String uploadFileName, File upload, String userId, String expireDate, String modifyUser) {
		InputStream inputStream = null;
		Workbook workbook = null;
		StringBuilder sb = new StringBuilder();

		try {
			// 上传文件不存在
			if (StringUtils.isEmpty(uploadFileName)) {
				return "请选择需要上传文件！";
			}

			String end = StringUtils.substring(uploadFileName, StringUtils.lastIndexOf(uploadFileName, '.'));

			// 文件格式不正确
			if (!".xls".equals(end) && !".xlsx".equals(end)) {
				return "上传文件类型无法识别！";
			}

			inputStream = new FileInputStream(upload);
			workbook = Workbook.getWorkbook(inputStream);
			Sheet sheet = workbook.getSheet(0);
			int columns = sheet.getColumns();
			int rows = sheet.getRows();

			if (1 != columns) {
				return "上传文件列数与下载模板不一致！";
			}

			Map<String, Boolean> map1 = new HashMap<String, Boolean>();

			for (int i = 1; i < rows; i++) {
				for (int j = 0; j < columns; j++) {

					// 第i行 第j列 rs.getCell(j, i).getContents()
					String value = sheet.getCell(j, i).getContents();

					// 验证value有效性 判断是否可为空
					if (StringUtils.isEmpty(value) || StringUtils.isEmpty(value.trim())) {
						if (sb.length() > 0) {
							sb.append("<br />");
						}
						sb.append("第").append(i + 1).append("行").append("第").append(j + 1).append("列").append("不能为空");
					} else {
						map1.put(value.trim(), true);
					}
				}
			}

			if (sb.length() > 0) {
				return sb.toString();
			}

			// 验证ids role_id是否存在
			// 遍历上传附件的map1
			String[] ids = new String[map1.size()];
			int co2 = 0;
			for (Map.Entry<String, Boolean> m : map1.entrySet()) {
				ids[co2++] = m.getKey();
			}

			Role r = new Role();
			r.setCodes(ids);
			int count = roleService.getRoleCount(r);
			// 存在问题
			if (count != co2) {
				Map<String, Boolean> map2 = new HashMap<String, Boolean>();
				r.setStart(0);
				r.setLimit(count);
				List<Role> list = roleService.getRoleList(r);

				if (list != null && list.size() > 0) {
					for (Role rr : list) {
						map2.put(rr.getRoleId(), true);
					}
				}

				// 遍历上传附件的map1
				for (Map.Entry<String, Boolean> m : map1.entrySet()) {
					String key = m.getKey();
					if (!map2.containsKey(key)) {
						sb.append(key).append(", ");
					}
				}

				if (sb.length() > 0) {
					sb.append("角色在系统中不存在！");
					return sb.toString();
				}
			}

			StringResult c = roleService.createRole4User(ids, userId, expireDate, modifyUser);

			if (IRoleService.SUCCESS.equals(c.getCode())) {
				return "";
			} else {
				return c.getResult();
			}
		} catch (Exception e) {
			logger.error(e);
			return IRoleService.ERROR_MESSAGE;
		} finally {
			if (workbook != null) {
				try {
					workbook.close();
				} catch (Exception e) {
					logger.error(e);
				}
			}
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (Exception e) {
					logger.error(e);
				}
			}
		}
	}

	public IRoleService getRoleService() {
		return roleService;
	}

	public void setRoleService(IRoleService roleService) {
		this.roleService = roleService;
	}

	public IUserService getUserService() {
		return userService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public IDictService getDictService() {
		return dictService;
	}

	public void setDictService(IDictService dictService) {
		this.dictService = dictService;
	}

	public List<Role> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
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

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getStaId() {
		return staId;
	}

	public void setStaId(String staId) {
		this.staId = staId;
	}

	public String getStaName() {
		return staName;
	}

	public void setStaName(String staName) {
		this.staName = staName;
	}

	public String getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}

	public String getStaIds() {
		return staIds;
	}

	public void setStaIds(String staIds) {
		this.staIds = staIds;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPassport() {
		return passport;
	}

	public void setPassport(String passport) {
		this.passport = passport;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getJsonList() {
		return jsonList;
	}

	public void setJsonList(String jsonList) {
		this.jsonList = jsonList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserIds() {
		return userIds;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPosId() {
		return posId;
	}

	public void setPosId(String posId) {
		this.posId = posId;
	}

	public String getPosName() {
		return posName;
	}

	public void setPosName(String posName) {
		this.posName = posName;
	}

	public String getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getStaState() {
		return staState;
	}

	public void setStaState(String staState) {
		this.staState = staState;
	}

	public String getUserState() {
		return userState;
	}

	public void setUserState(String userState) {
		this.userState = userState;
	}

	public String getCopyUserId() {
		return copyUserId;
	}

	public void setCopyUserId(String copyUserId) {
		this.copyUserId = copyUserId;
	}

	public String getDefaults() {
		return defaults;
	}

	public void setDefaults(String defaults) {
		this.defaults = defaults;
	}

	public File getUpload() {
		return upload;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

}
