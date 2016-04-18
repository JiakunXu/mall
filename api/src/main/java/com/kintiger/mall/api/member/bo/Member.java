package com.kintiger.mall.api.member.bo;

import java.math.BigDecimal;

/**
 * 会员(某店铺的会员).
 * 
 * @author
 * 
 */
public class Member extends MemberInfo {

	private static final long serialVersionUID = 8735000888062485530L;

	/**
	 * 等级.
	 */
	private String levelId;

	/**
	 * 级别名称.
	 */
	private String levelName;

	/**
	 * 积分.
	 */
	private BigDecimal points;

	/**
	 * 剩余积分.
	 */
	private BigDecimal surplusPoints;

	/**
	 * 状态 D:删除 U:正常 F:禁用.
	 */
	private String state;

	// >>>>>>>>>>以下是辅助属性<<<<<<<<<<

	/**
	 * 统计值.
	 */
	private int count;

	/**
	 * 统计维度 日期 2014-07-16.
	 */
	private String dates;

	/**
	 * 折扣 1 or 0.9.
	 */
	private BigDecimal discount;

	/**
	 * 等级.
	 */
	private int rank;

	public String getLevelId() {
		return levelId;
	}

	public void setLevelId(String levelId) {
		this.levelId = levelId;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public BigDecimal getPoints() {
		return points;
	}

	public void setPoints(BigDecimal points) {
		this.points = points;
	}

	public BigDecimal getSurplusPoints() {
		return surplusPoints;
	}

	public void setSurplusPoints(BigDecimal surplusPoints) {
		this.surplusPoints = surplusPoints;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getDates() {
		return dates;
	}

	public void setDates(String dates) {
		this.dates = dates;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

}
