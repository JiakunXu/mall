package com.kintiger.mall.api.coupon.bo;

import java.math.BigDecimal;
import java.util.Date;

import com.kintiger.mall.framework.bo.SearchInfo;

/**
 * 优惠券.
 * 
 * @author
 * 
 */
public class Coupon extends SearchInfo {

	private static final long serialVersionUID = -9197694083138030803L;

	/**
	 * ID.
	 */
	private String couponId;

	/**
	 * 优惠券名称.
	 */
	private String couponName;

	/**
	 * 店铺ID.
	 */
	private String shopId;

	/**
	 * 面值.
	 */
	private BigDecimal value;

	/**
	 * 会员级别.
	 */
	private String levelId;

	/**
	 * 限购：0 代表无限购.
	 */
	private int quota;

	/**
	 * 交易金额限制：0 代表不限.
	 */
	private BigDecimal atLeast;

	/**
	 * 发行总量.
	 */
	private int total;

	/**
	 * 生效时间.
	 */
	private Date startDate;

	/**
	 * 过期时间.
	 */
	private Date endDate;

	/**
	 * 到期提醒 Y or N.
	 */
	private String expireNotice;

	/**
	 * 备注.
	 */
	private String remark;

	/**
	 * 状态 D:删除 U:正常.
	 */
	private String state;

	/**
	 * 操作人ID.
	 */
	private String modifyUser;

	public String getCouponId() {
		return couponId;
	}

	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public String getLevelId() {
		return levelId;
	}

	public void setLevelId(String levelId) {
		this.levelId = levelId;
	}

	public int getQuota() {
		return quota;
	}

	public void setQuota(int quota) {
		this.quota = quota;
	}

	public BigDecimal getAtLeast() {
		return atLeast;
	}

	public void setAtLeast(BigDecimal atLeast) {
		this.atLeast = atLeast;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getExpireNotice() {
		return expireNotice;
	}

	public void setExpireNotice(String expireNotice) {
		this.expireNotice = expireNotice;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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
