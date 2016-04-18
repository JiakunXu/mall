package com.kintiger.mall.api.region.bo;

import com.kintiger.mall.framework.bo.SearchInfo;

/**
 * 地区.
 * 
 * @author
 * 
 */
public class Region extends SearchInfo {

	private static final long serialVersionUID = 1846865413354160686L;

	/**
	 * 地区ID.
	 */
	private String regionId;

	/**
	 * 父地区ID=0时，代表的是一级的类目.
	 */
	private String parentRegionId;

	/**
	 * 地区名称.
	 */
	private String regionName;

	/**
	 * P: PROVINCE C: CITY A: AREA.
	 */
	private String type;

	/**
	 * 排列序号.
	 */
	private int rank;

	/**
	 * 状态 D:删除 U:正常.
	 */
	private String state;

	/**
	 * 操作人ID.
	 */
	private String modifyUser;

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public String getParentRegionId() {
		return parentRegionId;
	}

	public void setParentRegionId(String parentRegionId) {
		this.parentRegionId = parentRegionId;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
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
