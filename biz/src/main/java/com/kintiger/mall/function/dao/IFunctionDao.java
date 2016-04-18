package com.kintiger.mall.function.dao;

import java.util.List;

import com.kintiger.mall.api.function.bo.Function;

/**
 * 
 * @author xujiakun
 * 
 */
public interface IFunctionDao {

	/**
	 * 获取已选角色权限点.
	 * 
	 * @param function
	 * @return
	 */
	List<Function> getFunAction4RoleList(Function function);

	/**
	 * 
	 * @param functionList
	 * @return
	 */
	int createFunAction4Role(List<Function> functionList);

	/**
	 * 根据fun对应action default 自动维护action_role.
	 * 
	 * @param roleId
	 * @param funId
	 * @param modifyUser
	 * @return
	 */
	int createFunAction4Role(String roleId, String funId, String modifyUser);

	/**
	 * 
	 * @param functionList
	 * @return
	 */
	int updateFunAction4Role(List<Function> functionList);

	/**
	 * 
	 * @param function
	 * @return
	 */
	int deleteFunAction4Role(Function function);

	/**
	 * 获取功能操作.
	 * 
	 * @param function
	 * @return
	 */
	int getFunActionCount(Function function);

	/**
	 * 获取功能操作.
	 * 
	 * @param function
	 * @return
	 */
	List<Function> getFunActionList(Function function);

	/**
	 * 批量创建权限点属性.
	 * 
	 * @param functionList
	 * @return
	 */
	int createFunAction(List<Function> functionList);

	/**
	 * 批量修改权限点属性.
	 * 
	 * @param functionList
	 * @return
	 */
	int updateFunAction(List<Function> functionList);

	/**
	 * 删除无效的fun_action (删除已经不存在function: menu).
	 * 
	 * @param function
	 * @return
	 */
	int deleteFunAction(Function function);

	/**
	 * 
	 * @param userId
	 * @param url
	 * @return
	 */
	List<Function> getFunAction(String userId, String url);

	/**
	 * 
	 * @param userId
	 * @param url
	 * @param alias
	 * @return
	 */
	int getFunAction(String userId, String url, String alias);

}
