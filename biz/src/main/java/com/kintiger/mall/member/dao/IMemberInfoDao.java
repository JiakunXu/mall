package com.kintiger.mall.member.dao;

import com.kintiger.mall.api.member.bo.MemberInfo;

/**
 * 会员信息.
 * 
 * @author xujiakun
 * 
 */
public interface IMemberInfoDao {

	/**
	 * 
	 * @param memberInfo
	 * @return
	 */
	MemberInfo getMemberInfo(MemberInfo memberInfo);

	/**
	 * 
	 * @param memberInfo
	 * @return
	 */
	int updateMemberInfo(MemberInfo memberInfo);

	/**
	 * 
	 * @param memberInfo
	 * @return
	 */
	String createMemberInfo(MemberInfo memberInfo);

}
