package com.kintiger.mall.member.dao.impl;

import java.util.List;

import com.kintiger.mall.api.member.bo.Member;
import com.kintiger.mall.framework.dao.impl.BaseDaoImpl;
import com.kintiger.mall.member.dao.IMemberDao;

/**
 * 会员管理 dao 实现.
 * 
 * @author xujiakun
 * 
 */
public class MemberDaoImpl extends BaseDaoImpl implements IMemberDao {

	@Override
	public String createMember(Member member) {
		return (String) getSqlMapClientTemplate().insert("member.createMember", member);
	}

	@Override
	public int getMemberCount(Member member) {
		return (Integer) getSqlMapClientTemplate().queryForObject("member.getMemberCount", member);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Member> getMemberList(Member member) {
		return (List<Member>) getSqlMapClientTemplate().queryForList("member.getMemberList", member);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Member> getMemberStats(Member member) {
		return (List<Member>) getSqlMapClientTemplate().queryForList("member.getMemberStats", member);
	}

	@Override
	public int getMemberCountStats(Member member) {
		return (Integer) getSqlMapClientTemplate().queryForObject("member.getMemberCountStats", member);
	}

	@Override
	public Member getMember(Member member) {
		return (Member) getSqlMapClientTemplate().queryForObject("member.getMember", member);
	}

	@Override
	public int updateMember(Member member) {
		return getSqlMapClientTemplate().update("member.updateMember", member);
	}

}
