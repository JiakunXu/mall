package com.kintiger.mall.api.advert.bo;

import java.math.BigDecimal;

import com.kintiger.mall.framework.bo.SearchInfo;

/**
 * 广告点击 赠送积分 记录.
 * 
 * @author xujiakun
 * 
 */
public class AdvertPoints extends SearchInfo {

	private static final long serialVersionUID = 955691265654164236L;

	private String id;

	/**
	 * 所属广告.
	 */
	private String advertId;

	/**
	 * 点击广告用户.
	 */
	private String userId;

	/**
	 * 是否最新赠送积分 记录.
	 */
	private String isLatest;

	/**
	 * 关联赠送积分表.
	 */
	private String memPointsId;

	/**
	 * 广告点击 获得积分赠送时间.
	 */
	private String pointsDate;

	private String modifyUser;

	// >>>>>>>>>>以下是辅助属性<<<<<<<<<<

	/**
	 * 店铺id.
	 */
	private String shopId;

	/**
	 * 广告标题名称.
	 */
	private String advertName;

	/**
	 * 用户名.
	 */
	private String userName;

	/**
	 * mobile.
	 */
	private String mobile;

	/**
	 * 统计维度 日期 2014-07-16.
	 */
	private String dates;

	/**
	 * 所赠积分.
	 */
	private BigDecimal points;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAdvertId() {
		return advertId;
	}

	public void setAdvertId(String advertId) {
		this.advertId = advertId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getIsLatest() {
		return isLatest;
	}

	public void setIsLatest(String isLatest) {
		this.isLatest = isLatest;
	}

	public String getMemPointsId() {
		return memPointsId;
	}

	public void setMemPointsId(String memPointsId) {
		this.memPointsId = memPointsId;
	}

	public String getPointsDate() {
		return pointsDate;
	}

	public void setPointsDate(String pointsDate) {
		this.pointsDate = pointsDate;
	}

	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getAdvertName() {
		return advertName;
	}

	public void setAdvertName(String advertName) {
		this.advertName = advertName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getDates() {
		return dates;
	}

	public void setDates(String dates) {
		this.dates = dates;
	}

	public BigDecimal getPoints() {
		return points;
	}

	public void setPoints(BigDecimal points) {
		this.points = points;
	}

}
