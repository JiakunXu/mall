package com.kintiger.mall.express.dao.impl;

import java.util.List;

import com.kintiger.mall.api.express.bo.Express;
import com.kintiger.mall.express.dao.IExpressDao;
import com.kintiger.mall.framework.dao.impl.BaseDaoImpl;

/**
 * 
 * @author xujiakun
 * 
 */
public class ExpressDaoImpl extends BaseDaoImpl implements IExpressDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Express> getExpressList() {
		return (List<Express>) getSqlMapClientTemplate().queryForList("express.getExpressList");
	}

}
