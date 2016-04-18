package com.kintiger.mall.api.employee;

import java.util.List;

import com.kintiger.mall.api.employee.bo.Employee;

/**
 * 雇员接口.
 * 
 * @author xujiakun
 * 
 */
public interface IEmployeeService {

	/**
	 * 获取 userId 雇员关系 可以受雇多家公司.
	 * 
	 * @param userId
	 * @return
	 */
	List<Employee> getEmployeeList(String userId);

}
