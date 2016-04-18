package com.kintiger.mall.sms.dao.impl;

import com.kintiger.mall.api.sms.bo.SMS;
import com.kintiger.mall.framework.dao.impl.BaseDaoImpl;
import com.kintiger.mall.sms.dao.ISMSDao;

/**
 * 
 * @author xujiakun
 * 
 */
public class SMSDaoImpl extends BaseDaoImpl implements ISMSDao {

	@Override
	public String createSMS(SMS sms) {
		return (String) getSqlMapClientTemplate().insert("sms.createSMS", sms);
	}

}
