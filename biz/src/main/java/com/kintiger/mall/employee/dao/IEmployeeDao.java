package com.kintiger.mall.employee.dao;

import java.util.List;

import com.kintiger.mall.api.employee.bo.Employee;

/**
 * 雇员 dao 接口.
 * 
 * @author xujiakun
 * 
 */
public interface IEmployeeDao {

	/**
	 * 
	 * @param employee
	 * @return
	 */
	List<Employee> getEmployeeList(Employee employee);

}
