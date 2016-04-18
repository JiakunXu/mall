package com.kintiger.mall.role.dao;

import java.util.List;

import com.kintiger.mall.api.role.bo.Role;

/**
 * role dao.
 * 
 * @author xujiakun
 * 
 */
public interface IRoleDao {

	/**
	 * 
	 * @param role
	 * @return
	 */
	int getRoleCount(Role role);

	/**
	 * 
	 * @param role
	 * @return
	 */
	List<Role> getRoleList(Role role);

	/**
	 * 
	 * @param role
	 * @return
	 */
	List<Role> getSAPRoleList(Role role);

	/**
	 * 
	 * @param role
	 * @return
	 */
	String createRole(Role role);

	/**
	 * 批量更新.
	 * 
	 * @param roles
	 * @return
	 */
	int createRole(List<Role> roles);

	/**
	 * 
	 * @param roles
	 * @return
	 */
	int createUserRole(List<Role> roles);

	/**
	 * 
	 * @param copyUserId
	 * @param userId
	 * @param expireDate
	 * @param modifyUser
	 * @return
	 */
	int createUserRole(String copyUserId, String userId, String expireDate, String modifyUser);

	/**
	 * 
	 * @param role
	 * @return
	 */
	int updateRole(Role role);

	/**
	 * 删除角色.
	 * 
	 * @param role
	 * @return
	 */
	int deleteRole(Role role);

	/**
	 * 删除sap角色.
	 * 
	 * @return
	 */
	int deleteSAPRole();

	/**
	 * 
	 * @param role
	 * @return
	 */
	int getRole4MenuCount(Role role);

	/**
	 * 
	 * @param role
	 * @return
	 */
	List<Role> getRole4MenuList(Role role);

	/**
	 * 
	 * @param roleId
	 * @return
	 */
	Role getRoleById(String roleId);

	/**
	 * 
	 * @param role
	 * @return
	 */
	int getRole4StationCount(Role role);

	/**
	 * 
	 * @param role
	 * @return
	 */
	List<Role> getRole4StationList(Role role);

	/**
	 * 判断选中 role 是否已存在.
	 * 
	 * @param role
	 * @return
	 */
	List<Role> getSelectedRole4Station(Role role);

	/**
	 * 
	 * @param role
	 * @return
	 */
	String selectRole4Station(Role role);

	/**
	 * 批量insert role 通过update 统计个数.
	 * 
	 * @param role
	 * @return
	 */
	int selectStationRole4Station(Role role);

	/**
	 * 
	 * @param role
	 * @return
	 */
	int deleteRole4Station(Role role);

	/**
	 * 
	 * @param roleType
	 * @return
	 */
	int deleteInvalidRole4Station(String roleType);

	/**
	 * 
	 * @param role
	 * @return
	 */
	int getRole4PositionCount(Role role);

	/**
	 * 
	 * @param role
	 * @return
	 */
	List<Role> getRole4PositionList(Role role);

	/**
	 * 判断选中 role 是否已存在.
	 * 
	 * @param role
	 * @return
	 */
	List<Role> getSelectedRole4Position(Role role);

	/**
	 * 
	 * @param role
	 * @return
	 */
	String selectRole4Position(Role role);

	/**
	 * 
	 * @param role
	 * @return
	 */
	int deleteRole4Position(Role role);

	/**
	 * 
	 * @param roleType
	 * @return
	 */
	int deleteInvalidRole4Position(String roleType);

	/**
	 * 
	 * @param role
	 * @return
	 */
	List<Role> getRoleListByUser(Role role);

	/**
	 * 
	 * @param role
	 * @return
	 */
	int getUserRoleCount(Role role);

	/**
	 * 
	 * @param role
	 * @return
	 */
	List<Role> getUserRoleList(Role role);

	/**
	 * 
	 * @param role
	 * @return
	 */
	int deleteUser4Role(Role role);

	/**
	 * 
	 * @param roleType
	 * @return
	 */
	int deleteInvalidUser4Role(String roleType);

	/**
	 * 
	 * @param role
	 * @return
	 */
	int updateUser4Role(Role role);

}
