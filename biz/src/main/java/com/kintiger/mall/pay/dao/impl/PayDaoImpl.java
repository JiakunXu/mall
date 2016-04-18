package com.kintiger.mall.pay.dao.impl;

import com.kintiger.mall.api.pay.bo.AliCallback;
import com.kintiger.mall.api.pay.bo.AliNotify;
import com.kintiger.mall.api.pay.bo.WxNotify;
import com.kintiger.mall.framework.dao.impl.BaseDaoImpl;
import com.kintiger.mall.pay.dao.IPayDao;

/***
 * 支付回调.
 * 
 * @author xujiakun
 * 
 */
public class PayDaoImpl extends BaseDaoImpl implements IPayDao {

	@Override
	public String createAliCallback(AliCallback callback) {
		return (String) getSqlMapClientTemplate().insert("pay.ali.createAliCallback", callback);
	}

	@Override
	public String createAliNotify(AliNotify notify) {
		return (String) getSqlMapClientTemplate().insert("pay.ali.createAliNotify", notify);
	}

	@Override
	public String createWxNotify(WxNotify notify) {
		return (String) getSqlMapClientTemplate().insert("pay.wx.createWxNotify", notify);
	}

	@Override
	public WxNotify getWxNotify(WxNotify notify) {
		return (WxNotify) getSqlMapClientTemplate().queryForObject("pay.wx.getWxNotify", notify);
	}

}
