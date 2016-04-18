package com.kintiger.mall.ip.dao.impl;

import com.kintiger.mall.api.ip.bo.IP;
import com.kintiger.mall.framework.dao.impl.BaseDaoImpl;
import com.kintiger.mall.ip.dao.IIPDao;

/**
 * 
 * @author xujiakun
 * 
 */
public class IPDaoImpl extends BaseDaoImpl implements IIPDao {

	@Override
	public int getIPCount(IP ip) {
		return (Integer) getSqlMapClientTemplate().queryForObject("ip.getIPCount", ip);
	}

	@Override
	public String createIP(IP ip) {
		return (String) getSqlMapClientTemplate().insert("ip.createIP", ip);
	}

}
