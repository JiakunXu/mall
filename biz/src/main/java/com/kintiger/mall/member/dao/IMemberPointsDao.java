package com.kintiger.mall.member.dao;

import java.math.BigDecimal;
import java.util.List;

import com.kintiger.mall.api.member.bo.MemberPoints;

/**
 * 
 * @author xujiakun
 * 
 */
public interface IMemberPointsDao {

	/**
	 * 
	 * @param memberPoints
	 * @return
	 */
	String createMemberPoints(MemberPoints memberPoints);

	/**
	 * 
	 * @param memberPoints
	 * @return
	 */
	List<MemberPoints> getMemberPointsList(MemberPoints memberPoints);

	/**
	 * 
	 * @param memberPoints
	 * @return
	 */
	BigDecimal getMemberPoints(MemberPoints memberPoints);

}
