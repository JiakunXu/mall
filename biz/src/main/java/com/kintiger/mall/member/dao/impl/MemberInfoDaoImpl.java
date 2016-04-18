package com.kintiger.mall.member.dao.impl;

import com.kintiger.mall.api.member.bo.MemberInfo;
import com.kintiger.mall.framework.dao.impl.BaseDaoImpl;
import com.kintiger.mall.member.dao.IMemberInfoDao;

/**
 * 
 * @author xujiakun
 * 
 */
public class MemberInfoDaoImpl extends BaseDaoImpl implements IMemberInfoDao {

	@Override
	public MemberInfo getMemberInfo(MemberInfo memberInfo) {
		return (MemberInfo) getSqlMapClientTemplate().queryForObject("member.info.getMemberInfo", memberInfo);
	}

	@Override
	public int updateMemberInfo(MemberInfo memberInfo) {
		return getSqlMapClientTemplate().update("member.info.updateMemberInfo", memberInfo);
	}

	@Override
	public String createMemberInfo(MemberInfo memberInfo) {
		return (String) getSqlMapClientTemplate().insert("member.info.createMemberInfo", memberInfo);
	}

}
