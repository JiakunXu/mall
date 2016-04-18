package com.kintiger.mall.api.points.bo;

import java.math.BigDecimal;
import java.util.Date;

import com.kintiger.mall.framework.bo.SearchInfo;

/**
 * 积分卡.
 * 
 * @author xujiakun
 * 
 */
public class PointsCard extends SearchInfo {

	private static final long serialVersionUID = -5669400491609311618L;

	private String id;

	/**
	 * 店铺 id.
	 */
	private String shopId;

	/**
	 * 积分卡号.
	 */
	private String cardNo;

	/**
	 * 密码.
	 */
	private String password;

	/**
	 * 充值积分.
	 */
	private BigDecimal points;

	/**
	 * 积分卡过期日期.
	 */
	private String expireDate;

	/**
	 * 积分卡使用人.
	 */
	private String userId;

	/**
	 * 用户名.
	 */
	private String userName;

	/**
	 * 是否使用.
	 */
	private String isUsed;

	/**
	 * 状态 D:删除 U:正常.
	 */
	private String state;

	private Date modifyDate;

	/**
	 * 操作人ID.
	 */
	private String modifyUser;

	// >>>>>>>>>>以下是辅助属性<<<<<<<<<<

	/**
	 * Y 已过期 or N 未过期.
	 */
	private String expire;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public BigDecimal getPoints() {
		return points;
	}

	public void setPoints(BigDecimal points) {
		this.points = points;
	}

	// >>>>>>>>>>以下是辅助属性<<<<<<<<<<

	public String getPoints4Excel() {
		return (this.points == null) ? "" : this.points.toString();
	}

	// >>>>>>>>>>以上是辅助属性<<<<<<<<<<

	public String getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(String isUsed) {
		this.isUsed = isUsed;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Date getModifyDate() {
		return modifyDate != null ? (Date) modifyDate.clone() : null;
	}

	public void setModifyDate(Date modifyDate) {
		if (modifyDate != null) {
			this.modifyDate = (Date) modifyDate.clone();
		}
	}

	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

	public String getExpire() {
		return expire;
	}

	public void setExpire(String expire) {
		this.expire = expire;
	}

}
