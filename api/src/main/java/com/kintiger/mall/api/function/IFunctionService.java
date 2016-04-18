package com.kintiger.mall.api.function;

import java.util.List;
import java.util.Map;

import com.kintiger.mall.api.function.bo.Function;
import com.kintiger.mall.framework.bo.BooleanResult;

/**
 * 
 * @author xujiakun
 * 
 */
public interface IFunctionService {

	String ERROR_MESSAGE = "操作失败！";

	String ERROR_INPUT_MESSAGE = "操作失败，输入有误！";

	/**
	 * 获取已选角色权限点.
	 * 
	 * @param function
	 * @return
	 */
	List<Function> getFunAction4RoleList(Function function);

	/**
	 * 批量更新updateFunAction4Role（可能存在insert update delete）.
	 * 
	 * @param roleId
	 * @param functionList
	 * @param modifyUser
	 * @return
	 */
	BooleanResult updateFunAction4Role(String roleId, List<Function> functionList, String modifyUser);

	/**
	 * 删除fun_action when 删除role.
	 * 
	 * @param function
	 * @return
	 */
	BooleanResult deleteFunAction4Role(Function function);

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
	 * 批量更新updateFunAction（可能存在insert update delete）.
	 * 
	 * @param funId
	 * @param functionList
	 * @param modifyUser
	 * @return
	 */
	BooleanResult updateFunAction(String funId, List<Function> functionList, String modifyUser);

	/**
	 * 获取当前用户权限点.
	 * 
	 * @param userId
	 * @param url
	 * @return
	 */
	Map<String, Boolean> getFunAction(String userId, String url);

	/**
	 * 验证权限点.
	 * 
	 * @param userId
	 * @param actionName
	 * @param alias
	 * @return
	 */
	boolean validateFunAction(String userId, String actionName, String alias);

}
