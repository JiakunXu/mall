package com.kintiger.mall.member.dao;

import java.util.List;

import com.kintiger.mall.api.member.bo.MemberLevel;

/**
 * 
 * @author xujiakun
 * 
 */
public interface IMemberLevelDao {

	/**
	 * 
	 * @param memberLevel
	 * @return
	 */
	List<MemberLevel> getMemberLevelList(MemberLevel memberLevel);

	/**
	 * 
	 * @param memberLevel
	 * @return
	 */
	MemberLevel getMemberLevel(MemberLevel memberLevel);

}
