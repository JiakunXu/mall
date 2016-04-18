package com.kintiger.mall.employee.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.kintiger.mall.api.employee.IEmployeeService;
import com.kintiger.mall.api.employee.bo.Employee;
import com.kintiger.mall.employee.dao.IEmployeeDao;
import com.kintiger.mall.framework.log.Logger4jCollection;
import com.kintiger.mall.framework.log.Logger4jExtend;
import com.kintiger.mall.framework.util.LogUtil;

/**
 * 雇员接口实现.
 * 
 * @author xujiakun
 * 
 */
public class EmployeeServiceImpl implements IEmployeeService {

	private Logger4jExtend logger = Logger4jCollection.getLogger(EmployeeServiceImpl.class);

	private IEmployeeDao employeeDao;

	@Override
	public List<Employee> getEmployeeList(String userId) {
		if (StringUtils.isBlank(userId)) {
			return null;
		}

		Employee employee = new Employee();
		employee.setUserId(userId.trim());

		try {
			return employeeDao.getEmployeeList(employee);
		} catch (Exception e) {
			logger.error(LogUtil.parserBean(employee), e);
		}

		return null;
	}

	public IEmployeeDao getEmployeeDao() {
		return employeeDao;
	}

	public void setEmployeeDao(IEmployeeDao employeeDao) {
		this.employeeDao = employeeDao;
	}

}
