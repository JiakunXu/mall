package com.kintiger.mall.api.advert.bo;

import java.math.BigDecimal;

import com.kintiger.mall.framework.bo.SearchInfo;

/**
 * ad.
 * 
 * @author xujiakun
 * 
 */
public class Advert extends SearchInfo {

	private static final long serialVersionUID = -7610173360442444766L;

	/**
	 * 主键.
	 */
	private String advertId;

	/**
	 * 店铺id.
	 */
	private String shopId;

	/**
	 * 广告标题名称.
	 */
	private String advertName;

	/**
	 * 点击后跳转地址.
	 */
	private String redirectUrl;

	/**
	 * 所赠积分.
	 */
	private BigDecimal points;

	/**
	 * 多少周期 积分赠送1次 最小单位 天.
	 */
	private int cycle;

	/**
	 * 是否上架下架 Y or N.
	 */
	private String isDisplay;

	private String modifyUser;

	// >>>>>>>>>>以下是辅助属性<<<<<<<<<<

	/**
	 * 统计维度 日期 2014-07-16.
	 */
	private String dates;

	/**
	 * 广告点击.
	 */
	private int pv;

	/**
	 * 广告点击.
	 */
	private int uv;

	public String getAdvertId() {
		return advertId;
	}

	public void setAdvertId(String advertId) {
		this.advertId = advertId;
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

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public BigDecimal getPoints() {
		return points;
	}

	public void setPoints(BigDecimal points) {
		this.points = points;
	}

	public int getCycle() {
		return cycle;
	}

	public void setCycle(int cycle) {
		this.cycle = cycle;
	}

	public String getIsDisplay() {
		return isDisplay;
	}

	public void setIsDisplay(String isDisplay) {
		this.isDisplay = isDisplay;
	}

	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

	public String getDates() {
		return dates;
	}

	public void setDates(String dates) {
		this.dates = dates;
	}

	public int getPv() {
		return pv;
	}

	public void setPv(int pv) {
		this.pv = pv;
	}

	public int getUv() {
		return uv;
	}

	public void setUv(int uv) {
		this.uv = uv;
	}

}
