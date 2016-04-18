package com.kintiger.mall.api.member;

import java.math.BigDecimal;
import java.util.List;

import com.kintiger.mall.api.member.bo.MemberLevel;

/**
 * 会员等级管理.
 * 
 * @author xujiakun
 * 
 */
public interface IMemberLevelService {

	/**
	 * 会员等级查询(不分页).
	 * 
	 * @param shopId
	 * @return
	 */
	List<MemberLevel> getMemberLevelList(String shopId);

	/**
	 * 获取积分对应会员等级.
	 * 
	 * @param shopId
	 * @param points
	 * @return
	 */
	MemberLevel getMemberLevel(String shopId, BigDecimal points);

}
