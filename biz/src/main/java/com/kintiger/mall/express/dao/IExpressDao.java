package com.kintiger.mall.express.dao;

import java.util.List;

import com.kintiger.mall.api.express.bo.Express;

/**
 * 物流.
 * 
 * @author xujiakun
 * 
 */
public interface IExpressDao {

	/**
	 * 查询物流公司信息.
	 * 
	 * @return
	 */
	List<Express> getExpressList();

}
