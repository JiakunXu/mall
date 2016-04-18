package com.kintiger.mall.function.dao.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.SqlMapClientCallback;

import com.ibatis.sqlmap.client.SqlMapExecutor;
import com.kintiger.mall.api.function.bo.Function;
import com.kintiger.mall.framework.dao.impl.BaseDaoImpl;
import com.kintiger.mall.function.dao.IFunctionDao;

/**
 * 
 * @author xujiakun
 * 
 */
public class FunctionDaoImpl extends BaseDaoImpl implements IFunctionDao {

	private static final String USER_ID = "userId";

	private static final String URL = "url";

	@SuppressWarnings("unchecked")
	public List<Function> getFunAction4RoleList(Function function) {
		return (List<Function>) getSqlMapClientTemplate().queryForList("function.getFunAction4RoleList", function);
	}

	public int createFunAction4Role(final List<Function> functionList) {
		return (Integer) getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
			public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
				int count = 0;
				executor.startBatch();
				for (Function f : functionList) {
					executor.insert("function.createFunAction4Role1", f);
					count++;
				}
				executor.executeBatch();

				return count;
			}
		});
	}

	public int createFunAction4Role(String roleId, String funId, String modifyUser) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("roleId", roleId);
		map.put("funId", funId);
		map.put("modifyUser", modifyUser);

		return getSqlMapClientTemplate().update("function.createFunAction4Role2", map);
	}

	public int updateFunAction4Role(final List<Function> functionList) {
		return (Integer) getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
			public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
				int count = 0;
				executor.startBatch();
				for (Function f : functionList) {
					executor.update("function.updateFunAction4Role", f);
					count++;
				}
				executor.executeBatch();

				return count;
			}
		});

	}

	public int deleteFunAction4Role(Function function) {
		return getSqlMapClientTemplate().delete("function.deleteFunAction4Role", function);
	}

	public int getFunActionCount(Function function) {
		return (Integer) getSqlMapClientTemplate().queryForObject("function.getFunActionCount", function);
	}

	@SuppressWarnings("unchecked")
	public List<Function> getFunActionList(Function function) {
		return (List<Function>) getSqlMapClientTemplate().queryForList("function.getFunActionList", function);
	}

	public int createFunAction(final List<Function> functionList) {
		return (Integer) getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
			public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
				int count = 0;
				executor.startBatch();
				for (Function u : functionList) {
					executor.insert("function.createFunAction", u);
					count++;
				}
				executor.executeBatch();

				return count;
			}
		});
	}

	public int updateFunAction(final List<Function> functionList) {
		return (Integer) getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
			public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
				int count = 0;
				executor.startBatch();
				for (Function u : functionList) {
					executor.update("function.updateFunAction", u);
					count++;
				}
				executor.executeBatch();

				return count;
			}
		});
	}

	public int deleteFunAction(Function function) {
		return getSqlMapClientTemplate().update("function.deleteFunAction", function);
	}

	@SuppressWarnings("unchecked")
	public List<Function> getFunAction(String userId, String url) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(USER_ID, userId);
		map.put(URL, url);
		return (List<Function>) getSqlMapClientTemplate().queryForList("function.getFunAction1", map);
	}

	public int getFunAction(String userId, String actionName, String alias) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(USER_ID, userId);
		map.put("actionName", actionName);
		map.put("alias", alias);
		return (Integer) getSqlMapClientTemplate().queryForObject("function.getFunAction2", map);
	}

}
