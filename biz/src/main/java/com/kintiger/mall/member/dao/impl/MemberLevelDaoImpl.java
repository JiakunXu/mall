package com.kintiger.mall.member.dao.impl;

import java.util.List;

import com.kintiger.mall.api.member.bo.MemberLevel;
import com.kintiger.mall.framework.dao.impl.BaseDaoImpl;
import com.kintiger.mall.member.dao.IMemberLevelDao;

/**
 * 
 * @author xujiakun
 * 
 */
public class MemberLevelDaoImpl extends BaseDaoImpl implements IMemberLevelDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<MemberLevel> getMemberLevelList(MemberLevel memberLevel) {
		return (List<MemberLevel>) getSqlMapClientTemplate().queryForList("member.level.getMemberLevelList",
			memberLevel);
	}

	@Override
	public MemberLevel getMemberLevel(MemberLevel memberLevel) {
		return (MemberLevel) getSqlMapClientTemplate().queryForObject("member.level.getMemberLevel", memberLevel);
	}

}
