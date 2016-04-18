package com.kintiger.mall.employee.dao.impl;

import java.util.List;

import com.kintiger.mall.api.employee.bo.Employee;
import com.kintiger.mall.employee.dao.IEmployeeDao;
import com.kintiger.mall.framework.dao.impl.BaseDaoImpl;

/**
 * 雇员 dao 接口实现.
 * 
 * @author xujiakun
 * 
 */
public class EmployeeDaoImpl extends BaseDaoImpl implements IEmployeeDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Employee> getEmployeeList(Employee employee) {
		return (List<Employee>) getSqlMapClientTemplate().queryForList("employee.getEmployeeList", employee);
	}

}
