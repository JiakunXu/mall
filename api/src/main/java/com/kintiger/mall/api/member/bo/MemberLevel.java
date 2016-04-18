package com.kintiger.mall.api.member.bo;

import java.math.BigDecimal;

import com.kintiger.mall.framework.bo.SearchInfo;

/**
 * 会员级别.
 * 
 * @author
 * 
 */
public class MemberLevel extends SearchInfo {

	private static final long serialVersionUID = 4379599122301259870L;

	/**
	 * ID.
	 */
	private String levelId;

	/**
	 * 店铺ID.
	 */
	private String shopId;

	/**
	 * 级别名称.
	 */
	private String levelName;

	/**
	 * 积分范围.
	 */
	private BigDecimal startPoints;

	/**
	 * 积分范围.
	 */
	private BigDecimal endPoints;

	/**
	 * 等级.
	 */
	private int rank;

	/**
	 * 获赠积分.
	 */
	private BigDecimal points;

	/**
	 * 折扣 1 or 0.9.
	 */
	private BigDecimal discount;

	/**
	 * 状态 D:删除 U:正常.
	 */
	private String state;

	/**
	 * 操作人ID.
	 */
	private String modifyUser;

	public String getLevelId() {
		return levelId;
	}

	public void setLevelId(String levelId) {
		this.levelId = levelId;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public BigDecimal getStartPoints() {
		return startPoints;
	}

	public void setStartPoints(BigDecimal startPoints) {
		this.startPoints = startPoints;
	}

	public BigDecimal getEndPoints() {
		return endPoints;
	}

	public void setEndPoints(BigDecimal endPoints) {
		this.endPoints = endPoints;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public BigDecimal getPoints() {
		return points;
	}

	public void setPoints(BigDecimal points) {
		this.points = points;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

}
