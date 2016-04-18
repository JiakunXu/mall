package com.kintiger.mall.api.member;

import com.kintiger.mall.api.member.bo.MemberInfo;
import com.kintiger.mall.framework.bo.BooleanResult;

/**
 * 会员信息.
 * 
 * @author xujiakun
 * 
 */
public interface IMemberInfoService {

	/**
	 * 获取会员信息.
	 * 
	 * @param memId
	 * @return
	 */
	MemberInfo getMemberInfo(String memId);

	/**
	 * 修改会员信息.
	 * 
	 * @param memId
	 * @param userId
	 * @param shopId
	 * @param memberInfo
	 * @return
	 */
	BooleanResult updateMemberInfo(String memId, String userId, String shopId, MemberInfo memberInfo);

}
