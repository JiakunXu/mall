package com.kintiger.mall.notify.dao.impl;

import java.util.List;

import com.kintiger.mall.api.notify.bo.Notify;
import com.kintiger.mall.framework.dao.impl.BaseDaoImpl;
import com.kintiger.mall.notify.dao.INotifyDao;

/**
 * 
 * @author xujiakun
 * 
 */
public class NotifyDaoImpl extends BaseDaoImpl implements INotifyDao {

	public int getNotifyCount(Notify notify) {
		return (Integer) getSqlMapClientTemplate().queryForObject("notify.getNotifyCount", notify);
	}

	@SuppressWarnings("unchecked")
	public List<Notify> getNotifyList(Notify notify) {
		return (List<Notify>) getSqlMapClientTemplate().queryForList("notify.getNotifyList", notify);
	}

	public String createNotify(Notify notify) {
		return (String) getSqlMapClientTemplate().insert("notify.createNotify", notify);
	}

	public int updateNotify(Notify notify) {
		return getSqlMapClientTemplate().update("notify.updateNotify", notify);
	}

}
