package com.kintiger.mall.function.service.impl;

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
import com.kintiger.mall.framework.bo.BooleanResult;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;
import com.kintiger.mall.framework.util.LogUtil;
import com.kintiger.mall.function.dao.IFunctionDao;

/**
 * 
 * @author xujiakun
 * 
 */
public class FunctionServiceImpl implements IFunctionService {

	private Logger4jExtend logger = Logger4jCollection.getLogger(FunctionServiceImpl.class);

	private TransactionTemplate transactionTemplate;

	private IFunctionDao functionDao;

	public List<Function> getFunAction4RoleList(Function function) {
		try {
			return functionDao.getFunAction4RoleList(function);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(function), e);
		}

		return null;
	}

	public BooleanResult updateFunAction4Role(String roleId, List<Function> functionList, String modifyUser) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);
		result.setCode(IFunctionService.ERROR_MESSAGE);

		if (StringUtils.isEmpty(roleId) || StringUtils.isEmpty(roleId.trim()) || StringUtils.isEmpty(modifyUser)
			|| StringUtils.isEmpty(modifyUser.trim())) {
			result.setCode(IFunctionService.ERROR_INPUT_MESSAGE);
			return result;
		}

		// 1. 根据userId 获取所有sap apr -> map
		Map<String, Function> map = new HashMap<String, Function>();

		final Function r = new Function();
		r.setRoleId(roleId.trim());
		List<Function> list0 = getFunAction4RoleList(r);

		if (list0 != null && list0.size() > 0) {
			for (Function u : list0) {
				map.put(u.getId(), u);
			}
		}

		// 2. 遍历userList set userId modifyUser
		final List<Function> list1 = new ArrayList<Function>();
		final List<Function> list2 = new ArrayList<Function>();
		// if 在map中存在 then -> list1 需要update
		// if 在map中不存在 then -> list2 需要insert
		// 剩下的map中 delete
		for (Function u : functionList) {
			String id = u.getId();
			if (StringUtils.isNotEmpty(id) && map.containsKey(id)) {
				u.setModifyUser(modifyUser);
				list1.add(u);
				map.remove(id);
			} else {
				u.setRoleId(roleId.trim());
				u.setModifyUser(modifyUser);
				list2.add(u);
			}
		}

		int i = 0;
		String[] ids = new String[map.size()];

		for (Map.Entry<String, Function> m : map.entrySet()) {
			ids[i++] = m.getKey();
		}

		r.setRoleId(null);
		r.setCodes(ids);

		boolean o = (Boolean) transactionTemplate.execute(new TransactionCallback() {
			public Object doInTransaction(TransactionStatus ts) {
				// update
				try {
					functionDao.updateFunAction4Role(list1);
				} catch (Exception e) {
					logger.error(LogUtil.parserBean(list1), e);
					ts.setRollbackOnly();
					return false;
				}

				// create
				try {
					functionDao.createFunAction4Role(list2);
				} catch (Exception e) {
					logger.error(LogUtil.parserBean(list2), e);
					ts.setRollbackOnly();
					return false;
				}

				// delete
				try {
					if (r.getCodes().length > 0) {
						functionDao.deleteFunAction4Role(r);
					}
				} catch (Exception e) {
					logger.error(LogUtil.parserBean(r), e);
					ts.setRollbackOnly();
					return false;
				}

				return true;
			}
		});

		if (o) {
			result.setResult(true);
			result.setCode("更新成功！");
		} else {
			result.setCode("更新失败！");
		}

		return result;
	}

	public BooleanResult deleteFunAction4Role(Function function) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);
		result.setCode(IFunctionService.ERROR_MESSAGE);

		try {
			int c = functionDao.deleteFunAction4Role(function);
			result.setCode(String.valueOf(c));
			result.setResult(true);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(function), e);
		}

		return result;
	}

	public int getFunActionCount(Function function) {
		try {
			return functionDao.getFunActionCount(function);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(function), e);
		}

		return 0;
	}

	public List<Function> getFunActionList(Function function) {
		try {
			return functionDao.getFunActionList(function);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(function), e);
		}

		return null;
	}

	public BooleanResult updateFunAction(String funId, List<Function> functionList, String modifyUser) {
		BooleanResult result = new BooleanResult();
		result.setResult(false);
		result.setCode(IFunctionService.ERROR_MESSAGE);

		if (StringUtils.isEmpty(funId) || StringUtils.isEmpty(funId.trim()) || StringUtils.isEmpty(modifyUser)
			|| StringUtils.isEmpty(modifyUser.trim())) {
			result.setCode(IFunctionService.ERROR_INPUT_MESSAGE);
			return result;
		}

		// 1. 根据funId 获取所有 -> map
		Map<String, Function> map = new HashMap<String, Function>();

		final Function r = new Function();
		r.setFunId(funId.trim());

		List<Function> list0 = null;

		int count = getFunActionCount(r);
		if (count > 0) {
			r.setStart(0);
			r.setLimit(count);
			list0 = getFunActionList(r);
		}

		if (list0 != null && list0.size() > 0) {
			for (Function u : list0) {
				map.put(u.getActionId(), u);
			}
		}

		// 2. 遍历functionList set ...
		final List<Function> list1 = new ArrayList<Function>();
		final List<Function> list2 = new ArrayList<Function>();
		// if 在map中存在 then -> list1 需要update
		// if 在map中不存在 then -> list2 需要insert
		// 剩下的map中 delete
		for (Function u : functionList) {
			String actionId = u.getActionId();
			if (StringUtils.isNotEmpty(actionId) && map.containsKey(actionId)) {
				u.setModifyUser(modifyUser);
				list1.add(u);
				map.remove(actionId);
			} else {
				u.setFunId(funId.trim());
				u.setModifyUser(modifyUser);
				list2.add(u);
			}
		}

		int i = 0;
		String[] ids = new String[map.size()];

		for (Map.Entry<String, Function> m : map.entrySet()) {
			ids[i++] = m.getKey();
		}

		r.setModifyUser(modifyUser);
		r.setCodes(ids);

		boolean o = (Boolean) transactionTemplate.execute(new TransactionCallback() {
			public Object doInTransaction(TransactionStatus ts) {
				// update
				try {
					functionDao.updateFunAction(list1);
				} catch (Exception e) {
					logger.error(LogUtil.parserBean(list1), e);
					ts.setRollbackOnly();
					return false;
				}

				// create
				try {
					functionDao.createFunAction(list2);
				} catch (Exception e) {
					logger.error(LogUtil.parserBean(list2), e);
					ts.setRollbackOnly();
					return false;
				}

				// delete
				try {
					if (r.getCodes().length > 0) {
						r.setFunId(null);
						functionDao.deleteFunAction(r);
					}
				} catch (Exception e) {
					logger.error(LogUtil.parserBean(r), e);
					ts.setRollbackOnly();
					return false;
				}

				return true;
			}
		});

		if (o) {
			result.setResult(true);
			result.setCode("更新成功！");
		} else {
			result.setCode("更新失败！");
		}

		return result;
	}

	public Map<String, Boolean> getFunAction(String userId, String url) {
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(userId.trim()) || StringUtils.isEmpty(url)
			|| StringUtils.isEmpty(url.trim())) {
			return map;
		}

		try {
			List<Function> list = functionDao.getFunAction(userId.trim(), url.trim());
			if (list != null && list.size() > 0) {
				for (Function f : list) {
					map.put(f.getAlias(), true);
				}
			}
		} catch (Exception e) {
			logger.error("userId:" + userId + "url:" + url, e);
		}

		return map;
	}

	public boolean validateFunAction(String userId, String actionName, String alias) {
		if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(userId.trim()) || StringUtils.isEmpty(actionName)
			|| StringUtils.isEmpty(actionName.trim()) || StringUtils.isEmpty(alias)
			|| StringUtils.isEmpty(alias.trim())) {
			return false;
		}

		try {
			int c = functionDao.getFunAction(userId.trim(), actionName.trim(), alias.trim());
			if (c > 0) {
				return true;
			}
		} catch (Exception e) {
			logger.error("userId:" + userId + "actionName:" + actionName, e);
		}

		return false;
	}

	public TransactionTemplate getTransactionTemplate() {
		return transactionTemplate;
	}

	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}

	public IFunctionDao getFunctionDao() {
		return functionDao;
	}

	public void setFunctionDao(IFunctionDao functionDao) {
		this.functionDao = functionDao;
	}

}
