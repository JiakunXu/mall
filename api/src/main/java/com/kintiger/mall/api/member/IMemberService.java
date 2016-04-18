package com.kintiger.mall.api.member;

import java.util.List;

import com.kintiger.mall.api.member.bo.Member;
import com.kintiger.mall.framework.bo.BooleanResult;

/**
 * 会员接口.
 * 
 * @author xujiakun
 * 
 */
public interface IMemberService {

	// >>>>>>>>>>以下是卖家<<<<<<<<<<

	/**
	 * 卖家查询所拥有的会员.
	 * 
	 * @param shopId
	 *            必填.
	 * @param member
	 * @return
	 */
	int getMemberCount(String shopId, Member member);

	/**
	 * 卖家查询所拥有的会员.
	 * 
	 * @param shopId
	 *            必填.
	 * @param member
	 * @return
	 */
	List<Member> getMemberList(String shopId, Member member);

	/**
	 * 获取公司会员概况统计.
	 * 
	 * @param shopId
	 *            必填.
	 * @param gmtStart
	 *            开始时间.
	 * @param gmtEnd
	 *            结束时间.
	 * @return
	 */
	List<Member> getMemberStats(String shopId, String gmtStart, String gmtEnd);

	/**
	 * 获取公司会员概况统计.
	 * 
	 * @param shopId
	 *            必填.
	 * @param gmtStart
	 *            开始时间.
	 * @param gmtEnd
	 *            结束时间.
	 * @return
	 */
	int getMemberCountStats(String shopId, String gmtStart, String gmtEnd);

	// >>>>>>>>>>以下是买家<<<<<<<<<<

	/**
	 * 验证 userId 是否已是 shopId 会员.
	 * 
	 * @param shopId
	 * @param userId
	 * @return if true then 返回 memId
	 */
	BooleanResult validateMember(String shopId, String userId);

	/**
	 * 买家注册会员创建.
	 * 
	 * @param shopId
	 * @param userId
	 * @return
	 */
	BooleanResult createMember(String shopId, String userId);

	/**
	 * 获取用户会员信息(包含会员等级).
	 * 
	 * @param shopId
	 * @param userId
	 * @return
	 */
	Member getMember(String shopId, String userId);

	/**
	 * 更新会员信息(积分汇总统计 更新member信息).
	 * 
	 * @param member
	 * @param modifyUser
	 * @return
	 */
	BooleanResult updateMember(Member member, String modifyUser);

}
