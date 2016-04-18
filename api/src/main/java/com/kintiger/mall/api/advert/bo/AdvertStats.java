package com.kintiger.mall.api.advert.bo;

import com.kintiger.mall.framework.bo.SearchInfo;

/**
 * 广告点击历史记录.
 * 
 * @author xujiakun
 * 
 */
public class AdvertStats extends SearchInfo {

	private static final long serialVersionUID = -6972018914405486954L;

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
	 * ip 地址.
	 */
	private String ip;

	/**
	 * 点击时间.
	 */
	private String createDate;

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

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
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
