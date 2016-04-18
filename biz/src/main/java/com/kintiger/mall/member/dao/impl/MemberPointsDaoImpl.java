package com.kintiger.mall.member.dao.impl;

import java.math.BigDecimal;
import java.util.List;

import com.kintiger.mall.api.member.bo.MemberPoints;
import com.kintiger.mall.framework.dao.impl.BaseDaoImpl;
import com.kintiger.mall.member.dao.IMemberPointsDao;

/**
 * 
 * @author xujiakun
 * 
 */
public class MemberPointsDaoImpl extends BaseDaoImpl implements IMemberPointsDao {

	@Override
	public String createMemberPoints(MemberPoints memberPoints) {
		return (String) getSqlMapClientTemplate().insert("member.points.createMemberPoints", memberPoints);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MemberPoints> getMemberPointsList(MemberPoints memberPoints) {
		return (List<MemberPoints>) getSqlMapClientTemplate().queryForList("member.points.getMemberPointsList",
			memberPoints);
	}

	@Override
	public BigDecimal getMemberPoints(MemberPoints memberPoints) {
		return (BigDecimal) getSqlMapClientTemplate().queryForObject("member.points.getMemberPoints", memberPoints);
	}

}
