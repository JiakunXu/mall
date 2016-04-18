package com.kintiger.mall.role.dao.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.orm.ibatis.SqlMapClientCallback;

import com.ibatis.sqlmap.client.SqlMapExecutor;
import com.kintiger.mall.api.role.bo.Role;
import com.kintiger.mall.framework.dao.impl.BaseDaoImpl;
import com.kintiger.mall.role.dao.IRoleDao;

/**
 * role dao.
 * 
 * @author xujiakun
 * 
 */
public class RoleDaoImpl extends BaseDaoImpl implements IRoleDao {

	public int getRoleCount(Role role) {
		return (Integer) getSqlMapClientTemplate().queryForObject("role.getRoleCount", role);
	}

	@SuppressWarnings("unchecked")
	public List<Role> getRoleList(Role role) {
		return (List<Role>) getSqlMapClientTemplate().queryForList("role.getRoleList", role);
	}

	@SuppressWarnings("unchecked")
	public List<Role> getSAPRoleList(Role role) {
		return (List<Role>) getSqlMapClientTemplate().queryForList("role.getSAPRoleList", role);
	}

	public String createRole(Role role) {
		return (String) getSqlMapClientTemplate().insert("role.createRole", role);
	}

	public int createRole(final List<Role> roles) {
		return (Integer) getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
			public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
				int count = 0;
				executor.startBatch();
				for (Role role : roles) {
					executor.insert("role.createRole", role);
					count++;
				}
				executor.executeBatch();

				return count;
			}
		});
	}

	public int createUserRole(final List<Role> roles) {
		return (Integer) getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
			public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
				int count = 0;
				executor.startBatch();
				for (Role role : roles) {
					executor.insert("role.createUserRole1", role);
					count++;
				}
				executor.executeBatch();

				return count;
			}
		});
	}

	public int createUserRole(String copyUserId, String userId, String expireDate, String modifyUser) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("copyUserId", copyUserId);
		map.put("userId", userId);
		map.put("expireDate", expireDate);
		map.put("modifyUser", modifyUser);

		return getSqlMapClientTemplate().update("role.createUserRole2", map);
	}

	public int updateRole(Role role) {
		return getSqlMapClientTemplate().update("role.updateRole", role);
	}

	public int deleteRole(Role role) {
		return getSqlMapClientTemplate().update("role.deleteRole", role);
	}

	public int deleteSAPRole() {
		return getSqlMapClientTemplate().update("role.deleteSAPRole");
	}

	public int getRole4MenuCount(Role role) {
		return (Integer) getSqlMapClientTemplate().queryForObject("role.getRole4MenuCount", role);
	}

	@SuppressWarnings("unchecked")
	public List<Role> getRole4MenuList(Role role) {
		return (List<Role>) getSqlMapClientTemplate().queryForList("role.getRole4MenuList", role);
	}

	public Role getRoleById(String roleId) {
		return (Role) getSqlMapClientTemplate().queryForObject("role.getRoleById", roleId);
	}

	public int getRole4StationCount(Role role) {
		return (Integer) getSqlMapClientTemplate().queryForObject("role.getRole4StationCount", role);
	}

	@SuppressWarnings("unchecked")
	public List<Role> getRole4StationList(Role role) {
		return (List<Role>) getSqlMapClientTemplate().queryForList("role.getRole4StationList", role);
	}

	@SuppressWarnings("unchecked")
	public List<Role> getSelectedRole4Station(Role role) {
		return (List<Role>) getSqlMapClientTemplate().queryForList("role.getSelectedRole4Station", role);
	}

	public String selectRole4Station(final Role role) {
		return (String) getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
			StringBuilder sb = new StringBuilder();

			public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
				executor.startBatch();
				Role r = new Role();
				for (String s : role.getCodes()) {
					if (StringUtils.isNotEmpty(s)) {
						r.setRoleId(s);
						r.setStaId(role.getStaId());
						r.setModifyUser(role.getModifyUser());
						if (sb.length() != 0) {
							sb.append(",");
						}
						sb.append(executor.insert("role.selectRole4Station", r));
					}
				}
				executor.executeBatch();
				return sb.toString();
			}
		});
	}

	public int selectStationRole4Station(Role role) {
		return getSqlMapClientTemplate().update("role.selectStationRole4Station", role);
	}

	public int deleteRole4Station(Role role) {
		return getSqlMapClientTemplate().delete("role.deleteRole4Station", role);
	}

	public int deleteInvalidRole4Station(String roleType) {
		return getSqlMapClientTemplate().delete("role.deleteInvalidRole4Station", roleType);
	}

	public int getRole4PositionCount(Role role) {
		return (Integer) getSqlMapClientTemplate().queryForObject("role.getRole4PositionCount", role);
	}

	@SuppressWarnings("unchecked")
	public List<Role> getRole4PositionList(Role role) {
		return (List<Role>) getSqlMapClientTemplate().queryForList("role.getRole4PositionList", role);
	}

	@SuppressWarnings("unchecked")
	public List<Role> getSelectedRole4Position(Role role) {
		return (List<Role>) getSqlMapClientTemplate().queryForList("role.getSelectedRole4Position", role);
	}

	public String selectRole4Position(final Role role) {
		return (String) getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
			StringBuilder sb = new StringBuilder();

			public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
				executor.startBatch();
				Role r = new Role();
				for (String s : role.getCodes()) {
					if (StringUtils.isNotEmpty(s)) {
						r.setRoleId(s);
						r.setPosId(role.getPosId());
						r.setModifyUser(role.getModifyUser());
						if (sb.length() != 0) {
							sb.append(",");
						}
						sb.append(executor.insert("role.selectRole4Position", r));
					}
				}
				executor.executeBatch();
				return sb.toString();
			}
		});
	}

	public int deleteRole4Position(Role role) {
		return getSqlMapClientTemplate().delete("role.deleteRole4Position", role);
	}

	public int deleteInvalidRole4Position(String roleType) {
		return getSqlMapClientTemplate().delete("role.deleteInvalidRole4Position", roleType);
	}

	@SuppressWarnings("unchecked")
	public List<Role> getRoleListByUser(Role role) {
		return (List<Role>) getSqlMapClientTemplate().queryForList("role.getRoleListByUser", role);
	}

	public int getUserRoleCount(Role role) {
		return (Integer) getSqlMapClientTemplate().queryForObject("role.getUserRoleCount", role);
	}

	@SuppressWarnings("unchecked")
	public List<Role> getUserRoleList(Role role) {
		return (List<Role>) getSqlMapClientTemplate().queryForList("role.getUserRoleList", role);
	}

	public int deleteUser4Role(Role role) {
		return getSqlMapClientTemplate().delete("role.deleteUser4Role", role);
	}

	public int deleteInvalidUser4Role(String roleType) {
		return getSqlMapClientTemplate().delete("role.deleteInvalidUser4Role", roleType);
	}

	public int updateUser4Role(Role role) {
		return getSqlMapClientTemplate().update("role.updateUser4Role", role);
	}

}
