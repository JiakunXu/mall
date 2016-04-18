package com.kintiger.mall.member.dao;

import java.util.List;

import com.kintiger.mall.api.member.bo.Member;

/**
 * 会员管理 dap 接口.
 * 
 * @author xujiakun
 * 
 */
public interface IMemberDao {

	/**
	 * 
	 * @param member
	 * @return
	 */
	String createMember(Member member);

	/**
	 * 
	 * @param member
	 * @return
	 */
	int getMemberCount(Member member);

	/**
	 * 
	 * @param member
	 * @return
	 */
	List<Member> getMemberList(Member member);

	/**
	 * 
	 * @param member
	 * @return
	 */
	List<Member> getMemberStats(Member member);

	/**
	 * 
	 * @param member
	 * @return
	 */
	int getMemberCountStats(Member member);

	/**
	 * 
	 * @param member
	 * @return
	 */
	Member getMember(Member member);

	/**
	 * 
	 * @param member
	 * @return
	 */
	int updateMember(Member member);

}
