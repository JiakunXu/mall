package com.kintiger.mall.role.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.kintiger.mall.api.function.IFunctionService;
import com.kintiger.mall.api.function.bo.Function;
import com.kintiger.mall.api.role.IRoleService;
import com.kintiger.mall.api.role.bo.Role;
import com.kintiger.mall.framework.bo.BooleanResult;
import com.kintiger.mall.framework.bo.StringResult;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;
import com.kintiger.mall.framework.util.LogUtil;
import com.kintiger.mall.role.dao.IRoleDao;

/**
 * role service.
 * 
 * @author xujiakun
 * 
 */
public class RoleServiceImpl implements IRoleService {

	private Logger4jExtend logger = Logger4jCollection.getLogger(RoleServiceImpl.class);

	private TransactionTemplate transactionTemplate;

	private IFunctionService functionService;

	private IRoleDao roleDao;

	public int getRoleCount(Role role) {
		try {
			return roleDao.getRoleCount(role);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(role), e);
		}

		return 0;
	}

	public List<Role> getRoleList(Role role) {
		try {
			return roleDao.getRoleList(role);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(role), e);
		}

		return null;
	}

	public List<Role> getSAPRoleList(Role role) {
		try {
			return roleDao.getSAPRoleList(role);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(role), e);
		}

		return null;
	}

	public StringResult createRole(Role role) {
		StringResult result = new StringResult();
		result.setCode(IRoleService.ERROR);
		result.setResult(IRoleService.ERROR_MESSAGE);

		if ("S".equals(role.getType())) {
			result.setResult("ERP角色的类型不能被创建！");
			return result;
		}

		try {
			String id = roleDao.createRole(role);
			result.setResult(id);
			result.setCode(IRoleService.SUCCESS);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(role), e);

			if (e.getMessage().indexOf("ORA-00001") != -1) {
				result.setResult("角色编号已存在！");
			}
		}

		return result;
	}

	public StringResult createRole(List<Role> roles) {
		StringResult result = new StringResult();
		result.setCode(IRoleService.ERROR);
		result.setResult(IRoleService.ERROR_MESSAGE);

		try {
			int id = roleDao.createRole(roles);
			result.setResult(String.valueOf(id));
			result.setCode(IRoleService.SUCCESS);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(roles), e);
		}

		return result;
	}

	public StringResult createUser4Role(String roleId, String[] userIds, String expireDate, String modifyUser) {
		StringResult result = new StringResult();
		result.setCode(IRoleService.ERROR);
		result.setResult(IRoleService.ERROR_MESSAGE);

		if (StringUtils.isEmpty(roleId) || StringUtils.isEmpty(roleId.trim()) || StringUtils.isEmpty(modifyUser)
			|| StringUtils.isEmpty(modifyUser.trim()) || userIds == null || userIds.length == 0
			|| StringUtils.isEmpty(expireDate) || StringUtils.isEmpty(expireDate.trim())) {
			result.setResult(IRoleService.ERROR_INPUT_MESSAGE);
			return result;
		}

		Map<String, Boolean> map = new HashMap<String, Boolean>();

		Role s = new Role();
		s.setRoleId(roleId.trim());
		s.setCodes(userIds);

		int c = getUserRoleCount(s);

		if (c > 0) {
			s.setStart(0);
			s.setLimit(c);
			List<Role> l = getUserRoleList(s);
			if (l != null && l.size() > 0) {
				for (Role ss : l) {
					map.put(ss.getUserId(), true);
				}
			}
		}

		List<Role> roles = new ArrayList<Role>();

		for (String userId : userIds) {
			if (!map.containsKey(userId)) {
				Role st = new Role();
				st.setRoleId(roleId.trim());
				st.setUserId(userId.trim());
				st.setModifyUser(modifyUser);
				st.setExpireDate(expireDate.trim());
				roles.add(st);
			}
		}

		if (roles.size() == 0) {
			result.setResult("人员都已维护角色！");
			return result;
		}

		try {
			int id = roleDao.createUserRole(roles);
			result.setResult(String.valueOf(id));
			result.setCode(IRoleService.SUCCESS);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(roles), e);
		}

		return result;
	}

	public StringResult createRole4User(String[] roleIds, String userId, String expireDate, String modifyUser) {
		StringResult result = new StringResult();
		result.setCode(IRoleService.ERROR);
		result.setResult(IRoleService.ERROR_MESSAGE);

		if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(userId.trim()) || StringUtils.isEmpty(modifyUser)
			|| StringUtils.isEmpty(modifyUser.trim()) || roleIds == null || roleIds.length == 0
			|| StringUtils.isEmpty(expireDate) || StringUtils.isEmpty(expireDate.trim())) {
			result.setResult(IRoleService.ERROR_INPUT_MESSAGE);
			return result;
		}

		Map<String, String> map = new HashMap<String, String>();

		Role s = new Role();
		s.setUserId(userId.trim());
		s.setCodes(roleIds);

		int c = getUserRoleCount(s);

		if (c > 0) {
			s.setStart(0);
			s.setLimit(c);
			List<Role> l = getUserRoleList(s);
			if (l != null && l.size() > 0) {
				for (Role ss : l) {
					map.put(ss.getRoleId(), ss.getId());
				}
			}
		}

		// 新建
		final List<Role> roles1 = new ArrayList<Role>();
		// 更新
		final Role role2 = new Role();
		String[] codes = new String[roleIds.length];
		int i = 0;

		for (String roleId : roleIds) {
			if (!map.containsKey(roleId)) {
				Role st1 = new Role();
				st1.setRoleId(roleId);
				st1.setUserId(userId.trim());
				st1.setModifyUser(modifyUser);
				st1.setExpireDate(expireDate.trim());
				roles1.add(st1);
			} else {
				codes[i++] = map.get(roleId);
			}
		}

		role2.setCodes(codes);
		role2.setModifyUser(modifyUser);
		role2.setExpireDate(expireDate.trim());

		boolean o = (Boolean) transactionTemplate.execute(new TransactionCallback() {
			public Object doInTransaction(TransactionStatus ts) {
				try {
					roleDao.createUserRole(roles1);
				} catch (Exception e) {
					logger.error(LogUtil.parserBean(roles1), e);
					ts.setRollbackOnly();
					return false;
				}

				try {
					roleDao.updateUser4Role(role2);
				} catch (Exception e) {
					logger.error(LogUtil.parserBean(role2), e);
					ts.setRollbackOnly();
					return false;
				}

				return true;
			}
		});

		if (o) {
			result.setCode(IRoleService.SUCCESS);
		}

		return result;
	}

	public StringResult copyRole4User(String copyUserId, String userId, String expireDate, String modifyUser) {
		StringResult result = new StringResult();
		result.setCode(IRoleService.ERROR);
		result.setResult(IRoleService.ERROR_MESSAGE);

		if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(userId.trim()) || StringUtils.isEmpty(modifyUser)
			|| StringUtils.isEmpty(modifyUser.trim()) || StringUtils.isEmpty(copyUserId)
			|| StringUtils.isEmpty(copyUserId.trim()) || StringUtils.isEmpty(expireDate)
			|| StringUtils.isEmpty(expireDate.trim())) {
			result.setResult(IRoleService.ERROR_INPUT_MESSAGE);
			return result;
		}

		try {
			int c = roleDao.createUserRole(copyUserId, userId, expireDate, modifyUser);
			if (c == 0) {
				result.setResult("人员都已维护角色！");
				return result;
			}

			result.setResult(String.valueOf(c));
			result.setCode(IRoleService.SUCCESS);
		} catch (Exception e) {
			logger.error("copyUserId:" + copyUserId + "userId:" + userId + "expireDate:" + expireDate + "modifyUser:"
				+ modifyUser, e);
		}

		return result;
	}

	public StringResult updateRole(Role role) {
		StringResult result = new StringResult();
		result.setCode(IRoleService.ERROR);

		if (role == null || StringUtils.isEmpty(role.getRoleId())) {
			result.setResult(IRoleService.ERROR_INPUT_MESSAGE);
			return result;
		}

		Role r = getRoleById(role.getRoleId());
		if (r == null) {
			result.setResult("被修改角色已不存在！");
			return result;
		}

		if ("S".equals(r.getType()) && !"S".equals(role.getType())) {
			result.setResult("ERP角色的类型不能被修改！");
			return result;
		}

		if (!"S".equals(r.getType()) && "S".equals(role.getType())) {
			result.setResult("其他角色不能修改为ERP角色！");
			return result;
		}

		// if sap role then 只能修改 descn or 是否默认角色
		if ("S".equals(r.getType())) {
			r.setDefaults(role.getDefaults());
			r.setRemark(role.getRemark());
			r.setModifyUser(role.getModifyUser());
			return updateSAPRole(r);
		} else {
			return updateSAPRole(role);
		}
	}

	public StringResult updateSAPRole(Role role) {
		StringResult result = new StringResult();
		result.setCode(IRoleService.ERROR);
		result.setResult(IRoleService.ERROR_MESSAGE);

		if (role == null || StringUtils.isEmpty(role.getRoleId())) {
			result.setResult(IRoleService.ERROR_INPUT_MESSAGE);
			return result;
		}

		try {
			int c = roleDao.updateRole(role);
			if (c == 1) {
				result.setCode(IRoleService.SUCCESS);
			}
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(role), e);
		}

		return result;
	}

	public StringResult deleteRole(final String roleId, final String modifyUser) {
		StringResult result = new StringResult();
		result.setCode(IRoleService.ERROR);

		if (StringUtils.isEmpty(roleId) || StringUtils.isEmpty(roleId.trim()) || StringUtils.isEmpty(modifyUser)
			|| StringUtils.isEmpty(modifyUser.trim())) {
			result.setResult("被删除角色不能为空！");
			return result;
		}

		Role role = getRoleById(roleId.trim());

		if (role == null) {
			result.setResult("被删除角色已不存在！");
			return result;
		}

		if ("S".equals(role.getType())) {
			result.setResult(role.getRoleId() + "角色不允许手工删除！");
			return result;
		}

		boolean o = (Boolean) transactionTemplate.execute(new TransactionCallback() {
			public Object doInTransaction(TransactionStatus ts) {
				// 删除role
				try {
					Role l = new Role();
					l.setRoleId(roleId);
					l.setModifyUser(modifyUser);

					int i = roleDao.deleteRole(l);
					if (i != 1) {
						ts.setRollbackOnly();
						return false;
					}
				} catch (Exception e) {
					logger.error("roleId:" + roleId, e);
					ts.setRollbackOnly();
					return false;
				}

				Role r = new Role();
				r.setRoleId(roleId);

				// 删除user_role
				try {
					roleDao.deleteUser4Role(r);
				} catch (Exception e) {
					logger.error("roleId:" + roleId, e);
					ts.setRollbackOnly();
					return false;
				}

				// 删除station_role
				try {
					roleDao.deleteRole4Station(r);
				} catch (Exception e) {
					logger.error("roleId:" + roleId, e);
					ts.setRollbackOnly();
					return false;
				}

				// 删除position_role
				try {
					roleDao.deleteRole4Position(r);
				} catch (Exception e) {
					logger.error("roleId:" + roleId, e);
					ts.setRollbackOnly();
					return false;
				}

				// 删除role_action
				Function function = new Function();
				function.setRoleId(roleId);
				BooleanResult res = functionService.deleteFunAction4Role(function);
				if (!res.getResult()) {
					ts.setRollbackOnly();
					return false;
				}

				return true;
			}
		});

		if (o) {
			result.setCode(IRoleService.SUCCESS);
			result.setResult("角色删除成功！");
		} else {
			result.setResult("角色删除失败！");
		}

		return result;
	}

	public StringResult deleteSAPRole() {
		StringResult result = new StringResult();
		result.setCode(IRoleService.ERROR);
		result.setResult(IRoleService.ERROR_MESSAGE);

		try {
			int c = roleDao.deleteSAPRole();
			result.setResult(String.valueOf(c));
			result.setCode(IRoleService.SUCCESS);
		} catch (Exception e) {
			logger.error(e);
		}

		return result;
	}

	public int getRole4MenuCount(Role role) {
		try {
			return roleDao.getRole4MenuCount(role);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(role), e);
		}

		return 0;
	}

	public List<Role> getRole4MenuList(Role role) {
		try {
			return roleDao.getRole4MenuList(role);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(role), e);
		}

		return null;
	}

	public Role getRoleById(String roleId) {
		try {
			return roleDao.getRoleById(roleId);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(roleId), e);
		}

		return null;
	}

	public int getRole4StationCount(Role role) {
		try {
			return roleDao.getRole4StationCount(role);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(role), e);
		}

		return 0;
	}

	public List<Role> getRole4StationList(Role role) {
		try {
			return roleDao.getRole4StationList(role);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(role), e);
		}

		return null;
	}

	public StringResult selectRole4Station(Role role) {
		StringResult result = new StringResult();
		result.setCode(IRoleService.ERROR);
		result.setResult(IRoleService.ERROR_MESSAGE);

		try {
			Map<String, Boolean> map = new HashMap<String, Boolean>();

			List<Role> list = roleDao.getSelectedRole4Station(role);

			if (list != null && list.size() > 0) {
				for (Role r : list) {
					map.put(r.getRoleId(), true);
				}
			}

			int i = 0;
			String[] temp = new String[role.getCodes().length];

			for (String code : role.getCodes()) {
				if (!map.containsKey(code)) {
					temp[i++] = code;
				}
			}

			if (i == 0) {
				result.setResult("选择角色都已维护！");
				return result;
			}

			role.setCodes(temp);

			String ids = roleDao.selectRole4Station(role);
			result.setResult(ids);
			result.setCode(IRoleService.SUCCESS);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(role), e);
		}

		return result;
	}

	public StringResult selectStationRole4Station(Role role) {
		StringResult result = new StringResult();
		result.setCode(IRoleService.ERROR);
		result.setResult(IRoleService.ERROR_MESSAGE);

		try {
			int o = roleDao.selectStationRole4Station(role);
			result.setResult(String.valueOf(o));
			result.setCode(IRoleService.SUCCESS);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(role), e);
		}

		return result;
	}

	public StringResult deleteRole4Station(Role role) {
		StringResult result = new StringResult();
		result.setCode(IRoleService.ERROR);
		result.setResult(IRoleService.ERROR_MESSAGE);

		try {
			int c = roleDao.deleteRole4Station(role);
			result.setResult(String.valueOf(c));
			result.setCode(IRoleService.SUCCESS);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(role), e);
		}

		return result;
	}

	public StringResult deleteInvalidRole4Station(String roleType) {
		StringResult result = new StringResult();
		result.setCode(IRoleService.ERROR);
		result.setResult(IRoleService.ERROR_MESSAGE);

		try {
			int c = roleDao.deleteInvalidRole4Station(roleType);
			result.setResult(String.valueOf(c));
			result.setCode(IRoleService.SUCCESS);
		} catch (Exception e) {
			logger.error(e);
		}

		return result;
	}

	public int getRole4PositionCount(Role role) {
		try {
			return roleDao.getRole4PositionCount(role);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(role), e);
		}

		return 0;
	}

	public List<Role> getRole4PositionList(Role role) {
		try {
			return roleDao.getRole4PositionList(role);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(role), e);
		}

		return null;
	}

	public StringResult selectRole4Position(Role role) {
		StringResult result = new StringResult();
		result.setCode(IRoleService.ERROR);
		result.setResult(IRoleService.ERROR_MESSAGE);

		try {
			Map<String, Boolean> map = new HashMap<String, Boolean>();

			List<Role> list = roleDao.getSelectedRole4Position(role);

			if (list != null && list.size() > 0) {
				for (Role r : list) {
					map.put(r.getRoleId(), true);
				}
			}

			int i = 0;
			String[] temp = new String[role.getCodes().length];

			for (String code : role.getCodes()) {
				if (!map.containsKey(code)) {
					temp[i++] = code;
				}
			}

			if (i == 0) {
				result.setResult("选择角色都已维护！");
				return result;
			}

			role.setCodes(temp);

			String ids = roleDao.selectRole4Position(role);
			result.setResult(ids);
			result.setCode(IRoleService.SUCCESS);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(role), e);
		}

		return result;
	}

	public StringResult deleteRole4Position(Role role) {
		StringResult result = new StringResult();
		result.setCode(IRoleService.ERROR);
		result.setResult(IRoleService.ERROR_MESSAGE);

		try {
			int c = roleDao.deleteRole4Position(role);
			result.setResult(String.valueOf(c));
			result.setCode(IRoleService.SUCCESS);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(role), e);
		}

		return result;
	}

	public StringResult deleteInvalidRole4Position(String roleType) {
		StringResult result = new StringResult();
		result.setCode(IRoleService.ERROR);
		result.setResult(IRoleService.ERROR_MESSAGE);

		try {
			int c = roleDao.deleteInvalidRole4Position(roleType);
			result.setResult(String.valueOf(c));
			result.setCode(IRoleService.SUCCESS);
		} catch (Exception e) {
			logger.error(e);
		}

		return result;
	}

	public List<Role> getRoleList(String type, String userId) {
		if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(userId.trim())) {
			return null;
		}

		Role role = new Role();
		role.setType(type);
		role.setUserId(userId.trim());

		try {
			return roleDao.getRoleListByUser(role);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(role), e);
		}

		return null;
	}

	public boolean validateUserRole(String userId, String roleId) {
		if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(userId.trim()) || StringUtils.isEmpty(roleId)
			|| StringUtils.isEmpty(roleId.trim())) {
			return false;
		}

		Role role = new Role();
		role.setUserId(userId.trim());
		role.setRoleId(roleId.trim());

		try {
			List<Role> roleList = roleDao.getRoleListByUser(role);
			if (roleList != null && roleList.size() == 1) {
				return true;
			}
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(role), e);
		}

		return false;
	}

	public int getUserRoleCount(Role role) {
		try {
			return roleDao.getUserRoleCount(role);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(role), e);
		}

		return 0;
	}

	public List<Role> getUserRoleList(Role role) {
		try {
			return roleDao.getUserRoleList(role);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(role), e);
		}

		return null;
	}

	public StringResult deleteUser4Role(Role role) {
		StringResult result = new StringResult();
		result.setCode(IRoleService.ERROR);

		try {
			int c = roleDao.deleteUser4Role(role);
			result.setResult(String.valueOf(c));
			result.setCode(IRoleService.SUCCESS);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(role), e);
		}

		return result;
	}

	public StringResult deleteInvalidUser4Role(String roleType) {
		StringResult result = new StringResult();
		result.setCode(IRoleService.ERROR);
		result.setResult(IRoleService.ERROR_MESSAGE);

		try {
			int c = roleDao.deleteInvalidUser4Role(roleType);
			result.setResult(String.valueOf(c));
			result.setCode(IRoleService.SUCCESS);
		} catch (Exception e) {
			logger.error(e);
		}

		return result;
	}

	public StringResult updateExpireDate(Role role) {
		StringResult result = new StringResult();
		result.setCode(IRoleService.ERROR);
		result.setResult(IRoleService.ERROR_MESSAGE);

		try {
			int c = roleDao.updateUser4Role(role);
			if (c == 1) {
				result.setCode(IRoleService.SUCCESS);
			}
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(role), e);
		}

		return result;
	}

	public TransactionTemplate getTransactionTemplate() {
		return transactionTemplate;
	}

	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}

	public IFunctionService getFunctionService() {
		return functionService;
	}

	public void setFunctionService(IFunctionService functionService) {
		this.functionService = functionService;
	}

	public IRoleDao getRoleDao() {
		return roleDao;
	}

	public void setRoleDao(IRoleDao roleDao) {
		this.roleDao = roleDao;
	}

}
