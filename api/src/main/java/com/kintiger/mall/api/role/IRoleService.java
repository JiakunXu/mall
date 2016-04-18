package com.kintiger.mall.api.role;

import java.util.List;

import com.kintiger.mall.api.role.bo.Role;
import com.kintiger.mall.framework.bo.StringResult;

/**
 * role.
 * 
 * @author xujiakun
 * 
 */
public interface IRoleService {

	String SUCCESS = "success";

	String ERROR = "error";

	String ERROR_MESSAGE = "操作失败！";

	String ERROR_INPUT_MESSAGE = "操作失败，输入有误！";

	String ERROR_NULL_MESSAGE = "操作失败，单据已不存在！";

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
	 * 获取sap role.
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
	StringResult createRole(Role role);

	/**
	 * 批量更新role.
	 * 
	 * @param roles
	 * @return
	 */
	StringResult createRole(List<Role> roles);

	/**
	 * 批量维护user_role.
	 * 
	 * @param roleId
	 * @param userIds
	 * @param expireDate
	 * @param modifyUser
	 * @return
	 */
	StringResult createUser4Role(String roleId, String[] userIds, String expireDate, String modifyUser);

	/**
	 * 批量维护user_role.
	 * 
	 * @param roleIds
	 * @param userId
	 * @param expireDate
	 * @param modifyUser
	 * @return
	 */
	StringResult createRole4User(String[] roleIds, String userId, String expireDate, String modifyUser);

	/**
	 * 批量复制user_role.
	 * 
	 * @param copyUserId
	 * @param userId
	 * @param expireDate
	 * @param modifyUser
	 * @return
	 */
	StringResult copyRole4User(String copyUserId, String userId, String expireDate, String modifyUser);

	/**
	 * 
	 * @param role
	 * @return
	 */
	StringResult updateRole(Role role);

	/**
	 * 
	 * @param role
	 * @return
	 */
	StringResult updateSAPRole(Role role);

	/**
	 * 删除非sap role.
	 * 
	 * @param roleId
	 * @param modifyUser
	 * @return
	 */
	StringResult deleteRole(String roleId, String modifyUser);

	/**
	 * 逻辑删除sap role.
	 * 
	 * @return
	 */
	StringResult deleteSAPRole();

	/**
	 * 菜单选择角色页面.
	 * 
	 * @param role
	 * @return
	 */
	int getRole4MenuCount(Role role);

	/**
	 * 菜单选择角色页面.
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
	 * 获取岗位已选择的角色.
	 * 
	 * @param role
	 * @return
	 */
	int getRole4StationCount(Role role);

	/**
	 * 获取岗位已选择的角色.
	 * 
	 * @param role
	 * @return
	 */
	List<Role> getRole4StationList(Role role);

	/**
	 * 根据岗位选择角色.
	 * 
	 * @param role
	 * @return
	 */
	StringResult selectRole4Station(Role role);

	/**
	 * 通过复制其他岗位角色方式 维护岗位角色关系.
	 * 
	 * @param role
	 * @return
	 */
	StringResult selectStationRole4Station(Role role);

	/**
	 * 删除权限岗位已选择角色.
	 * 
	 * @param role
	 * @return
	 */
	StringResult deleteRole4Station(Role role);

	/**
	 * 删除已经无效的station_role.
	 * 
	 * @return
	 */
	StringResult deleteInvalidRole4Station(String roleType);

	/**
	 * 获取编制已选择的角色.
	 * 
	 * @param role
	 * @return
	 */
	int getRole4PositionCount(Role role);

	/**
	 * 获取编制已选择的角色.
	 * 
	 * @param role
	 * @return
	 */
	List<Role> getRole4PositionList(Role role);

	/**
	 * 根据编制选择角色.
	 * 
	 * @param role
	 * @return
	 */
	StringResult selectRole4Position(Role role);

	/**
	 * 删除编制已选择角色.
	 * 
	 * @param role
	 * @return
	 */
	StringResult deleteRole4Position(Role role);

	/**
	 * 删除已经无效的position_role.
	 * 
	 * @return
	 */
	StringResult deleteInvalidRole4Position(String roleType);

	/**
	 * 根据user获取role.
	 * 
	 * @param type
	 * @param userId
	 * @return
	 */
	List<Role> getRoleList(String type, String userId);

	/**
	 * 验证user是否拥有role.
	 * 
	 * @param userId
	 * @param roleId
	 * @return
	 */
	boolean validateUserRole(String userId, String roleId);

	/**
	 * user_role.
	 * 
	 * @param role
	 * @return
	 */
	int getUserRoleCount(Role role);

	/**
	 * user_role.
	 * 
	 * @param role
	 * @return
	 */
	List<Role> getUserRoleList(Role role);

	/**
	 * 删除 user_role.
	 * 
	 * @param role
	 * @return
	 */
	StringResult deleteUser4Role(Role role);

	/**
	 * 删除已经无效的user_role.
	 * 
	 * @return
	 */
	StringResult deleteInvalidUser4Role(String roleType);

	/**
	 * 修改 user_role 过期日期.
	 * 
	 * @param role
	 * @return
	 */
	StringResult updateExpireDate(Role role);

}
